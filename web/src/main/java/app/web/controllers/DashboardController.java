package app.web.controllers;

import app.web.DTO.TaskDTO;
import app.core.models.Task;
import app.core.models.User;
import app.core.services.ServiceException;
import app.core.services.interfaces.TaskService;
import app.core.services.interfaces.UserService;
import app.web.validators.TaskValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DashboardController {
    private final static Logger log = LogManager.getLogger(DashboardController.class);
    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    TaskService taskService;

    @Autowired
    UserService userService;

    @Autowired
    TaskValidator taskValidator;

    @GetMapping({"/dashboard"})
    String getDashboard(Model model) {
        log.info("GET request /dashboard");

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);

        model.addAttribute("newTasks", taskService.findAllNew());
        model.addAttribute("assignedTasks", taskService.findAllAssignedToUser(user));
        model.addAttribute("finishedTasks", taskService.findAllFinishedByUser(user));
        model.addAttribute("task", new TaskDTO());

        return "dashboard";
    }

    @PostMapping({"/dashboard/select-task"})
    String selectTask(@RequestParam("selectId") String selectId, RedirectAttributes redirectAttributes) {
        log.info(String.format("Task #%s selection", selectId));

        try {
            Long taskId = Long.parseLong(selectId);
            taskService.selectTask(taskId);
        } catch (NumberFormatException | ServiceException e) {
            log.error(e);
            redirectAttributes.addFlashAttribute("error",
                    "An error occurred during task selection. Please, try again later.");
            return "redirect:/dashboard";
        }

        return "redirect:/dashboard";
    }

    @PostMapping({"/dashboard/reject-task"})
    String rejectTask(@RequestParam("rejectId") String rejectId, RedirectAttributes redirectAttributes) {
        log.info(String.format("Task #%s rejection", rejectId));

        try {
            Long taskId = Long.parseLong(rejectId);
            taskService.rejectTask(taskId);
        } catch (NumberFormatException | ServiceException e) {
            log.error(e);
            redirectAttributes.addFlashAttribute("error",
                    "An error occurred during task rejection. Please, try again later.");
            return "redirect:/dashboard";
        }

        return "redirect:/dashboard";
    }

    @PostMapping({"/dashboard/finish-task"})
    String finishTask(@RequestParam("doneId") String finishedId, RedirectAttributes redirectAttributes) {
        log.info(String.format("Task #%s finishing", finishedId));

        try {
            Long taskId = Long.parseLong(finishedId);
            taskService.finishTask(taskId);
        } catch (NumberFormatException | ServiceException e) {
            log.error(e);
            redirectAttributes.addFlashAttribute("error",
                    "An error occurred during task finishing. Please, try again later.");
            return "redirect:/dashboard";
        }

        return "redirect:/dashboard";
    }

    @PostMapping("/dashboard/add-task")
    String addTask(@ModelAttribute("task") TaskDTO taskDTO, BindingResult bindingResult,
                   RedirectAttributes redirectAttributes) {
        log.info("Task creation");

        taskValidator.validate(taskDTO, bindingResult);

        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", "Task Description cannot be empty.");
            return "redirect:/dashboard";
        }

        Task task = modelMapper.map(taskDTO, Task.class);
        taskService.addNewTaskManually(task);

        return "redirect:/dashboard";
    }
}
