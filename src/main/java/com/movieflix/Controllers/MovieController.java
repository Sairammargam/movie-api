package com.movieflix.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movieflix.Exceptions.EmptyFileException;
import com.movieflix.Utils.AppConstants;
import com.movieflix.dto.MovieDto;
import com.movieflix.dto.MoviePageResponse;
import com.movieflix.entites.Movie;
import com.movieflix.services.MovieService;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/movie")
public class MovieController {
     private  final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }


    //adding new movie
      @PostMapping("/add-movie")
      public ResponseEntity<MovieDto> addMovieHandler(@RequestPart MultipartFile file, @RequestPart String movieDto) throws IOException, EmptyFileException {


        if(file.isEmpty()){
            throw new EmptyFileException("File is empty please send another file");
        }
          MovieDto dto = movieDtoConverter(movieDto);
          return new ResponseEntity<>(movieService.addMovie(dto,file), HttpStatus.CREATED);
      }
    //String to moviedto obj converter
      private MovieDto movieDtoConverter(String movieDtoObj) throws JsonProcessingException {
        MovieDto movieDto = new MovieDto();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(movieDtoObj,MovieDto.class);
      }

      //retreving movie by movieId
      @GetMapping("/{movieId}")
       public ResponseEntity<MovieDto> getMovieHandler(@PathVariable Integer movieId){
        return ResponseEntity.ok(movieService.getMovie(movieId));
      }

      //retreving all movies
      public ResponseEntity<List<MovieDto>> getAllMoviesHandler(){
        return ResponseEntity.ok(movieService.getAllMovies());
      }

      //updating movie by movie Id
      @PutMapping("/update/{movieId}")
       public ResponseEntity<MovieDto> updateMovieHandler(@PathVariable Integer movieId,
                                                          @RequestPart String movieDtoObj,
                                                          @RequestPart MultipartFile file) throws IOException {
        if(file.isEmpty())
            file=null;
        MovieDto movieDto = movieDtoConverter(movieDtoObj);
        return ResponseEntity.ok(movieService.updateMovie(movieId,movieDto,file));
      }

      // deleting movies by their ids
      @DeleteMapping("/delete/{movieId}")
      public ResponseEntity<String> deleteMovieHandler(@PathVariable Integer movieId) throws IOException {
        return ResponseEntity.ok(movieService.deleteMovie(movieId));
      }


      @GetMapping("/allMoviePages")
      public ResponseEntity<MoviePageResponse> getAllMovieResponseWithPagenationHandler(
              @RequestParam(defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
              @RequestParam(defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize
      ){
        return ResponseEntity.ok(movieService.getAllMovieResponseWithPagenation(pageNumber,pageSize));
      }

      @GetMapping("/allMoviePageSort")
    public ResponseEntity<MoviePageResponse> getAllMovieResponseWithPagenationAndSortingHandler(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(defaultValue = AppConstants.SORT_BY,required = false) String sortBy,
            @RequestParam(defaultValue = AppConstants.DIR,required = false) String dir
    ){
        return ResponseEntity.ok(movieService.getAllMovieResponseWithPagenationAndSorting(pageNumber,pageSize,sortBy,dir));
    }

}
