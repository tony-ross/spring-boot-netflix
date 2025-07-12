package com.yourapp.movies.service;

import com.yourapp.movies.dto.CreateMovieDto;
import com.yourapp.movies.dto.MovieDto;
import com.yourapp.movies.dto.ReviewSummaryDto;
import com.yourapp.movies.entity.Movie;
import com.yourapp.movies.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MovieDto> findAll() {
        return movieRepository.findAllWithReviews()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public MovieDto findById(Long id) {
        Movie movie = movieRepository.findByIdWithReviews(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found with id: " + id));
        return convertToDto(movie);
    }

    @Override
    public MovieDto create(CreateMovieDto createMovieDto) {
        Movie movie = convertToEntity(createMovieDto);
        Movie savedMovie = movieRepository.save(movie);
        return convertToDto(savedMovie);
    }

    @Override
    public MovieDto update(Long id, CreateMovieDto updateMovieDto) {
        Movie existingMovie = movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found with id: " + id));

        existingMovie.setTitle(updateMovieDto.title());
        existingMovie.setDescription(updateMovieDto.description());
        existingMovie.setReleaseDate(updateMovieDto.releaseDate());
        existingMovie.setGenre(updateMovieDto.genre());
        existingMovie.setDirector(updateMovieDto.director());

        Movie updatedMovie = movieRepository.save(existingMovie);
        return convertToDto(updatedMovie);
    }

    @Override
    public void deleteById(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new IllegalArgumentException("Movie not found with id: " + id);
        }
        movieRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovieDto> findByTitle(String title) {
        return movieRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovieDto> findByGenre(String genre) {
        return movieRepository.findByGenre(genre)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovieDto> findByDirector(String director) {
        return movieRepository.findByDirectorContainingIgnoreCase(director)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private MovieDto convertToDto(Movie movie) {
        return new MovieDto(
                movie.getId(),
                movie.getTitle(),
                movie.getDescription(),
                movie.getReleaseDate(),
                movie.getGenre(),
                movie.getDirector(),
                movie.getReviews() != null ? movie.getReviews().stream()
                        .map(review -> new ReviewSummaryDto(
                                review.getId(),
                                review.getText(),
                                review.getRating(),
                                review.getUser() != null ? review.getUser().getUsername() : null
                        ))
                        .collect(Collectors.toList()) : List.of()
        );
    }

    private Movie convertToEntity(CreateMovieDto dto) {
        Movie movie = new Movie();
        movie.setTitle(dto.title());
        movie.setDescription(dto.description());
        movie.setReleaseDate(dto.releaseDate());
        movie.setGenre(dto.genre());
        movie.setDirector(dto.director());
        return movie;
    }
}
