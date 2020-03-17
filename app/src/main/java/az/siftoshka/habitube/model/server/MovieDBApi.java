package az.siftoshka.habitube.model.server;

import az.siftoshka.habitube.entities.credits.Credits;
import az.siftoshka.habitube.entities.movie.Movie;
import az.siftoshka.habitube.entities.movielite.MovieResponse;
import az.siftoshka.habitube.entities.person.Person;
import az.siftoshka.habitube.entities.show.Show;
import az.siftoshka.habitube.entities.video.VideoResponse;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieDBApi {

    @GET("movie/upcoming")
    Single<MovieResponse> getUpcomingMovies(
            @Query("language") String language
    );

    @GET("trending/movie/day")
    Single<MovieResponse> getTrendingMovies(
            @Query("language") String language
    );

    @GET("trending/tv/day")
    Single<MovieResponse> getTrendingTVShows(
            @Query("language") String language
    );

    @GET("tv/airing_today")
    Single<MovieResponse> getAirTodayShows(
            @Query("language") String language
    );

    @GET("movie/{movie_id}")
    Single<Movie> getMovie(
            @Path("movie_id") int movieId,
            @Query("language") String language
            );

    @GET("movie/{movie_id}/similar")
    Single<MovieResponse> getSimilarMovies(
            @Path("movie_id") int movieId,
            @Query("language") String language

    );

    @GET("movie/{movie_id}/videos")
    Single<VideoResponse> getMovieVideos(
            @Path("movie_id") int movieId,
            @Query("language") String language
    );

    @GET("tv/{tv_id}")
    Single<Show> getTVShow(
            @Path("tv_id") int showId,
            @Query("language") String language

    );

    @GET("tv/{tv_id}/similar")
    Single<MovieResponse> getSimilarTVShow(
            @Path("tv_id") int showId,
            @Query("language") String language

    );

    @GET("tv/{tv_id}/videos")
    Single<VideoResponse> getTVShowVideos(
            @Path("tv_id") int movieId,
            @Query("language") String language
    );

    @GET("person/{person_id}")
    Single<Person> getMovieStar(
            @Path("person_id") int starId,
            @Query("language") String language

    );

    @GET("movie/{movie_id}/credits")
    Single<Credits> getCredits(
            @Path("movie_id") int movieId
    );

    @GET("tv/{tv_id}/credits")
    Single<Credits> getShowCredits(
            @Path("tv_id") int tvId
    );

    @GET("person/{person_id}/movie_credits")
    Single<az.siftoshka.habitube.entities.personcredits.Credits> getPersonMovieCredits(
            @Path("person_id") int personId,
            @Query("language") String language
    );

    @GET("person/{person_id}/tv_credits")
    Single<az.siftoshka.habitube.entities.personcredits.Credits> getPersonShowCredits(
            @Path("person_id") int personId,
            @Query("language") String language
    );

    @GET("search/multi")
    Observable<MovieResponse> getSearchResults(
            @Query("language") String language,
            @Query("query") String searchQuery,
            @Query("page") int page,
            @Query("include_adult") boolean isAdult
    );

    @GET("search/movie")
    Observable<MovieResponse> getMovieSearchResults(
            @Query("language") String language,
            @Query("query") String searchQuery,
            @Query("page") int page,
            @Query("include_adult") boolean isAdult
    );

    @GET("search/tv")
    Observable<MovieResponse> getShowSearchResults(
            @Query("language") String language,
            @Query("query") String searchQuery,
            @Query("page") int page,
            @Query("include_adult") boolean isAdult
    );

    @GET("search/person")
    Observable<MovieResponse> getPersonSearchResults(
            @Query("language") String language,
            @Query("query") String searchQuery,
            @Query("page") int page,
            @Query("include_adult") boolean isAdult
    );

    @GET("discover/movie")
    Observable<MovieResponse> getDiscoverMovies(
        @Query("language") String language,
        @Query("sort_by") String sort,
        @Query("page") int page,
        @Query("include_adult") boolean isAdult,
        @Query("primary_release_date.lte") String releaseDate,
        @Query("vote_average.gte") int rating,
        @Query("vote_count.gte") int voteCount
    );

    @GET("discover/tv")
    Observable<MovieResponse> getDiscoverShows(
            @Query("language") String language,
            @Query("sort_by") String sort,
            @Query("page") int page,
            @Query("include_adult") boolean isAdult,
            @Query("with_networks") String network,
            @Query("first_air_date.lte") String releaseDate,
            @Query("vote_average.gte") int rating,
            @Query("vote_count.gte") int voteCount
    );
}
