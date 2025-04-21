package com.inventory.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventory.entities.Employee;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);
}
