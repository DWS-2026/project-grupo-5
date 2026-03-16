package com.canaryshop.canaryshop.repositories;

import com.canaryshop.canaryshop.entities.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository extends JpaRepository<Product, Long>{
    
    Page <Product> findAll(Pageable page);
    Page <Product> findByNameContainingOrDescriptionContaining(String name, String description, Pageable page);
    Page<Product> findByVendorId(long id, Pageable page);
}
