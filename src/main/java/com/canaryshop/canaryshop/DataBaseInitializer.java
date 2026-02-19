package com.canaryshop.canaryshop;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;

@Configuration
public class DataBaseInitializer {
    
    @Bean
    public CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {

            if (userRepository.count() == 0) {
                User admin = new User("admin", "admin@canaryshop.com", "admin123");
                admin.setAdmin(true);
                userRepository.save(admin);

            }
            
        };
    }
}
