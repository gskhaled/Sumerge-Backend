package com.example.controllers;

import com.example.dtos.MovieEditForm;
import com.example.dtos.MovieIdForm;
import com.example.dtos.RatingForm;
import com.example.entities.Movie;
import com.example.services.MovieService;
import com.example.services.MovieServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MovieController {
    @Autowired
    private MovieService movieService = new MovieServiceImpl();

    @RequestMapping("/movies/{id}")
    public Movie getMovie(@PathVariable String id) {
        return movieService.getMovie(Integer.parseInt(id));
    }

    @RequestMapping("/movies")
    public List<Movie> getAllMovies(@RequestParam(name = "page", required = false, defaultValue = "1") String page) {
        return movieService.getAllMovies(Integer.parseInt(page));
    }

    @RequestMapping("/movies/recommend")
    public List<Movie> recommendMovies(@RequestHeader("Authorization") String token) {
        return movieService.recommendMovies(token.substring(7));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/movies/editMovie")
    public Movie editMovie(@RequestBody MovieEditForm form) {
        return movieService.editMovie(form.getMovieId(), form.getLanguage(), form.getRelease_date(), form.getGenre_ids());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/movies")
    public Movie addMovie(@RequestBody Movie movie) {
        return movieService.addMovie(movie);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/movies/addRating")
    private String addRating(@RequestBody RatingForm form, @RequestHeader("Authorization") String token) {
        return movieService.addRating(form.getMovieId(), token.substring(7), Short.parseShort(form.getRating()));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/movies/addFlag")
    private String addFlag(@RequestBody MovieIdForm form, @RequestHeader("Authorization") String token) {
        return movieService.addFlag(form.getMovieId(), token.substring(7));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/movies/removeFlag")
    private String removeFlag(@RequestBody MovieIdForm form, @RequestHeader("Authorization") String token) {
        return movieService.removeFlag(form.getMovieId(), token.substring(7));
    }

    //    @RolesAllowed("admin")
    @RequestMapping(method = RequestMethod.POST, value = "/movies/hideMovie")
    private String hideMovie(@RequestBody MovieIdForm movieId) {
        return movieService.hideMovie(movieId.getMovieId());
    }

    //    @RolesAllowed("admin")
    @RequestMapping(method = RequestMethod.POST, value = "/movies/showMovie")
    private String showMovie(@RequestBody MovieIdForm movieId) {
        return movieService.showMovie(movieId.getMovieId());
    }
}
