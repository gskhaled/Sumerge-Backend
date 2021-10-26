package com.example.controllers;

import com.example.entities.Movie;
import com.example.services.MovieService;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
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
        return movieService.addRating(form.movieId, token.substring(7), Short.parseShort(form.rating));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/movies/addFlag")
    private String addFlag(@RequestBody MovieIdForm form, @RequestHeader("Authorization") String token) {
        return movieService.addFlag(form.movieId, token.substring(7));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/movies/removeFlag")
    private String removeFlag(@RequestBody MovieIdForm form, @RequestHeader("Authorization") String token) {
        return movieService.removeFlag(form.movieId, token.substring(7));
    }

    //    @RolesAllowed("admin")
    @RequestMapping(method = RequestMethod.POST, value = "/movies/hideMovie")
    private String hideMovie(@RequestBody MovieIdForm movieId) {
        return movieService.hideMovie(movieId.movieId);
    }

    //    @RolesAllowed("admin")
    @RequestMapping(method = RequestMethod.POST, value = "/movies/showMovie")
    private String showMovie(@RequestBody MovieIdForm movieId) {
        return movieService.showMovie(movieId.movieId);
    }

    private static class RatingForm {
        int movieId;
        String rating;

        public RatingForm(int movieId, String rating) {
            this.movieId = movieId;
            this.rating = rating;
        }
    }

    private static class MovieIdForm {
        int movieId;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public MovieIdForm(int movieId) {
            this.movieId = movieId;
        }
    }

    private static class MovieEditForm {
        private int movieId;
        private String language;
        private List<Integer> genre_ids;
        private Date release_date;

        public MovieEditForm(int movieId, String language, ArrayList<Integer> genre_ids, Date release_date) {
            this.movieId = movieId;
            this.language = language;
            this.genre_ids = genre_ids;
            this.release_date = release_date;
        }

        public MovieEditForm() {
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public List<Integer> getGenre_ids() {
            return genre_ids;
        }

        public void setGenre_ids(List<Integer> genre_ids) {
            this.genre_ids = genre_ids;
        }

        public Date getRelease_date() {
            return release_date;
        }

        public void setRelease_date(Date release_date) {
            this.release_date = release_date;
        }

        public int getMovieId() {
            return movieId;
        }

        public void setMovieId(int movieId) {
            this.movieId = movieId;
        }
    }
}
