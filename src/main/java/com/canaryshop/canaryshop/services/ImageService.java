package com.canaryshop.canaryshop.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.sql.rowset.serial.SerialBlob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.canaryshop.canaryshop.entities.Image;
import com.canaryshop.canaryshop.repositories.ImageRepository;

import net.coobird.thumbnailator.Thumbnails;

@Service
public class ImageService {
    @Autowired
    private ImageRepository images;
    private final Logger log = LoggerFactory.getLogger(ImageService.class);

    public Image createImage(MultipartFile imageFile){
        if (imageFile == null || imageFile.isEmpty()){
            return null;
        }
        try {
            SerialBlob imageFileBlob = new SerialBlob(this.resizeMultipartFile(imageFile));
            Image image = new Image(imageFileBlob);
            return image;
        } catch (Exception e){
            log.error("Couldn't load image");
            return null;
        }
    }

    public List<Image> createImages(List<MultipartFile> imageFiles){
        List<Image> imageList = new LinkedList<>();
        for (MultipartFile imageFile: imageFiles){
            Image image = createImage(imageFile);
            if (image == null){
                continue;
            }
            image.setIndexInList(imageList.size());
            imageList.add(image);
        }
        return imageList;
    }

    public Resource getImage(long id) throws ResponseStatusException{
        try {
            Image image = images.findById(id).orElseThrow();
            Blob imageFile = image.getImageFile();
            if (imageFile == null) {
                throw new RuntimeException();
            }
            return new InputStreamResource(imageFile.getBinaryStream());
        } catch (NoSuchElementException | SQLException e){
            log.error("Couldn't retrieve image by id {}", id);
        }
        catch (RuntimeException e) {
            log.error("Image by id {} does not have an image file associated", id);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found");
    }
    public Image createImage(String filePath){
        try {
            Path path = Paths.get(filePath);
            SerialBlob imageFileBlob = new SerialBlob(resizeFile(path));
            Image image = new Image(imageFileBlob);
            return image;
        } catch (Exception e){
            log.error("Couldn't load image");
            return null;
        }
    }
    private byte[] resizeMultipartFile(MultipartFile imageFile) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Thumbnails.of(imageFile.getInputStream())
                .forceSize(800, 800) // Force size to 800x800 
                .outputFormat("png") // Force format to png
                .toOutputStream(outputStream);

        return outputStream.toByteArray();
    }
    private byte[] resizeFile(Path path) throws IOException{
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Thumbnails.of(path.toFile())
                .forceSize(800, 800) // Force size to 800x800 
                .outputFormat("png") // Force format to png
                .toOutputStream(outputStream);

        return outputStream.toByteArray();
    }
    public void addImage(Image img){
        this.images.save(img);
    }

}
