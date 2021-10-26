package com.example.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;

public class MovieIdForm {
    int movieId;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public MovieIdForm(int movieId) {
        this.movieId = movieId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
}
