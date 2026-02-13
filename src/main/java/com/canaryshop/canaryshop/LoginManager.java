package com.canaryshop.canaryshop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class LoginManager {
    @GetMapping("/login")
    public String index(Model model) {
        model.addAttribute("showLogin", true);
        model.addAttribute("showRegister", false);
        return "iniciosesion";
    }

    @GetMapping("/register")
    public String getMethodName(Model model) {
        model.addAttribute("showRegister", true);
        model.addAttribute("showLogin", false);
        return "iniciosesion";
    }
    
}
