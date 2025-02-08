package com.cart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Disable CSRF for testing (enable in production)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/cart/**").permitAll()  // Allow all requests to /cart
                        .anyRequest().authenticated()
                )
                .httpBasic(httpBasic -> httpBasic.disable()) // Disable Basic Authentication
                .formLogin(form -> form.disable()); // Disable default login page

        return http.build();
    }
}
