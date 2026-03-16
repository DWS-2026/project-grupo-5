package com.canaryshop.canaryshop.controllers;

import java.util.List;

import com.canaryshop.canaryshop.services.PageHandler;
import com.canaryshop.canaryshop.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.canaryshop.canaryshop.entities.Image;
import com.canaryshop.canaryshop.entities.Product;
import com.canaryshop.canaryshop.entities.User;
import com.canaryshop.canaryshop.services.ImageService;
import com.canaryshop.canaryshop.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class UserManager {
    
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private PageHandler pageHandler;

    @GetMapping("/user/{id}")
    public String getUser(Model model, @PathVariable long id) {
        model.addAttribute("user",this.userService.findById(id));
        List<Product> products = this.productService.getProductsByVendor(id);
        products = products.subList(0, products.size() >= 5 ? 4 : products.size());
        List<Product> orders = this.userService.getOrdersByVendor(id);
        model.addAttribute("products",products);
        model.addAttribute("orders",orders);
        return "user";
    }
    @GetMapping("/user/{id}/products")
    public String getAllUserProducts(Model model, @PathVariable long id, @PageableDefault(size=12) Pageable page) {

        Page<Product> results = this.productService.getProductsByVendor(id, page);
        pageHandler.handleProductPage(model, results, page);
        return "index";
    }
    @PostMapping("/user/{id}/image")
    public String postMethodName(Model model, @RequestParam MultipartFile image, @PathVariable long id) {
        Image img= this.imageService.createImage(image);
        this.imageService.addImage(img);
        User u=this.userService.findById(id);
        u.setImage(img);
        this.userService.addUser(u);
        return "redirect:/user/"+id;
    }
    @PostMapping("/user/{id}/edit")
    public String postMethodName(Model model, @RequestParam String userName, @RequestParam String email, @PathVariable long id) {
        User u=this.userService.findById(id);
        u.setEmail(email);
        u.setUsername(userName);
        this.userService.addUser(u);
        return "redirect:/user/"+id;
    }
    
    
    
}
