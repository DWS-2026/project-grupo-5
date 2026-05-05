package com.canaryshop.canaryshop.services;

import com.canaryshop.canaryshop.entities.Product;
import com.canaryshop.canaryshop.entities.Review;
import com.canaryshop.canaryshop.entities.User;
import com.canaryshop.canaryshop.repositories.ReviewRepository;
import org.owasp.html.PolicyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviews;
    @Autowired
    private ProductService products;
    @Autowired
    private PolicyFactory enrichedTextSanitizer;

    public void modifyCheck(User user, Review review){
        if (!review.canEdit(user)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User cannot modify review");
        }
    }
    public List<Review> getReviews(long product_id) {
        List<Review> reviews = products.getProduct(product_id).getReviews();
        if (reviews.isEmpty()){ throw new NoSuchElementException(); }
        return reviews;
    }
    public Review getReview(long id){
        return reviews.findById(id).orElseThrow();
    }
    public Review getReview(User user, Product product){
        return reviews.findByAuthorAndProduct(user, product).orElseThrow();
    }
    public void createReview(Review review, Product product){
        if (review == null){
            throw new IllegalArgumentException();
        }
        review.setDescription(enrichedTextSanitizer.sanitize(review.getDescription()));
        if (!review.isValid()){
            throw new IllegalArgumentException();
        }
        if (reviews.existsByAuthorAndProduct(review.getAuthor(), product)){
            throw new IllegalArgumentException();
        }
        product.addReview(review);
        products.addProduct(product);
    }
    public void deleteReview(User user, Review review){
        this.modifyCheck(user, review);
        Product product = review.getProduct();
        if (product == null || !product.getReviews().contains(review)){
            throw new NoSuchElementException();
        }
        product.removeReview(review);
        products.addProduct(product);
        reviews.deleteById(review.getId());
    }
    public void editReview(User user, Review review, Review modification){
        if (review == null) {
            throw new IllegalArgumentException();
        }
        this.modifyCheck(user, review);
        Product product = review.getProduct();
        Review modified = review.modify(modification);
        modified.setDescription(enrichedTextSanitizer.sanitize(modified.getDescription()));
        if (!modified.isValid()){
            throw new IllegalArgumentException();
        }
        product.removeReview(review);
        product.addReview(modified);
        products.addProduct(product);
    }
    public Page<Review> getReviewsByAuthor(User u, Pageable page){
        return this.reviews.findByAuthor(u, page);
    }

    public void addFile(Review review, String fileName){
        List<String> files = review.getFiles();
        if (files == null){
            files = new java.util.LinkedList<>();
        }
        files.add(fileName);
        review.setFiles(files);
        reviews.save(review);
    }

        public void removeFile(Review review, String fileName){
            List<String> files = review.getFiles();
            if (files == null || !files.contains(fileName)){
                throw new NoSuchElementException();
            }
            files.remove(fileName);
            review.setFiles(files);
            reviews.save(review);
        }
}
