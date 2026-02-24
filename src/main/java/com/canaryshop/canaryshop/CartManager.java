package com.canaryshop.canaryshop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;


@Controller
public class CartManager {


    @GetMapping("/cart")
    public String index(Model model) {
        model.addAttribute("showCart", true);
        return "carrito";
    }

}
