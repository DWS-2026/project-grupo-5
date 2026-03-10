package com.canaryshop.canaryshop.controllers;

import com.canaryshop.canaryshop.entities.*;
import com.canaryshop.canaryshop.services.ProductService;
import com.canaryshop.canaryshop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.security.Principal;


@Controller
public class CartManager {

    @Autowired
    private ProductService products; // Siguiendo tu estilo de ProductManager

    @Autowired
    private UserService userService;

    @GetMapping("/cart")
    public String serveCart(Model model, HttpSession session, Principal principal) {
        Order cart = (Order) session.getAttribute("cart");

        // Si no está en sesión pero el usuario está logueado, recuperamos su carrito de la BD
        if (cart == null && principal != null) {
            User user = userService.findByUsername(principal.getName()).orElse(null);
            if (user != null && user.getCart() != null) {
                cart = user.getCart();
                session.setAttribute("cart", cart);
            }
        }

        if (cart != null) {
            // Pasamos la lista de OrderProduct (entidad intermedia)
            model.addAttribute("items", cart.getProducts());
            model.addAttribute("totalPrice", cart.getPrice());
        } else {
            model.addAttribute("totalPrice", 0.0);
        }

        model.addAttribute("showCart", true);
        return "cart";
    }

    @PostMapping("/cart/add/{id}")
    public String addToCart(@PathVariable long id, HttpSession session, Principal principal) {
        Product product = products.getProduct(id);
        Order cart = (Order) session.getAttribute("cart");

        if (cart == null) {
            cart = new Order();
        }

        // addProduct internamente crea el OrderProduct(this, product, 1)
        cart.addProduct(product);
        session.setAttribute("cart", cart);

        // Si hay usuario, vinculamos y persistimos en BD
        if (principal != null) {
            User user = userService.findByUsername(principal.getName()).orElseThrow();
            user.setCart(cart); // Asegúrate de tener este setter en User.java
            userService.addUser(user);
        }

        return "redirect:/cart";
    }

    @PostMapping("/cart/remove/{id}")
    public String removeFromCart(@PathVariable long id, HttpSession session, Principal principal) {
        Order cart = (Order) session.getAttribute("cart");
        Product product = products.getProduct(id);

        if (cart != null) {
            cart.removeProduct(product);
            session.setAttribute("cart", cart);

            if (principal != null) {
                User user = userService.findByUsername(principal.getName()).orElseThrow();
                userService.addUser(user);
            }
        }

        return "redirect:/cart";
    }
}