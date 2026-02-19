package com.canaryshop.canaryshop;

import java.sql.Blob;
import java.util.LinkedList;

import jakarta.persistence.*;


@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Lob
    private Blob images;
}
