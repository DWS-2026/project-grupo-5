package com.canaryshop.canaryshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Autowired
    private ProductService productService;

    public void addUser(User user){
        this.repo.save(user);
    }
    public User getUser(String name){
        Optional<User> user = repo.findByUsername(name);
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

    public void deleteUser(Long id){
        User u=this.findById(id);
        List<Product> products = u.getProducts();
        for (Product p : products){
            this.productService.deleteProduct(p);
        }
        repo.deleteById(id);
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
        products = products.subList(0, Math.min(6, products.size()));
        List<Product> only6= new LinkedList<>();
        products.forEach(product -> only6.add(product.getProduct()));
        return only6;
    }
    public Page<User> getPageUser(String username, Pageable page ){
        if(username!=null){
            return this.repo.findByUsernameContaining(username, page);
        }
        return this.repo.findAll(page);
    }
    public Page<User> getReportedUser(String username, Pageable page){
        if(username!=null){
            return this.repo.findReportedUserByUsername(username, page);
        }
        return this.repo.findReportedUser(page);
    }
    public List<Product> getProductsByVendor(long id){
        User user = this.findById(id);
        return user.getProducts();
    }
    public User findByEmail(String email){
        Optional<User> u=this.repo.findByEmail(email);
        if(!u.isPresent()){
            return null;
        }else{
            return u.get();
        }
    }
}
