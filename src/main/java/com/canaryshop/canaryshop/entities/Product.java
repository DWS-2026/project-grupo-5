package com.canaryshop.canaryshop.entities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private final List<Review> reviews = new LinkedList<>();
    @OneToMany(cascade = CascadeType.ALL)
    private List<Image> productImages;

    @ManyToOne
    private User vendor;


    private float rating;
    private Integer reported;

    protected Product() {}
    public Product(User vendor, String name, String description, Double price, Integer stock, List<Image> images) {
        this.vendor = vendor;
        this.name= name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.reported=0;
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
    public String getFormattedPrice(){
        double price = getPrice();
        return BigDecimal.valueOf(price).setScale(2, RoundingMode.FLOOR).toString();
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

    public List<Review> getReviewsExcluding(Review... excluding){
        List<Review> reviews = new LinkedList<>(this.reviews);
        reviews.removeAll(Arrays.asList(excluding));
        return reviews;
    }

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
    public String getFormattedRating(){
        float rating = getRating();
        BigDecimal format = BigDecimal.valueOf(rating);
        return format.setScale(2, RoundingMode.HALF_UP).toString();
    }
    public long getId(){
        return id;
    }
    public boolean isValid(){
        return (
                !name.isBlank() &&
                !description.isBlank() &&
                price >= 0.01 &&
                stock > 0 &&
                !productImages.isEmpty()
        );
    }
    public boolean canEdit(User user){
        return !(user==null) && (vendor.equals(user) || user.isAdmin());
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
        review.setProduct(this);
        this.reviews.add(review);
        this.rating/=this.reviews.size();
    }
    public void removeReview(Review review){
        this.calculateRating(-(review.getRating()));
        this.reviews.remove(review);
        review.setProduct(null);
        if (reviews.isEmpty()){
            return;
        }
        this.rating/=this.reviews.size();
    }
    private void calculateRating(Integer reviewRating){
        this.rating*=(reviews.size());
        this.rating+=reviewRating.floatValue();
    }
    public Image getFirstImage() {
    if (productImages != null && !productImages.isEmpty()) {
        return productImages.get(0);
    }
    return null;
}
    
}
