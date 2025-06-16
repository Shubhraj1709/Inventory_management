package com.inventory.utils;

import com.inventory.entities.User;
import com.inventory.repositories.UserRepository;
import com.inventory.config.JwtTokenProvider;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenUtils {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public Long extractBusinessOwnerIdFromToken(String authHeader) {
        String token = authHeader.substring(7); // remove "Bearer "
        String email = jwtTokenProvider.extractUsername(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return user.getBusiness().getBusinessOwner().getId();  
        // Or just user.getBusinessOwner().getId(); depending on your entity relationship
    }
}
