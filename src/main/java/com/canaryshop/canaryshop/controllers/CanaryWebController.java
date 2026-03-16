package com.canaryshop.canaryshop.controllers;
import com.canaryshop.canaryshop.services.UserService;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class CanaryWebController {

    @Autowired
    private UserService users;

    @ModelAttribute
    public void addGlobalAttributes(Model model, HttpServletRequest request) {
        
        Principal principal = request.getUserPrincipal();

        if (principal != null) {
            model.addAttribute("loggedUser", users.getUser(principal.getName()));
            model.addAttribute("isAdmin", request.isUserInRole("ADMIN"));
        }else {
            model.addAttribute("isLoggedIn", false);
        }
       
    }

    
    
}
