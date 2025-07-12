package com.yourapp.users.dto;

public record CreateUserDto(
        String username,
        String email,
        String password,
        String firstName,
        String lastName
) {
    public CreateUserDto {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (username.length() < 3 || username.length() > 50) {
            throw new IllegalArgumentException("Username must be between 3 and 50 characters");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (email.length() > 100) {
            throw new IllegalArgumentException("Email must not exceed 100 characters");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }
        if (firstName != null && firstName.length() > 100) {
            throw new IllegalArgumentException("First name must not exceed 100 characters");
        }
        if (lastName != null && lastName.length() > 100) {
            throw new IllegalArgumentException("Last name must not exceed 100 characters");
        }
    }
}
