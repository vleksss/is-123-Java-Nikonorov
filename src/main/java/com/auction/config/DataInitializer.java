package com.auction.config;

import com.auction.model.Role;
import com.auction.model.User;
import com.auction.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        createOrUpdate("admin", "admin@auction.local", Role.ADMIN);
        createOrUpdate("owner", "owner@auction.local", Role.OWNER);
        createOrUpdate("user", "user@auction.local", Role.USER);
    }

    private void createOrUpdate(String username, String email, Role role) {
        User user = userRepository.findByUsername(username).orElseGet(User::new);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("123456"));
        user.setRole(role);
        user.setEnabled(true);
        userRepository.save(user);
    }
}