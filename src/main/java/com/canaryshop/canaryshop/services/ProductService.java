package com.canaryshop.canaryshop.services;

import com.canaryshop.canaryshop.entities.Product;
import com.canaryshop.canaryshop.repositories.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductsRepository products;

    public Product getProduct(Long id){
        Optional<Product> request = products.findById(id);
        if (request.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        return request.get();
    }

    public void addProduct(Product product){
        products.save(product);
    }
}
