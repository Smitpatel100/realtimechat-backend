package com.smit.RealTimeChat.service;

import com.smit.RealTimeChat.dto.LoginRequest;
import com.smit.RealTimeChat.dto.LoginResponse;
import com.smit.RealTimeChat.dto.RegisterRequest;
import com.smit.RealTimeChat.dto.UserResponse;
import com.smit.RealTimeChat.entity.User;
import com.smit.RealTimeChat.exception.EmailAlreadyExistsException;
import com.smit.RealTimeChat.exception.InvalidCredentialsException;
import com.smit.RealTimeChat.exception.UserNotFoundException;
import com.smit.RealTimeChat.repository.UserRepository;
import com.smit.RealTimeChat.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public User register(RegisterRequest request) {

        // Step 1: Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(
                    "Email already in use: " + request.getEmail()
            );
        }

        // Step 2: Hash the password before storing it
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        // Step 3: Build the User entity from validated DTO data
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(hashedPassword);

        // Step 4: Persist the user
        return userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {

        // Step 1: Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        // Step 2 & 3: Compare raw password against the stored BCrypt hash
        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!passwordMatches) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        // Step 4: Generate JWT for the authenticated user
        String token = jwtService.generateToken(user.getEmail());

        // Step 5: Return token wrapped in response DTO
        return new LoginResponse(token);
    }

    public UserResponse getCurrentUser(String email) {

        // Step 1: Find user by email, throw if not found
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        // Step 2: Map entity to UserResponse DTO (no password field)
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}