package com.movieflix.movieApi.services.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.movieflix.movieApi.dto.MovieDto;
import com.movieflix.movieApi.dto.response.ResponseApiDto;
import com.movieflix.movieApi.dto.response.ResponseListApiDto;
import com.movieflix.movieApi.entities.Movie;
import com.movieflix.movieApi.exception.DataNotFoundException;
import com.movieflix.movieApi.exception.FileExistException;
import com.movieflix.movieApi.repositories.MovieRepository;
import com.movieflix.movieApi.services.FileService;
import com.movieflix.movieApi.services.MovieService;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    private final FileService fileService;

    @Value("${project.poster}")
    private String path;

    @Value("${base.url}")
    private String baseUrl;

    public MovieServiceImpl(MovieRepository movieRepository, FileServiceImpl fileService) {
        this.movieRepository = movieRepository;
        this.fileService = fileService;
    }

    @Override
    public ResponseApiDto createMovie(MovieDto movieDto, MultipartFile file) throws IOException {
        if (Files.exists(Paths.get(path + File.separator + file.getOriginalFilename()))){
            throw new FileExistException("File already exists! Please upload another file");
        }
        String uploadFile = fileService.uploadFile(path, file);
        movieDto.setPoster(uploadFile);

        Movie movie = new Movie(
            null,
            movieDto.getTitle(),
            movieDto.getDirector(),
            movieDto.getStudio(),
            movieDto.getMovieCast(),
            movieDto.getRealeaseYear(),
            movieDto.getPoster()
        );

        String posterUrl = baseUrl.replace("\"", "") + "/file/" + movie.getPoster();
        Movie savedMovie = movieRepository.save(movie);
        
        MovieDto response = new MovieDto(
            savedMovie.getId(),
            savedMovie.getTitle(),
            savedMovie.getDirector(),
            savedMovie.getStudio(),
            savedMovie.getMovieCast(),
            savedMovie.getRealeaseYear(),
            savedMovie.getPoster(),
            posterUrl
        );

        return ResponseApiDto.builder()
            .statusCode(HttpStatus.CREATED.value())
            .message("Movie created successfully")
            .status(HttpStatus.CREATED)
            .data(response)
            .build();
    }

    @Override
    public ResponseApiDto getMovieById(UUID id) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Movie not found with id : " + id));
        String posterUrl = baseUrl.replace("\"", "") + "/file/" + movie.getPoster();

        MovieDto response = new MovieDto(
            movie.getId(),
            movie.getTitle(),
            movie.getDirector(),
            movie.getStudio(),
            movie.getMovieCast(),
            movie.getRealeaseYear(),
            movie.getPoster(),
            posterUrl
        );
        
        return ResponseApiDto.builder()
            .statusCode(HttpStatus.OK.value())
            .message("Movie retrieved successfully")
            .status(HttpStatus.OK)
            .data(response)
            .build();
    }

    @Override
    public ResponseListApiDto getAllMovie(String title, String director, Integer page, Integer size, String sortBy, String sortDir) {
        Pageable pageable = PageRequest.of(page, size, 
                            sortDir.equalsIgnoreCase("asc") ? 
                            Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        
        Page<Movie> moviePage;

        if (title != null) {
            moviePage = movieRepository.findByTitleContainingIgnoreCase(title, pageable);
        } else {
            moviePage = movieRepository.findAll(pageable);
        }

        List<MovieDto> movies = moviePage.stream().map(movie -> {
            String posterUrl = baseUrl.replace("\"", "") + "/file/" + movie.getPoster();
            return new MovieDto(
                movie.getId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getRealeaseYear(),
                movie.getPoster(),
                posterUrl
            );
        }).toList();

        int totalPages = moviePage.getTotalPages();
        long totalData = moviePage.getTotalElements();

        return ResponseListApiDto.builder()
        .statusCode(HttpStatus.OK.value())
        .message("Movies retrieved successfully")
        .status(HttpStatus.OK)
        .totalPages(totalPages)
        .totalData(totalData)
        .currentPage(page)
        .pageSize(size)
        .data(movies)
        .build();
    }

    @Override
    public ResponseApiDto updateMovieDto(UUID id, MovieDto movieDto, MultipartFile file)  throws IOException {
        Movie mv = movieRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Movie not found with id : " + id));
        String fileName = mv.getPoster();
        if (fileName != null) {
            Files.deleteIfExists(Paths.get(path + fileName));
            fileName = fileService.uploadFile(path, file);
        }
        movieDto.setPoster(fileName);

        Movie movie = new Movie(
            mv.getId(),
            movieDto.getTitle(),
            movieDto.getDirector(),
            movieDto.getStudio(),
            movieDto.getMovieCast(),
            movieDto.getRealeaseYear(),
            movieDto.getPoster()
        );
        Movie updatedMovie = movieRepository.save(movie);
        String posterUrl = baseUrl.replace("\"", "") + "/file/" + movie.getPoster();

        MovieDto response = new MovieDto(
            updatedMovie.getId(),
            updatedMovie.getTitle(),
            updatedMovie.getDirector(),
            updatedMovie.getStudio(),
            updatedMovie.getMovieCast(),
            updatedMovie.getRealeaseYear(),
            updatedMovie.getPoster(),
            posterUrl
        );

        return ResponseApiDto.builder()
            .statusCode(HttpStatus.OK.value())
            .message("Movie updated successfully")
            .status(HttpStatus.OK)
            .data(response)
            .build();
    }

    @Transactional
    @Override
    public ResponseApiDto deleteMovie(UUID id) throws IOException {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Movie not found with id : " + id));
        Hibernate.initialize(movie.getMovieCast());
        Files.deleteIfExists(Paths.get(path + movie.getPoster()));
        movieRepository.delete(movie);

        MovieDto movieDto = new MovieDto();
        movieDto.setId(movie.getId());
        movieDto.setTitle(movie.getTitle());
        movieDto.setDirector(movie.getDirector());
        movieDto.setStudio(movie.getStudio());
        movieDto.setMovieCast(movie.getMovieCast());
        movieDto.setRealeaseYear(movie.getRealeaseYear());
        movieDto.setPoster(movie.getPoster());
        movieDto.setPosterUrl(movie.getPoster()); 
        
        return ResponseApiDto.builder()
            .statusCode(HttpStatus.OK.value())
            .message("Movie with id : " + id + " successfully deleted")
            .status(HttpStatus.OK)
            .data(movieDto)
            .build();
    }
    
}
