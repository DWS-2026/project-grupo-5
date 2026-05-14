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

    @ManyToOne
    private User author;
    @ManyToOne
    private Product product;

    private List<String> files = new LinkedList<>();

    public Review() {
    }

    public Review(User author, String description, Integer rating, String name, List<String> files) {
        this.author = author;
        this.description = description;
        this.rating = rating;
        this.name = name;
        this.files = files;
    }

    public Review(User author, String description, Integer rating, String name, String... files) {
        this(author, description, rating, name, new LinkedList<>(List.of(files)));
    }

    public Review(Review review) {
        this.id = review.id;
        this.description = review.description;
        this.rating = review.rating;
        this.name = review.name;
        this.author = review.author;
        this.files = review.files;
        this.product = review.product;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getAuthor() {
        return author;
    }

    public Long getId() {
        return id;
    }

    public boolean isValid() {
        return (!description.isBlank() &&
                rating > 0 &&
                !name.isBlank());
    }

    public boolean canEdit(User user) {
        return !(user == null) && (author.equals(user) || user.isAdmin());
    }

    public Review modify(Review review) {
        Review copy = new Review(this);
        copy.description = review.description;
        copy.rating = review.rating;
        copy.name = review.name;
        if (review.files != null && !review.files.isEmpty()) {
            copy.files = review.files;
        }
        return copy;
    }

    public void setAuthor(User author) {
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

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

}