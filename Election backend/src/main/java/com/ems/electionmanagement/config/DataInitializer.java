package com.ems.electionmanagement.config;

import com.ems.electionmanagement.entity.AdminUser;
import com.ems.electionmanagement.entity.Role;
import com.ems.electionmanagement.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Locale;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final AdminUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner createBootstrapAdmin(
        @Value("${app.bootstrap-admin.username}") String username,
        @Value("${app.bootstrap-admin.email}") String email,
        @Value("${app.bootstrap-admin.password}") String password
    ) {
        return args -> {
            if (userRepository.count() > 0) {
                return;
            }

            AdminUser admin = new AdminUser();
            admin.setUsername(username.trim());
            admin.setEmail(email.trim().toLowerCase(Locale.ROOT));
            admin.setPasswordHash(passwordEncoder.encode(password));
            admin.setRole(Role.ADMIN);
            admin.setEnabled(true);
            userRepository.save(admin);
        };
    }
}
