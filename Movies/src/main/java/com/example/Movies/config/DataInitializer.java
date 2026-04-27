package com.example.Movies.config;

import com.example.Movies.entity.Role;
import com.example.Movies.entity.User;
import com.example.Movies.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                User admin = User.builder()
                        .name("Admin")
                        .email("admin@movies.com")
                        .password(passwordEncoder.encode("admin123")) // ✅ Password encodé
                        .role(Role.ADMIN)
                        .build();

                userRepository.save(admin);
                System.out.println("✅ Compte Admin créé par défaut.");
            }
        };
    }
}