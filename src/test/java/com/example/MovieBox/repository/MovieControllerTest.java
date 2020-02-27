package com.example.MovieBox.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieControllerTest {

    @LocalServerPort
    private String port;
    private RestTemplate restTemplate = new RestTemplate();

    private MovieEntity movie;

    @Autowired
    private MovieRepository movieRepository;

    @BeforeEach
    public void setup() {
        movie = MovieEntity.builder()
                .id("movie1")
                .name("Star Wars 1")
                .description("Best Movie Ever")
                .build();
    }

    @Test
    void creteMovie() {
        HttpEntity request = new HttpEntity(movie);

        ResponseEntity<MovieEntity> response = restTemplate.exchange("http://localhost:" + port + "/movieBox/movie", HttpMethod.POST, request, MovieEntity.class);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(movie.id, response.getBody().id);
    }

    @Test
    void getMovieById_success() {
        movieRepository.save(movie);

        ResponseEntity<MovieEntity> response = restTemplate.exchange("http://localhost:" + port + "/movieBox/movie/" + movie.id, HttpMethod.GET, null, MovieEntity.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    void getMovieById_failure() {
        Assertions.assertThrows(HttpClientErrorException.class, () -> {
            ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/movieBox/movie/banana", HttpMethod.GET, null, String.class);

            Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            Assertions.assertNotNull(response.getBody());
        });
    }

    @Test
    void getAllMovies() {
        ResponseEntity<MovieEntity[]> response = restTemplate.exchange("http://localhost:" + port + "/movieBox/movies", HttpMethod.GET, null, MovieEntity[].class);
    }

    @Test
    void deleteMovie() {
    }
}