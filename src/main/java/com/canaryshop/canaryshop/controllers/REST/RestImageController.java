package com.canaryshop.canaryshop.controllers.REST;

import com.canaryshop.canaryshop.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/images")
public class RestImageController {
    @Autowired
    private ImageService images;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getImageData(@PathVariable long id){
        Resource image = images.getImage(id);
        MediaType mediaType = images.getImageMediaType(image);
        return ResponseEntity.ok().contentType(mediaType).body(image);
    }
}
