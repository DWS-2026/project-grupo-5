package com.canaryshop.canaryshop.repositories;

import com.canaryshop.canaryshop.entities.Product;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductsRepository extends JpaRepository<Product, Long>{
    
    Page <Product> findAll(Pageable page);
    Page <Product> findByNameContainingOrDescriptionContaining(String name, String description, Pageable page);
    Page<Product> findByVendorId(long id, Pageable page);
    @Query("SELECT p FROM Product p WHERE (p.name LIKE %:name% OR p.description LIKE %:description%) AND p.reports IS NOT EMPTY")
    Page<Product> findReportedProductsByNameAndDescription(@Param("name") String name, @Param("description") String description,Pageable page);
    @Query("SELECT p FROM Product p WHERE p.reports IS NOT EMPTY")
    Page<Product> findReportedProduct(Pageable page);
    Optional<Product> findByReviewsId(Long reviewId);
}
