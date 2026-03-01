package com.canaryshop.canaryshop.controllers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;

@Controller
public class IndexManager {
    @GetMapping({"/", "/index"})
    public String index(Model model) {
        return "index";
    }
    
}
