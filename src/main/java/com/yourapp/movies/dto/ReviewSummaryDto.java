package com.yourapp.movies.dto;

public record ReviewSummaryDto(
        Long id,
        String text,
        Integer rating,
        String userName
) {
    public ReviewSummaryDto {
        if (rating != null && (rating < 1 || rating > 5)) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
    }
}
