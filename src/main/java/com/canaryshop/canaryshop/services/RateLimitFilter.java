package com.canaryshop.canaryshop.services;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Configuration
public class RateLimitFilter extends OncePerRequestFilter {
    // Amount of requests allowed per minute, ideally defined in configuration file in production
    private final int REQUESTS_PER_MINUTE = 100;

    private final ConcurrentMap<String, Bucket> bucketCache = new ConcurrentHashMap<>();

    // Returns a bucket object that holds request tokens and refills with time
    private Bucket bucket(){
        Bandwidth limit = Bandwidth.builder()
                .capacity(REQUESTS_PER_MINUTE)
                .refillIntervally(REQUESTS_PER_MINUTE, Duration.ofMinutes(1))
                .build();
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    // Filter implementation that gets or creates a bucket for the requesting IP address
    // and checks if it can make a request based on the amount of tokens
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String ip = request.getRemoteAddr();
        Bucket clientBucket = bucketCache.computeIfAbsent(ip, k -> bucket());
        if (clientBucket.tryConsume(1)){
           filterChain.doFilter(request, response);
        }
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setHeader("Retry-After", "60");
    }
}
