package com.inventory.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import com.inventory.entities.User;
import com.inventory.entities.BusinessOwner;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;

@Component
public class JwtTokenProvider {

    private final String SECRET_KEY = "your-256-bit-secret-your-256-bit-secret"; // 32-byte minimum
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 48; // 48 hours

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // ✅ Generate token for UserDetails (normal way)
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

    // ✅ Generate token for your custom User entity (with Subscription Plan info if Business Owner)
    public String generateToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getEmail());

        if (user.getRole().name().equals("EMPLOYEE")) {
            claims.put("roles", List.of("ROLE_EMPLOYEE"));
            claims.put("authorities", List.of("VIEW_ORDERS", "MANAGE_PRODUCTS")); // Example permissions
        } 
        else if (user.getRole().name().equals("BUSINESS_OWNER")) {
            BusinessOwner owner = user.getBusinessOwner();

            if (owner != null) {
                claims.put("subscription", owner.getSubscriptionPlan().name());

                if (owner.getSubscriptionPlan().name().equals("PREMIUM")) {
                    claims.put("roles", List.of("ROLE_BUSINESS_OWNER", "ROLE_PREMIUM_ACCESS"));
                } else if (owner.getSubscriptionPlan().name().equals("PAID")) {
                    claims.put("roles", List.of("ROLE_BUSINESS_OWNER", "ROLE_PAID_ACCESS"));
                } else {
                    claims.put("roles", List.of("ROLE_BUSINESS_OWNER", "ROLE_FREE_ACCESS"));
                }
            } else {
                claims.put("roles", List.of("ROLE_BUSINESS_OWNER"));
            }
        }
        else {
            claims.put("roles", List.of("ROLE_" + user.getRole().name()));
        }

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    // ✅ Extract username
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // ✅ Extract expiration
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // ✅ Generic claim extractor
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // ✅ Validate token
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




