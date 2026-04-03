package com.concord.trivio.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, "/equipments").permitAll()
                .requestMatchers(HttpMethod.GET, "/equipments/**").permitAll()
                .requestMatchers(HttpMethod.PATCH, "/equipments/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/equipments").permitAll()
                .requestMatchers(HttpMethod.GET, "/clients").permitAll()
                .requestMatchers(HttpMethod.GET, "/clients/**").permitAll()
                .requestMatchers(HttpMethod.PATCH, "/clients/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/clients").permitAll()
                .requestMatchers(HttpMethod.GET, "/contracts").permitAll()
                .requestMatchers(HttpMethod.GET, "/contracts/**").permitAll()
                .requestMatchers(HttpMethod.PATCH, "/contracts/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/contracts").permitAll()
                .requestMatchers(HttpMethod.GET, "/employees").permitAll()
                .requestMatchers(HttpMethod.GET, "/employees/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/employees").permitAll()
                .requestMatchers(HttpMethod.PUT, "/employees/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/maintenances").permitAll()
                .requestMatchers(HttpMethod.GET, "/maintenances/**").permitAll()
                .requestMatchers(HttpMethod.PATCH, "/maintenances/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/maintenances").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/error").permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic(basic -> {});
        return http.build();
    }
}



