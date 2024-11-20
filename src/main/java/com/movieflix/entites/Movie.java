package com.movieflix.entites;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer movieId;

    @Column(nullable = false,length = 200)
    @NotBlank(message = "Please enter the movie title")
    private String title;

    @Column(nullable = false)
    @NotBlank(message = "Please enter name of director")
    private String director;

    @Column(nullable = false)
    @NotBlank(message = "Please enter the studio name")
    private  String studio;

    @ElementCollection
    @CollectionTable(name = "movie_cast")
    private List<String> movieCast;

    @Column(nullable = false)
    private Integer releaseYear;

    @Column(nullable = false)
    @NotBlank(message = "please upload the poster of movie")
    private String poster;


}
