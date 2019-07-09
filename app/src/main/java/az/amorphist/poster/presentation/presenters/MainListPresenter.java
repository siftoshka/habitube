package az.amorphist.poster.presentation.presenters;

import java.util.List;

import javax.inject.Inject;

import az.amorphist.poster.Screens;
import az.amorphist.poster.di.providers.ApiProvider;
import az.amorphist.poster.entities.Movie;
import az.amorphist.poster.entities.MoviePager;
import az.amorphist.poster.presentation.views.MainListView;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.terrakok.cicerone.Router;

import static az.amorphist.poster.App.apiKey;

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
        addMovies();
        addShows();
    }

    private void addMovies() {
        provider.get().getTrendingMovies(apiKey).enqueue(new Callback<MoviePager>() {
            @Override
            public void onResponse(Call<MoviePager> call, Response<MoviePager> response) {
                if(response.isSuccessful()) {
                    MoviePager pager = response.body();
                    List<Movie> movies = pager.getResults();
                    getViewState().getMovieList(movies);
                } else {
                    getViewState().unsuccessfulQueryError();
                }
            }

            @Override
            public void onFailure(Call<MoviePager> call, Throwable t) {
                getViewState().unsuccessfulQueryError();
            }
        });
    }
    private void addShows() {
        provider.get().getTrendingTVShows(apiKey).enqueue(new Callback<MoviePager>() {
            @Override
            public void onResponse(Call<MoviePager> call, Response<MoviePager> response) {
                if(response.isSuccessful()) {
                    MoviePager pager = response.body();
                    List<Movie> tvShows = pager.getResults();
                    getViewState().getTVShowList(tvShows);
                } else {
                    getViewState().unsuccessfulQueryError();
                }
            }

            @Override
            public void onFailure(Call<MoviePager> call, Throwable t) {
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
}
