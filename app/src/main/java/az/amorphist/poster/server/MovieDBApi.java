package az.amorphist.poster.server;

import az.amorphist.poster.entities.MoviePager;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieDBApi {

    @GET("trending/movie/week")
    Call<MoviePager> getTrendingMovies(
            @Query("api_key") String apiKey
    );

    @GET("trending/tv/week")
    Call<MoviePager> getTrendingTVShows(
            @Query("api_key") String apiKey
    );
}
