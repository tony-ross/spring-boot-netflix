package com.yourapp.reviews.dto;

public record CreateReviewDto(
        String text,
        Integer rating,
        Long movieId,
        Long userId
) {
    public CreateReviewDto {
        if (rating == null) {
            throw new IllegalArgumentException("Rating cannot be null");
        }
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        if (text != null && text.length() > 1000) {
            throw new IllegalArgumentException("Review text must not exceed 1000 characters");
        }
        if (movieId == null) {
            throw new IllegalArgumentException("Movie ID cannot be null");
        }
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
    }
}
