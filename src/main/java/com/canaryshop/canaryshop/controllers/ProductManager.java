package com.canaryshop.canaryshop.controllers;

import com.canaryshop.canaryshop.entities.Image;
import com.canaryshop.canaryshop.entities.Product;
import com.canaryshop.canaryshop.entities.Review;
import com.canaryshop.canaryshop.services.ImageService;
import com.canaryshop.canaryshop.services.ProductService;
import com.canaryshop.canaryshop.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedList;
import java.util.List;

@Controller
public class ProductManager {

    @Autowired
    private ProductService products;
    @Autowired
    private ReviewService reviews;
    @Autowired
    private ImageService images;

    @GetMapping({"/products/{id}", "/product/{id}"})
    public String serveProduct(Model model, @PathVariable long id) {
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
    @PostMapping("/product/{id}/delete")
    public String deleteProduct(@PathVariable long id) {
        Product product = products.getProduct(id);
        products.deleteProduct(product);
        return "redirect:/";
    }
    @GetMapping("/product/{id}/edit")
    public String serveEditProductForm(@PathVariable long id, Model model){
        Product product = products.getProduct(id);
        model.addAttribute("product", product);
        return "addProduct";
    }
    @PostMapping("/product/{id}/edit")
    public String editProduct(@PathVariable long id, List<MultipartFile> imageFiles, @RequestParam String title, @RequestParam String description, @RequestParam Double price, @RequestParam Integer stock){
        Product product = products.getProduct(id);
        Product modification = new Product(title, description, price, stock, images.createImages(imageFiles));
        products.editProduct(product, modification);
        return "redirect:/product/" + product.getId();
    }
    @PostMapping("/product/{id}/review/new")
    public String addNewReviewToProduct(@PathVariable long id, @RequestParam String title, @RequestParam int stars, @RequestParam String description, List<MultipartFile> imageFiles){
        List<Image> imageList = images.createImages(imageFiles);
        Review review = new Review(description, stars, title, imageList);
        Product product = products.getProduct(id);
        reviews.createReview(review, product);
        return "redirect:/product/" + product.getId();
    }
    @PostMapping("/product/{product_id}/review/{review_id}/delete")
    public String deleteReview(@PathVariable long product_id, @PathVariable long review_id){
        Product product = products.getProduct(product_id);
        Review review = reviews.getReview(review_id);
        reviews.deleteReview(review, product);
        return "redirect:/product/" + product.getId();
    }
    @GetMapping("/product/{product_id}/review/{review_id}/edit")
    public String serveEditReviewForm(@PathVariable long product_id, @PathVariable long review_id, Model model){
        Review review = reviews.getReview(review_id);
        model.addAttribute("edit-review", review);
        model.addAttribute("product", products.getProduct(product_id));
        return "product";
    }
    @PostMapping("/product/{product_id}/review/{review_id}/edit")
    public String editReview(@PathVariable long product_id, @PathVariable long review_id, @RequestParam String title, @RequestParam int stars, @RequestParam String description, List<MultipartFile> imageFiles){
        Review review = reviews.getReview(review_id);
        Review modification = new Review(description, stars, title, images.createImages(imageFiles));
        reviews.editReview(review, modification);
        return "redirect:/product/" + product_id;
    }
}
