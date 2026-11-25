
package com.example.FitnessChallengeApp_Test.controller;

import com.example.FitnessChallengeApp_Test.model.User;
import com.example.FitnessChallengeApp_Test.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @ModelAttribute User user,
            @RequestParam(defaultValue = "false")
            boolean registerAsCoach,
            Model model) {

        if (user.getUsername() == null ||
                user.getUsername().isBlank() ||
                user.getPassword() == null ||
                user.getPassword().isBlank()) {

            model.addAttribute("error",
                    "Username and password are required.");
            return "register";
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            model.addAttribute("error",
                    "Username already exists. Please choose another.");
            return "register";
        }

        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(
                passwordEncoder.encode(user.getPassword())
        );
        newUser.setRole(registerAsCoach ? "COACH" : "USER");

        userRepository.save(newUser);

        return "redirect:/login?registered";
    }
}
