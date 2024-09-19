package com.movieflix.movieApi.entities;

import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "title", nullable = false, length = 100)
    @NotBlank(message = "Please provide a title!")
    private String title;

    @Column(name = "director", length = 100, nullable = false)
    @NotBlank(message = "Please provide a director!")
    private String director;

    @Column(name = "studio", length = 100, nullable = false)
    @NotBlank(message = "Please provide a studio!")
    private String studio;

    @ElementCollection
    @Column(name = "movie_cast")
    private Set<String> movieCast;

    @NotNull(message = "Please provide a release year!")
    @Column(name = "release_year", nullable = false)
    private Integer realeaseYear;
    
    @NotBlank(message = "Please provide a poster url!")
    @Column(name = "poster", nullable = false)
    private String poster;
}
