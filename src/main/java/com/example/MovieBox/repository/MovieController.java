package com.example.MovieBox.repository;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @GetMapping("/movies")
    @ResponseStatus(HttpStatus.OK)
    public List<MovieEntity> getAllMovies() {
        List<MovieEntity> movies = new ArrayList<MovieEntity>();
        movieRepository.findAll().forEach(movie -> movies.add(movie));
        return movies;
    }

    @DeleteMapping("/movie/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteMovie(@RequestBody MovieEntity movie) {
        movieRepository.delete(movie);
    }

    @ExceptionHandler(MovieNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleMovieNotFound(MovieNotFoundException ex) {
        return ex.getMessage();
    }
}
