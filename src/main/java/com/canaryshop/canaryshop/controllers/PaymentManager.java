package com.canaryshop.canaryshop.controllers;

import java.security.Principal;

import com.canaryshop.canaryshop.services.NumberFormatter;
import com.canaryshop.canaryshop.services.OrderService;
import com.canaryshop.canaryshop.services.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.ui.Model;

import com.canaryshop.canaryshop.entities.User;
import com.canaryshop.canaryshop.services.UserService;
import com.canaryshop.canaryshop.entities.Order;
import org.springframework.web.bind.annotation.PostMapping;
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
    // Get the cart o the product that the user want to buy
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
    // In case the user have any discount
    @PostMapping("/payment")
        public String postCode(Model model, @RequestParam String code, HttpSession session) {
        Order cart = ((Order)session.getAttribute("cart")); 
        model.addAttribute("cart",cart);  
        float discount =this.orderService.getDiscount(code);
        model.addAttribute("discount",discount);      
        model.addAttribute("totalPrice",
                NumberFormatter.getFormattedNumber(cart.getPrice() * discount));
        return "payment";
    }
    // To complete the payment and add it to the orders
    @PostMapping("/success")
    public String getSuccessPage(Model model, Principal principal, HttpSession session, @RequestParam(required = false) Float discount){ 

        User user = userService.getUser(principal.getName());
        Order cart = ((Order)session.getAttribute("cart"));
        cart=this.orderService.renewOrder(cart);
        if(cart.getProducts().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Some products may aren't available");
        }
        float disc;
        if(discount==null){
            disc=1f;
        }else{
            disc=discount.floatValue();
        }
        if(cart.getId()!= null && cart.getId().equals(user.getCart().getId())){
            this.orderService.closeCart(user,disc);
        }else{
            this.orderService.closeOrder(user,cart,disc);
        }
        session.removeAttribute("cart");
        session.removeAttribute("discount");
        model.addAttribute("date", LocalDate.now());
        model.addAttribute("totalPrice", cart.getPrice());
        model.addAttribute("id", user.getId());

        return "success";
    }
    
}
