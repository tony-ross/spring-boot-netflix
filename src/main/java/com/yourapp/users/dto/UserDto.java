package com.yourapp.users.dto;

import com.yourapp.movies.dto.ReviewSummaryDto;

import java.time.LocalDateTime;
import java.util.List;

public record UserDto(
        Long id,
        String username,
        String email,
        String firstName,
        String lastName,
        List<ReviewSummaryDto> reviews,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public UserDto {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
    }

    public String fullName() {
        if (firstName == null && lastName == null) {
            return username;
        }
        return ((firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "")).trim();
    }
}
