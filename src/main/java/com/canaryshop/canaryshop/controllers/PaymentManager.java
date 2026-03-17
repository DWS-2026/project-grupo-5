package com.canaryshop.canaryshop.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import com.canaryshop.canaryshop.entities.User;
import com.canaryshop.canaryshop.services.UserService;
import com.canaryshop.canaryshop.entities.Order;


@Controller
public class PaymentManager {

    @Autowired
    private UserService userService;
    
    @GetMapping("/payment")
    public String getPaymentPage(Model model, Principal principal) {
        User user = userService.getUser(principal.getName());
        Order cart = user.getCart();
        model.addAttribute("cartProducts", cart.getProducts());
        model.addAttribute("totalPrice", cart.getPrice());
        return "payment";
    }
    
}
