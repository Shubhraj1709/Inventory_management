package com.inventory.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.inventory.entities.Employee;
import com.inventory.entities.User;
import com.inventory.repositories.EmployeeRepository;
import com.inventory.repositories.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

    	
    	// ✅ Additional logic for employees (runs only if user not found)
        Optional<Employee> empOptional = employeeRepository.findByEmail(email);
        if (empOptional.isPresent()) {
            Employee emp = empOptional.get();

            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
            authorities.addAll(emp.getPermissions().stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList()));

            return org.springframework.security.core.userdetails.User.builder()
                    .username(emp.getEmail())
                    .password(emp.getPassword())
                    .authorities(authorities)
                    .build();
        }
        // ✅ Your original user loading logic (not modified at all)
        Optional<User> userOptional = userRepository.findByEmail(email);
        System.out.println("Loading user: " + email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            System.out.println("Password match: " + encoder.matches("firm123", user.getPasswordHash()));
            System.out.println("User role: " + user.getRole().name());
            System.out.println("Loaded user: " + user.getEmail());
            System.out.println("Password in DB: " + user.getPasswordHash());

            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail())
                    .password(user.getPasswordHash())
                    .roles(user.getRole().name())
                    .build();
        }

      

        // ✅ Final fallback if neither user nor employee found
        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}
