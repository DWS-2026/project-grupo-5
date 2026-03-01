package com.canaryshop.canaryshop.entities;

import java.sql.Blob;

import jakarta.persistence.*;


@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Lob
    private Blob images;
}
