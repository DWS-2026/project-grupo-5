package com.canaryshop.canaryshop.controllers;

import com.canaryshop.canaryshop.entities.*;
import com.canaryshop.canaryshop.services.ProductService;
import com.canaryshop.canaryshop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@Controller
public class CartManager {

    @Autowired
    private ProductService products;

    @Autowired
    private UserService userService;

    @GetMapping("/cart")
    public String serveCart(Model model, Principal principal) {
        User user = userService.getUser(principal.getName());
        Order cart = user.getCart();
        model.addAttribute("items", cart.getProducts());
        model.addAttribute("totalPrice", cart.getPrice());
        return "cart";
    }

    @PostMapping("/cart/add/{id}")
    public String addToCart(@PathVariable long id, Principal principal) {
        Product product = products.getProduct(id);
        User user = userService.getUser(principal);
        Order cart = user.getCart();
        cart.addProduct(product);
        userService.addUser(user);
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove/{id}")
    public String removeFromCart(@PathVariable long id, Principal principal) {
        Product product = products.getProduct(id);
        User user = userService.getUser(principal);
        Order cart = user.getCart();
        cart.removeProduct(product);
        userService.addUser(user);
        return "redirect:/cart";
    }
}