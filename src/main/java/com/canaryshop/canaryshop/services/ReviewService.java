package com.canaryshop.canaryshop.services;

import com.canaryshop.canaryshop.entities.Product;
import com.canaryshop.canaryshop.entities.Review;
import com.canaryshop.canaryshop.entities.User;
import com.canaryshop.canaryshop.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.NoSuchElementException;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviews;
    @Autowired
    private ProductService products;

    public Review getReview(long id){
        return reviews.findById(id).orElseThrow();
    }
    public Review getReview(User user, Product product){
        return reviews.findByAuthorAndProduct(user, product).orElseThrow();
    }
    public void createReview(Review review, Product product){
        if (review == null || !review.isValid()){
            throw new IllegalArgumentException();
        }
        if (reviews.existsByAuthorAndProduct(review.getAuthor(), product)){
            throw new IllegalArgumentException();
        }
        product.addReview(review);
        products.addProduct(product);
    }
    public void deleteReview(Review review){
        Product product = review.getProduct();
        if (product == null || !product.getReviews().contains(review)){
            throw new NoSuchElementException();
        }
        product.removeReview(review);
        products.addProduct(product);
        reviews.deleteById(review.getId());
    }
    public void deleteReview(long id){
        Review review = reviews.findById(id).orElseThrow();
        deleteReview(review);
    }
    public void editReview(Review review, Review modification){
        if (review == null) {
            throw new IllegalArgumentException();
        }
        Product product = review.getProduct();
        Review modified = review.modify(modification);
        if (!modified.isValid()){
            throw new IllegalArgumentException();
        }
        product.removeReview(review);
        product.addReview(modified);
        products.addProduct(product);
    }
}
