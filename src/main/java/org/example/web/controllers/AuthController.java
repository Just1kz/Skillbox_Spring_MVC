package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.app.service.AuthService;
import org.example.web.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/auth")
public class AuthController {

    private final Logger logger = Logger.getLogger(AuthController.class);
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public String login(Model model) {
        logger.info("GET /login returns index.html");
        model.addAttribute("users", new User());
        return "login_page";
    }

    @GetMapping("/register")
    public String reg(Model model) {
        logger.info("GET /login returns index.html");
        model.addAttribute("users", new User());
        return "reg_page";
    }

    @PostMapping
    public String authenticate(User user) {
        if (authService.authenticate(user)) {
            logger.info("login OK redirect to book shelf");
                return "redirect:/books/shelf";
            }
        else {
            logger.info("login FAIL redirect back to login");
            return "redirect:/auth";
        }
    }

    @PostMapping("/register")
    public String register(User user) {
        if (!user.getEmail().isEmpty()
                && !user.getPassword().isEmpty()) {
            authService.register(user);
            logger.info("reg is OK redirect to auth");
            return "redirect:/auth";
        } else {
            logger.info("reg FAIL redirect back to reg");
            return "redirect:/register";
        }
    }
}
