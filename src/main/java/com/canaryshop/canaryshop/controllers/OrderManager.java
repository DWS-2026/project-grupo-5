package com.canaryshop.canaryshop.controllers;

import com.canaryshop.canaryshop.entities.*;
import com.canaryshop.canaryshop.services.OrderService;
import com.canaryshop.canaryshop.services.ProductService;
import com.canaryshop.canaryshop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.security.Principal;

@Controller
public class OrderManager {

    @Autowired
    private ProductService products;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService os;

    @GetMapping("/cart")
    public String serveCart(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUser(userDetails.getUsername());
        Order cart = user.getCart();
        this.os.renewOrder(cart);
        model.addAttribute("order", cart);
        return "cart";
    }

    @GetMapping("/order/{order_id}")
    public String serveClosedOrder(Model model, @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable long order_id) {
        User user = userService.getUser(userDetails.getUsername());
        Order order = os.getOrder(order_id, user);
        if (!order.getIsClosed()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order is not closed");
        }
        model.addAttribute("order", order);
        return "cart";
    }

    @PostMapping("/cart/increase/{id}")
    public String increaseItem(@PathVariable long id, Principal principal) {
        User user = userService.getUser(principal);
        Order cart = user.getCart();
        Product product = products.getProduct(id);
        cart.setProductQuantity(product, cart.getProductQuantity(product) + 1);
        os.addOrder(cart);
        userService.addUser(user);
        return "redirect:/cart";
    }

    @PostMapping("/cart/decrease/{id}")
    public String decreaseItem(@PathVariable long id, Principal principal) {
        User user = userService.getUser(principal);
        Order cart = user.getCart();
        Product product = products.getProduct(id);
        cart.setProductQuantity(product, cart.getProductQuantity(product) - 1);
        os.addOrder(cart);
        userService.addUser(user);
        return "redirect:/cart";
    }
}