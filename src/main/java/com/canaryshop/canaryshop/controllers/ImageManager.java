package com.canaryshop.canaryshop.controllers;

import com.canaryshop.canaryshop.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ImageManager {

    @Autowired
    private ImageService images;

    @GetMapping("/images/{id}")
    public ResponseEntity<Object> retrieveImage(@PathVariable long id){
        Resource image = images.getImage(id);
        MediaType mediaType = MediaTypeFactory.getMediaType(image).orElse(MediaType.IMAGE_JPEG);
        return ResponseEntity.ok().contentType(mediaType).body(image);
    }
}
