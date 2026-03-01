package com.canaryshop.canaryshop.repositories;

import com.canaryshop.canaryshop.entities.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
}
