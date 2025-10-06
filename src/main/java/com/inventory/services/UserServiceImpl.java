package com.inventory.services;

import com.inventory.entities.SubscriptionPlan;
import com.inventory.entities.User;
import com.inventory.enums.PlanType;
import com.inventory.repositories.SubscriptionPlanRepository;
import com.inventory.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder; // ✅ inject encoder here
    
    @Autowired
    private SubscriptionPlanRepository planRepository;



    @Override
    public User saveUser(User user) {
        // ✅ Hash the password before saving
        String encodedPassword = passwordEncoder.encode(user.getPasswordHash());
        user.setPasswordHash(encodedPassword);
        return userRepository.save(user);
    }
    

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
    @Override
    public void assignPlan(Long userId, String planTypeStr) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));

        // Convert String -> Enum
        PlanType planType;
        try {
            planType = PlanType.valueOf(planTypeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid plan type: " + planTypeStr);
        }

        // Fetch subscription plan from DB
        SubscriptionPlan plan = planRepository.findByPlanType(planType)
                .orElseThrow(() -> new RuntimeException("Plan not found: " + planType));

        // Save only the plan type name in User (since subscriptionPlan is a String)
        user.setSubscriptionPlan(plan.getPlanType().name());

        // Optionally set start & end dates
        user.setPlanStart(java.time.LocalDate.now());
        user.setPlanEnd(java.time.LocalDate.now().plusMonths(1));

        userRepository.save(user);
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }



}
