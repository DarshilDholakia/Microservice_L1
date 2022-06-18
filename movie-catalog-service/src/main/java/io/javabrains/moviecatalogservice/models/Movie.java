package io.javabrains.moviecatalogservice.models;

public class Movie {
    private String movieId;
    private String name;

    // empty constructor needed for when we try to unmarshal data received from an API call into an object (in this case,
    // a Movie object)
    public Movie() {}

    public Movie(String movieId, String name) {
        this.movieId = movieId;
        this.name = name;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
