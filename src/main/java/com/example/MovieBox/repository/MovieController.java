package com.example.MovieBox.repository;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movieBox")
public class MovieController {

    private final MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @PostMapping("/movie")
    @ResponseStatus(HttpStatus.CREATED)
    public MovieEntity creteMovie(@RequestBody MovieEntity movie) {
        return movieRepository.save(movie);
    }

    @GetMapping("/movie/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MovieEntity getMovieById(@PathVariable String id) {
        return movieRepository.findById(id).orElseThrow(() -> new MovieNotFoundException("Movie with id " + id + " was not found"));
    }


    public MovieEntity[] getAllMovies() {
        return null; //TODO: Out of time :(
//        Iterable<MovieEntity> movies = movieRepository.findAll();
//        movies.
//        MovieEntity[] moviesArray =
//        return movieRepository.findAll().;
    }

    public MovieEntity deleteMovie() {
        return null;
    }

    @ExceptionHandler(MovieNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleMovieNotFound(MovieNotFoundException ex) {
        return ex.getMessage();
    }
}
