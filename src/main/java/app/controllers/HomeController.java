package app.controllers;

import app.DAO.TaskDAO;
import app.models.Task;
import app.models.User;
import app.services.TaskService;
import app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;


@Controller
public class HomeController {

    @Autowired
    TaskService taskService;

    @Autowired
    UserService userService;

    @GetMapping({"/", "/home"})
    String getHome(Model model) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();

        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Task task = new Task();
        task.setName("Megatask");
        task.setDescription("DO THIS !");
        task.setUser(user);
        task.setCreated(LocalDateTime.now());
        task.setFinished(null);
        task.setDone(false);

        taskService.save(task);

        model.addAttribute("tasks", taskService.findAll(user));
        return "home";
    }
}
