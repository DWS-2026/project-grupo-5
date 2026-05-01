package com.canaryshop.canaryshop.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.canaryshop.canaryshop.entities.Product;
import com.canaryshop.canaryshop.entities.User;
import com.canaryshop.canaryshop.services.PageHandler;
import com.canaryshop.canaryshop.services.ProductService;
import com.canaryshop.canaryshop.services.UserService;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminManager {
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private PageHandler pageHandler;

    // To view de admin dashboard with de admin user
    @GetMapping("/admin/{id}")
    public String getAdminPanel(Model model, @PathVariable long id) {

        User u = userService.findById(id);
        model.addAttribute("user", u);
        model.addAttribute("dashboard", true);
        return "adminDashboard";
    }

    // To view all products
    @GetMapping("/admin/products")
    public String getReportedProducts(Model model, @RequestParam(required = false) String search,
            @PageableDefault(size = 12) Pageable page) {

        Page<Product> results;
        results = productService.getReportedProducts(search, search, page);
        if (search != null) {
            model.addAttribute("search", search);
        }
        pageHandler.handleProductPage(model, results, page);
        model.addAttribute("navProducts", true);
        return "adminProducts";
    }

    // To delete product's reports
    @PostMapping("/admin/productReport/{id}/delete")
    public String deleteReportedProduct(@PathVariable long id, @RequestParam String report, Principal principal) {
        Product product = productService.getProduct(id);
        User user = userService.getUser(principal);
        if (user == null || !user.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only administrator can do this");
        }
        product.unreport(report);
        this.productService.addProduct(product);
        return "redirect:/admin/products";
    }

    // To view all users
    @GetMapping("/admin/users")
    public String getUsers(Model model, @RequestParam(required = false) String search,
            @PageableDefault(size = 12) Pageable page) {
        Page<User> results;
        results = userService.getPageUser(search, page);
        if (search != null) {
            model.addAttribute("search", search);
        }
        pageHandler.handleUserPage(model, results, page);
        model.addAttribute("all", true);
        model.addAttribute("navUsers", true);
        return "adminAllUsers";
    }

    // To delete users
    @PostMapping("/admin/user/{id}/delete")
    public String postDeleteUser(@PathVariable long id, Principal principal) {
        User currentUser = userService.getUser(principal);
        User user = userService.findById(id);
        userService.deleteUser(currentUser, user);
        return "redirect:/admin/users";
    }

    // To view all reported users
    @GetMapping("/admin/reportedUsers")
    public String getReportedUsers(Model model, @RequestParam(required = false) String search,
            @PageableDefault(size = 12) Pageable page) {
        Page<User> results;
        results = userService.getReportedUser(search, page);
        if (search != null) {
            model.addAttribute("search", search);
        }
        pageHandler.handleUserPage(model, results, page);
        model.addAttribute("reported", true);
        model.addAttribute("navUsers", true);
        return "adminReportedUsers";
    }

    // To delete user's reports
    @PostMapping("/admin/userReport/{id}/delete")
    public String postDeleteUserReport(@PathVariable long id, @RequestParam String report, Principal principal) {
        User currentUser = userService.getUser(principal);
        if (currentUser == null || !currentUser.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only administrator can do this");
        }
        User u = userService.findById(id);
        u.unreport(report);
        this.userService.addUser(u);
        return "redirect:/admin/reportedUsers";
    }

}
