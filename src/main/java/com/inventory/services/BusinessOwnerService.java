package com.inventory.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.inventory.entities.BusinessOwner;
import com.inventory.entities.User;
import com.inventory.enums.Role;
import com.inventory.exceptions.ResourceNotFoundException;
import com.inventory.repositories.BusinessOwnerRepository;
import com.inventory.repositories.UserRepository;

import com.inventory.dto.UserDTO;
import com.inventory.dto.EmployeeRequest;
import com.inventory.dto.EmployeeUpdateRequest;

import com.inventory.entities.UserPermission;
	

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class BusinessOwnerService {

    private final UserRepository userRepository;

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

}
