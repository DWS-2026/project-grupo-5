package com.canaryshop.canaryshop.repositories;

import com.canaryshop.canaryshop.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
