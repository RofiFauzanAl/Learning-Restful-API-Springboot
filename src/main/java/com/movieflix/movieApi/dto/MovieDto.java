package com.movieflix.movieApi.dto;

import java.util.Set;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {
    private UUID id;

    @NotBlank(message = "Please provide a title!")
    private String title;

    @NotBlank(message = "Please provide a director!")
    private String director;

    @NotBlank(message = "Please provide a studio!")
    private String studio;

    private Set<String> movieCast;

    @NotNull(message = "Please provide a release year!")
    private Integer realeaseYear;

    @NotBlank(message = "Please provide a poster url!")
    private String poster;

    @NotBlank(message = "Please provide poster's url!")
    private String posterUrl;
}
