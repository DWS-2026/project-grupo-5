package com.canaryshop.canaryshop.services;

import java.security.Principal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.canaryshop.canaryshop.entities.Order;
import com.canaryshop.canaryshop.entities.OrderProduct;
import com.canaryshop.canaryshop.entities.Product;
import com.canaryshop.canaryshop.entities.User;
import com.canaryshop.canaryshop.repositories.UserRepository;


@Service
public class UserService {
    
    @Autowired
    private UserRepository repo;
    @Autowired
    private ProductService productService;

    public void addUser(User user){
        this.repo.save(user);
    }
    public User getUser(String name){
        return repo.findByUsername(name).orElseThrow();
    }
    // To get the user in session
    public User getUser(Principal principal){
        return principal == null ? null : getUser(principal.getName());
    }

    public User findById(Long id){
        return repo.findById(id).orElseThrow();
    }

    public void deleteUser(Long id){
        User u=this.findById(id);
        u.getProducts().forEach(p -> this.productService.deleteProduct(p));
        repo.deleteById(id);
    }

    public List<User> findAll() {
        return repo.findAll();
    }
    // Get a user page depending on the search
    public Page<User> getPageUser(String username, Pageable page){
        if(username!=null){
            return this.repo.findByUsernameContaining(username, page);
        }
        return this.repo.findAll(page);
    }
    // Get a page of reported users depending on the search
    public Page<User> getReportedUser(String username, Pageable page){
        if(username!=null){
            return this.repo.findReportedUserByUsername(username, page);
        }
        return this.repo.findReportedUser(page);
    }
    public User findByEmail(String email){
        Optional<User> u=this.repo.findByEmail(email);
        return repo.findByEmail(email).orElse(null);
    }
}
