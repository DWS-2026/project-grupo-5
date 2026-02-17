package com.canaryshop.canaryshop;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String description;
    private String author;
    private Integer valoration;
    private String name;

    
    protected Review() {}

    public Review(String description, String author, Integer valoration, String name) {

        this.description = description;
        this.author = author;
        this.valoration = valoration;
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getAuthor() {
        return author;
    }
    public Integer getValoration() {
        return valoration;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setValoration(Integer valoration) {
        this.valoration = valoration;
    }
    public void setName(String name) {
        this.name = name;
    }
    
}
