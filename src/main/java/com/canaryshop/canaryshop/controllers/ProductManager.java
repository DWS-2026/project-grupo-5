package com.canaryshop.canaryshop.controllers;

import com.canaryshop.canaryshop.entities.Image;
import com.canaryshop.canaryshop.entities.Product;
import com.canaryshop.canaryshop.services.ImageService;
import com.canaryshop.canaryshop.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedList;
import java.util.List;

@Controller
public class ProductManager {

    @Autowired
    private ProductService products;
    @Autowired
    private ImageService images;

    @GetMapping({"/products/{id}", "/product/{id}"})
    public String serveProduct(Model model, @PathVariable long id){
        Product product = products.getProduct(id);
        model.addAttribute("product", product);
        return "product";
    }

    @GetMapping("/product/new")
    public String serveNewProductForm(){
        return "addProduct";
    }

    @PostMapping("/product/new")
    public String createNewProduct(List<MultipartFile> imageFiles, @RequestParam String title, @RequestParam String description, @RequestParam Double price, @RequestParam Integer stock){
        List<Image> imageList = images.createImages(imageFiles);
        Product product = new Product(title, description, price, stock, imageList);
        products.addProduct(product);
        return "redirect:/product/" + product.getId();
    }
}
