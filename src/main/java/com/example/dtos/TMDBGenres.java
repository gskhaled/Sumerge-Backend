package com.example.dtos;

import com.example.entities.Genre;

import java.util.List;

public class TMDBGenres {
    private List<Genre> genres;

    public TMDBGenres() {
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
}