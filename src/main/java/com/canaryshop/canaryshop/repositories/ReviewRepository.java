package com.canaryshop.canaryshop.repositories;

import com.canaryshop.canaryshop.entities.Product;
import com.canaryshop.canaryshop.entities.Review;
import com.canaryshop.canaryshop.entities.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    public Optional<Review> findByAuthorAndProduct(User author, Product product);

    Page<Review> findByAuthor(User author, Pageable page);

    boolean existsByAuthorAndProduct(User author, Product product);
}
