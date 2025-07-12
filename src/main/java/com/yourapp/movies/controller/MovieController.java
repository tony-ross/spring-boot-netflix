package com.yourapp.movies.controller;

import com.yourapp.common.dto.ApiResponse;
import com.yourapp.movies.dto.CreateMovieDto;
import com.yourapp.movies.dto.MovieDto;
import com.yourapp.movies.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<MovieDto>>> getAllMovies() {
        try {
            List<MovieDto> movies = movieService.findAll();
            return ResponseEntity.ok(
                ApiResponse.success("Movies retrieved successfully", movies)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to retrieve movies"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MovieDto>> getMovieById(@PathVariable Long id) {
        try {
            MovieDto movie = movieService.findById(id);
            return ResponseEntity.ok(
                ApiResponse.success("Movie retrieved successfully", movie)
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to retrieve movie"));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MovieDto>> createMovie(@Valid @RequestBody CreateMovieDto createMovieDto) {
        try {
            MovieDto createdMovie = movieService.create(createMovieDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Movie created successfully", createdMovie));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to create movie"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MovieDto>> updateMovie(@PathVariable Long id,
                                                            @Valid @RequestBody CreateMovieDto updateMovieDto) {
        try {
            MovieDto updatedMovie = movieService.update(id, updateMovieDto);
            return ResponseEntity.ok(
                ApiResponse.success("Movie updated successfully", updatedMovie)
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to update movie"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMovie(@PathVariable Long id) {
        try {
            movieService.deleteById(id);
            return ResponseEntity.ok(
                ApiResponse.success("Movie deleted successfully", null)
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to delete movie"));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<MovieDto>>> searchMovies(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String director) {
        try {
            List<MovieDto> movies;
            if (title != null && !title.trim().isEmpty()) {
                movies = movieService.findByTitle(title);
            } else if (genre != null && !genre.trim().isEmpty()) {
                movies = movieService.findByGenre(genre);
            } else if (director != null && !director.trim().isEmpty()) {
                movies = movieService.findByDirector(director);
            } else {
                movies = movieService.findAll();
            }
            return ResponseEntity.ok(
                ApiResponse.success("Movies retrieved successfully", movies)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to search movies"));
        }
    }
}
