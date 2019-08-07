package az.amorphist.poster.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

import javax.inject.Inject;

import az.amorphist.poster.R;
import az.amorphist.poster.adapters.MovieAdapter;
import az.amorphist.poster.adapters.SeasonAdapter;
import az.amorphist.poster.adapters.ShowAdapter;
import az.amorphist.poster.model.repository.WatchedRoomRepository;
import az.amorphist.poster.di.modules.MovieModule;
import az.amorphist.poster.di.modules.SearchModule;
import az.amorphist.poster.entities.movie.Movie;
import az.amorphist.poster.entities.movie.MovieGenre;
import az.amorphist.poster.entities.movielite.MovieLite;
import az.amorphist.poster.entities.show.Season;
import az.amorphist.poster.entities.show.ShowGenre;
import az.amorphist.poster.presentation.post.PostPresenter;
import az.amorphist.poster.presentation.post.PostView;
import az.amorphist.poster.utils.GlideLoader;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Scope;
import toothpick.Toothpick;

import static az.amorphist.poster.App.IMDB_WEBSITE;
import static az.amorphist.poster.di.DI.APP_SCOPE;
import static az.amorphist.poster.di.DI.POST_SCOPE;

public class PostFragment extends MvpAppCompatFragment implements PostView {

    @InjectPresenter PostPresenter postPresenter;

    private Toolbar toolbar;
    private RecyclerView recyclerViewSimilarMovies, recyclerViewSimilarShows, recyclerViewSeasons;
    private RelativeLayout mainScreen, showScreen, personScreen;
    private LinearLayout loadingScreen, errorScreen;
    private LinearLayout imdbButton, watchedButton, planningButton;
    private ImageView posterBackground, posterMain, posterShow, posterShowBackground, posterPerson;
    private TextView posterTitle, posterDate, posterRate, posterViews, posterDesc;
    private TextView posterShowTitle, posterShowDate, posterShowRate, posterShowViews, posterShowDesc;
    private TextView posterPersonName, posterPersonBirthDate, posterPersonLocation, posterPersonPopularity, posterPersonBio;
    private ChipGroup movieGenresChip, showGenresChip;
    private MovieAdapter similarMoviesAdapter;
    private ShowAdapter similarShowsAdapter;
    private SeasonAdapter seasonAdapter;
    private LinearLayoutManager layoutManagerSimilarMovies, layoutManagerSimilarShows, layoutManagerSeasons;

    @ProvidePresenter
    PostPresenter postPresenter() {
        final Bundle movieBundle = getArguments();
        final Integer postPosition = movieBundle.getInt("postPosition");
        final Integer showPosition = movieBundle.getInt("showPosition");
        final Integer upcomingPosition = movieBundle.getInt("upcomingPosition");

        final Bundle searchBundle = getArguments();
        final Integer postId = searchBundle.getInt("postId");
        final Integer mediaType = searchBundle.getInt("mediaType");

        final Scope temporaryPostScope = Toothpick.openScopes(APP_SCOPE, POST_SCOPE);
        temporaryPostScope.installModules(new MovieModule(postPosition, showPosition, upcomingPosition));
        temporaryPostScope.installModules(new SearchModule(postId, mediaType));
        final PostPresenter postPresenter = temporaryPostScope.getInstance(PostPresenter.class);
        Toothpick.closeScope(POST_SCOPE);
        return postPresenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toothpick.inject(this, Toothpick.openScope(APP_SCOPE));
        similarMoviesAdapter = new MovieAdapter(postId -> postPresenter.goToDetailedMovieScreen(postId));
        similarShowsAdapter = new ShowAdapter(showId -> postPresenter.goToDetailedShowScreen(showId));
        seasonAdapter = new SeasonAdapter(position -> showBottomSeasonDialog(position));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        toolbar = view.findViewById(R.id.post_toolbar);

        initFunctionalButtons(view);
        initPersonItems(view);
        initMovieItems(view);
        initShowItems(view);
        initScreens(view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        toolbar.setNavigationOnClickListener(v -> postPresenter.goBack());

        layoutManagerSimilarMovies = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerViewSimilarMovies.setLayoutManager(layoutManagerSimilarMovies);
        recyclerViewSimilarMovies.setItemAnimator(new DefaultItemAnimator());
        recyclerViewSimilarMovies.setHasFixedSize(true);
        recyclerViewSimilarMovies.setAdapter(similarMoviesAdapter);

        layoutManagerSimilarShows = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerViewSimilarShows.setLayoutManager(layoutManagerSimilarShows);
        recyclerViewSimilarShows.setItemAnimator(new DefaultItemAnimator());
        recyclerViewSimilarShows.setHasFixedSize(true);
        recyclerViewSimilarShows.setAdapter(similarShowsAdapter);

        layoutManagerSeasons = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerViewSeasons.setLayoutManager(layoutManagerSeasons);
        recyclerViewSeasons.setItemAnimator(new DefaultItemAnimator());
        recyclerViewSeasons.setHasFixedSize(true);
        recyclerViewSeasons.setAdapter(seasonAdapter);

    }

    @Override
    public void getMovie(String image, String background, String title, String date,
                         double rate, int views, List<MovieGenre> movieGenres,
                         String imdbId, String description) {
        Movie movie = new Movie(false, background, 1, imdbId, title, description, rate, image, date, 1, description, background, title, rate, views);
        GlideLoader.load(getContext(), image, posterMain);
        GlideLoader.loadBackground(getContext(), background, posterBackground);
        posterTitle.setText(title);
        posterDate.setText(date);
        posterRate.setText(String.valueOf(rate));
        posterViews.setText(String.valueOf(views));

        for(MovieGenre mGenres: movieGenres) {
            Chip chip = new Chip(getContext());
            ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(getContext(),
                    null, 0, R.style.Widget_MaterialComponents_Chip_Action);
            chip.setChipDrawable(chipDrawable);
            chip.setText(mGenres.getName());
            movieGenresChip.addView(chip);
        }

        posterDesc.setText(description);
        showImdbWeb(imdbId);
        addMovieToWatched(movie);
    }

    private void addMovieToWatched(Movie movie) {
        watchedButton.setOnClickListener(v -> postPresenter.addMovieAsWatched(movie));
    }

    @Override
    public void getShow(String image, String background, String title, String date,
                        float rate, float views, List<ShowGenre> showGenres,
                        String description, List<Season> seasons) {
        GlideLoader.load(getContext(), image, posterShow);
        GlideLoader.loadBackground(getContext(), background, posterShowBackground);
        posterShowTitle.setText(title);
        posterShowDate.setText(date);
        posterShowRate.setText(String.valueOf(rate));
        posterShowViews.setText(String.valueOf(views));

        for(ShowGenre sGenres: showGenres) {
            Chip chip = new Chip(getContext());
            ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(getContext(),
                    null, 0, R.style.Widget_MaterialComponents_Chip_Action);
            chip.setChipDrawable(chipDrawable);
            chip.setText(sGenres.getName());
            showGenresChip.addView(chip);
        }

        posterShowDesc.setText(description);
        seasonAdapter.addAllMovies(seasons);
    }

    @Override
    public void getPerson(String image, String name, String birthdate, String placeOfBirth,
                          double popularity, String bio) {
        GlideLoader.load(getContext(), image, posterPerson);
        posterPersonName.setText(name);
        posterPersonBirthDate.setText(birthdate);
        posterPersonLocation.setText(placeOfBirth);
        posterPersonPopularity.setText(String.valueOf(popularity));
        posterPersonBio.setText(bio);
    }

    @Override
    public void showSimilarMovieList(List<MovieLite> similarMovies) {
        similarMoviesAdapter.addAllMovies(similarMovies);
    }

    @Override
    public void showSimilarTVShowList(List<MovieLite> similarShows) {
        similarShowsAdapter.addAllMovies(similarShows);
    }

    private void showImdbWeb(String imdbId) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(IMDB_WEBSITE + imdbId));
        imdbButton.setOnClickListener(v -> startActivity(intent));
    }

    @Override
    public void showProgress(boolean loadingState) {
        if(loadingState){
            loadingScreen.setVisibility(View.VISIBLE);
        } else {
            loadingScreen.setVisibility(View.GONE);
        }
    }

    @Override
    public void showMovieScreen() {
        mainScreen.setVisibility(View.VISIBLE);
        errorScreen.setVisibility(View.GONE);
        showScreen.setVisibility(View.GONE);
        personScreen.setVisibility(View.GONE);
    }

    @Override
    public void showErrorScreen() {
        mainScreen.setVisibility(View.GONE);
        errorScreen.setVisibility(View.VISIBLE);
        showScreen.setVisibility(View.GONE);
        personScreen.setVisibility(View.GONE);
    }

    @Override
    public void showTVShowScreen() {
        mainScreen.setVisibility(View.GONE);
        errorScreen.setVisibility(View.GONE);
        showScreen.setVisibility(View.VISIBLE);
        personScreen.setVisibility(View.GONE);
    }

    @Override
    public void showPersonScreen() {
        mainScreen.setVisibility(View.GONE);
        errorScreen.setVisibility(View.GONE);
        showScreen.setVisibility(View.GONE);
        personScreen.setVisibility(View.VISIBLE);
    }

    @Override
    public void showBottomSeasonDialog(int position) {
        SeasonBottomDialog seasonBottomDialog = new SeasonBottomDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("SEASON", seasonAdapter.getSeason(position));
        seasonBottomDialog.setArguments(bundle);
        seasonBottomDialog.show(getChildFragmentManager(), null);
    }

    private void initFunctionalButtons(View view) {
        imdbButton = view.findViewById(R.id.imdb_button);
        watchedButton = view.findViewById(R.id.watched_button);
        planningButton = view.findViewById(R.id.planning_button);
    }

    private void initPersonItems(View view) {
        posterPerson = view.findViewById(R.id.poster_person_post);
        posterPersonName = view.findViewById(R.id.poster_person_title);
        posterPersonBirthDate = view.findViewById(R.id.poster_person_birthdate);
        posterPersonLocation = view.findViewById(R.id.poster_person_location);
        posterPersonPopularity = view.findViewById(R.id.poster_person_popularity);
        posterPersonBio = view.findViewById(R.id.poster_person_bio);
    }

    private void initMovieItems(View view) {
        posterBackground = view.findViewById(R.id.poster_background);
        posterMain = view.findViewById(R.id.poster_movie_post);
        posterTitle = view.findViewById(R.id.poster_title);
        posterDate = view.findViewById(R.id.poster_date);
        posterRate = view.findViewById(R.id.poster_rate);
        posterViews = view.findViewById(R.id.poster_views);
        movieGenresChip = view.findViewById(R.id.movie_genres);
        posterDesc = view.findViewById(R.id.poster_desc);
        recyclerViewSimilarMovies = view.findViewById(R.id.recycler_view_similar_movies);
    }

    private void initShowItems(View view) {
        posterShowBackground = view.findViewById(R.id.show_poster_background);
        posterShow = view.findViewById(R.id.poster_show_post);
        posterShowTitle = view.findViewById(R.id.poster_show_title);
        posterShowDate = view.findViewById(R.id.poster_show_date);
        posterShowRate = view.findViewById(R.id.poster_show_rate);
        posterShowViews = view.findViewById(R.id.poster_show_views);
        showGenresChip = view.findViewById(R.id.show_genres);
        posterShowDesc = view.findViewById(R.id.poster_show_desc);
        recyclerViewSeasons = view.findViewById(R.id.recycler_view_seasons);
        recyclerViewSimilarShows = view.findViewById(R.id.recycler_view_similar_shows);
    }

    private void initScreens(View view) {
        loadingScreen = view.findViewById(R.id.loading_screen);
        mainScreen = view.findViewById(R.id.main_screen);
        errorScreen = view.findViewById(R.id.error_screen);
        showScreen = view.findViewById(R.id.show_screen);
        personScreen = view.findViewById(R.id.person_screen);
    }
}
