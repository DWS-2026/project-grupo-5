package com.canaryshop.canaryshop.services;

import com.canaryshop.canaryshop.entities.User;
import com.canaryshop.canaryshop.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;


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
        return repo.findByEmail(name).orElseThrow();
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
        u.getProducts().forEach(p -> this.productService.deleteProduct(u, p));
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
        return repo.findByEmail(email).orElse(null);
    }
}
