package com.netflix.movies.repository;

import com.netflix.movies.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("SELECT m FROM Movie m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Movie> findByTitleContainingIgnoreCase(@Param("title") String title);

    @Query("SELECT m FROM Movie m WHERE m.genre = :genre")
    List<Movie> findByGenre(@Param("genre") String genre);

    @Query("SELECT m FROM Movie m WHERE LOWER(m.director) LIKE LOWER(CONCAT('%', :director, '%'))")
    List<Movie> findByDirectorContainingIgnoreCase(@Param("director") String director);
}
