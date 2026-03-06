package com.canaryshop.canaryshop.entities;

import jakarta.persistence.*;

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

    
    protected Review() {}

    public Review(String description, Integer rating, String name, List<Image> images) {
        this.description = description;
        this.rating=rating;
        this.name=name;
        this.images=images;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public User getAuthor() {
        return author;
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
