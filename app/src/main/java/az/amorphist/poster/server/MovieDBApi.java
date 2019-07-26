package az.amorphist.poster.server;

import az.amorphist.poster.entities.movie.Movie;
import az.amorphist.poster.entities.movielite.MovieResponse;
import az.amorphist.poster.entities.person.Person;
import az.amorphist.poster.entities.show.Show;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieDBApi {

    @GET("movie/upcoming")
    Single<MovieResponse> getUpcomingMovies();

    @GET("trending/movie/day")
    Single<MovieResponse> getTrendingMovies();

    @GET("trending/tv/day")
    Single<MovieResponse> getTrendingTVShows();

    @GET("movie/{movie_id}")
    Single<Movie> getMovie(
            @Path("movie_id") int movieId
    );

    @GET("movie/{movie_id}/similar")
    Single<MovieResponse> getSimilarMovies(
            @Path("movie_id") int movieId
    );

    @GET("tv/{tv_id}")
    Single<Show> getTVShow(
            @Path("tv_id") int showId
    );

    @GET("tv/{tv_id}/similar")
    Single<MovieResponse> getSimilarTVShow(
            @Path("tv_id") int showId
    );

    @GET("person/{person_id}")
    Single<Person> getMovieStar(
            @Path("person_id") int starId
    );

    @GET("search/multi")
    Observable<MovieResponse> getSearchResults(
            @Query("language") String language,
            @Query("query") String searchQuery,
            @Query("page") int page,
            @Query("include_adult") boolean isAdult
    );
}
