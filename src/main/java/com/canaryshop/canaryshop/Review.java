package com.canaryshop.canaryshop;

import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

import java.util.LinkedList;

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
    private LinkedList<Image> images;

    
    protected Review() {}

    public Review(String description, Integer rating, String name) {
        this.description = description;
        this.rating = rating;
        this.name = name;
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
