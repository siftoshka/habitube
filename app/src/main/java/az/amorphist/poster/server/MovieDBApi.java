package az.amorphist.poster.server;

import az.amorphist.poster.entities.movie.Movie;
import az.amorphist.poster.entities.movielite.MoviePager;
import az.amorphist.poster.entities.person.Person;
import az.amorphist.poster.entities.show.Show;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieDBApi {

    @GET("movie/upcoming")
    Single<MoviePager> getUpcomingMoviesLite (
            @Query("api_key") String apiKey
    );

    @GET("trending/movie/day")
    Single<MoviePager> getTrendingMoviesLite (
            @Query("api_key") String apiKey
    );

    @GET("trending/tv/day")
    Single<MoviePager> getTrendingTVShowsLite (
            @Query("api_key") String apiKey
    );

    @GET("movie/{movie_id}")
    Single<Movie> getMovie (
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey
    );

    @GET("tv/{tv_id}")
    Single<Show> getTVShow (
            @Path("tv_id") int showId,
            @Query("api_key") String apiKey
    );

    @GET("person/{person_id}")
    Single<Person> getMovieStar (
            @Path("person_id") int starId,
            @Query("api_key") String apiKey
    );

    @GET("search/multi")
    Single<MoviePager> getSearchResults (
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("query") String searchQuery,
            @Query("page") int page,
            @Query("include_adult") boolean isAdult
    );
}
