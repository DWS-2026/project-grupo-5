package com.canaryshop.canaryshop.controllers;

import com.canaryshop.canaryshop.entities.User;
import com.canaryshop.canaryshop.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class LoginManager {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        model.addAttribute("showLogin", true);
        model.addAttribute("showRegister", false);
        return "login";
    }

    @GetMapping("/loginerror")
    public String getLoginErrorPage(Model model) {
        model.addAttribute("showLogin", true);
        model.addAttribute("showRegister", false);
        model.addAttribute("error", "Invalid email or password");
        return "login";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("showRegister", true);
        model.addAttribute("showLogin", false);
        return "login";
    }

    @PostMapping("/register")
    public String postMethodName(@RequestParam String username, @RequestParam String email,
            @RequestParam String password, Model model) {
        if (userRepository.findByEmail(email).isPresent()) {
            model.addAttribute("showRegister", true);
            model.addAttribute("showLogin", false);
            model.addAttribute("error", "Email already in use");
            return "redirect:/register";
        }

        User entity = new User(username, email, password);
        userRepository.save(entity);
        model.addAttribute("showLogin", true);
        model.addAttribute("showRegister", false);
        return "redirect:/login";
    }

}