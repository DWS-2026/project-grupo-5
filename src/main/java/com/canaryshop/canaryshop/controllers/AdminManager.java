package com.canaryshop.canaryshop.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.canaryshop.canaryshop.entities.Product;
import com.canaryshop.canaryshop.entities.User;
import com.canaryshop.canaryshop.services.PageHandler;
import com.canaryshop.canaryshop.services.ProductService;
import com.canaryshop.canaryshop.services.UserService;

import jakarta.servlet.http.HttpServletRequest;


@Controller
public class AdminManager {
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private PageHandler pageHandler;
    
    @GetMapping("/admin/{id}")
    public String getMethodName(Model model, @PathVariable long id) {

        User u= userService.findById(id);
        model.addAttribute("user",u);
        return "adminDashboard";
    }

    @GetMapping("/admin/products")
    public String index(Model model, @RequestParam(required = false) String search,@PageableDefault(size=12) Pageable page) {

        Page<Product> results;
        results= productService.getReportedProducts(search, search, page);
        if (search!=null){
            model.addAttribute("search", search);
        }
        pageHandler.handleProductPage(model, results, page);
        return "adminProducts";
    }
    
    
}
