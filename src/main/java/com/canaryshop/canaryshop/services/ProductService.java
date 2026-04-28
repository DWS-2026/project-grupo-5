package com.canaryshop.canaryshop.services;

import com.canaryshop.canaryshop.entities.Image;
import com.canaryshop.canaryshop.entities.OrderProduct;
import com.canaryshop.canaryshop.entities.Product;
import com.canaryshop.canaryshop.entities.User;
import com.canaryshop.canaryshop.repositories.OrderProductRepository;
import com.canaryshop.canaryshop.repositories.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {
    @Autowired
    private ProductsRepository products;
    @Autowired
    private OrderProductRepository opp;
    @Autowired
    private ImageService images;

    public void modifyCheck(User user, Product product){
        if (!product.canEdit(user)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User cannot modify product");
        }
    }

    // Gets product by product id, throws HTTP error if it doesn't exist
    public Product getProduct(long id){
        return products.findById(id).orElseThrow();
    }

    // Returns a product page based on vendor id and requested page
    public Page<Product> getProductsByVendor(long id, Pageable page){
        return products.findByVendorId(id, page);
    }

    // Adds a product to the database, throws HTTP error if the product given is invalid
    public void addProduct(Product product){
        if (!product.isValid()){
            throw new IllegalArgumentException();
        }
        products.save(product);
    }

    // Gets products in a page filtered by name or description, or all products if either are null
    public Page<Product> getPageProducts(String name, String description, Pageable page){
        Page<Product> productsPage;
        if (name==null && description==null){
            productsPage = products.findAll(page);
        } else{
            productsPage = products.findByNameContainingOrDescriptionContaining(name, description, page);
        }
        if (page.getPageNumber() >= productsPage.getTotalPages()){
            throw new IllegalArgumentException();
        }
        if (productsPage.isEmpty()){
            throw new NoSuchElementException();
        }
        return productsPage;
    }

    // Deletes all intermediate OrderProduct entities associated with the product, then the product itself
    public void deleteProduct(User user, Product product){
        this.modifyCheck(user, product);
        List<OrderProduct> list= opp.findByProduct(product);
        opp.deleteAll(list);
        products.deleteById(product.getId());
    }

    // Edits the product and adds it to the database, throws HTTP error if the modified product is invalid
    public void editProduct(User user, Product product, Product modification){
        this.modifyCheck(user, product);
        product.copy(modification);
        if (!product.isValid()){
            throw new IllegalArgumentException();
        }
        products.save(product);
    }

    // Gets reported products in a page by name or description, or all reported products if either are null
    public Page<Product> getReportedProducts(String name, String description, Pageable page) {
        if (name != null && description != null) {
            return products.findReportedProductsByNameAndDescription(name, description, page);
        } else {
            return products.findReportedProduct(page);
        }
    }
    // To decrease stock
    public void productPurchased(Product product){
        if(product.getStock()<1){
            return;
        }else{
            product.decreaseStock();
            this.products.save(product);
        }
    }

    public void addImage(User user, Product product, Image image){
        if (image==null || product==null){
            throw new IllegalArgumentException();
        }
        modifyCheck(user, product);
        List<Image> images = product.getProductImages();
        image.setIndexInList(images.size());
        images.add(image);
        products.save(product);
    }

    public void deleteImage(User user, Product product, Image image){
        if (image==null || product==null){
            throw new IllegalArgumentException();
        }
        modifyCheck(user, product);
        List<Image> images = product.getProductImages();
        if (!images.remove(image)){
            throw new NoSuchElementException();
        }
        for (int i = 0; i<images.size(); i++) {
            images.get(i).setIndexInList(i);
        }
        products.save(product);
        this.images.deleteImage(image);
    }
}
