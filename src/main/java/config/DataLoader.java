package com.example.FitnessChallengeApp_Test.config;

import com.example.FitnessChallengeApp_Test.model.User;
import com.example.FitnessChallengeApp_Test.repository.UserRepository;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserRepository userRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // Check if admin account already exists
        if (userRepository.findByUsername("admin").isEmpty()) {

            User admin = new User();

            // Set admin username
            admin.setUsername("admin");

            // Encrypt admin password
            admin.setPassword(passwordEncoder.encode("admin123"));

            // Assign ADMIN role
            admin.setRole("ADMIN");

            // Save admin account to database
            userRepository.save(admin);

            System.out.println("Default ADMIN account created successfully!");
        }
    }
}