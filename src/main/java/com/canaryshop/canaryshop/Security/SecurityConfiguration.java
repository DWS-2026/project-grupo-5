package com.canaryshop.canaryshop.Security;

import com.canaryshop.canaryshop.Security.jwt.JwtRequestFilter;
import com.canaryshop.canaryshop.Security.jwt.JwtTokenProvider;
import com.canaryshop.canaryshop.Security.jwt.UnauthorizedHandlerJwt;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired
	private UnauthorizedHandlerJwt unauthorizedHandlerJwt;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private RepositoryUserDetailsService userDetailService;
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}
	@Bean
	public PolicyFactory enrichedTextSanitizer(){
		return Sanitizers.FORMATTING;
	}
    @Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailService);
        authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
    @Bean
	@Order(1)
	public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {

		http.authenticationProvider(authenticationProvider());

		http.securityMatcher("/api/v1/**")
				.exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandlerJwt));

		http.authorizeHttpRequests(authorize -> authorize

						// PRIVATE ENDPOINTS
						// Images
						//.requestMatchers(HttpMethod.PUT, "/api/images/*/media").hasRole("USER")
						//.requestMatchers(HttpMethod.DELETE, "/api/books/*/images/*").hasRole("USER")
						// Books
						//.requestMatchers(HttpMethod.POST, "/api/books/**").hasRole("USER")
						//.requestMatchers(HttpMethod.PUT, "/api/books/**").hasRole("USER")
						//.requestMatchers(HttpMethod.DELETE, "/api/books/**").hasRole("ADMIN")
						// Shops
						//.requestMatchers(HttpMethod.PUT, "/api/shops/**").hasRole("ADMIN")
						//.requestMatchers(HttpMethod.PUT, "/api/shops/**").hasRole("ADMIN")
						//.requestMatchers(HttpMethod.DELETE, "/api/shops/**").hasRole("ADMIN")
						// PUBLIC ENDPOINTS
						.anyRequest().permitAll()
		);

		// Disable Form login Authentication
		http.formLogin(formLogin -> formLogin.disable());

		// Disable CSRF protection (it is difficult to implement in REST APIs)
		http.csrf(csrf -> csrf.disable());

		// Disable Basic Authentication
		http.httpBasic(httpBasic -> httpBasic.disable());

		// Stateless session
		http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		// Add JWT Token filter
		http.addFilterBefore(new JwtRequestFilter(userDetailService, jwtTokenProvider),UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
	@Bean
    @Order(2)
	public SecurityFilterChain WebfilterChain(HttpSecurity http) {
        http.authenticationProvider(authenticationProvider());
        http.authorizeHttpRequests(authorize -> {
                String[] userPages = { "/product/new", "/product/*/edit", "/product/*/delete", "/product/*/review/**", "/cart/**", "/order/**", "/user/*/image", "/user/*/edit", "/user/*/delete", "/logout", "/payment", "/success" };
                String[] adminPages = { "/admin/**" };
                for (String page: adminPages) { authorize.requestMatchers(page).hasAnyRole("ADMIN"); }
                for (String page: userPages) { authorize.requestMatchers(page).authenticated(); }
                authorize.anyRequest().permitAll();
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
