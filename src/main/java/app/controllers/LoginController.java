package app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    String getLoginPage(Model model){
        model.addAttribute("Hello, Denis!");
        return "login";
    }
}
