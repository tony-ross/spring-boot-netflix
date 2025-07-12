package com.netflix.movies.service;

import com.netflix.movies.dto.CreateMovieDto;
import com.netflix.movies.dto.MovieDto;

import java.util.List;

public interface MovieService {

    List<MovieDto> findAll();

    MovieDto findById(Long id);

    MovieDto create(CreateMovieDto createMovieDto);

    MovieDto update(Long id, CreateMovieDto updateMovieDto);

    void deleteById(Long id);

    List<MovieDto> findByTitle(String title);

    List<MovieDto> findByGenre(String genre);

    List<MovieDto> findByDirector(String director);
}
