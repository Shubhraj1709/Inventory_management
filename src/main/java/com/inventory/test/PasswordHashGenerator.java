package com.inventory.test;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("firm123");
        System.out.println("BCrypt Hash for 'firm123': " + hash);
    }
}
