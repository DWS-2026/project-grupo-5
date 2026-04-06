package com.canaryshop.canaryshop.controllers;

import com.canaryshop.canaryshop.entities.User;
import com.canaryshop.canaryshop.services.UserService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class LoginManager {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String getLoginPage(Model model, @RequestParam(required = false) boolean registerSuccess) {
        model.addAttribute("showLogin", true);
        model.addAttribute("showRegister", false);
        model.addAttribute("registerSuccess", registerSuccess);
        return "login";
    }

    @GetMapping("/loginerror")
    public String getLoginErrorPage(Model model) {
        model.addAttribute("showLogin", true);
        model.addAttribute("showRegister", false);
        model.addAttribute("LoginError", "Invalid email or password");
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
            @RequestParam String password, @RequestParam String confirmPassword, Model model) {
        if (userService.findByEmail(email)!=null) {
            model.addAttribute("showRegister", true);
            model.addAttribute("showLogin", false);
            model.addAttribute("RegisterError", "Email already in use");
            return "login";
        }
        if(!password.equals(confirmPassword)){
            model.addAttribute("showRegister", true);
            model.addAttribute("showLogin", false);
            model.addAttribute("RegisterError", "The passwords are different");
            return "login";
        }

        User entity = new User(username, email, passwordEncoder.encode(password), ("USER"));
        userService.addUser(entity);
        model.addAttribute("showLogin", true);
        model.addAttribute("showRegister", false);
        return "redirect:/login?registerSuccess=true";
    }

}