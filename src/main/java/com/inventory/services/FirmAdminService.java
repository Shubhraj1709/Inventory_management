package com.inventory.services;

import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.inventory.dto.BusinessOwnerDTO;
import com.inventory.dto.BusinessOwnerRequest;
import com.inventory.entities.BusinessOwner;
import com.inventory.entities.FirmAdmin;
import com.inventory.exceptions.ResourceNotFoundException;
import com.inventory.repositories.BusinessOwnerRepository;
import com.inventory.repositories.FirmAdminRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FirmAdminService {

    private final BusinessOwnerRepository businessOwnerRepository;

    public BusinessOwnerDTO registerBusiness(BusinessOwnerRequest request) {
        BusinessOwner businessOwner = new BusinessOwner();
        businessOwner.setName(request.getBusinessName());
        businessOwner.setEmail(request.getEmail());
        businessOwner.setPasswordHash(new BCryptPasswordEncoder().encode(request.getPassword()));
        businessOwner.setActive(false); //  Correct method name
        businessOwnerRepository.save(businessOwner);
        return new BusinessOwnerDTO(businessOwner);
    }

    public void enableBusiness(Long id) {
        BusinessOwner business = businessOwnerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));
        business.setActive(true); //  Correct method name
        businessOwnerRepository.save(business);
    }

    public void disableBusiness(Long id) {
        BusinessOwner business = businessOwnerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));
        business.setActive(false); //  Correct method name
        businessOwnerRepository.save(business);
    }

    public List<BusinessOwnerDTO> getAllBusinesses() {
        return businessOwnerRepository.findAll().stream()
                .map(BusinessOwnerDTO::new)
                .collect(Collectors.toList());
    }
}
