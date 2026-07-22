package com.ems.electionmanagement.service;

import com.ems.electionmanagement.dto.auth.AuthResponse;
import com.ems.electionmanagement.dto.auth.LoginRequest;
import com.ems.electionmanagement.dto.auth.RegisterRequest;
import com.ems.electionmanagement.dto.auth.UserResponse;
import com.ems.electionmanagement.entity.AdminUser;
import com.ems.electionmanagement.entity.Role;
import com.ems.electionmanagement.exception.ConflictException;
import com.ems.electionmanagement.exception.ResourceNotFoundException;
import com.ems.electionmanagement.repository.AdminUserRepository;
import com.ems.electionmanagement.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AdminUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String username = request.username().trim();
        String email = request.email().trim().toLowerCase(Locale.ROOT);

        if (userRepository.existsByUsernameIgnoreCase(username)) {
            throw new ConflictException("That username is already registered.");
        }
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new ConflictException("That email address is already registered.");
        }

        AdminUser user = new AdminUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(Role.ADMIN);
        user.setEnabled(true);
        userRepository.save(user);

        UserDetails details = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtService.generateToken(details, user.getRole().name());
        return new AuthResponse(token, "Bearer", toResponse(user));
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String username = request.username().trim();
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, request.password())
        );

        AdminUser user = userRepository.findByUsernameIgnoreCase(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found."));
        UserDetails details = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtService.generateToken(details, user.getRole().name());
        return new AuthResponse(token, "Bearer", toResponse(user));
    }

    @Transactional(readOnly = true)
    public UserResponse getCurrentUser(String username) {
        AdminUser user = userRepository.findByUsernameIgnoreCase(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found."));
        return toResponse(user);
    }

    private UserResponse toResponse(AdminUser user) {
        return new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getRole().name()
        );
    }
}
