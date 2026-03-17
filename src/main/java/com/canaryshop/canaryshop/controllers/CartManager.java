package com.canaryshop.canaryshop.controllers;

import com.canaryshop.canaryshop.entities.*;
import com.canaryshop.canaryshop.repositories.OrderProductRepository;
import com.canaryshop.canaryshop.repositories.OrderRepository;
import com.canaryshop.canaryshop.services.OrderService;
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
    
    @Autowired
    private OrderService os;

    @GetMapping("/cart")
    public String serveCart(Model model, Principal principal) {
        User user = userService.getUser(principal.getName());
        Order cart = user.getCart();
        model.addAttribute("items", cart.getProducts());
        model.addAttribute("totalPrice", cart.getPrice());
        return "cart";
    }

   @PostMapping("/cart/increase/{id}")
    public String increaseItem(@PathVariable long id, Principal principal) {
        OrderProduct orderProduct = os.getOrderProduct(id);
        User user = userService.getUser(principal.getName());
        Order cart = user.getCart();
        os.setAmount(orderProduct,orderProduct.getQuantity()+1);
        os.addOrder(cart);
        userService.addUser(user); 
        return "redirect:/cart";
    }

    @PostMapping("/cart/decrease/{id}")
    public String decreaseItem(@PathVariable long id, Principal principal) {
       OrderProduct orderProduct = os.getOrderProduct(id);
        User user = userService.getUser(principal.getName());
        Order cart = user.getCart();
        if(orderProduct.getQuantity()==1){
            cart.removeProduct(orderProduct);
        }
        else{
            os.setAmount(orderProduct,orderProduct.getQuantity()-1); 
        }
        os.addOrder(cart);
        userService.addUser(user);
        return "redirect:/cart";
    }
}