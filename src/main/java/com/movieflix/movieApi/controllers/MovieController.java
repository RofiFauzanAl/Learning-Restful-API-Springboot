package com.movieflix.movieApi.controllers;

import java.io.IOException;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movieflix.movieApi.dto.MovieDto;
import com.movieflix.movieApi.dto.response.ResponseApiDto;
import com.movieflix.movieApi.dto.response.ResponseListApiDto;
// import com.movieflix.movieApi.entities.Movie;
import com.movieflix.movieApi.services.MovieService;

@RestController
@RequestMapping("/api/v1/movie")
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping(path = "/add-movie")
    public ResponseEntity<ResponseApiDto> addMovieHandler(
        @RequestPart("file") MultipartFile file, 
        @RequestPart("movieDto") String movieDto) throws IOException {
        MovieDto moviedto = convertToMovieDto(movieDto);
        ResponseApiDto response = movieService.createMovie(moviedto, file);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseApiDto> getMovieByIdHandler(@PathVariable UUID id) {
        ResponseApiDto response = movieService.getMovieById(id);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/get-movies")
    public ResponseEntity<ResponseListApiDto> getAllMovies(
        @RequestParam(value = "title", required = false) String title,
        @RequestParam(value = "director", required = false) String director,
        @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
        @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
        @RequestParam(value = "sortBy", required = false, defaultValue = "title") String sortBy,
        @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir){
        ResponseListApiDto response = movieService.getAllMovie(title, director, page, size, sortBy, sortDir);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PutMapping("/update-movie/{id}")
    public ResponseEntity<ResponseApiDto> updateMovie(
        @PathVariable UUID id, @RequestPart MultipartFile file, @RequestPart String movieDto 
    ) throws IOException {
        if (file.isEmpty()) file = null;
        MovieDto moviedto = convertToMovieDto(movieDto);
        ResponseApiDto response = movieService.updateMovieDto(id, moviedto, file);
        return new ResponseEntity<>(response, response.getStatus());
    }   

    @DeleteMapping("/delete-movie/{id}")
    public ResponseEntity<ResponseApiDto> deleteMovie(@PathVariable UUID id) throws IOException {
        ResponseApiDto response = movieService.deleteMovie(id);
        return new ResponseEntity<>(response, response.getStatus());
    }

    private MovieDto convertToMovieDto(String movieToObj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("Movie to obj: " + movieToObj);
        return objectMapper.readValue(movieToObj, MovieDto.class);
    }

}
