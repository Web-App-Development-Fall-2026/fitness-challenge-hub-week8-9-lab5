package com.example.FitnessChallengeApp_Test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
            .authorizeHttpRequests(auth -> auth

                // Temporarily allow H2 Console for troubleshooting
                .requestMatchers("/h2-console", "/h2-console/**")
                .permitAll()

                // Public pages
                .requestMatchers(
                    "/login",
                    "/register",
                    "/css/**",
                    "/js/**",
                    "/images/**"
                ).permitAll()

                // Create Challenge - COACH and ADMIN
                .requestMatchers(
                    "/create-challenge",
                    "/create-challenge/**"
                ).hasAnyRole("COACH", "ADMIN")

                // Register Coach - ADMIN only
                .requestMatchers(
                    "/create-coach",
                    "/create-coach/**"
                ).hasRole("ADMIN")

                // Other pages require authentication
                .anyRequest().authenticated()
            )

            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/challenges", true)
                .permitAll()
            )

            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )

            .csrf(csrf -> csrf
                .ignoringRequestMatchers(
                    "/h2-console",
                    "/h2-console/**"
                )
            )

            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            );

        return http.build();
    }
}