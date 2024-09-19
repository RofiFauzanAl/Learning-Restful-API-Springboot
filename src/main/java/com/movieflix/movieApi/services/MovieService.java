package com.movieflix.movieApi.services;

import java.io.IOException;
// import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.movieflix.movieApi.dto.MovieDto;
import com.movieflix.movieApi.dto.response.ResponseApiDto;
import com.movieflix.movieApi.dto.response.ResponseListApiDto;

public interface MovieService {
    ResponseApiDto createMovie (MovieDto movieDto, MultipartFile file) throws IOException;

    ResponseApiDto getMovieById(UUID id);

    ResponseListApiDto getAllMovie(String title, String director, Integer page, Integer size, String sortBy, String sortDir);

    MovieDto updateMovieDto(UUID id, MovieDto movieDto, MultipartFile file);
}
