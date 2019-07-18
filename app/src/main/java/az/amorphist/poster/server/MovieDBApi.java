package az.amorphist.poster.server;

import az.amorphist.poster.entities.movie.MoviePager;
import az.amorphist.poster.entities.movielite.MoviePagerLite;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieDBApi {

    @GET("movie/upcoming")
    Single<MoviePager> getUpcomingMovies(
            @Query("api_key") String apiKey
    );

    @GET("movie/upcoming")
    Single<MoviePagerLite> getUpcomingMoviesLite(
            @Query("api_key") String apiKey
    );

    @GET("trending/movie/week")
    Single<MoviePager> getTrendingMovies(
            @Query("api_key") String apiKey
    );

    @GET("trending/movie/week")
    Single<MoviePagerLite> getTrendingMoviesLite(
            @Query("api_key") String apiKey
    );

    @GET("trending/tv/week")
    Single<MoviePager> getTrendingTVShows(
            @Query("api_key") String apiKey
    );

    @GET("trending/tv/week")
    Single<MoviePagerLite> getTrendingTVShowsLite(
            @Query("api_key") String apiKey
    );

    @GET("search/multi")
    Observable<MoviePagerLite> getSearchResults (
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("query") String searchQuery,
            @Query("page") int page,
            @Query("include_adult") boolean isAdult
    );
}
