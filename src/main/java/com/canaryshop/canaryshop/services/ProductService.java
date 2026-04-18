package com.canaryshop.canaryshop.services;

import java.io.InvalidObjectException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.canaryshop.canaryshop.entities.OrderProduct;
import com.canaryshop.canaryshop.entities.Product;
import com.canaryshop.canaryshop.repositories.OrderProductRepository;
import com.canaryshop.canaryshop.repositories.ProductsRepository;

@Service
public class ProductService {
    @Autowired
    private ProductsRepository products;
    @Autowired
    private OrderProductRepository opp;

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
        if (name!=null && description!=null){
            return products.findByNameContainingOrDescriptionContaining(name, description, page);
        }else{
            return products.findAll(page);
        }
    }

    // Deletes all intermediate OrderProduct entities associated with the product, then the product itself
    public void deleteProduct(Product product){
        List<OrderProduct> list= opp.findByProduct(product);
        opp.deleteAll(list);
        products.deleteById(product.getId());
    }

    // Edits the product and adds it to the database, throws HTTP error if the modified product is invalid
    public void editProduct(Product product, Product modification){
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
}
