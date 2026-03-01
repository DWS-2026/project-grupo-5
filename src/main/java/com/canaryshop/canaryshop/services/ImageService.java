package com.canaryshop.canaryshop.services;

import com.canaryshop.canaryshop.entities.Image;
import com.canaryshop.canaryshop.repositories.ImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;

@Service
public class ImageService {
    @Autowired
    private ImageRepository images;
    private final Logger log = LoggerFactory.getLogger(ImageService.class);

    public Image createImage(MultipartFile image){
        try {
            SerialBlob imageFile = new SerialBlob(image.getBytes());
            return new Image(imageFile);
        } catch (Exception e){
            log.error("Couldn't load image");
            return null;
        }
    }
}
