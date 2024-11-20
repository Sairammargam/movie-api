package com.movieflix.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MovieDto {


    private Integer movieId;


    @NotBlank(message = "Please enter the movie title")
    private String title;


    @NotBlank(message = "Please enter name of director")
    private String director;


    @NotBlank(message = "Please enter the studio name")
    private  String studio;


    private List<String> movieCast;


    private Integer releaseYear;


    @NotBlank(message = "please upload the poster of movie")
    private String poster;

    @NotBlank(message = "please enter the posters url")
    private String posterUrl;
}
