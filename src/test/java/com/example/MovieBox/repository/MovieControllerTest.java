package com.example.MovieBox.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;


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
        movieRepository.deleteAll();;
        movie = createMovie("movie1", "Star Wars 1", "Best Movie Ever");
    }

    private MovieEntity createMovie(String id, String name, String description) {
        return MovieEntity.builder().id(id).name(name).description(description).build();
    }

    @Test
    void creteMovie() {
        HttpEntity request = new HttpEntity(movie);

        ResponseEntity<MovieEntity> response = restTemplate.exchange(
                "http://localhost:" + port + "/movieBox/movie",
                HttpMethod.POST,
                request,
                MovieEntity.class);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(movie.id, response.getBody().id);
    }

    @Test
    void getMovieById_success() {
        movieRepository.save(movie);

        ResponseEntity<MovieEntity> response = restTemplate.exchange(
                "http://localhost:" + port + "/movieBox/movie/" + movie.id,
                HttpMethod.GET,
                null,
                MovieEntity.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    void getMovieById_failure() {
        Assertions.assertThrows(HttpClientErrorException.class, () -> {
            ResponseEntity<String> response = restTemplate.exchange(
                    "http://localhost:" + port + "/movieBox/movie/banana",
                    HttpMethod.GET,
                    null,
                    String.class);

            Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            Assertions.assertNotNull(response.getBody());
        });
    }

    @Test
    void getAllMovies() {
        movieRepository.save(createMovie("movie1", "Star Wars 1", "Best Movie Ever"));
        movieRepository.save(createMovie("movie2", "Star Wars 2", "Best Movie Ever"));
        movieRepository.save(createMovie("movie3", "Star Wars 3", "Best Movie Ever"));

        ResponseEntity<List<MovieEntity>> response = restTemplate.exchange(
                "http://localhost:" + port + "/movieBox/movies",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<MovieEntity>>() {}
                );

        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(3, response.getBody().size());
    }

    @Test
    void deleteMovie() {
        HttpEntity request = new HttpEntity(movie);
        movieRepository.save(movie);

        ResponseEntity<?> response = restTemplate.exchange(
                "http://localhost:" + port + "/movieBox/movie/delete",
                HttpMethod.DELETE,
                request,
                MovieEntity.class
        );

        Assertions.assertEquals(0, movieRepository.count());
    }
}