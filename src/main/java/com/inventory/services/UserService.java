package com.inventory.services;

import com.inventory.entities.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User saveUser(User user);
    Optional<User> getUserByEmail(String email);
    List<User> getAllUsers();
    void deleteUser(Long id);
}
