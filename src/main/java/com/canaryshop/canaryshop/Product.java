package com.canaryshop.canaryshop;

import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.*;

@Entity

public class Product {

    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @OneToMany(cascade = CascadeType.ALL)
    private final List<Image> images = new LinkedList<>();
    private String name;
    private String description;
    private Double price;
    private Integer stock;

    @OneToMany(cascade = CascadeType.ALL)
    private final List<Review> reviewList = new LinkedList<>();
    @OneToMany(cascade = CascadeType.ALL)
    private final List<Image> productImages = new LinkedList<>();

    @ManyToOne
    private User vendor;

    private float rating;
    private Integer reported;

    protected Product() {}
    public Product(String name, String description, Double price, Integer stock) {
        this.name= name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.reported=0;
        this.rating =0;
    }

    public List<Image> getImages() {
        return images;
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
        this.reviewList.add(review);
        this.calculateRating(review.getRating());
    }
    private void calculateRating(Integer reviewRating){
        float tmp=this.rating *(reviewList.size()-1);
        tmp+=reviewRating.floatValue();
        this.rating =tmp/this.reviewList.size();
    }
    
}
