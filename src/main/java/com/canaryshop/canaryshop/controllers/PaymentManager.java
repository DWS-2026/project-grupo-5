package com.canaryshop.canaryshop.controllers;

import java.security.Principal;

import com.canaryshop.canaryshop.services.NumberFormatter;
import com.canaryshop.canaryshop.services.OrderService;
import com.canaryshop.canaryshop.services.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.ui.Model;

import com.canaryshop.canaryshop.entities.User;
import com.canaryshop.canaryshop.services.UserService;
import com.canaryshop.canaryshop.entities.Product;
import com.canaryshop.canaryshop.entities.Order;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;


@Controller
public class PaymentManager {

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    
    @GetMapping("/payment")
    public String getPaymentPage(Model model, Principal principal, @RequestParam(required = false) Long productID, HttpSession session){
        User user = userService.getUser(principal.getName());
        Order cart= new Order();
        if(productID!=null){    
            cart.setProductQuantity(this.productService.getProduct(productID), 1);
            cart.setUser(user);
        }else{
            cart = user.getCart();
            this.orderService.renewOrder(cart);
            if(cart.getProducts().isEmpty()){
                return "redirect:/cart";
            }
        }

        session.setAttribute("cart", cart);
        model.addAttribute("cart", cart);

        model.addAttribute("totalPrice",
                NumberFormatter.getFormattedNumber(cart.getPrice()));
        return "payment";
    }
    @PostMapping("/payment")
        public String postCode(Model model, @RequestParam String code, HttpSession session) {
        Order cart = ((Order)session.getAttribute("cart")); 
        model.addAttribute("cart",cart);        
        model.addAttribute("totalPrice",
                NumberFormatter.getFormattedNumber(cart.getPrice() * this.orderService.getDiscount(code)));
        return "payment";
    }

    @PostMapping("/success")
    public String getSuccessPage(Model model, Principal principal, @RequestParam float price, HttpSession session){ 

        User user = userService.getUser(principal.getName());
        Order cart = ((Order)session.getAttribute("cart"));
        cart=this.orderService.renewOrder(cart);
        if(cart.getProducts().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Some products may aren't available");
        }
        if(cart.getId()!= null && cart.getId().equals(user.getCart().getId())){
            this.orderService.closeCart(user,price);
        }else{
            this.orderService.closeOrder(user,cart,price);
        }
        session.removeAttribute("cart");
        model.addAttribute("date", LocalDate.now());
        model.addAttribute("totalPrice", price);
        model.addAttribute("id", user.getId());

        return "success";
    }
    
}
