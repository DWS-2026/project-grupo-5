package com.canaryshop.canaryshop.services;

import java.util.List;
import java.util.Optional;

import com.canaryshop.canaryshop.entities.User;
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
    @Autowired
    private UserService users;

    public Product getProduct(long id) throws ResponseStatusException {
        Optional<Product> request = products.findById(id);
        if (request.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        return request.get();
    }

    public List<Product> getProductsByVendor(long id){
        User user = users.findById(id);
        return user.getProducts();
    }

    public Page<Product> getProductsByVendor(long id, Pageable page){
        return products.findByVendorId(id, page);
    }

    public void addProduct(Product product){
        if (!product.isValid()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product invalid");
        }
        products.save(product);
    }
    public Page<Product> getPageProducts(String name, String description, Pageable page){
        if (name!=null && description!=null){
            return products.findByNameContainingOrDescriptionContaining(name, description, page);
        }else{
            return products.findAll(page);
        }
    }
    public void deleteProduct(Product product){
        products.deleteById(product.getId());
    }
    public void editProduct(Product product, Product modification){
        product.copy(modification);
        if (!product.isValid()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product invalid");
        }
        products.save(product);
    }
}
