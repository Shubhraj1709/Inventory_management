package com.inventory.services;

import com.inventory.dto.EmployeeDTO;
import com.inventory.dto.EmployeeRequest;
import com.inventory.dto.EmployeeUpdateRequest;
import com.inventory.entities.Business;
import com.inventory.entities.BusinessOwner;
import com.inventory.entities.Employee;
import com.inventory.entities.Subscription;
import com.inventory.entities.User;
import com.inventory.enums.EmployeeRole;
import com.inventory.enums.Role;
import com.inventory.exceptions.ResourceNotFoundException;
import com.inventory.repositories.BusinessOwnerRepository;
import com.inventory.repositories.BusinessRepository;
import com.inventory.repositories.EmployeeRepository;
import com.inventory.repositories.SubscriptionRepository;
import com.inventory.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;
    private final BusinessOwnerRepository businessOwnerRepository;
    private final PasswordEncoder passwordEncoder; // For encoding passwords
    private final SubscriptionRepository subscriptionRepository;


    // Method to add an employee
    public void addEmployee(EmployeeRequest request) {
        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setPassword(passwordEncoder.encode(request.getPassword()));
        employee.setRole(Role.EMPLOYEE);
        employee.setPermissions(request.getPermissions());

        employeeRepository.save(employee);
        
     // Step 2: Add to User table
        Business business = businessRepository.findById(request.getBusinessId())
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));

        List<BusinessOwner> owners = businessOwnerRepository.findByBusiness_Id(request.getBusinessId());
        if (owners.isEmpty()) {
            throw new ResourceNotFoundException("Business owner not found");
        }
        BusinessOwner owner = owners.get(0); // Pick first one or customize logic

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.EMPLOYEE);
        user.setBusiness(business);
        user.setBusinessOwner(owner);
        user.setSubscriptionPlan(owner.getSubscriptionPlan().toDbValue());
        
     // ✅ NEW: Set plan start and end from active subscription
        Subscription activeSubscription = subscriptionRepository
                .findTopByBusinessOwnerIdAndIsActiveTrueOrderByStartDateDesc(owner.getId())
                .orElse(null);

        if (activeSubscription != null) {
            user.setPlanStart(activeSubscription.getStartDate());
            user.setPlanEnd(activeSubscription.getEndDate());
        }else {
            // Default to 1 month
            user.setPlanStart(LocalDate.now());
            user.setPlanEnd(LocalDate.now().plusMonths(1));
        }
        
        System.out.println("Active Subscription: " + activeSubscription);

        userRepository.save(user);
    
    }

    // Method to update an employee
    public void updateEmployee(Long id, EmployeeUpdateRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        employee.setPermissions(request.getPermissions());
        employeeRepository.save(employee);
    }

    // Method to get all employees
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(e -> new EmployeeDTO(e.getId(), e.getName(), e.getRole()))
                .collect(Collectors.toList());
    }
    
    public void deleteEmployeeById(Long id) {
        employeeRepository.deleteById(id);
    }

}