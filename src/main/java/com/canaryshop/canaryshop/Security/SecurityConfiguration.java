package com.canaryshop.canaryshop.Security;

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
    private RepositoryUserDetailsService userDetailService;
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
	public SecurityFilterChain filterChain(HttpSecurity http) {
        http.authenticationProvider(authenticationProvider());
        http.authorizeHttpRequests(authorize -> {
                String[] guestPages = { "/", "/index", "/product/**", "/login", "/register", "/images/**", "/js/**", "/css/**", "/assets/**", "/error", "/logout", "/user/**" };
                String[] userPages = { "/product/new", "/product/*/edit", "/product/*/delete", "/product/*/review/**", "/cart/**", "/order/**", "/user/*/image", "/user/*/edit", "/user/*/delete", "/logout", "/payment", "/success" };
                String[] adminPages = { "/admin/**" };
                for (String page: adminPages) { authorize.requestMatchers(page).hasAnyRole("ADMIN"); }
                for (String page: userPages) { authorize.requestMatchers(page).authenticated(); }
                for (String page: guestPages) { authorize.requestMatchers(page).permitAll(); }
            }
        ).formLogin(formLogin -> formLogin
            .loginPage("/login")
            .usernameParameter("email")
            .defaultSuccessUrl("/")
            .failureUrl("/loginerror")
            .permitAll()
        ).logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")
            .permitAll()
        );
        return http.build();
	}
}
