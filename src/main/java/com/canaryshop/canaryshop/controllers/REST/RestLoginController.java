package com.canaryshop.canaryshop.controllers.REST;

import com.canaryshop.canaryshop.Security.jwt.AuthResponse;
import com.canaryshop.canaryshop.Security.jwt.LoginRequest;
import com.canaryshop.canaryshop.Security.jwt.UserLoginService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class RestLoginController {
    @Autowired
    UserLoginService userService;

    @PreAuthorize("isAnonymous()")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request, HttpServletResponse response){
        return userService.login(response, request);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@CookieValue(name = "RefreshToken", required = false) String refreshToken, HttpServletResponse response){
        return userService.refresh(response, refreshToken);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(HttpServletResponse response){
        return ResponseEntity.ok(new AuthResponse(AuthResponse.Status.SUCCESS, userService.logout(response)));
    }
}
