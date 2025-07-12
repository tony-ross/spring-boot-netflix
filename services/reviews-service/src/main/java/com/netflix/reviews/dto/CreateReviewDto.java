package com.netflix.reviews.dto;

public record CreateReviewDto(
        String text,
        Integer rating,
        Long movieId,
        Long userId
) {
    public CreateReviewDto {
        if (rating == null) {
            throw new IllegalArgumentException("Rating is required");
        }
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        if (movieId == null) {
            throw new IllegalArgumentException("Movie ID is required");
        }
        if (userId == null) {
            throw new IllegalArgumentException("User ID is required");
        }
        if (text != null && text.length() > 1000) {
            throw new IllegalArgumentException("Review text must not exceed 1000 characters");
        }
    }
}
