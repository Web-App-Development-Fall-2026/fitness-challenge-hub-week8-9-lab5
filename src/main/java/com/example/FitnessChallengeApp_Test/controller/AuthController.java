package com.example.FitnessChallengeApp_Test.controller;

import com.example.FitnessChallengeApp_Test.model.User;
import com.example.FitnessChallengeApp_Test.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Display login page
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Display registration page
    @GetMapping("/register")
    public String showRegister(Model model) {

        model.addAttribute("user", new User());

        return "register";
    }

    // Process user registration
    @PostMapping("/register")
    public String registerUser(
            @ModelAttribute("user") User user,
            Model model) {

        // Validate username and password
        if (user.getUsername() == null
                || user.getUsername().trim().length() < 3
                || user.getPassword() == null
                || user.getPassword().length() < 6) {

            model.addAttribute(
                    "error",
                    "Username must have at least 3 characters and password at least 6."
            );

            return "register";
        }

        // Check if username already exists
        if (userRepository.existsByUsername(user.getUsername())) {

            model.addAttribute(
                    "error",
                    "Username already exists!"
            );

            return "register";
        }

        // Encode password using BCrypt
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        // Public registration always creates a regular USER
        user.setRole("USER");

        // Prevent users from setting their own database ID
        user.setId(null);

        // Save user to H2 database
        userRepository.save(user);

        // Redirect to login page
        return "redirect:/login?registered";
    }

    // TEMPORARY: Check authenticated user's authorities
    // Remove this method before final submission
    @GetMapping("/debug-role")
    @ResponseBody
    public String debugRole(Authentication authentication) {

        return "Username: " + authentication.getName()
                + " | Authorities: " + authentication.getAuthorities();
    }
}