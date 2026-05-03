package com.canaryshop.canaryshop.services;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.canaryshop.canaryshop.entities.Product;
import com.canaryshop.canaryshop.entities.OrderProduct;
import com.canaryshop.canaryshop.entities.Order;
import com.canaryshop.canaryshop.entities.User;
import com.canaryshop.canaryshop.repositories.OrderProductRepository;
import com.canaryshop.canaryshop.repositories.OrderRepository;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderProductRepository orderProductRepository;

    public Order getOrder(long id, User u) {
        Order o = orderRepository.findById(id).orElseThrow();
        if (!u.getOrders().contains(o)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "This user can not access to this order");
        }
        return o;
    }

    public void addOrder(Order o) {
        orderRepository.save(o);
    }

    // To get the discount for the code
    public float getDiscount(String code) {
        float discount = switch (code == null ? "" : code) {
            case "DiegoEsElMejor" -> 0;
            case "JaimeEsElMejor" -> 0.25f;
            case "VictorEsElMejor" -> 0.5f;
            case "JorgeEsElMejor" -> 0.75f;
            default -> 1f;
        };
        return discount;
    }

    // To create a new cart for the user
    public void closeCart(User u, float discount) {
        this.closeOrder(u, u.getCart(), discount);
        Order newCart = new Order();
        u.setCart(newCart);
        this.orderRepository.save(newCart);
        this.userService.addUser(u);
    }

    // To close and order with the discount
    public void closeOrder(User u, Order order, float discount) {
        order.closeOrder();
        order.setDiscount(discount);
        this.productsPurchased(order);
        u.addOrder(order);
        this.orderRepository.save(order);
        this.userService.addUser(u);
    }

    // To decrease the stock of the products
    private void productsPurchased(Order o) {
        o.getProducts().forEach(op -> this.productService.productPurchased(op.getProduct()));
    }

    // Renew the order by removing the out-of-stock products
    public Order renewOrder(Order o) {
        List<OrderProduct> products = new LinkedList<>(o.getProducts());
        for (OrderProduct op : products) {
            Product p = productService.getProduct(op.getProduct().getId());
            if (!p.isAvailable()) {
                o.setProductQuantity(op.getProduct(), 0);
            }
        }
        return this.orderRepository.save(o);
    }

    public List<Order> findAllOrders() {
        return this.orderRepository.findAll();
    }

    public Page<Order> getPageOrders(Pageable pageable, User u) {
        return this.orderRepository.findByUser(u, pageable);
    }

    public Page<OrderProduct> getPageOrderProductsByOrder(long id, Pageable pageable, User u) {
        Order o = this.getOrder(id, u);
        return this.orderProductRepository.findByOrder(o, pageable);
    }

    public OrderProduct getOrderProductByProductAndCart(Product p, Order cart) {
        return this.orderProductRepository.findByProductAndOrder(p, cart).orElseThrow();
    }

    public void clearOder(Order o) {
        List<OrderProduct> products = o.getProducts();
        products.clear();
        this.orderRepository.save(o);
    }

    public Order createTempCart(User user, Long productID) {
        Order temp = new Order();
        if (productID != null) {
            temp.setProductQuantity(this.productService.getProduct(productID), 1);
            temp.setUser(user);
        } else {
            temp = user.getCart();
            temp = this.renewOrder(temp);
        }

        return temp;
    }
}