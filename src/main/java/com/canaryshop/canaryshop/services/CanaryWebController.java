package com.canaryshop.canaryshop.services;
import com.canaryshop.canaryshop.entities.User;

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
    // Change the header of the page
    @ModelAttribute
    public void addGlobalAttributes(Model model, HttpServletRequest request) {
        
        Principal principal = request.getUserPrincipal();
        User user = users.getUser(principal);
        model.addAttribute("user", user);
        if (user != null){
            model.addAttribute("isAdmin", request.isUserInRole("ADMIN"));
        }
       
    }


    
}
