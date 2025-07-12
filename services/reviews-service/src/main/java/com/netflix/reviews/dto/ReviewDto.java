package com.netflix.reviews.dto;

import java.time.LocalDateTime;

public record ReviewDto(
        Long id,
        String text,
        Integer rating,
        Long movieId,
        Long userId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public ReviewDto {
        if (rating != null && (rating < 1 || rating > 5)) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
    }
}
