package com.canaryshop.canaryshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import com.canaryshop.canaryshop.repositories.ReviewRepository;
import com.canaryshop.canaryshop.repositories.UserRepository;
import com.canaryshop.canaryshop.entities.Order;
import com.canaryshop.canaryshop.entities.OrderProduct;
import com.canaryshop.canaryshop.entities.Product;
import com.canaryshop.canaryshop.entities.Review;
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
    @Autowired
    private ReviewService reviewService;

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

    public void deleteUser(Long id){
        User u=this.findById(id);
        List<Product> products = u.getProducts();
        for (Product p : products){
            this.productService.deleteProduct(p);
        }
        List<Review> reviews= this.reviewService.getByAuthor(u);
        for(Review rev: reviews){
            this.reviewService.deleteReview(rev, this.productService.getByReview(rev));
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
    public Page<User> getPageUsers(String username, Pageable page ){
        if(username!=null){
            return this.repo.findByUsernameContaining(username, page);
        }
        return this.repo.findAll(page);
    }
    public List<Product> getProductsByVendor(long id){
        User user = this.findById(id);
        return user.getProducts();
    }
}
