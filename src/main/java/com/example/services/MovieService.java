package com.example.services;

import com.example.entities.Movie;

import java.util.Date;
import java.util.List;

public interface MovieService {
    void populateDatabase();

    List<Movie> getAllMovies(int page);

    Movie getMovie(int id);

    List<Movie> recommendMovies(String token);

    Movie addMovie(Movie movie);

    Movie editMovie(int movieId, String language, Date release_date, List<Integer> genre_ids);

    String addRating(int movieId, String token, short rating);

    String addFlag(int movieId, String token);

    String removeFlag(int movieId, String token);

    String hideMovie(int movieId);

    String showMovie(int movieId);

}
