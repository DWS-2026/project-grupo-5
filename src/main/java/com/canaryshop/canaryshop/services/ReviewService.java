package com.canaryshop.canaryshop.services;

import com.canaryshop.canaryshop.entities.Product;
import com.canaryshop.canaryshop.entities.Review;
import com.canaryshop.canaryshop.entities.User;
import com.canaryshop.canaryshop.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
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
    public Review getReview(User user, Product product){
        if (user == null || product == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found");
        }
        Optional<Review> review = reviews.findByAuthorAndProduct(user, product);
        if (review.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found");
        }
        return review.get();
    }
    public void createReview(Review review, Product product){
        if (review == null || !review.isValid()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Review invalid");
        }
        try {
            getReview(review.getAuthor(), product);
        }
        catch (ResponseStatusException ignored) {
            product.addReview(review);
            products.addProduct(product);
            return;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Author already has review in same product");
    }
    public void deleteReview(Review review){
        Product product = review.getProduct();
        if (product == null || !product.getReviews().contains(review)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Review or product not found");
        }
            product.removeReview(review);
            products.addProduct(product);
            reviews.deleteById(review.getId());
    }
    public void editReview(Review review, Review modification){
        if (review == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Review is null");
        }
        Product product = review.getProduct();
        Review modified = review.modify(modification);
        if (!modified.isValid()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Review invalid");
        }
        product.removeReview(review);
        product.addReview(modified);
        products.addProduct(product);
    }
    public List<Review> getByAuthor(User user){
        return this.reviews.findByAuthor(user);
    }
}
