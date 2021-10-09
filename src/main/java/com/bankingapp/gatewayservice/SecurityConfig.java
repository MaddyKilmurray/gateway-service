package com.bankingapp.gatewayservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.ArrayList;
import java.util.List;

@EnableWebFluxSecurity
public class SecurityConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http.authorizeExchange()
                .pathMatchers(HttpMethod.GET,"/users/byid/**", "/accounts/byid/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                .pathMatchers(HttpMethod.GET,"/**").hasAnyAuthority("ROLE_ADMIN")
                .pathMatchers(HttpMethod.DELETE, "/**").hasAnyAuthority("ROLE_ADMIN")
                .pathMatchers(HttpMethod.POST,"/**").hasAnyAuthority("ROLE_ADMIN")
                .anyExchange().authenticated()
                .and()
                .csrf().disable()
                .httpBasic()
                .and()
                .build();
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        UserDetails user = User
                .withUsername("user")
                .password(passwordEncoder.encode("user1"))
                .roles("USER")
                .build();

        UserDetails admin = User
                .withUsername("admin")
                .password(passwordEncoder.encode("admin1"))
                .roles("ADMIN")
                .build();

        List<UserDetails> userDetailsList = new ArrayList<>();
        userDetailsList.add(user);
        userDetailsList.add(admin);

        return new MapReactiveUserDetailsService(userDetailsList);

    }
}
