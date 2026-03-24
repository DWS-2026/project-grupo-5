package com.canaryshop.canaryshop.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.canaryshop.canaryshop.entities.OrderProduct;
import com.canaryshop.canaryshop.entities.Product;
import com.canaryshop.canaryshop.entities.Review;
import com.canaryshop.canaryshop.repositories.OrderProductRepository;
import com.canaryshop.canaryshop.repositories.ProductsRepository;

@Service
public class ProductService {
    @Autowired
    private ProductsRepository products;
    @Autowired
    private OrderProductRepository opp;

    public Product getProduct(long id) throws ResponseStatusException {
        Optional<Product> request = products.findById(id);
        if (request.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        return request.get();
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
        List<OrderProduct> list= opp.findByProduct(product);
        for (OrderProduct temp : list){
            opp.delete(temp);
        }
        products.deleteById(product.getId());
    }
    public void editProduct(Product product, Product modification){
        product.copy(modification);
        if (!product.isValid()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product invalid");
        }
        products.save(product);
    }
    public Page<Product> getReportedProducts(String name, String description, Pageable page){
        if (name!=null && description!=null){
            return products.findReportedProductsByNameAndDescription(name, description, page);
        }else{
            return products.findReportedProduct(page);
        }
    }
    public Product getByReview(Review review){
        Optional<Product> p= this.products.findByReviewsId(review.getId());
        if(p.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        return p.get();
    }
}
