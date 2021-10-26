package com.example.dtos;

import com.example.entities.Movie;

import java.util.List;

public class MovieResponseDTO {
    List<Movie> results;

    public List<Movie> getMovies() {
        return results;
    }

    public void setMovies(List<Movie> results) {
        this.results = results;
    }
}
