package com.canaryshop.canaryshop.repositories;

import com.canaryshop.canaryshop.entities.User;

import org.springframework.boot.data.autoconfigure.web.DataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUser(String user);
    Page<User> findByUserContainingOrEmailContaining(String user, String email, Pageable page);
    Page<User> findALl(Pageable page);
}
