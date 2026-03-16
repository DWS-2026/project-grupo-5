package com.canaryshop.canaryshop.controllers;

import com.canaryshop.canaryshop.entities.Image;
import com.canaryshop.canaryshop.entities.Product;
import com.canaryshop.canaryshop.entities.Review;
import com.canaryshop.canaryshop.entities.User;
import com.canaryshop.canaryshop.services.ImageService;
import com.canaryshop.canaryshop.services.ProductService;
import com.canaryshop.canaryshop.services.ReviewService;
import com.canaryshop.canaryshop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@Controller
public class ProductManager {

    @Autowired
    private ProductService products;
    @Autowired
    private ReviewService reviews;
    @Autowired
    private ImageService images;
    @Autowired
    private UserService users;

    @GetMapping({"/products/{id}", "/product/{id}"})
    public String serveProduct(Model model, @PathVariable long id, Principal principal) {
        Product product = products.getProduct(id);
        User user = users.getUser(principal);
        model.addAttribute("product", product);
        model.addAttribute("canEditProduct", product.canEdit(user));
        // This mess is necessary to separate all reviews from the current user's, if it exists
        Review userReview;
        try { userReview = reviews.getReview(user, product); }
        catch (ResponseStatusException exception) { userReview = null; }
        model.addAttribute("userReview", userReview);
        model.addAttribute("productReviews", product.getReviewsExcluding(userReview));
        return "product";
    }
    @GetMapping("/product/new")
    public String serveNewProductForm(){
        return "addProduct";
    }
    @PostMapping("/product/new")
    public String createNewProduct(List<MultipartFile> imageFiles, @RequestParam String title, @RequestParam String description, @RequestParam Double price, @RequestParam Integer stock, Principal principal){
        List<Image> imageList = images.createImages(imageFiles);
        User user = users.getUser(principal);
        Product product = new Product(user, title, description, price, stock, imageList);
        products.addProduct(product);
        return "redirect:/product/" + product.getId();
    }
    @PostMapping("/product/{id}/delete")
    public String deleteProduct(@PathVariable long id, Principal principal) {
        Product product = products.getProduct(id);
        User user = users.getUser(principal);
        if (!product.canEdit(user)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User cannot modify product");
        }
        products.deleteProduct(product);
        return "redirect:/";
    }
    @GetMapping("/product/{id}/edit")
    public String serveEditProductForm(@PathVariable long id, Model model, Principal principal){
        Product product = products.getProduct(id);
        User user = users.getUser(principal);
        if (!product.canEdit(user)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User cannot modify product");
        }
        model.addAttribute("product", product);
        return "addProduct";
    }
    @PostMapping("/product/{id}/edit")
    public String editProduct(@PathVariable long id, List<MultipartFile> imageFiles, @RequestParam String title, @RequestParam String description, @RequestParam Double price, @RequestParam Integer stock, Principal principal){
        Product product = products.getProduct(id);
        User user = users.getUser(principal);
        if (!product.canEdit(user)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User cannot modify product");
        }
        Product modification = new Product(user, title, description, price, stock, images.createImages(imageFiles));
        products.editProduct(product, modification);
        return "redirect:/product/" + product.getId();
    }
    @PostMapping("/product/{id}/review/new")
    public String addNewReviewToProduct(@PathVariable long id, @RequestParam String title, @RequestParam int stars, @RequestParam String description, List<MultipartFile> imageFiles, Principal principal){
        List<Image> imageList = images.createImages(imageFiles);
        User user = users.getUser(principal);
        Review review = new Review(user, description, stars, title, imageList);
        Product product = products.getProduct(id);
        reviews.createReview(review, product);
        return "redirect:/product/" + product.getId();
    }
    @PostMapping("/product/{product_id}/review/{review_id}/delete")
    public String deleteReview(@PathVariable long product_id, @PathVariable long review_id, Principal principal){
        Review review = reviews.getReview(review_id);
        User user = users.getUser(principal);
        if (!review.canEdit(user)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User cannot modify review");
        }
        Product product = products.getProduct(product_id);
        reviews.deleteReview(review, product);
        return "redirect:/product/" + product.getId();
    }
    @GetMapping("/product/{product_id}/review/{review_id}/edit")
    public String serveEditReviewForm(@PathVariable long product_id, @PathVariable long review_id, Model model, Principal principal){
        Review review = reviews.getReview(review_id);
        User user = users.getUser(principal);
        if (!review.canEdit(user)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User cannot modify review");
        }
        model.addAttribute("edit-review", review);
        model.addAttribute("product", products.getProduct(product_id));
        return "product";
    }
    @PostMapping("/product/{product_id}/review/{review_id}/edit")
    public String editReview(@PathVariable long product_id, @PathVariable long review_id, @RequestParam String title, @RequestParam int stars, @RequestParam String description, List<MultipartFile> imageFiles, Principal principal){
        Review review = reviews.getReview(review_id);
        User user = users.getUser(principal);
        if (!review.canEdit(user)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User cannot modify review");
        }
        Review modification = new Review(user, description, stars, title, images.createImages(imageFiles));
        reviews.editReview(review, modification);
        return "redirect:/product/" + product_id;
    }
}
