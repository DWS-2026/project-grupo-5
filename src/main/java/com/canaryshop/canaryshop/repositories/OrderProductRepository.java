package com.canaryshop.canaryshop.repositories;

import com.canaryshop.canaryshop.entities.Order;
import com.canaryshop.canaryshop.entities.OrderProduct;
import com.canaryshop.canaryshop.entities.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    List<OrderProduct> findByProduct(Product product);

    Page<OrderProduct> findByOrder(Order order, Pageable pageable);

    Optional<OrderProduct> findByProductAndOrder(Product product, Order order);
}
