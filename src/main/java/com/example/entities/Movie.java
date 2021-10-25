package com.example.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Table(name = "movies")
@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "original_title", nullable = false)
    private String original_title;

    @Column(name = "adult", nullable = false)
    private boolean adult;

    @Column(name = "video", nullable = false)
    private boolean video;

    @Column(name = "backdrop_path", nullable = false, length = 50)
    private String backdrop_path;

    @Column(name = "poster_path", nullable = false, length = 50)
    private String poster_path;

    @Column(name = "original_language", nullable = false, length = 3)
    private String original_language;

    @Column(name = "popularity", nullable = false)
    private float popularity;

    @Column(name = "vote_average", nullable = false)
    private float voteAverage;

    @Column(name = "vote_count", nullable = false)
    private Integer vote_count;

    @Column(name = "overview", nullable = false, columnDefinition = "TEXT")
    private String overview;

    @Column(name = "release_date", nullable = false)
    private Date release_date;

    //    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    //    @JoinColumn(name = "movie_id")
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "movie_genres",
            joinColumns = @JoinColumn(name = "movie"),
            inverseJoinColumns = @JoinColumn(name = "genre")
    )
    private List<Genre> genres = new ArrayList<>();

    @OneToMany(mappedBy = "id", fetch = FetchType.EAGER)
    private List<Rating> ratings = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "flagged_movies",
            joinColumns = @JoinColumn(name = "movie"),
            inverseJoinColumns = @JoinColumn(name = "user")
    )
    private List<User> flaggedMovies = new ArrayList<>();

    @Column(name = "hidden")
    @ColumnDefault("false")
    private boolean hidden;

    public Movie(Integer id, String title, String original_title, boolean adult, boolean video, String backdrop_path, String poster_path, String original_language, float popularity, float voteAverage, Integer vote_count, String overview, Date release_date) {
        this.id = id;
        this.title = title;
        this.original_title = original_title;
        this.adult = adult;
        this.video = video;
        this.backdrop_path = backdrop_path;
        this.poster_path = poster_path;
        this.original_language = original_language;
        this.popularity = popularity;
        this.voteAverage = voteAverage;
        this.vote_count = vote_count;
        this.overview = overview;
        this.release_date = release_date;
    }

    public Movie() {
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", original_title='" + original_title + '\'' +
                ", adult=" + adult +
                ", video=" + video +
                ", backdrop_path='" + backdrop_path + '\'' +
                ", poster_path='" + poster_path + '\'' +
                ", original_language='" + original_language + '\'' +
                ", popularity=" + popularity +
                ", vote_average=" + voteAverage +
                ", vote_count=" + vote_count +
                ", overview='" + overview + '\'' +
                ", release_date=" + release_date +
                ", genres=" + genres +
                ", ratings=" + ratings +
                ", flaggedMovies=" + flaggedMovies +
                ", hidden=" + hidden +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public float getVote_average() {
        return voteAverage;
    }

    public void setVote_average(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Integer getVote_count() {
        return vote_count;
    }

    public void setVote_count(Integer vote_count) {
        this.vote_count = vote_count;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Date getRelease_date() {
        return release_date;
    }

    public void setRelease_date(Date release_date) {
        this.release_date = release_date;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<Genre> genres) {
        this.genres = genres;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public List<User> getFlagged() {
        return flaggedMovies;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean wasRatedBefore(String userId) {
        for (Rating rating :
                ratings) {
            if (rating.getUser().getUsername().equals(userId) && rating.getMovie().getId() == id)
                return true;
        }
        return false;
    }

    public boolean wasFlaggedBefore(String userId) {
        for (User user :
                flaggedMovies) {
            if (user.getUsername().equals(userId))
                return true;
        }
        return false;
    }

    public void recalculateVote(int vote) {
        float voteSum = this.voteAverage * this.vote_count;
        this.voteAverage = (voteSum + vote) / (this.vote_count + 1);
    }
}