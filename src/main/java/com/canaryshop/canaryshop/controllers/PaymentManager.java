package com.canaryshop.canaryshop.controllers;

import com.canaryshop.canaryshop.services.NumberFormatter;
import com.canaryshop.canaryshop.services.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.ui.Model;

import com.canaryshop.canaryshop.entities.User;
import com.canaryshop.canaryshop.services.UserService;
import com.canaryshop.canaryshop.entities.Order;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;

@Controller
public class PaymentManager {

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    // Get the cart o the product that the user want to buy
    @GetMapping("/payment")
    public String getPaymentPage(Model model, @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) Long productID) {
        User user = userService.getUser(userDetails.getUsername());
        Order cart = this.orderService.createTempCart(user, productID);
        if (cart.getProducts().isEmpty()) {
            return "redirect:/cart";
        }

        model.addAttribute("productID", productID);

        model.addAttribute("cart", cart);

        model.addAttribute("totalPrice",
                NumberFormatter.getFormattedNumber(cart.getPrice()));
        return "payment";
    }

    // In case the user have any discount
    @PostMapping("/payment")
    public String postCode(Model model, @RequestParam String code, @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) Long productID) {
        Order cart = this.orderService.createTempCart(this.userService.getUser(userDetails.getUsername()), productID);
        model.addAttribute("productID", productID);
        model.addAttribute("cart", cart);
        float discount = this.orderService.getDiscount(code);
        model.addAttribute("code", code);
        model.addAttribute("totalPrice",
                NumberFormatter.getFormattedNumber(cart.getPrice() * discount));
        return "payment";
    }

    // To complete the payment and add it to the orders
    @PostMapping("/success")
    public String getSuccessPage(Model model, @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String code, @RequestParam(required = false) Long productID) {

        User user = userService.getUser(userDetails.getUsername());
        Order cart = this.orderService.createTempCart(user, productID);
        if (cart.getProducts().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart is empty");
        }
        float disc = this.orderService.getDiscount(code);
        
        this.orderService.closeOrder(user, cart, disc);
        
        model.addAttribute("date", LocalDate.now());
        model.addAttribute("totalPrice", cart.getPrice());
        model.addAttribute("id", user.getId());

        return "success";
    }

}
