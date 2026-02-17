package com.canaryshop.canaryshop;

import java.sql.Blob;
import java.util.LinkedList;
import java.util.List;

import org.springframework.data.annotation.Id;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;

@Entity

public class Product {

    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @OneToMany(cascade = CascadeType.ALL)
    private LinkedList<Image> images;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private String vendetor;

    @OneToMany(cascade = CascadeType.ALL)
    private LinkedList<Review> reviewList= new LinkedList<>();
    private float valoration;
    private Integer reported;

    protected Product() {}
    public Product(String name, String description, Double price, Integer stock, String vendetor, String urlImage) {
        this.name= name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.vendetor = vendetor;
        this.urlImage = urlImage;
        this.reported=0;
        this.valoration=0;
    }

    public String getUrlImage() {
        return urlImage;
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

    public String getVendetor() {
        return vendetor;
    }

    public List<Review> getReviewList() {
        return reviewList;
    }

    public float getValoration() {
        return valoration;
    }
    
    public void report(){
        this.reported+=1;
    }
    public void serReports(){
        this.reported=0;
    }

    public Integer getReported() {
        return reported;
    }
    public void addReview(Review review){
        this.reviewList.add(review);
        this.calulateValoration(review.getValoration());
    }
    private void calulateValoration(Integer reviewValoration){
        float tmp=this.valoration*reviewList.size()-1;
        tmp+=reviewValoration.floatValue();
        this.valoration=Math.round((tmp/this.reviewList.size())*10.0)/10; 
    }
    
}
