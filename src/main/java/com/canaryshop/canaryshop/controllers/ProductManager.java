package com.canaryshop.canaryshop.controllers;

import com.canaryshop.canaryshop.entities.Product;
import com.canaryshop.canaryshop.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedList;

@Controller
public class ProductManager {

    @Autowired
    private ProductService products;

    @GetMapping({"/products/{id}", "/product/{id}"})
    public String serveProduct(Model model, @PathVariable long id){
        Product product = products.getProduct(id);
        model.addAttribute(product);
        return "product";
    }

    @GetMapping("/products/new")
    public String serveNewProductForm(){
        return "addProduct";
    }

    @PostMapping("/products/new")
    public String createNewProduct(@RequestParam String title, @RequestParam String description, @RequestParam Double price, @RequestParam Integer stock){
        Product product = new Product(title, description, price, stock, new LinkedList<>());
        products.addProduct(product);
        return "redirect:/products/" + product.getId(); // todo: make the findById query work
    }
}
