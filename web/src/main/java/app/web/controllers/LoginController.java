package app.web.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    private final static Logger log = LogManager.getLogger(LoginController.class);

    @GetMapping("/login")
    public String loginGet(Model model, String error, String logout) {
        log.info("GET request /login");

        if (error != null) {
            log.debug("Wrong username or password.");
            model.addAttribute("error", "Wrong username or password");
        }
        if (logout != null) {
            log.debug("User has logged out.");
            model.addAttribute("message", "Logged out");
        }

        return "login";
    }
}
