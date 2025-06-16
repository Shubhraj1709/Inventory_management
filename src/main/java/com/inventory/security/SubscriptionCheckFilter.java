package com.inventory.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.inventory.entities.User;
import com.inventory.enums.Role;
import com.inventory.repositories.UserRepository;

import java.io.IOException;
import java.time.LocalDate;

@Component
public class SubscriptionCheckFilter extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();  // email = username
            User user = userRepository.findByEmail(email).orElse(null);

            if (user != null && user.getRole() == Role.BUSINESS_OWNER && isExpired(user)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("❌ Access denied: Subscription expired.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isExpired(User user) {
        LocalDate now = LocalDate.now();
        return user.getPlanEnd() == null || now.isAfter(user.getPlanEnd());
    }
}
