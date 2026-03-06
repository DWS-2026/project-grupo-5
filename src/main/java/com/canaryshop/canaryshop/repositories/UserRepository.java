package com.canaryshop.canaryshop.repositories;

import com.canaryshop.canaryshop.entities.User;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Page<User> findByUsernameContainingOrEmailContaining(String username, String email, Pageable page);
}
