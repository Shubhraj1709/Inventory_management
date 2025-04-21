package com.inventory.services;

import com.inventory.dto.AuthenticationResponse;
import com.inventory.dto.LoginRequest;
import com.inventory.config.JwtTokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    public ResponseEntity<?> login(LoginRequest request) {
        System.out.println("Logging in: " + request.getEmail());
        System.out.println("Password: " + request.getPassword());

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

     // ✅ Get UserDetails from authentication
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // ✅ Pass UserDetails to generateToken()
        String token = jwtTokenProvider.generateToken(userDetails);
        System.out.println("Generated token: " + token);
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }
}
