package com.example.services;

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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private GenreRepository genreRepository;

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
                    response.results) {
                Movie movie = movieDTO.mapToMovie();
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
//        return movies.stream().filter(t -> t.getId().equals(id)).findFirst().get();
        return movieRepository.findById(id).orElse(null);
    }

    public Movie addMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public String addRating(int movieId, String userId, short rating) {
        Movie movie = movieRepository.findById(movieId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        if (movie != null && user != null && rating > 0 && rating <= 10) {
            if (!movie.wasRatedBefore(userId)) {
                movie.recalculateVote(rating);
                ratingRepository.save(new Rating(rating, user, movie));
                movieRepository.save(movie);
                return "OK";
            }
            return "Was rated before.";
        }
        return "Error";
    }

    public String addFlag(int movieId, String userId) {
        Movie movie = movieRepository.findById(movieId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        if (movie != null && user != null) {
            if (!movie.wasFlaggedBefore(userId)) {
                System.out.println("BEFORE: " + movie.getFlagged());
                movie.getFlagged().add(user);
                System.out.println("AFTER: " + movie.getFlagged());
                movieRepository.save(movie);
                return "OK";
            }
            return "Was flagged before.";
        }
        return "Error";
    }

    public String removeFlag(int movieId, String userId) {
        Movie movie = movieRepository.findById(movieId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        if (movie != null && user != null) {
            if (movie.wasFlaggedBefore(userId)) {
                System.out.println("BEFORE: " + movie.getFlagged());
                movie.getFlagged().remove(user);
                System.out.println("AFTER: " + movie.getFlagged());
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

    private static class MovieDTO {
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

    private static class TMDBGenres {
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

    private static class TMDBMovies {
        int page;
        List<MovieDTO> results;
        int total_pages;
        int total_results;

        public TMDBMovies() {
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public List<MovieDTO> getResults() {
            return results;
        }

        public void setResults(List<MovieDTO> results) {
            this.results = results;
        }

        public int getTotal_pages() {
            return total_pages;
        }

        public void setTotal_pages(int total_pages) {
            this.total_pages = total_pages;
        }

        public int getTotal_results() {
            return total_results;
        }

        public void setTotal_results(int total_results) {
            this.total_results = total_results;
        }

        @Override
        public String toString() {
            return "TMDBResponse{" +
                    "page=" + page +
                    ", results=" + results +
                    ", total_pages=" + total_pages +
                    ", total_results=" + total_results +
                    '}';
        }
    }
}
