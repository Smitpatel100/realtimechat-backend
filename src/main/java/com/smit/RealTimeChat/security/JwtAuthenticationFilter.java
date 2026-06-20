package com.smit.RealTimeChat.security;

import com.smit.RealTimeChat.entity.User;
import com.smit.RealTimeChat.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userrepository;

    public JwtAuthenticationFilter(JwtService jwtService, UserRepository userrepository) {
        this.jwtService = jwtService;
        this.userrepository = userrepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // Step 1: Read the Authorization header from the request
        String authHeader = request.getHeader("Authorization");

        // Step 2: If header is missing or doesn't start with "Bearer ", skip this filter
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Step 3: Extract the raw token by removing "Bearer " prefix (7 characters)
        String token = authHeader.substring(7);

        // Step 4: Extract the email from inside the token
        String email = jwtService.extractEmail(token);

        // Step 5: Proceed only if email was extracted AND no auth is already set in context
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Step 6: Load user from database using the email from the token
            User user = userrepository.findByEmail(email).orElse(null);

            if (user != null && jwtService.isTokenValid(token, user.getEmail())) {

                // Step 7: Create Authentication object with user details
            	 UsernamePasswordAuthenticationToken authToken =
            		    new UsernamePasswordAuthenticationToken(
            		        user.getEmail(),
            		        null,
            		        Collections.emptyList()
            		    );

                // Step 8: Attach request details to the authentication object
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // Step 9: Set authentication into SecurityContextHolder
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Step 10: Continue the request down the filter chain
        filterChain.doFilter(request, response);
    }
}