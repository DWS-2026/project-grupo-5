package com.canaryshop.canaryshop;

import com.canaryshop.canaryshop.entities.Product;
import com.canaryshop.canaryshop.entities.User;
import com.canaryshop.canaryshop.entities.Image;
import com.canaryshop.canaryshop.services.ImageService;
import com.canaryshop.canaryshop.services.ProductService;
import com.canaryshop.canaryshop.services.UserService;

import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class DataBaseInitializer {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ProductService productService;
    @Autowired
    private ImageService imageService;
    
    @PostConstruct
    public void initDatabase() {
        userService.addUser(new User("Admin", "admin@canaryshop.com", passwordEncoder.encode("admin"),"User","Admin"));
        userService.addUser(new User("User1", "user1@canaryshop.com", passwordEncoder.encode("user1"),"User"));
        Image Image1 = this.imageService.createImage("src/main/resources/static/assets/logo.png");
        productService.addProduct(new Product("Samsung A56", "Buena calidad", 300.50, 1, Image1));
    }
}
