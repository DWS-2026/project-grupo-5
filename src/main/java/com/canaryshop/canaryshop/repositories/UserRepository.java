package com.canaryshop.canaryshop.repositories;

import com.canaryshop.canaryshop.entities.Product;
import com.canaryshop.canaryshop.entities.User;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Page<User> findByUsernameContaining(String username, Pageable page);
    @Query("SELECT u FROM User u WHERE u.username LIKE %:username% AND u.reports IS NOT EMPTY")
    Page<User> findReportedUserByUsername(@Param("username") String username, Pageable page);
    @Query("SELECT u FROM User u WHERE u.reports IS NOT EMPTY")
    Page<User> findReportedUser(Pageable page);
}
