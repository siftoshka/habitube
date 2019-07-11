package az.amorphist.poster.server;

import az.amorphist.poster.entities.MoviePager;
import az.amorphist.poster.entities.MoviePagerLite;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieDBApi {

    @GET("movie/upcoming")
    Call<MoviePager> getUpcomingMovies(
            @Query("api_key") String apiKey
    );

    @GET("movie/upcoming")
    Call<MoviePagerLite> getUpcomingMoviesLite(
            @Query("api_key") String apiKey
    );

    @GET("trending/movie/week")
    Call<MoviePager> getTrendingMovies(
            @Query("api_key") String apiKey
    );

    @GET("trending/movie/week")
    Call<MoviePagerLite> getTrendingMoviesLite(
            @Query("api_key") String apiKey
    );

    @GET("trending/tv/week")
    Call<MoviePager> getTrendingTVShows(
            @Query("api_key") String apiKey
    );

    @GET("trending/tv/week")
    Call<MoviePagerLite> getTrendingTVShowsLite(
            @Query("api_key") String apiKey
    );
}
