package io.javabrains.moviecatalogservice.resources;

import io.javabrains.moviecatalogservice.models.CatalogItem;
import io.javabrains.moviecatalogservice.models.Movie;
import io.javabrains.moviecatalogservice.models.Rating;
import io.javabrains.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping ("/catalog")
public class MovieCatalogResource {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    //        List<Rating> ratings = Arrays.asList(
//                new Rating("1234", 4),
//                new Rating("5678", 3)
//        );
    @RequestMapping("/{userId}")
    // technically, getCatalog() method below must return an object not a List<CatalogItem>!
    public List<CatalogItem> getCatalog(@PathVariable ("userId") String userId) {

        // getting List<Rating> and wrapping it using UserRating wrapper
        UserRating ratings = restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/" + userId, UserRating.class);

        return ratings.getUserRating().stream().map(rating -> {
            // ========== RESTTEMPLATE ==========
//            Movie movie = restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);

            // ========== WEBCLIENT =============
            Movie movie = webClientBuilder.build()
                    .get() // since the API endpoint we are targetting is a GET request
                    .uri("http://movie-info-service/movies/" + rating.getMovieId()) // where we need the request to be made
                    .retrieve()
                    .bodyToMono(Movie.class) // whatever we get back, convert it to instance of Movie class
                    .block(); // we have to wait around for this mono return Movie object back since we return a List<CatalogItem>
            // and for that we need to first return CatalogItem which requires a Movie object in the line below
            // we are blocking the executing till the Mono is fulfilled
            return new CatalogItem(movie.getName(), "Test", rating.getRating());
        })
                .collect(Collectors.toList());
    }
}
