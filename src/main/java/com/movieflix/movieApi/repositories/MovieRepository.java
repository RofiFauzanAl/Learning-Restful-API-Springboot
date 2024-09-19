package com.movieflix.movieApi.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import com.movieflix.movieApi.entities.Movie;

public interface MovieRepository extends JpaRepository<Movie, UUID>{
    Page<Movie> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
