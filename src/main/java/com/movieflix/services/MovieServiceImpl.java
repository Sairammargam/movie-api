package com.movieflix.services;

import com.movieflix.Exceptions.FileExistsException;
import com.movieflix.Exceptions.MovieNotFoundException;
import com.movieflix.Repository.MovieRepository;
import com.movieflix.dto.MovieDto;
import com.movieflix.dto.MoviePageResponse;
import com.movieflix.entites.Movie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
@Service
public class MovieServiceImpl implements MovieService {
    private  final MovieRepository movieRepository;
    private final FileService fileService;


    public MovieServiceImpl(MovieRepository movieRepository, FileService fileService) {
        this.movieRepository = movieRepository;
        this.fileService = fileService;
    }
    @Value("${project.poster}")
    private String path;
    @Value("${base.url}")
    private String baseUrl;

    @Override
    public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException {
        //1.upload the file
        if(Files.exists(Paths.get(path+ File.separator+file.getOriginalFilename()))){
            throw new FileExistsException("File already exixts please enter another file name!!");
        }
         String uploadedFileName=fileService.uploadFile(path,file);
        //2.set the value of field poster as file name
        movieDto.setPoster(uploadedFileName);


        //3.map dto to movie object
        Movie movie= new Movie(
                null,
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );


        //4.save the movie object->saved movie object
      Movie savedMovie= movieRepository.save(movie);

        //5.generate the poster url
     String posterUrl = baseUrl+"/file/"+uploadedFileName;
        //6. map move object to dto object and return it
        MovieDto response= new MovieDto(
               savedMovie.getMovieId(),
               savedMovie.getTitle(),
                savedMovie.getDirector(),
                savedMovie.getStudio(),
                savedMovie.getMovieCast(),
                savedMovie.getReleaseYear(),
                savedMovie.getPoster(),
                posterUrl
        );
        return response;
    }

    @Override
    public MovieDto getMovie(Integer movieId) {
        //finding movie by movie ID
       Movie movie= movieRepository.findById(movieId).orElseThrow(()->new MovieNotFoundException("Movie not found with id = "+movieId));


       //poster url generation
        String posterUrl = baseUrl+"/file/"+movie.getPoster();
       //maping movie to movie Dto
        MovieDto response= new MovieDto(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getReleaseYear(),
                movie.getPoster(),
                posterUrl
        );

       return response;

    }

    @Override
    public List<MovieDto> getAllMovies() {
        //fetching all movies into list
        List<Movie> movies= movieRepository.findAll();

        List<MovieDto> movieDtos = new ArrayList<>();

        //mapping all movies to movie dto

        for(Movie movie : movies){
            String posterUrl = baseUrl+"/file/"+movie.getPoster();
            MovieDto movieDto= new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl
            );
            movieDtos.add(movieDto);
        }
        return movieDtos;
    }

    @Override
    public MovieDto updateMovie(Integer movieId, MovieDto movieDto, MultipartFile file) throws IOException {
       //1.check if movie obj exists
        Movie mv= movieRepository.findById(movieId).orElseThrow(()->new MovieNotFoundException("Movie not found with id = "+movieId));

        //2.if file is null, create it
        //if file is not null then we have to delete that old record and upload the new file
        String filename = mv.getPoster();
        if(file != null){
            Files.deleteIfExists(Paths.get(path+"/file"+filename));
            filename=fileService.uploadFile(path,file);
        }


        //3.set movie dtos
        movieDto.setPoster(filename);

        //4.map to movie objects
        Movie movie= new Movie(
                movieDto.getMovieId(),
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );

        //5.save the movie objects
       Movie updatedMovie =  movieRepository.save(movie);

        //6.generate the posterurls
        String posterUrl = baseUrl+"/file/"+filename;

        //7.save the moviedtos and return them
        MovieDto response= new MovieDto(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getReleaseYear(),
                movie.getPoster(),
                posterUrl
        );



        return response;
    }




    @Override
    public String deleteMovie(Integer movieId) throws IOException {
        // 1. check if movie obj exits
        Movie mv= movieRepository.findById(movieId).orElseThrow(()->new MovieNotFoundException("Movie not found with id = "+movieId));
        Integer id= mv.getMovieId();

        // 2.delete the file associated with movieId
      Files.deleteIfExists(Paths.get(path+"/file/"+mv.getPoster()));

        //3.delete the movie obj
       movieRepository.delete(mv);

        return id+"is deleted from database";
    }




    @Override
    public MoviePageResponse getAllMovieResponseWithPagenation(Integer pageNumber, Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNumber,pageSize);
       Page<Movie> moviePages=  movieRepository.findAll(pageable);
       List<Movie> movies = moviePages.getContent();
        List<MovieDto> movieDtos = new ArrayList<>();
        for(Movie movie : movies){
            String posterUrl = baseUrl+"/file/"+movie.getPoster();
            MovieDto movieDto= new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl
            );
            movieDtos.add(movieDto);
        }
        return new MoviePageResponse(movieDtos,pageNumber,pageSize,moviePages.getTotalElements(),
                moviePages.getTotalPages(),moviePages.isLast());
    }

    @Override
    public MoviePageResponse getAllMovieResponseWithPagenationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String dir) {
   Sort sort= dir.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Movie> moviePages=  movieRepository.findAll(pageable);
        List<Movie> movies = moviePages.getContent();
        List<MovieDto> movieDtos = new ArrayList<>();
        for(Movie movie : movies){
            String posterUrl = baseUrl+"/file/"+movie.getPoster();
            MovieDto movieDto= new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl
            );
            movieDtos.add(movieDto);
        }
        return new MoviePageResponse(movieDtos,pageNumber,pageSize,moviePages.getTotalElements(),
                moviePages.getTotalPages(),moviePages.isLast());
    }

}
