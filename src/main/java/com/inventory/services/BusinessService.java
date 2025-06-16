package com.inventory.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inventory.dto.BusinessRequest;
import com.inventory.dto.BusinessDTO;
import com.inventory.entities.Business;
import com.inventory.entities.BusinessOwner;
import com.inventory.entities.User;
import com.inventory.enums.Role;
import com.inventory.enums.SubscriptionLevel;
import com.inventory.enums.PaymentStatus;
import com.inventory.exceptions.ResourceNotFoundException;
import com.inventory.repositories.BusinessOwnerRepository;
import com.inventory.repositories.BusinessRepository;
import com.inventory.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BusinessService {

    private final BusinessOwnerRepository businessOwnerRepository;
    private final BusinessRepository businessRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional
    public void registerBusiness(BusinessRequest request) {
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        System.out.println("Raw plan from request: " + request.getSubscriptionPlan());  // 👈 add this

        SubscriptionLevel plan = SubscriptionLevel.valueOf(request.getSubscriptionPlan().toUpperCase());

        // 1. Create and save BusinessOwner first
        BusinessOwner businessOwner = new BusinessOwner();
        businessOwner.setBusinessName(request.getBusinessName());
        businessOwner.setName(request.getOwnerName());
        businessOwner.setEmail(request.getEmail());
        businessOwner.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        businessOwner.setSubscriptionPlan(plan);
        businessOwner.setActive(false);
        
        System.out.println("Owner Name: " + request.getOwnerName());

        businessOwner = businessOwnerRepository.saveAndFlush(businessOwner); // must be saved first

        // 2. Now create and save Business with business_owner_id set
        Business business = new Business();
        business.setBusinessName(request.getBusinessName());
        business.setStatus("Inactive");
        business.setOwnerUsername(request.getOwnerName());
        business.setOwnerPassword(passwordEncoder.encode(request.getPassword()));
        business.setSubscriptionPlan(plan);
        business.setPaymentStatus(PaymentStatus.UNPAID);
        business.setNextDueDate(LocalDate.now().plusMonths(1));
        business.setMaxUsers(10);
        
        // Establish two-way relationship BEFORE saving
        business.setBusinessOwner(businessOwner); // set owning side now
        business = businessRepository.saveAndFlush(business);

        businessOwner.setBusiness(business);

        System.out.println("BusinessOwner name: " + businessOwner.getName());

        // 3. Update BusinessOwner with business ID (optional if reverse needed)
        businessOwnerRepository.save(businessOwner);

        // 4. Create user
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.BUSINESS_OWNER);
        user.setBusinessOwner(businessOwner);
        user.setBusiness(business);
//        user.setSubscriptionPlan(plan);
        user.setSubscriptionPlan(plan.toDbValue());

        user.setName(request.getOwnerName());
//        user.setSubscriptionPlan(SubscriptionLevel.fromDbValue("PREMIUM"));
        user.setSubscriptionPlan(SubscriptionLevel.fromDbValue("PREMIUM").toDbValue());
        user.setPlanStart(LocalDate.now());
        user.setPlanEnd(LocalDate.now().plusMonths(1));
        userRepository.save(user);
    }


    public void enableBusiness(Long id) {
        Business business = businessRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));
        business.setStatus("Active");
        businessRepository.save(business);

        BusinessOwner owner = business.getBusinessOwner();
        owner.setActive(true);
        businessOwnerRepository.save(owner);
    }

    public void disableBusiness(Long id) {
        Business business = businessRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));
        business.setStatus("Inactive");
        businessRepository.save(business);

        BusinessOwner owner = business.getBusinessOwner();
        owner.setActive(false);
        businessOwnerRepository.save(owner);
    }

    public List<BusinessDTO> getAllBusinesses() {
        return businessRepository.findAll().stream()
                .map(b -> new BusinessDTO(
                        b.getId(),
                        b.getBusinessName(),
                        b.getOwnerUsername(),
                        "Active".equalsIgnoreCase(b.getStatus())
                ))
                .collect(Collectors.toList());
    }
    
    public void markPaymentAsCompleted(Long businessId) {
        Business business = businessRepository.findById(businessId)
            .orElseThrow(() -> new ResourceNotFoundException("Business not found"));

        business.setPaymentStatus(PaymentStatus.PAID);
        business.setStatus("Active"); // Optional
        business.setNextDueDate(LocalDate.now().plusMonths(1));
        businessRepository.save(business);

        // Optionally activate owner
        BusinessOwner owner = business.getBusinessOwner();
        owner.setActive(true);
        businessOwnerRepository.save(owner);
    }

}
