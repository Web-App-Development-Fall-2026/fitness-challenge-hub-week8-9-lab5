
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

                // Public pages
                .requestMatchers(
                    "/login",
                    "/register",
                    "/css/**",
                    "/js/**"
                ).permitAll()

                // Coach and Admin access
                .requestMatchers("/create-challenge")
                .hasAnyRole("COACH", "ADMIN")

                // Admin-only access
                .requestMatchers(
                    "/create-coach",
                    "/h2-console/**"
                ).hasRole("ADMIN")

                // All other pages require login
                .anyRequest().authenticated()
            )

            // Custom login page
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/challenges", true)
                .permitAll()
            )

            // Logout
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )

            // Allow H2 console requests
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**")
            )

            // Allow H2 console frames
            .headers(headers -> headers
                .frameOptions(frame -> frame.disable())
            );

        return http.build();
    }
}
