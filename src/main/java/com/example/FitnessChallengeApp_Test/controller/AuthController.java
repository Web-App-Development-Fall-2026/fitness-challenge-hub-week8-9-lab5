package com.example.FitnessChallengeApp_Test.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.FitnessChallengeApp_Test.model.User;
import com.example.FitnessChallengeApp_Test.repository.UserRepository;

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
	public String showLoginPage() {
		return "login";
	}
	@GetMapping("/register")
	public String showRegisterPage(Model model) {
		model.addAttribute("user", new User());
		return "register";
	}
	@PostMapping("/register")
	public String registerUser(
			@ModelAttribute("user") User user,
			@RequestParam(defaultValue = "false")
				boolean registerAsCoach,
			Model model) {
		if(user.getUsername() == null
				|| user.getUsername().isBlank()
				|| user.getPassword() == null
				|| user.getPassword().isBlank()) {
			model.addAttribute("error","Username and password are required");
		return "register";
		}
		if(userRepository.existsByUsername(user.getUsername())) {
			model.addAttribute("error","This username is already taken");
			return "register";
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole(registerAsCoach ? "COACH" : "USER");
		user.setId(null);
		userRepository.save(user);
		return "redirect:/login?registered";
	}
	
}
