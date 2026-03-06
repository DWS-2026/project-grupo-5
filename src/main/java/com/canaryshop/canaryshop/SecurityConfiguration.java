package com.canaryshop.canaryshop;

import com.canaryshop.canaryshop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    private UserService userDetailService;
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailService);
        authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authenticationProvider(authenticationProvider());
        http.authorizeHttpRequests(authorize -> {
                String[] guestPages = { "/", "/index", "/product/**", "/user/**", "/login", "/register", "/images/**", "/js/**", "/css/**" };
                String[] userPages = { "/product/new", "/product/*/newReview", "/cart", "/logout" };
                String[] adminPages = { "/admin/**" };
                for (String page: guestPages) { authorize.requestMatchers(page).permitAll(); }
                for (String page: userPages) { authorize.requestMatchers(page).authenticated(); }
                for (String page: adminPages) { authorize.requestMatchers(page).hasAnyRole("ADMIN"); }
            }
        );
        http.csrf(csrf -> csrf.disable());
        return http.build();
	}
}
