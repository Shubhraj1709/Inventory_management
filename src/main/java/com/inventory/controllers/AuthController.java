package com.inventory.controllers;

import com.inventory.dto.LoginRequest;

import com.inventory.repositories.BusinessRepository; // Add this import

import com.inventory.dto.RegisterRequest;
import com.inventory.entities.Business;
import com.inventory.entities.User;
import com.inventory.enums.Role;
import com.inventory.repositories.UserRepository;
import com.inventory.services.UserService;
import com.inventory.dto.AuthenticationResponse;
import com.inventory.dto.JwtResponse;
import com.inventory.config.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final UserService userService; // ✅ Add this
    
    private final UserRepository userRepository;

    private final BusinessRepository businessRepository; // ✅ Add this line
    
    private final PasswordEncoder passwordEncoder;


    

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest request) {
        
    	// Debug logs
        System.out.println("Logging in: " + request.getEmail());
        System.out.println("Password: " + request.getPassword());
        System.out.println("AuthManager class: " + authenticationManager.getClass().getName());

    	authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtTokenProvider.generateToken(userDetails);

     // Debug token
        System.out.println("Generated token: " + token);
	
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }
    
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        // Create User object and set fields
        User newUser = new User();
        newUser.setName(request.getName());  // ✅ Important
        newUser.setEmail(request.getEmail());
        newUser.setPasswordHash(request.getPassword()); // ✅ raw password
        newUser.setRole(Enum.valueOf(com.inventory.enums.Role.class, request.getRole().toUpperCase())); // Convert String to Enum

     // 🔥 Important: Set Business object if businessId is present
        if (request.getBusinessId() != null) {
            Business business = businessRepository.findById(request.getBusinessId())
                .orElseThrow(() -> new RuntimeException("Business not found with ID: " + request.getBusinessId()));
            newUser.setBusiness(business); // <-- Assigning the business
        }
        
        userService.saveUser(newUser); // ✅ password will be hashed inside UserServiceImpl
        
        System.out.println("Registering user with name: " + newUser.getName());

        return ResponseEntity.ok("User registered successfully!");
    }
    
    @PostMapping("/test")
    public String testLogin() {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken("alice@firm.com", "firm123")
        );
        return "Auth OK!";
    }
    
    @PostMapping("/login-business-owner")
    public ResponseEntity<?> loginBusinessOwner(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Only allow BUSINESS_OWNER here
        if (!user.getRole().equals(Role.BUSINESS_OWNER)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is not a Business Owner");
        }

        String token = jwtTokenProvider.generateToken(user);
        return ResponseEntity.ok(new JwtResponse(token));
    }
    
 // ✅ NEW: Register Employee endpoint
    
    @PostMapping("/login-employee")
    public ResponseEntity<?> loginEmployee(@RequestBody LoginRequest request) {
        try {
            System.out.println("Received login request: " + request.getEmail());

            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            System.out.println("Authentication successful");


            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Cast to your custom User class (if needed)
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // Check if user has EMPLOYEE role
            if (user.getRole().equals(Role.EMPLOYEE) && user.getBusiness() == null) {
                throw new RuntimeException("Business is required for Employee");
            }


            // Generate token
            String jwtToken = jwtTokenProvider.generateToken(userDetails);
            
            System.out.println("Token generated: " + jwtToken);

            return ResponseEntity.ok(new JwtResponse(jwtToken));

        } catch (BadCredentialsException ex) {
            System.out.println("Bad credentials: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
        catch (Exception e) {
            System.out.println("Other error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
