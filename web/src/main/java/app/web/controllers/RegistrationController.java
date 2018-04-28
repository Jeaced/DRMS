package app.web.controllers;

import app.web.DTO.UserDTO;
import app.core.models.User;
import app.core.services.interfaces.UserService;
import app.web.validators.UserValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {
    private final static Logger log = LogManager.getLogger(RegistrationController.class);
    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration(Model model) {
        log.info("GET request /registration");

        model.addAttribute("user", new UserDTO());
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("user") UserDTO userDTO, BindingResult bindingResult, Model model) {
        log.info("POST request /registration");

        userValidator.validate(userDTO, bindingResult);

        for (Object object : bindingResult.getAllErrors()) {
            if (object instanceof FieldError) {
                FieldError fieldError = (FieldError) object;

                if ("NotEmpty".equals(fieldError.getCode())) {
                    model.addAttribute("error", "Field should not be empty");
                } else if ("Pass.Too.Short".equals(fieldError.getCode())) {
                    model.addAttribute("error", "Password should contain at least 6 symbols");
                } else if ("UsernameExists".equals(fieldError.getCode())) {
                    model.addAttribute("error",
                            String.format("Username \"%s\" already exists", userDTO.getUsername()));
                }
                return "registration";
            }
        }

        User user = modelMapper.map(userDTO, User.class);
        userService.save(user);

        return "redirect:/login";
    }
}
