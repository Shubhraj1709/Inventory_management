package com.inventory.test;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {
    public static void main(String[] args) {
        String rawPassword = "employee123";
        String encodedPassword = new BCryptPasswordEncoder().encode(rawPassword);
        System.out.println("BCrypt Encoded: " + encodedPassword);
    }
}
