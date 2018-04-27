package app.controllers;

import app.models.Task;
import app.models.TaskStatus;
import app.models.User;
import app.services.TaskService;
import app.services.interfaces.ITaskService;
import app.services.interfaces.IUserService;
import app.validators.TaskValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DashboardController {
    @Autowired
    ITaskService taskService;

    @Autowired
    IUserService userService;

    @Autowired
    TaskValidator taskValidator;

    @GetMapping({"/dashboard"})
    String getDashboard(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userService.findByUsername(username);

        List<Task> newTasks = taskService.findAllNew();
        List<Task> userTasks = taskService.findAll(user);

        List<Task> assignedTasks = userTasks
                .stream()
                .filter(t -> t.getStatus() == TaskStatus.ASSIGNED)
                .collect(Collectors.toList());

        List<Task> finishedTasks = userTasks
                .stream()
                .filter(t -> t.getStatus() == TaskStatus.DONE)
                .collect(Collectors.toList());

        model.addAttribute("newTasks", newTasks);
        model.addAttribute("assignedTasks", assignedTasks);
        model.addAttribute("finishedTasks", finishedTasks);
        model.addAttribute("task", new Task());

        return "dashboard";
    }

    @PostMapping({"/dashboard/select-task"})
    String selectTask(@RequestParam("selectId") String selectId) {
        taskService.selectTask(selectId);
        return "redirect:/dashboard";
    }

    @PostMapping({"/dashboard/reject-task"})
    String rejectTask(@RequestParam("rejectId") String rejectId) {
        taskService.rejectTask(rejectId);
        return "redirect:/dashboard";
    }

    @PostMapping({"/dashboard/finish-task"})
    String finishTask(@RequestParam("doneId") String doneId) {
        taskService.finishTask(doneId);
        return "redirect:/dashboard";
    }

    @PostMapping("/dashboard/add-task")
    String addTask(@ModelAttribute("task") Task task, BindingResult bindingResult) {

        taskValidator.validate(task,bindingResult);
        if (bindingResult.hasErrors()){
            return  "redirect:/dashboard";
        }
        taskService.addNewTaskManually(task.getDescription());
        return "redirect:/dashboard";
    }


}
