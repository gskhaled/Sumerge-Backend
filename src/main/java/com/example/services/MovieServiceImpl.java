package com.example.services;

import com.example.dtos.MovieDTO;
import com.example.dtos.TMDBGenres;
import com.example.dtos.TMDBMovies;
import com.example.entities.Genre;
import com.example.entities.Movie;
import com.example.entities.Rating;
import com.example.entities.User;
import com.example.repositories.GenreRepository;
import com.example.repositories.MovieRepository;
import com.example.repositories.RatingRepository;
import com.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private JwtUtil jwtUtil;

    @PostConstruct
    public void populateDatabase() {
        populateGenres();
        populateMovies();
    }

    public void populateMovies() {
        RestTemplate restTemplate = new RestTemplate();
        String apiKey = "a8bd7f3d0cff0c86e330f635ea81ce95";
        for (int page = 1; page < 6; page++) {
            TMDBMovies response = restTemplate.getForObject(
                    "https://api.themoviedb.org/3/movie/top_rated?api_key="
                            + apiKey
                            + "&language=en-US"
                            + "&page" + page,
                    TMDBMovies.class);
            for (MovieDTO movieDTO :
                    response.getResults()) {
                Movie movie = movieDTO.mapToMovie();
                movie.setAddedBy("api");
                for (int genreId :
                        movieDTO.getGenre_ids()) {
                    Genre genre = genreRepository.findById(genreId).orElse(null);
                    if (genre != null) {
                        movie.getGenres().add(genre);
                    }
                }
                movieRepository.save(movie);
            }
        }
    }

    public void populateGenres() {
        RestTemplate restTemplate = new RestTemplate();
        String apiKey = "a8bd7f3d0cff0c86e330f635ea81ce95";
        TMDBGenres response = restTemplate.getForObject(
                "https://api.themoviedb.org/3/genre/movie/list"
                        + "?api_key=" + apiKey
                        + "&language=en-US",
                TMDBGenres.class);
        for (Genre genre :
                response.getGenres()) {
            genreRepository.save(genre);
        }
    }

    public List<Movie> hidAllFlaggedMovies(List<Movie> movies) {
        for (Movie movie :
                movies) {
            if (movie.getFlagged().size() > 10)
                movie.setHidden(true);
        }
        return movies;
    }

    public List<Movie> getAllMovies(int page) {
        List<Movie> movies = movieRepository.findAll(
                PageRequest.of(
                        page - 1,
                        10,
                        Sort.by(Sort.Direction.DESC, "voteAverage")
                )).toList();
        movies = hidAllFlaggedMovies(movies);
        return movies.stream().filter(m -> !m.isHidden()).collect(Collectors.toList());
    }

    public Movie getMovie(int id) {
        return movieRepository.findById(id).orElse(null);
    }

    public List<Movie> recommendMovies(String token) {
        String username = jwtUtil.extractUsername(token);
        User user = userRepository.findById(username).orElse(null);

        // first get genres from rated movies
        List<Genre> ratedGenres = new ArrayList<>();
        for (Rating ratedMovie :
                user.getRatings()) {
            ratedGenres.addAll(ratedMovie.getMovie().getGenres());
        }

        // then get genres from flagged movies
        List<Movie> flaggedMovies = user.getFlaggedMovies();
        List<Genre> flaggedGenres = new ArrayList<>();
        for (Movie movie :
                flaggedMovies) {
            flaggedGenres.addAll(movie.getGenres());
        }

        // find and filter movies with these genres
        List<Movie> movies = movieRepository.findAll();
        List<Movie> recommendedMovies = new ArrayList<>();
        for (Movie movie :
                movies) {
            if ((ratedGenres.size() == 0 || !Collections.disjoint(movie.getGenres(), ratedGenres))
                    && Collections.disjoint(movie.getGenres(), flaggedGenres)) {
                recommendedMovies.add(movie);
            }
            if (recommendedMovies.size() > 10)
                break;
        }
        return recommendedMovies;
    }

    public Movie addMovie(Movie movie) {
        movie.setAddedBy("admin");
        return movieRepository.save(movie);
    }

    public Movie editMovie(int movieId, String language, Date release_date, List<Integer> genre_ids) {
        List<Genre> genre_list = new ArrayList<>();
        for (int genreId :
                genre_ids) {
            Genre genre = genreRepository.findById(genreId).orElse(null);
            if (genre != null)
                genre_list.add(genre);
        }
        Movie movie = movieRepository.findById(movieId).orElse(null);
        if (movie != null) {
            if (!language.equals(""))
                movie.setOriginal_language(language);
            if (release_date != null)
                movie.setRelease_date(release_date);
            movie.getGenres().addAll(genre_list);
            movieRepository.save(movie);
        }
        return movie;
    }

    public String addRating(int movieId, String token, short rating) {
        Movie movie = movieRepository.findById(movieId).orElse(null);
        String username = jwtUtil.extractUsername(token);
        User user = userRepository.findById(username).orElse(null);
        if (movie != null && user != null && rating > 0 && rating <= 10) {
            if (!movie.wasRatedBefore(username)) {
                movie.recalculateVote(rating);
                Rating toAdd = new Rating(rating, user, movie);
//                user.addRating(toAdd);
//                userRepository.save(user);
                ratingRepository.save(toAdd);
                movieRepository.save(movie);
                return "OK";
            }
            return "Was rated before.";
        }
        return "Error";
    }

    public String addFlag(int movieId, String token) {
        Movie movie = movieRepository.findById(movieId).orElse(null);
        String username = jwtUtil.extractUsername(token);
        User user = userRepository.findById(username).orElse(null);
        if (movie != null && user != null) {
            if (!movie.wasFlaggedBefore(username)) {
                movie.getFlagged().add(user);
                movieRepository.save(movie);
                return "OK";
            }
            return "Was flagged before.";
        }
        return "Error";
    }

    public String removeFlag(int movieId, String token) {
        Movie movie = movieRepository.findById(movieId).orElse(null);
        String username = jwtUtil.extractUsername(token);
        User user = userRepository.findById(username).orElse(null);
        if (movie != null && user != null) {
            if (movie.wasFlaggedBefore(username)) {
                movie.getFlagged().remove(user);
                movieRepository.save(movie);
                return "OK";
            }
            return "Was not flagged before.";
        }
        return "Error";
    }

    public String hideMovie(int movieId) {
        Movie movie = movieRepository.findById(movieId).orElse(null);
        if (movie != null) {
            movie.setHidden(true);
            movieRepository.save(movie);
            return "OK";
        }
        return "Movie not found";
    }

    public String showMovie(int movieId) {
        Movie movie = movieRepository.findById(movieId).orElse(null);
        if (movie != null) {
            movie.setHidden(false);
            movieRepository.save(movie);
            return "OK";
        }
        return "Movie not found";
    }
}
