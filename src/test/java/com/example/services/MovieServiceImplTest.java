package com.example.services;

import com.example.entities.Genre;
import com.example.entities.Movie;
import com.example.entities.Rating;
import com.example.entities.User;
import com.example.repositories.GenreRepository;
import com.example.repositories.MovieRepository;
import com.example.repositories.RatingRepository;
import com.example.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.internal.matchers.Equality.areEqual;

class MovieServiceImplTest {
    @InjectMocks
    private MovieServiceImpl movieService;
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RatingRepository ratingRepository;
    @Mock
    private GenreRepository genreRepository;
    @Mock
    private JwtUtil jwtUtil;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllMovies() {
        Page<Movie> pages = Mockito.mock(Page.class);
        List<Movie> movies = new ArrayList<>();
        when(movieRepository.findAll(any(PageRequest.class))).thenReturn(pages);
        when(pages.toList()).thenReturn(movies);
        areEqual(movies, movieService.getAllMovies(1));
    }

    @Test
    void getMovie() {
        Movie movie = new Movie();
        when(movieRepository.findById(anyInt())).thenReturn(Optional.of(movie));
        areEqual(movie, movieService.getMovie(1));
    }

    @Test
    void recommendMovies() {
        // Rated movie genre
        User user = new User();
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(jwtUtil.extractUsername(anyString())).thenReturn("");
        Genre shouldBeThere = new Genre();
        shouldBeThere.setName("shouldBeThere");
        List<Genre> goodGenres = new ArrayList<>();
        goodGenres.add(shouldBeThere);
        Movie movie = new Movie();
        movie.setTitle("Recommended");
        movie.setGenres(goodGenres);
        Rating rating = new Rating((short) 1, user, movie);
        List<Rating> ratings = new ArrayList<>();
        ratings.add(rating);
        user.setRatings(ratings);
        List<Movie> moviesList = new ArrayList<>();
        moviesList.add(movie);
        when(movieRepository.findAll()).thenReturn(moviesList);
        assertEquals(1, movieService.recommendMovies("").size());

        // Flagged movie genre
        Genre shouldNotBeThere = new Genre();
        shouldNotBeThere.setName("shouldNotBeThere");
        List<Genre> badGenres = new ArrayList<>();
        badGenres.add(shouldNotBeThere);
        movie.setTitle("Not Recommended");
        movie.setGenres(badGenres);
        moviesList.add(movie);
        user.setFlaggedMovies(moviesList);
        assertEquals(0, movieService.recommendMovies("").size());
    }

    @Test
    void addMovie() {
        Movie movie = new Movie();
        areEqual(movie, movieService.addMovie(movie));
    }

    @Test
    void editMovie() {
        Movie movie = new Movie();
        movie.setOriginal_language("test");
        when(genreRepository.findById(anyInt())).thenReturn(Optional.of(new Genre()));
        when(movieRepository.findById(anyInt())).thenReturn(Optional.of(new Movie()));
        Movie returnedMovie = movieService.editMovie(1, "test", new Date(), new ArrayList<>());
        assertEquals(movie.getOriginal_language(), returnedMovie.getOriginal_language());
    }

    @Test
    void addRating() {
        Movie movie = new Movie();
        movie.setVote_count(1);
        movie.setVote_average(0);
        when(movieRepository.findById(anyInt())).thenReturn(Optional.of(movie));
        when(userRepository.findById(anyString())).thenReturn(Optional.of(new User()));
        when(jwtUtil.extractUsername(anyString())).thenReturn("");
        assertEquals("OK", movieService.addRating(1, "", (short) 5));
        assertEquals("Error", movieService.addRating(1, "", (short) -1));
    }

    @Test
    void addFlag() {
        String username = "user";
        User flagger = new User();
        flagger.setUsername(username);
        Movie movie = new Movie();
        when(movieRepository.findById(anyInt())).thenReturn(Optional.of(movie));
        when(userRepository.findById(anyString())).thenReturn(Optional.of(flagger));
        when(jwtUtil.extractUsername(anyString())).thenReturn(username);
        assertEquals("OK", movieService.addFlag(1, ""));
        assertEquals("Was flagged before.", movieService.addFlag(1, ""));
    }

    @Test
    void removeFlag() {
        String username = "user";
        User flagger = new User();
        flagger.setUsername(username);
        Movie movie = new Movie();
        movie.getFlagged().add(flagger);
        when(movieRepository.findById(anyInt())).thenReturn(Optional.of(movie));
        when(userRepository.findById(anyString())).thenReturn(Optional.of(flagger));
        when(jwtUtil.extractUsername(anyString())).thenReturn(username);
        assertEquals("OK", movieService.removeFlag(1, ""));
        assertEquals("Was not flagged before.", movieService.removeFlag(1, ""));
    }

    @Test
    void hideMovie() {
        Movie movie = new Movie();
        when(movieRepository.findById(anyInt())).thenReturn(Optional.of(movie));
        assertEquals("OK", movieService.hideMovie(1));
    }

    @Test
    void showMovie() {
        Movie movie = new Movie();
        when(movieRepository.findById(anyInt())).thenReturn(Optional.of(movie));
        assertEquals("OK", movieService.showMovie(1));
    }
}