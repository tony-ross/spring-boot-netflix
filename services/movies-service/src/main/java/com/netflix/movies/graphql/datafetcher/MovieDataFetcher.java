package com.netflix.movies.graphql.datafetcher;

import com.netflix.movies.dto.CreateMovieDto;
import com.netflix.movies.dto.MovieDto;
import com.netflix.movies.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

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
        return movieService.findById(Long.valueOf(id));
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
    public MovieDto createMovie(@Argument CreateMovieInput input) {
        CreateMovieDto createDto = new CreateMovieDto(
                input.title(),
                input.description(),
                input.releaseDate(),
                input.genre(),
                input.director()
        );
        return movieService.create(createDto);
    }

    @MutationMapping
    public MovieDto updateMovie(@Argument String id, @Argument UpdateMovieInput input) {
        CreateMovieDto updateDto = new CreateMovieDto(
                input.title(),
                input.description(),
                input.releaseDate(),
                input.genre(),
                input.director()
        );
        return movieService.update(Long.valueOf(id), updateDto);
    }

    @MutationMapping
    public Boolean deleteMovie(@Argument String id) {
        movieService.deleteById(Long.valueOf(id));
        return true;
    }

    // Input record classes for GraphQL
    public record CreateMovieInput(
            String title,
            String description,
            java.time.LocalDate releaseDate,
            String genre,
            String director
    ) {}

    public record UpdateMovieInput(
            String title,
            String description,
            java.time.LocalDate releaseDate,
            String genre,
            String director
    ) {}
}
