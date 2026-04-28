package com.canaryshop.canaryshop.entities;

import java.sql.Blob;

import jakarta.persistence.*;


@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Lob
    private Blob imageFile;
    private int indexInList = 0;

    protected Image(){}
    public Image(Blob file){
        this.imageFile = file;
    }
    public Blob getImageFile(){
        return this.imageFile;
    }
    public void setImageFile(Blob imageFile){
        this.imageFile = imageFile;
    }
    public void setIndexInList(int index){
        this.indexInList = index;
    }
    public int getIndexInList() {
        return indexInList;
    }
    public long getId() {
        return id;
    }
}
