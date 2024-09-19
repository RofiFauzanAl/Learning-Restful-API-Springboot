package com.movieflix.movieApi.dto.mapper;

import com.movieflix.movieApi.dto.MovieDto;
import com.movieflix.movieApi.entities.Movie;

public class MovieMapper {
    
    public static MovieDto mapToMovieDto (Movie movie) {
        return new MovieDto(
            movie.getId(),
            movie.getTitle(),
            movie.getDirector(),
            movie.getStudio(),
            movie.getMovieCast(),
            movie.getRealeaseYear(),
            movie.getPoster(), null
        );
    }

    public static Movie mapTMovie(MovieDto movieDto) {
        return new Movie(
            movieDto.getId(),
            movieDto.getTitle(),
            movieDto.getDirector(),
            movieDto.getStudio(),
            movieDto.getMovieCast(),
            movieDto.getRealeaseYear(),
            movieDto.getPoster()
        );
    }
}
