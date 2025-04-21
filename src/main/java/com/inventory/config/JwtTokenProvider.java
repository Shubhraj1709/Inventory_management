package com.inventory.config;

import io.jsonwebtoken.*;

import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import com.inventory.entities.User;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
	

@Component
public class JwtTokenProvider {

    // Secret key for signing (should be stored securely!)
    private final String SECRET_KEY = "your-256-bit-secret-your-256-bit-secret"; // 32-byte minimum

    // Token validity (e.g., 24 hours)
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 48;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    
    // Generate token	
    
    public String generateToken(UserDetails userDetails) {
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        claims.put("roles", userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .toList());

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }

 // 🔁 Overloaded method to support custom User entity
    public String generateToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getEmail());
        if (user.getRole().name().equals("EMPLOYEE")) {
            // Fetch permissions from employee table if needed
            // Or assume permissions are already available on User (not ideal)
            // Better approach: avoid this method for employees and always use UserDetails
            claims.put("roles", List.of("ROLE_EMPLOYEE"));
            claims.put("authorities", List.of("VIEW_ORDERS", "MANAGE_PRODUCTS")); // 🔁 Replace this dynamically
        } else {
            claims.put("roles", List.of("ROLE_" + user.getRole().name()));
        }

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }
    
    // Extract username from token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract expiration date
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Generic claim extractor
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Validate token
    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
