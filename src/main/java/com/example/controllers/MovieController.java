package com.example.controllers;

import com.example.services.MovieService;
import com.example.tables.Movie;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MovieController {
    @Autowired
    private MovieService movieService;

    @RequestMapping("/movies/{id}")
    public Movie getMovie(@PathVariable String id) {
        return movieService.getMovie(Integer.parseInt(id));
    }

    @RequestMapping("/movies")
    public List<Movie> getAllMovies(@RequestParam(name = "page", required = false, defaultValue = "1") String page) {
        return movieService.getAllMovies(Integer.parseInt(page));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/movies")
    public Movie addMovie(@RequestBody Movie movie) {
        System.out.println("To add: " + movie);
        return movieService.addMovie(movie);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/movies/addRating")
    private String addRating(@RequestBody RatingForm form) {
        return movieService.addRating(form.movieId, form.userId, Short.parseShort(form.rating));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/movies/addFlag")
    private String addFlag(@RequestBody FlagForm form) {
        return movieService.addFlag(form.movieId, form.userId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/movies/removeFlag")
    private String removeFlag(@RequestBody FlagForm form) {
        return movieService.removeFlag(form.movieId, form.userId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/movies/hideMovie")
    private String hideMovie(@RequestBody MovieIdForm movieId) {
        return movieService.hideMovie(movieId.movieId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/movies/showMovie")
    private String showMovie(@RequestBody MovieIdForm movieId) {
        return movieService.showMovie(movieId.movieId);
    }

    private static class RatingForm {
        String userId;
        int movieId;
        String rating;

        public RatingForm(String userId, int movieId, String rating) {
            this.userId = userId;
            this.movieId = movieId;
            this.rating = rating;
        }
    }

    private static class FlagForm {
        String userId;
        int movieId;

        public FlagForm(String userId, int movieId) {
            this.userId = userId;
            this.movieId = movieId;
        }
    }

    private static class MovieIdForm {
        int movieId;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public MovieIdForm(int movieId) {
            this.movieId = movieId;
        }
    }
}
