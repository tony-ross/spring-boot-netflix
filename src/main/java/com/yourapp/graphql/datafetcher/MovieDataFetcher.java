package com.yourapp.graphql.datafetcher;

import com.yourapp.movies.dto.CreateMovieDto;
import com.yourapp.movies.dto.MovieDto;
import com.yourapp.movies.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
public class MovieDataFetcher {

    @Autowired
    private MovieService movieService;

    @QueryMapping
    public List<MovieDto> movies() {
        return movieService.findAll();
    }

    @QueryMapping
    public MovieDto movie(@Argument String id) {
        return movieService.findById(Long.parseLong(id));
    }

    @QueryMapping
    public List<MovieDto> moviesByTitle(@Argument String title) {
        return movieService.findByTitle(title);
    }

    @QueryMapping
    public List<MovieDto> moviesByGenre(@Argument String genre) {
        return movieService.findByGenre(genre);
    }

    @QueryMapping
    public List<MovieDto> moviesByDirector(@Argument String director) {
        return movieService.findByDirector(director);
    }

    @MutationMapping
    public MovieDto createMovie(@Argument Map<String, Object> input) {
        CreateMovieDto createMovieDto = new CreateMovieDto(
            (String) input.get("title"),
            (String) input.get("description"),
            input.get("releaseDate") != null ? LocalDate.parse((String) input.get("releaseDate")) : null,
            (String) input.get("genre"),
            (String) input.get("director")
        );
        return movieService.create(createMovieDto);
    }

    @MutationMapping
    public MovieDto updateMovie(@Argument String id, @Argument Map<String, Object> input) {
        CreateMovieDto updateMovieDto = new CreateMovieDto(
            (String) input.get("title"),
            (String) input.get("description"),
            input.get("releaseDate") != null ? LocalDate.parse((String) input.get("releaseDate")) : null,
            (String) input.get("genre"),
            (String) input.get("director")
        );
        return movieService.update(Long.parseLong(id), updateMovieDto);
    }

    @MutationMapping
    public Boolean deleteMovie(@Argument String id) {
        try {
            movieService.deleteById(Long.parseLong(id));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Schema mappings for computed fields
    @SchemaMapping(typeName = "Movie", field = "averageRating")
    public Float averageRating(MovieDto movie) {
        if (movie.reviews() == null || movie.reviews().isEmpty()) {
            return null;
        }
        return (float) movie.reviews().stream()
            .mapToInt(review -> review.rating())
            .average()
            .orElse(0.0);
    }

    @SchemaMapping(typeName = "Movie", field = "reviewCount")
    public Integer reviewCount(MovieDto movie) {
        return movie.reviews() != null ? movie.reviews().size() : 0;
    }
}
