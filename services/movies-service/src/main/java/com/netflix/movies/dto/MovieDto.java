package com.netflix.movies.dto;

import java.time.LocalDate;

public record MovieDto(
        Long id,
        String title,
        String description,
        LocalDate releaseDate,
        String genre,
        String director
) {
    public MovieDto {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
    }
}
