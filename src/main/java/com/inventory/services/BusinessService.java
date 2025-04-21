package com.inventory.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.inventory.dto.BusinessRequest;

import com.inventory.dto.BusinessDTO;
import com.inventory.entities.Business;
import com.inventory.entities.BusinessOwner;
import com.inventory.enums.SubscriptionPlan;
import com.inventory.exceptions.ResourceNotFoundException;
import com.inventory.repositories.BusinessOwnerRepository;
import com.inventory.repositories.BusinessRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BusinessService {

	private final BusinessOwnerRepository businessOwnerRepository;
	private final BusinessRepository businessRepository;
	private final PasswordEncoder passwordEncoder;

	public void registerBusiness(BusinessRequest request) {
		if (request.getPassword() == null || request.getPassword().isBlank()) {
			throw new IllegalArgumentException("Password cannot be null or empty");
		}
		BusinessOwner businessOwner  = new BusinessOwner();
		businessOwner.setBusinessName(request.getBusinessName());
		businessOwner.setEmail(request.getEmail());
		businessOwner.setPasswordHash(passwordEncoder.encode(request.getPassword()));

		SubscriptionPlan plan = SubscriptionPlan.valueOf(request.getSubscriptionPlan().toUpperCase());
		businessOwner.setSubscriptionPlan(plan);

		businessOwner.setActive(false);
		

		businessOwnerRepository.save(businessOwner);

		
		// create and save Business
	    Business business = new Business();
	    business.setBusinessName(request.getBusinessName());
	    business.setStatus("Inactive");
	    business.setOwnerUsername(request.getOwnerName());
	    business.setOwnerPassword(passwordEncoder.encode(request.getPassword()));
	    business.setSubscriptionPlan(plan);
	    business.setMaxUsers(10); // or based on plan
	    
	    business.setBusinessOwner(businessOwner);

	    
		businessRepository.save(business);
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
	                b.getOwnerUsername(), // used as email or name
	                "Active".equalsIgnoreCase(b.getStatus()) // isActive
	            ))
	            .collect(Collectors.toList());
	}
}
