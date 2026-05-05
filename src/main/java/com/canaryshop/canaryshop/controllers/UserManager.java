package com.canaryshop.canaryshop.controllers;

import java.security.Principal;
import java.util.List;

import com.canaryshop.canaryshop.entities.Order;
import com.canaryshop.canaryshop.services.PageHandler;
import com.canaryshop.canaryshop.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.canaryshop.canaryshop.entities.Image;
import com.canaryshop.canaryshop.entities.Product;
import com.canaryshop.canaryshop.entities.User;
import com.canaryshop.canaryshop.services.ImageService;
import com.canaryshop.canaryshop.services.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class UserManager {

    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private PageHandler pageHandler;

    @GetMapping("/user/{id}")
    public String getUser(Model model, @PathVariable long id, Principal principal) {
        User user = this.userService.findById(id);
        User currentUser = userService.getUser(principal);
        model.addAttribute("canEdit", user.canEdit(currentUser));
        model.addAttribute("userView", user);
        List<Product> products = user.getProducts();
        products = products.subList(0, Math.min(products.size(), 5));
        List<Order> orders = user.getOrders();
        orders.removeIf(element -> !element.getIsClosed());
        orders = orders.subList(0, Math.min(orders.size(), 5));
        model.addAttribute("products", products);
        model.addAttribute("orders", orders);
        if (currentUser != null) {
            model.addAttribute("currentUser", currentUser.equals(user));
        } else {
            model.addAttribute("currentUser", false);
        }

        return "user";
    }

    // To view all the user's products
    @GetMapping("/user/{id}/products")
    public String getAllUserProducts(Model model, @PathVariable long id, @PageableDefault(size = 12) Pageable page) {
        Page<Product> results = this.productService.getProductsByVendor(id, page);
        pageHandler.handleProductPage(model, results, page);
        return "index";
    }

    // To change the image of the user
    @PostMapping("/user/{id}/image")
    public String postMethodName(@RequestParam MultipartFile image, @PathVariable long id, Principal principal) {
        User user = userService.findById(id);
        User currentUser = userService.getUser(principal);
        this.userService.updateUserImage(currentUser, user, image);
        return "redirect:/user/" + id;
    }

    // To edit the users
    @PostMapping("/user/{id}/edit")
    public String postMethodName(@RequestParam String userName, @RequestParam String email, @PathVariable long id,
            Principal principal) {
        User user = this.userService.findById(id);
        User currentUser = userService.getUser(principal);
        user.setEmail(email);
        user.setUsername(userName);
        this.userService.updateUser(currentUser, user);
        return "redirect:/user/" + id;
    }

    // To delete the users
    @PostMapping("/user/{id}/delete")
    public String deleteUser(@PathVariable long id, Principal principal, HttpServletRequest request)
            throws ServletException {
        User user = userService.findById(id);
        User currentUser = userService.getUser(principal);
        userService.deleteUser(currentUser, user);
        if (currentUser.getId().equals(id)) {
            request.logout();
        }
        return "redirect:/";
    }

    // To report the users
    @PostMapping("/user/{id}/report")
    public String reportUser(@PathVariable long id, @RequestParam String report, Principal principal) {
        User currentUser = this.userService.getUser(principal);
        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must log in to report");
        }
        User u = this.userService.findById(id);
        u.report(report);
        this.userService.addUser(u);
        return "redirect:/user/" + id;
    }
}
