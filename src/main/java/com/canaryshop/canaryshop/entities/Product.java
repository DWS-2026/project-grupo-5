package com.canaryshop.canaryshop.entities;

import java.util.LinkedList;
import java.util.List;

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

    @OneToMany(cascade = CascadeType.ALL)
    private final List<Review> reviewList = new LinkedList<>();
    @OneToMany(cascade = CascadeType.ALL)
    private List<Image> productImages;

    @ManyToOne
    private User vendor;


    private float rating;
    private Integer reported;

    protected Product() {}
    public Product(String name, String description, Double price, Integer stock, List<Image> images) {
        this.name= name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.reported=0;
        this.rating =0;
        this.productImages=images;
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

    public Integer getStock() {
        return stock;
    }

    public User getVendor() {
        return vendor;
    }

    public List<Review> getReviewList() {
        return reviewList;
    }

    public float getRating() {
        return rating;
    }
    public long getId(){
        return id;
    }

    public void report(){
        this.reported+=1;
    }
    public void resetReports(){
        this.reported=0;
    }

    public Integer getReported() {
        return reported;
    }
    public void addReview(Review review){
        this.calculateRating(review.getRating());
        this.reviewList.add(review);
        this.rating/=this.reviewList.size();
    }
    public void removeReview(Review review){
        this.calculateRating(-(review.getRating()));
        this.reviewList.remove(review);
        this.rating/=this.reviewList.size();
    }
    private void calculateRating(Integer reviewRating){
        this.rating*=(reviewList.size());
        this.rating+=reviewRating.floatValue();
    }
    public void addCart(Order ord){
        this.carts.add(ord);
    }
    public void removeCart(Order ord){
        this.carts.remove(ord);
    }
    
}
