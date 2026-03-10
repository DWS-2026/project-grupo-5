package com.canaryshop.canaryshop.controllers;


import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.canaryshop.canaryshop.entities.User;
import com.canaryshop.canaryshop.repositories.ProductsRepository;
import com.canaryshop.canaryshop.repositories.UserRepository;
import com.canaryshop.canaryshop.entities.Product;


@Controller
public class CanaryWebController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductsRepository productsRepository;

    
    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {
        
        Principal principal = request.getUserPrincipal();

        if (principal != null) {
            model.addAttribute("isLoggedIn", true);
            model.addAttribute("username", principal.getName());
            model.addAttribute("isAdmin", request.isUserInRole("ADMIN"));
        }else {
            model.addAttribute("isLoggedIn", false);
        }
       
    }

    @GetMapping({"/", "/index"})
    public String index(Model model) {
        
        model.addAttribute("products", productsRepository.findAll());
        return "index";
    }

    
}
