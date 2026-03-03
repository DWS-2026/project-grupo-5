package com.canaryshop.canaryshop.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.canaryshop.canaryshop.entities.Product;
import com.canaryshop.canaryshop.repositories.ProductsRepository;

@Service
public class ProductService {
    @Autowired
    private ProductsRepository products;

    public Product getProduct(long id){
        Optional<Product> request = products.findById(id);
        if (request.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        return request.get();
    }

    public void addProduct(Product product){
        products.save(product);
    }
    public Page<Product> getPageProducts(String name, String description, Pageable page ){
        if (name!=null && description!=null){
            return products.findByNameContainingOrDescriptionContaining(name, description, page);
        }else{
            return products.findAll(page);
        }
    }
    public void borrarAnuncio(Product product){
        products.delete(product);
    }
}
