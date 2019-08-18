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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

import az.amorphist.poster.R;
import az.amorphist.poster.adapters.MovieAdapter;
import az.amorphist.poster.adapters.SeasonAdapter;
import az.amorphist.poster.adapters.ShowAdapter;
import az.amorphist.poster.di.modules.MovieModule;
import az.amorphist.poster.di.modules.SearchModule;
import az.amorphist.poster.entities.movie.Movie;
import az.amorphist.poster.entities.movie.MovieGenre;
import az.amorphist.poster.entities.movielite.MovieLite;
import az.amorphist.poster.entities.person.Person;
import az.amorphist.poster.entities.show.Show;
import az.amorphist.poster.entities.show.ShowGenre;
import az.amorphist.poster.presentation.post.PostPresenter;
import az.amorphist.poster.presentation.post.PostView;
import az.amorphist.poster.utils.GlideLoader;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Scope;
import toothpick.Toothpick;

import static az.amorphist.poster.Constants.DI.APP_SCOPE;
import static az.amorphist.poster.Constants.DI.POST_SCOPE;
import static az.amorphist.poster.Constants.SYSTEM.IMDB_WEBSITE;

public class PostFragment extends MvpAppCompatFragment implements PostView {

    @InjectPresenter PostPresenter postPresenter;

    private Toolbar toolbar;
    private RecyclerView recyclerViewSimilarMovies, recyclerViewSimilarShows, recyclerViewSeasons;
    private RelativeLayout mainScreen, showScreen, personScreen;
    private LinearLayout loadingScreen, errorScreen;
    private LinearLayout imdbButton, watchedButton, watchedButtonAlt, planningButton;
    private ImageView posterBackground, posterMain, posterShow, posterShowBackground, posterPerson;
    private TextView posterTitle, posterDate, posterRate, posterViews, posterDesc;
    private TextView posterShowTitle, posterShowDate, posterShowRate, posterShowViews, posterShowDesc;
    private TextView posterPersonName, posterPersonBirthDate, posterPersonLocation, posterPersonPopularity, posterPersonBio;
    private ChipGroup movieGenresChip, showGenresChip;
    private MovieAdapter similarMoviesAdapter;
    private ShowAdapter similarShowsAdapter;
    private SeasonAdapter seasonAdapter;
    private LinearLayout similarMoviesCard, similarShowsCard, descMovieCard, descShowCard;
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
        final View view = inflater.inflate(R.layout.fragment_post, container, false);
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
        watchedButton.setVisibility(View.VISIBLE);
        watchedButtonAlt.setVisibility(View.GONE);
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
    public void getMovie(Movie movie) {
        GlideLoader.load(getContext(), movie.getPosterPath(), posterMain);
        GlideLoader.loadBackground(getContext(), movie.getBackdropPath(), posterBackground);
        posterTitle.setText(movie.getTitle());
        posterDate.setText(movie.getReleaseDate());
        posterRate.setText(String.valueOf(movie.getVoteAverage()));
        posterViews.setText(String.valueOf(movie.getVoteCount()));

        for(MovieGenre mGenres: movie.getMovieGenres()) {
            Chip chip = new Chip(requireContext());
            ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(requireContext(),
                    null, 0, R.style.Widget_MaterialComponents_Chip_Action);
            chip.setChipDrawable(chipDrawable);
            chip.setText(mGenres.getName());
            movieGenresChip.addView(chip);
        }

        if(movie.getOverview().equals("")) {
            descMovieCard.setVisibility(View.GONE);
        }

        posterDesc.setText(movie.getOverview());
        showImdbWeb(movie.getImdbId());
        addMovieToWatched(movie, movie.getMovieGenres());
        deleteMovieFromWatched(movie);
    }

    private void addMovieToWatched(Movie movie, List<MovieGenre> movieGenres) {
        watchedButton.setOnClickListener(v -> postPresenter.addMovieAsWatched(movie, movieGenres));
    }

    private void deleteMovieFromWatched(Movie movie) {
        watchedButtonAlt.setOnClickListener(v -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext(), R.style.AppCompatAlertDialogStyle);
            alertDialogBuilder.setTitle(getResources().getString(R.string.delete_movie));
            alertDialogBuilder.setMessage(getResources().getString(R.string.are_you_sure));
            alertDialogBuilder.setPositiveButton(getResources().getString(R.string.yes),
                    (arg0, arg1) -> postPresenter.deleteMovieFromWatched(movie));

            alertDialogBuilder.setNegativeButton(getResources().getString(R.string.no), (dialog, which) -> dialog.dismiss());

            alertDialogBuilder.show();
        });
    }

    @Override
    public void getShow(Show show) {
        GlideLoader.load(getContext(), show.getPosterPath(), posterShow);
        GlideLoader.loadBackground(getContext(), show.getBackdropPath(), posterShowBackground);
        posterShowTitle.setText(show.getName());
        posterShowDate.setText(show.getFirstAirDate());
        posterShowRate.setText(String.valueOf(show.getVoteAverage()));
        posterShowViews.setText(String.valueOf(show.getVoteCount()));

        for(ShowGenre sGenres: show.getShowGenres()) {
            Chip chip = new Chip(requireContext());
            ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(requireContext(),
                    null, 0, R.style.Widget_MaterialComponents_Chip_Action);
            chip.setChipDrawable(chipDrawable);
            chip.setText(sGenres.getName());
            showGenresChip.addView(chip);
        }

        if(show.getOverview().equals("")) {
            descShowCard.setVisibility(View.GONE);
        }

        posterShowDesc.setText(show.getOverview());
        seasonAdapter.addAllMovies(show.getSeasons());
    }

    @Override
    public void getPerson(Person person) {
        GlideLoader.load(getContext(), person.getProfilePath(), posterPerson);
        posterPersonName.setText(person.getName());
        posterPersonBirthDate.setText(person.getBirthday());
        posterPersonLocation.setText(person.getPlaceOfBirth());
        posterPersonPopularity.setText(String.valueOf(person.getPopularity()));
        posterPersonBio.setText(person.getBiography());
    }

    @Override
    public void showSimilarMovieList(List<MovieLite> similarMovies) {
        if(similarMovies.isEmpty()) {
            similarMoviesCard.setVisibility(View.GONE);
        }
        similarMoviesAdapter.addAllMovies(similarMovies);
    }

    @Override
    public void showSimilarTVShowList(List<MovieLite> similarShows) {
        if(similarShows.isEmpty()) {
            similarShowsCard.setVisibility(View.GONE);
        }
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
    public void setSaveButtonEnabled(boolean enabled) {
        if (enabled) {
            watchedButton.setVisibility(View.GONE);
            watchedButtonAlt.setVisibility(View.VISIBLE);
        } else {
            watchedButton.setVisibility(View.VISIBLE);
            watchedButtonAlt.setVisibility(View.GONE);
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
        watchedButtonAlt = view.findViewById(R.id.watched_button_alt);
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

        similarMoviesCard = view.findViewById(R.id.similar_movies_card_layout);
        similarShowsCard = view.findViewById(R.id.similar_shows_card_layout);

        descMovieCard = view.findViewById(R.id.desc_movie_card_layout);
        descShowCard = view.findViewById(R.id.desc_show_card_layout);
    }
}
