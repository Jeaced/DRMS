package app.controllers;

import app.models.Task;
import app.models.TaskStatus;
import app.models.User;
import app.services.interfaces.ITaskService;
import app.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DashboardController {
    @Autowired
    ITaskService taskService;

    @Autowired
    IUserService userService;

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

        return "dashboard";
    }

    @PostMapping({"/dashboard/select-task"})
    String selectTask(@RequestParam("selectId") String selectId, Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);

        Task task = taskService.find(Long.parseLong(selectId));
        task.setUser(user);
        task.setStatus(TaskStatus.ASSIGNED);
        taskService.save(task);

        return "redirect:/dashboard";
    }

    @PostMapping({"/dashboard/reject-task"})
    String rejectTask(@RequestParam("rejectId") String rejectId, Model model) {
        Task task  = taskService.find(Long.parseLong(rejectId));
        task.setUser(null);
        task.setStatus(TaskStatus.NEW);
        taskService.save(task);

        return "redirect:/dashboard";
    }

    @PostMapping({"/dashboard/finish-task"})
    String finishTask(@RequestParam("doneId") String doneId, Model model) {
        Task task  = taskService.find(Long.parseLong(doneId));
        task.setFinished(LocalDateTime.now());
        task.setStatus(TaskStatus.DONE);
        taskService.save(task);

        return "redirect:/dashboard";
    }
}
