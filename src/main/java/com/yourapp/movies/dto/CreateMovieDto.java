package com.yourapp.movies.dto;

import java.time.LocalDate;

public record CreateMovieDto(
        String title,
        String description,
        LocalDate releaseDate,
        String genre,
        String director
) {
    public CreateMovieDto {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (title.length() > 255) {
            throw new IllegalArgumentException("Title must not exceed 255 characters");
        }
        if (description != null && description.length() > 1000) {
            throw new IllegalArgumentException("Description must not exceed 1000 characters");
        }
        if (genre != null && genre.length() > 100) {
            throw new IllegalArgumentException("Genre must not exceed 100 characters");
        }
        if (director != null && director.length() > 200) {
            throw new IllegalArgumentException("Director must not exceed 200 characters");
        }
    }
}
