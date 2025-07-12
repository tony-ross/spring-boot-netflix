package com.yourapp.movies.dto;

import java.time.LocalDate;
import java.util.List;

public record MovieDto(
        Long id,
        String title,
        String description,
        LocalDate releaseDate,
        String genre,
        String director,
        List<ReviewSummaryDto> reviews
) {
    public MovieDto {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
    }
}
