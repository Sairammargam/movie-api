package com.movieflix.services;

import com.movieflix.dto.MovieDto;
import com.movieflix.dto.MoviePageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MovieService {



     MovieDto addMovie(MovieDto movieDto , MultipartFile file) throws IOException;



     MovieDto getMovie(Integer movieId);



     List<MovieDto> getAllMovies();




     MovieDto updateMovie(Integer movieId,MovieDto movieDto,MultipartFile file) throws IOException;




     String deleteMovie(Integer movieId) throws IOException;


     MoviePageResponse getAllMovieResponseWithPagenation(Integer pageNumber , Integer pageSize);


     MoviePageResponse getAllMovieResponseWithPagenationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String dir);


}
