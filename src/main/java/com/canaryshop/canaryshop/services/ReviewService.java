package com.canaryshop.canaryshop.services;

import com.canaryshop.canaryshop.entities.Product;
import com.canaryshop.canaryshop.entities.Review;
import com.canaryshop.canaryshop.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviews;
    @Autowired
    private ProductService products;

    public Review getReview(long id){
        Optional<Review> review = reviews.findById(id);
        if (review.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found");
        }
        return review.get();
    }
    public void createReview(Review review, Product product){
        product.addReview(review);
        products.addProduct(product);
    }
    public void deleteReview(Review review, Product product){
        if (product.getReviews().contains(review)){
            product.removeReview(review);
            reviews.delete(review);
            products.addProduct(product);
        }
    }
}
