package az.amorphist.poster.presentation.main;

import java.util.List;

import javax.inject.Inject;

import az.amorphist.poster.Screens;
import az.amorphist.poster.di.providers.ApiProvider;
import az.amorphist.poster.entities.Movie;
import az.amorphist.poster.entities.MovieLite;
import az.amorphist.poster.entities.MoviePager;
import az.amorphist.poster.entities.MoviePagerLite;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.terrakok.cicerone.Router;

import static az.amorphist.poster.App.API_KEY;

@InjectViewState
public class MainListPresenter extends MvpPresenter<MainListView> {

    private final Router router;
    private ApiProvider provider;

    @Inject
    public MainListPresenter(Router router, ApiProvider provider) {
        this.router = router;
        this.provider = provider;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        addUpcomingMovies();
        addMovies();
        addShows();
    }

    private void addUpcomingMovies() {
        provider.get().getUpcomingMoviesLite(API_KEY).enqueue(new Callback<MoviePagerLite>() {
            @Override
            public void onResponse(Call<MoviePagerLite> call, Response<MoviePagerLite> response) {
                if(response.isSuccessful()) {
                    MoviePagerLite pager = response.body();
                    List<MovieLite> upcomingMovies = pager.getResults();
                    getViewState().getUpcomingMovieList(upcomingMovies);
                } else {
                    getViewState().unsuccessfulQueryError();
                }
            }

            @Override
            public void onFailure(Call<MoviePagerLite> call, Throwable t) {
                getViewState().unsuccessfulQueryError();
            }
        });

    }
    private void addMovies() {
        provider.get().getTrendingMoviesLite(API_KEY).enqueue(new Callback<MoviePagerLite>() {
            @Override
            public void onResponse(Call<MoviePagerLite> call, Response<MoviePagerLite> response) {
                if(response.isSuccessful()) {
                    MoviePagerLite pager = response.body();
                    List<MovieLite> movies = pager.getResults();
                    getViewState().getMovieList(movies);
                } else {
                    getViewState().unsuccessfulQueryError();
                }
            }

            @Override
            public void onFailure(Call<MoviePagerLite> call, Throwable t) {
                getViewState().unsuccessfulQueryError();
            }
        });
    }
    private void addShows() {
        provider.get().getTrendingTVShowsLite(API_KEY).enqueue(new Callback<MoviePagerLite>() {
            @Override
            public void onResponse(Call<MoviePagerLite> call, Response<MoviePagerLite> response) {
                if(response.isSuccessful()) {
                    MoviePagerLite pager = response.body();
                    List<MovieLite> tvShows = pager.getResults();
                    getViewState().getTVShowList(tvShows);
                } else {
                    getViewState().unsuccessfulQueryError();
                }
            }

            @Override
            public void onFailure(Call<MoviePagerLite> call, Throwable t) {
                getViewState().unsuccessfulQueryError();
            }
        });
    }

    public void goToDetailedMovieScreen(Integer id) {
        router.navigateTo(new Screens.PostMovieScreen(id));
    }

    public void goToDetailedShowScreen(Integer id) {
        router.navigateTo(new Screens.PostShowScreen(id));
    }

    public void goToDetailedUpcomingScreen(Integer id) {
        router.navigateTo(new Screens.PostUpcomingScreen(id));
    }
}
