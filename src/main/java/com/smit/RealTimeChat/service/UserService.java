package com.smit.RealTimeChat.service;


import com.smit.RealTimeChat.dto.RegisterRequest;
import com.smit.RealTimeChat.exception.EmailAlreadyExistsException;
import com.smit.RealTimeChat.entity.User;
import com.smit.RealTimeChat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
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
}