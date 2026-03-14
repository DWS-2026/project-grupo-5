package com.canaryshop.canaryshop.entities;

import jakarta.persistence.*;

import java.util.LinkedList;
import java.util.List;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String description;
    private Integer rating;
    private String name;

    @OneToOne
    private User author;
    @OneToMany(cascade=CascadeType.ALL)
    private List<Image> images;
    @ManyToOne
    private Product product;

    
    protected Review() {}

    public Review(User author, String description, Integer rating, String name, List<Image> images) {
        this.author = author;
        this.description = description;
        this.rating=rating;
        this.name=name;
        this.images=images;
    }
    public Review(User author, String description, Integer rating, String name, Image... images) {
        this(author, description, rating, name, new LinkedList<>(List.of(images)));
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public Product getProduct(){
        return product;
    }
    public void setProduct(Product product){
        this.product = product;
    }
    public User getAuthor() {
        return author;
    }
    public Long getId(){
        return id;
    }
    public boolean isValid(){
        return (
                !description.isBlank() &&
                rating > 0 &&
                !name.isBlank()
        );
    }
    public boolean canEdit(User user){
        return !(user==null) && (author.equals(user) || user.getRoles().contains("ADMIN"));
    }
    public void copy(Review review){
        description = review.description;
        rating = review.rating;
        name = review.name;
        if (!review.images.isEmpty()){
            images = review.images;
        }
    }
    public void setAuthor(User author){
        this.author = author;
    }
    public Integer getRating() {
        return rating;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    public void setName(String name) {
        this.name = name;
    }
    
}
