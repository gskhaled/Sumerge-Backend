package com.example.dtos;

import com.example.entities.Movie;

import java.util.Date;
import java.util.List;

public class MovieDTO {
    float popularity;
    String backdrop_path;
    List<Integer> genre_ids;
    int id;
    String original_language;
    String original_title;
    String overview;
    String title;
    String poster_path;
    Date release_date;
    boolean video;
    int vote_count;
    float vote_average;
    boolean adult;

    public MovieDTO() {
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public List<Integer> getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(List<Integer> genre_ids) {
        this.genre_ids = genre_ids;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public Date getRelease_date() {
        return release_date;
    }

    public void setRelease_date(Date release_date) {
        this.release_date = release_date;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public float getVote_average() {
        return vote_average;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    @Override
    public String toString() {
        return "MovieDTO{" +
                "popularity=" + popularity +
                ", backdrop_path='" + backdrop_path + '\'' +
                ", genre_ids=" + genre_ids +
                ", id=" + id +
                ", original_language='" + original_language + '\'' +
                ", original_title='" + original_title + '\'' +
                ", overview='" + overview + '\'' +
                ", title='" + title + '\'' +
                ", poster_path='" + poster_path + '\'' +
                ", release_date=" + release_date +
                ", video=" + video +
                ", vote_count=" + vote_count +
                ", vote_average=" + vote_average +
                ", adult=" + adult +
                '}';
    }

    public Movie mapToMovie() {
        Movie movie = new Movie(this.id,
                this.title,
                this.original_title,
                this.adult,
                this.video,
                this.backdrop_path,
                this.poster_path,
                this.original_language,
                this.popularity,
                this.vote_average,
                this.vote_count,
                this.overview,
                this.release_date);
        return movie;
    }
}