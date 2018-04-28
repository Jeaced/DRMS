package app.web.controllers;

import app.core.DAO.TaskDAO;
import app.core.models.TaskType;
import app.web.DTO.ResourceDTO;
import app.web.DTO.TaskDTO;
import app.core.models.Task;
import app.core.models.User;
import app.core.services.ServiceException;
import app.core.services.interfaces.TaskService;
import app.core.services.interfaces.UserService;
import app.web.validators.TaskValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Autowired
    TaskDAO taskRepository;

    @GetMapping({"/dashboard"})
    String getDashboard(Model model) {
        log.info("GET request /dashboard");

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);

        List<TaskDTO> newTasks = taskService.findAllNew()
                .stream()
                .map(task -> modelMapper.map(task, TaskDTO.class))
                .collect(Collectors.toList());

        List<TaskDTO> assignedTasks = taskService.findAllAssignedToUser(user)
                .stream()
                .map(task -> modelMapper.map(task, TaskDTO.class))
                .collect(Collectors.toList());

        List<TaskDTO> finishedTasks = taskService.findAllFinishedByUser(user)
                .stream()
                .map(task -> modelMapper.map(task, TaskDTO.class))
                .collect(Collectors.toList());

        model.addAttribute("newTasks", newTasks);
        model.addAttribute("assignedTasks", assignedTasks);
        model.addAttribute("finishedTasks", finishedTasks);
        model.addAttribute("task", new TaskDTO());

        ArrayList<String> types = new ArrayList<>();
        for (TaskType a : TaskType.values())
            types.add(a.name());

        model.addAttribute("types", types);

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

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Please select the type of the task.");
            return "redirect:/dashboard";
        }

        Task task = modelMapper.map(taskDTO, Task.class);
        taskService.addNewTaskManually(task);

        return "redirect:/dashboard";
    }

    @PostMapping("/dashboard/download")
    String getData(HttpServletResponse response) {
        ArrayList<Task> tasks = (ArrayList<Task>) taskRepository.findAll();
        response.setContentType("text/plain");
        String fileName = LocalDateTime.now() + "_tasks";
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".txt");
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONArray jsonArray = new JSONArray();
        for (Task o : tasks) {
            jsonArray.put(toJsonObject(o));
        }
        String a = null;
        try {
            a = jsonArray.toString(1);
            out.println(a);
            out.flush();
            out.close();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "dashboard";
    }

    JSONObject toJsonObject(Task obj) {
        JSONObject o = new JSONObject();
        try {
            o.put("id", obj.getId());
            o.put("description", obj.getDescription());
            if (obj.getUser() != null)
                o.put("user", obj.getUser().getUsername());
            else o.put("user", "-");
            o.put("created", obj.getCreated());
            o.put("finished", obj.getFinished());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return o;
    }
}
