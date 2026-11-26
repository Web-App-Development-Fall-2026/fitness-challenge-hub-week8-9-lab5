package com.example.FitnessChallengeApp_Test.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.example.FitnessChallengeApp_Test.model.User;
import com.example.FitnessChallengeApp_Test.repository.UserRepository;

@Component
public class DataLoader implements ApplicationRunner{
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public DataLoader(UserRepository userRepository,
			PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (userRepository.findByUsername("admin").isEmpty()) {
			User admin = new User();
			admin.setUsername("admin");
			admin.setPassword(passwordEncoder.encode("admin123"));
			admin.setRole("ADMIN");
			userRepository.save(admin);
		}
	}
}
