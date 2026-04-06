package com.canaryshop.canaryshop.controllers;

import java.security.Principal;

import com.canaryshop.canaryshop.services.NumberFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import com.canaryshop.canaryshop.entities.User;
import com.canaryshop.canaryshop.services.UserService;
import com.canaryshop.canaryshop.entities.Order;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;


@Controller
public class PaymentManager {

    @Autowired
    private UserService userService;
    
    @GetMapping("/payment")
    public String getPaymentPage(Model model, Principal principal, @RequestParam(required = false) String code){
        User user = userService.getUser(principal.getName());
        Order cart = user.getCart();

        model.addAttribute("cart", cart);
        float discount = switch(code){
            case "DiegoEsElMejor" -> 0;
            case "JaimeEsElMejor" -> 0.25f;
            case "VictorEsElMejor" -> 0.5f;
            case "JorgeEsElMejor" -> 0.75f;
            default -> 1f;
        };
        model.addAttribute("totalPrice",
                NumberFormatter.getFormattedNumber(cart.getPrice() * discount));
        return "payment";
    }

    @PostMapping("/success")
    public String getSuccessPage(Model model, Principal principal, @RequestParam float price){ 

        User user = userService.getUser(principal.getName());
        Order cart = user.getCart();
        model.addAttribute("date", LocalDate.now()  );
        model.addAttribute("cart", cart);
        model.addAttribute("totalPrice", price);
        model.addAttribute("id", user.getId());
        
        return "success";
    }
    
}
