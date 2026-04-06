package com.canaryshop.canaryshop.entities;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.canaryshop.canaryshop.services.NumberFormatter;
import jakarta.persistence.*;

@Entity

public class Product {

    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String description;
    private Double price;
    private Integer stock;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private final List<Review> reviews = new LinkedList<>();
    @OneToMany(cascade = CascadeType.ALL)
    private List<Image> productImages;

    @ManyToOne
    private User vendor;


    private float rating;
    @ElementCollection
    private List<String> reports = new LinkedList<>();

    protected Product() {}
    public Product(User vendor, String name, String description, Double price, Integer stock, List<Image> images) {
        this.vendor = vendor;
        this.name= name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.rating =0;
        this.productImages=images;
    }
    public Product(User vendor, String name, String description, Double price, Integer stock, Image... images) {
        this(vendor, name, description, price, stock, new LinkedList<>(List.of(images)));
    }

    public List<Image> getProductImages() {
        return productImages;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }
    // Formats product price to two decimal places
    public String getFormattedPrice(){
        return NumberFormatter.getFormattedNumber(this.price);
    }

    public Integer getStock() {
        return stock;
    }

    public User getVendor() {
        return vendor;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    // Returns a list of reviews for the current product excluding provided reviews
    public List<Review> getReviewsExcluding(Review... excluding){
        List<Review> reviews = new LinkedList<>(this.reviews);
        reviews.removeAll(Arrays.asList(excluding));
        return reviews;
    }

    // Copies over a product's details while keeping id, vendor and reviews
    // Used for editing the current product
    public void copy(Product product){
        name = product.name;
        description = product.description;
        price = product.price;
        stock = product.stock;
        if (!product.productImages.isEmpty()){
            productImages = product.productImages;
        }
    }

    public float getRating() {
        return rating;
    }
    // Formats the product rating to two decimal places
    public String getFormattedRating(){
        return NumberFormatter.getFormattedNumber(this.rating);
    }
    public long getId(){
        return id;
    }
    // Returns whether the current product is considered valid, and can be added to the database
    public boolean isValid(){
        return (
                !name.isBlank() &&
                !description.isBlank() &&
                price >= 0.01 &&
                stock > 0 &&
                !productImages.isEmpty()
        );
    }
    // Returns whether a given user can edit the current product
    public boolean canEdit(User user){
        return !(user==null) && (vendor.equals(user) || user.isAdmin());
    }

    public void report(String report){
        this.reports.add(report);
    }
    public void resetReports(){
        this.reports.clear();
    }
    public void unreport(String report){
        this.reports.remove(report);
    }
    public List<String> getReported() {
        return reports;
    }
    // Adds a review to the current product's review list and modifies the product rating
    public void addReview(Review review){
        this.modifyRating(review.getRating());
        review.setProduct(this);
        this.reviews.add(review);
    }
    // Removes a review from the current product's review list and modifies the product rating
    public void removeReview(Review review){
        this.modifyRating(-review.getRating());
        review.setProduct(null);
        this.reviews.remove(review);
    }
    // Calculates the new product rating based on the provided review rating
    private void modifyRating(Integer reviewRating){
        this.rating*=reviews.size();
        this.rating+=reviewRating.floatValue();
        // Calculates the expected new size of the review list
        int newSize = reviewRating > 0 ? reviews.size()+1 : reviews.size()-1;
        if (newSize == 0){
            return;
        }
        this.rating/=newSize;
    }
    public void decreaseStock(){
        this.stock-=1;
    }

    public boolean isAvailable(){
        return this.stock > 0;
    }
}