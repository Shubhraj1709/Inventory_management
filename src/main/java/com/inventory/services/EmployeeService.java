package com.inventory.services;

import com.inventory.dto.EmployeeDTO;
import com.inventory.dto.EmployeeRequest;
import com.inventory.dto.EmployeeUpdateRequest;
import com.inventory.entities.Employee;
import com.inventory.enums.EmployeeRole;
import com.inventory.enums.Role;
import com.inventory.exceptions.ResourceNotFoundException;
import com.inventory.repositories.EmployeeRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder; // For encoding passwords

    // Method to add an employee
    public void addEmployee(EmployeeRequest request) {
        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setPassword(passwordEncoder.encode(request.getPassword()));
        employee.setRole(Role.EMPLOYEE);
        employee.setPermissions(request.getPermissions());

        employeeRepository.save(employee);
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
}