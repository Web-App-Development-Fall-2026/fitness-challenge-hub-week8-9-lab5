package com.example.FitnessChallengeApp_Test.controller;

import com.example.FitnessChallengeApp_Test.model.User;
import com.example.FitnessChallengeApp_Test.repository.UserRepository;

import jakarta.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @InitBinder("user")
    public void initBinder(WebDataBinder binder) {
        binder.setAllowedFields("username", "password");
    }

    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("user") User user,
            BindingResult result,
            @RequestParam(defaultValue = "false") boolean registerAsCoach) {

        if (user.getUsername() != null
                && !user.getUsername().isBlank()
                && userRepository.existsByUsername(user.getUsername())) {

            result.rejectValue(
                "username",
                "duplicate",
                "This username is already taken"
            );
        }

        if (result.hasErrors()) {
            return "register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setRole(registerAsCoach ? "COACH" : "USER");

        userRepository.save(user);

        return "redirect:/login?registered";
    }
}