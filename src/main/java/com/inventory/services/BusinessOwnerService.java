package com.inventory.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.inventory.entities.Business;
import com.inventory.entities.BusinessOwner;
import com.inventory.entities.User;
import com.inventory.enums.Role;
import com.inventory.enums.SubscriptionLevel;
import com.inventory.exceptions.ResourceNotFoundException;
import com.inventory.repositories.BusinessOwnerRepository;
import com.inventory.repositories.BusinessRepository;
import com.inventory.repositories.UserRepository;

import com.inventory.dto.UserDTO;
import com.inventory.dto.BusinessOwnerDTO;
import com.inventory.dto.BusinessOwnerRequest;
import com.inventory.dto.EmployeeRequest;
import com.inventory.dto.EmployeeUpdateRequest;

import com.inventory.entities.UserPermission;
	

import lombok.RequiredArgsConstructor;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BusinessOwnerService {

    private final UserRepository userRepository;
    
    private final BusinessOwnerRepository businessOwnerRepository;
    private final BusinessRepository businessRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    

    public UserDTO addEmployee(EmployeeRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(new BCryptPasswordEncoder().encode(request.getPassword()));
        user.setRole(Role.EMPLOYEE);

        // Convert List<String> to List<UserPermission>
        List<UserPermission> permissionEntities = request.getPermissions().stream()
            .map(permission -> {
                UserPermission up = new UserPermission();
                if (permission.equalsIgnoreCase("read")) up.setCanRead(true);
                if (permission.equalsIgnoreCase("write")) up.setCanWrite(true);
                if (permission.equalsIgnoreCase("delete")) up.setCanDelete(true);
                up.setUser(user);  // Important: set the user relationship
                return up;
            })
            .toList();

        user.setPermissions(permissionEntities);
        userRepository.save(user);
        return new UserDTO(user);
    }


    public void updateEmployee(Long id, EmployeeUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        List<UserPermission> permissionEntities = request.getPermissions().stream()
            .map(permission -> {
                UserPermission up = new UserPermission();
                if (permission.equalsIgnoreCase("read")) up.setCanRead(true);
                if (permission.equalsIgnoreCase("write")) up.setCanWrite(true);
                if (permission.equalsIgnoreCase("delete")) up.setCanDelete(true);
                up.setUser(user);
                return up;
            })
            .toList();

        user.setPermissions(permissionEntities);
        userRepository.save(user);
    }
    
    
    public BusinessOwnerDTO createBusinessOwner(BusinessOwnerRequest request) {
        Business business = businessRepository.findById(request.getBusinessId())
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));

        BusinessOwner owner = new BusinessOwner();
        owner.setBusinessName(request.getBusinessName());
        owner.setName(request.getName());
        owner.setEmail(request.getEmail());
        owner.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        owner.setActive(true);
        owner.setBusiness(business);
        owner.setSubscriptionPlan(SubscriptionLevel.fromDbValue(request.getSubscriptionPlan()));

        BusinessOwner saved = businessOwnerRepository.save(owner);
        return new BusinessOwnerDTO(saved);
    }

    
    
    public List<BusinessOwnerDTO> getAllBusinessOwners() {
        List<BusinessOwner> owners = businessOwnerRepository.findAll();
        return owners.stream()
                     .map(this::convertToDTO)
                     .collect(Collectors.toList());
    }

    private BusinessOwnerDTO convertToDTO(BusinessOwner owner) {
        BusinessOwnerDTO dto = new BusinessOwnerDTO();
        dto.setId(owner.getId());
        dto.setEmail(owner.getEmail());
        dto.setName(owner.getName());
        // add other fields
        return dto;
    }

    
    public BusinessOwnerDTO updateBusinessOwner(Long id, BusinessOwnerRequest request) {
        BusinessOwner owner = businessOwnerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Business owner not found"));

        owner.setName(request.getName());
        owner.setEmail(request.getEmail());
        // Update other fields if needed

        businessOwnerRepository.save(owner);

        return mapToDTO(owner);
    }

    
    public void deleteBusinessOwner(Long id) {
        BusinessOwner owner = businessOwnerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Business owner not found"));

        // Optional: also delete associated User & Business
        //businessOwnerRepository.delete(owner);
        businessOwnerRepository.deleteById(id);

    }
    
    private BusinessOwnerDTO mapToDTO(BusinessOwner owner) {
        return new BusinessOwnerDTO(owner);
    }


 // BusinessOwnerService.java

    public BusinessOwnerDTO getBusinessOwnerById(Long id) {
        Optional<BusinessOwner> ownerOpt = businessOwnerRepository.findById(id);
        if (ownerOpt.isEmpty()) return null;

        BusinessOwner owner = ownerOpt.get();
        return new BusinessOwnerDTO(owner.getId(), owner.getName(), owner.getEmail(), owner.getBusiness().getId());
    }

}
