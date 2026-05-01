package com.canaryshop.canaryshop.services;

import com.canaryshop.canaryshop.entities.User;
import com.canaryshop.canaryshop.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;
    @Autowired
    private ProductService productService;

    public void modifyCheck(User currentUser, User user) {
        if (!user.canEdit(currentUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User cannot modify this user");
        }
    }

    public void addUser(User user) {
        this.repo.save(user);
    }

    public User getUser(String name) {
        return repo.findByEmail(name).orElseThrow();
    }

    // To get the user in session
    public User getUser(Principal principal) {
        return principal == null ? null : getUser(principal.getName());
    }

    public User findById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public void deleteUser(User currentUser, User user) {
        this.modifyCheck(currentUser, user);
        user.getProducts().forEach(p -> this.productService.deleteProduct(user, p));
        repo.deleteById(user.getId());
    }

    public List<User> findAll() {
        return repo.findAll();
    }

    // Get a user page depending on the search
    public Page<User> getPageUser(String username, Pageable page) {
        if (username != null) {
            return this.repo.findByUsernameContaining(username, page);
        }
        return this.repo.findAll(page);
    }

    // Get a page of reported users depending on the search
    public Page<User> getReportedUser(String username, Pageable page) {
        if (username != null) {
            return this.repo.findReportedUserByUsername(username, page);
        }
        return this.repo.findReportedUser(page);
    }

    public User findByEmail(String email) {
        return repo.findByEmail(email).orElse(null);
    }

    public void updateUser(User currentUser, User user) {
        this.modifyCheck(currentUser, user);
        this.repo.save(user);
    }
}
