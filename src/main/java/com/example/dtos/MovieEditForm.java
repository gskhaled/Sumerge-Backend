package com.example.dtos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MovieEditForm {
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