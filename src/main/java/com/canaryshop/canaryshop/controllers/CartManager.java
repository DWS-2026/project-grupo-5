package com.canaryshop.canaryshop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class CartManager {


    @GetMapping("/cart")
    public String index(Model model) {
        model.addAttribute("showCart", true);
        return "carrito";
    }

}
