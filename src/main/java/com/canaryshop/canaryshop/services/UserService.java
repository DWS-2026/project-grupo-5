package com.canaryshop.canaryshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.canaryshop.canaryshop.repositories.UserRepository;
import com.canaryshop.canaryshop.entities.Order;
import com.canaryshop.canaryshop.entities.OrderProduct;
import com.canaryshop.canaryshop.entities.Product;
import com.canaryshop.canaryshop.entities.User;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {
    
    @Autowired
    private UserRepository repo;

    public void addUser(User user){
        this.repo.save(user);
    }
    public User getUser(String email){
        Optional<User> user = repo.findByEmail(email);
        if (user.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return user.get();
    }
    public User getUser(Principal principal){
        return principal == null ? null : getUser(principal.getName());
    }

    public User findById(Long id){
        Optional<User> user = repo.findById(id);
        if (user.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return user.get();
    }

    public List<User> findAll() {
        return repo.findAll();
    }
    public List<Product> getOrdersByVendor(long id){
        User u= this.findById(id);
        List<Order> orders = u.getOrders();
        if(orders==null || orders.isEmpty()){
            return null;
        } 
        List<OrderProduct> products= orders.getLast().getProducts();
        List<Product> only6= new LinkedList<>();
        int limite = Math.min(6, products.size()); 
        for (int i = 0; i < limite; i++) {
            only6.add(products.get(i).getProduct());
        }
        return only6;
    }
}
