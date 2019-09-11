package az.siftoshka.habitube.ui.show;

import android.annotation.SuppressLint;
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

import az.siftoshka.habitube.R;
import az.siftoshka.habitube.adapters.SeasonAdapter;
import az.siftoshka.habitube.adapters.ShowAdapter;
import az.siftoshka.habitube.di.modules.MovieModule;
import az.siftoshka.habitube.di.modules.SearchModule;
import az.siftoshka.habitube.entities.movielite.MovieLite;
import az.siftoshka.habitube.entities.show.Show;
import az.siftoshka.habitube.entities.show.ShowGenre;
import az.siftoshka.habitube.presentation.show.ShowPresenter;
import az.siftoshka.habitube.presentation.show.ShowView;
import az.siftoshka.habitube.ui.season.SeasonBottomDialog;
import az.siftoshka.habitube.utils.DateChanger;
import az.siftoshka.habitube.utils.GlideLoader;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Scope;
import toothpick.Toothpick;

import static az.siftoshka.habitube.Constants.DI.APP_SCOPE;
import static az.siftoshka.habitube.Constants.DI.POST_SCOPE;

public class ShowFragment extends MvpAppCompatFragment implements ShowView {

    @InjectPresenter
    ShowPresenter showPresenter;

    @BindView(R.id.show_toolbar) Toolbar toolbar;
    @BindView(R.id.recycler_view_similar_shows) RecyclerView recyclerViewSimilarShows;
    @BindView(R.id.recycler_view_seasons) RecyclerView recyclerViewSeasons;
    @BindView(R.id.show_screen) RelativeLayout showScreen;
    @BindView(R.id.loading_screen) View loadingScreen;
    @BindView(R.id.error_screen) View errorScreen;
    @BindView(R.id.poster_show_post) ImageView posterShow;
    @BindView(R.id.show_poster_background) ImageView posterShowBackground;
    @BindView(R.id.poster_show_title) TextView posterShowTitle;
    @BindView(R.id.poster_show_date) TextView posterShowDate;
    @BindView(R.id.poster_show_rate) TextView posterShowRate;
    @BindView(R.id.poster_show_views) TextView posterShowViews;
    @BindView(R.id.poster_show_duration) TextView posterShowDuration;
    @BindView(R.id.poster_show_desc) TextView posterShowDesc;
    @BindView(R.id.show_genres) ChipGroup showGenresChip;
    @BindView(R.id.similar_shows_card_layout) LinearLayout similarShowsCard;
    @BindView(R.id.desc_show_card_layout) LinearLayout descShowCard;

    private ShowAdapter similarShowsAdapter;
    private SeasonAdapter seasonAdapter;
    private DateChanger dateChanger = new DateChanger();
    private Unbinder unbinder;

    @ProvidePresenter
    ShowPresenter showPresenter() {
        final Bundle showBundle = getArguments();
        final Integer showPosition = showBundle.getInt("showPosition");
        final Bundle searchBundle = getArguments();
        final Integer postId = searchBundle.getInt("postId");
        final Integer mediaType = searchBundle.getInt("mediaType");

        final Scope temporaryPostScope = Toothpick.openScopes(APP_SCOPE, POST_SCOPE);
        temporaryPostScope.installModules(new MovieModule(0, showPosition, 0));
        temporaryPostScope.installModules(new SearchModule(postId, mediaType));
        final ShowPresenter showPresenter = temporaryPostScope.getInstance(ShowPresenter.class);
        Toothpick.closeScope(POST_SCOPE);
        return showPresenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toothpick.inject(this, Toothpick.openScope(APP_SCOPE));
        similarShowsAdapter = new ShowAdapter(showId -> showPresenter.goToDetailedShowScreen(showId));
        seasonAdapter = new SeasonAdapter(this::showBottomSeasonDialog);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_show, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        toolbar.setNavigationOnClickListener(v -> showPresenter.goBack());

        LinearLayoutManager layoutManagerSimilarShows = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerViewSimilarShows.setLayoutManager(layoutManagerSimilarShows);
        recyclerViewSimilarShows.setItemAnimator(new DefaultItemAnimator());
        recyclerViewSimilarShows.setHasFixedSize(true);
        recyclerViewSimilarShows.setAdapter(similarShowsAdapter);

        LinearLayoutManager layoutManagerSeasons = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerViewSeasons.setLayoutManager(layoutManagerSeasons);
        recyclerViewSeasons.setItemAnimator(new DefaultItemAnimator());
        recyclerViewSeasons.setHasFixedSize(true);
        recyclerViewSeasons.setAdapter(seasonAdapter);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void showTVShow(Show show) {
        toolbar.setTitle(show.getName());
        GlideLoader.load(getContext(), show.getPosterPath(), posterShow);
        GlideLoader.loadBackground(getContext(), show.getBackdropPath(), posterShowBackground);
        posterShowTitle.setText(show.getName());
        posterShowDate.setText(dateChanger.changeDate(show.getFirstAirDate()));
        posterShowRate.setText(String.valueOf(show.getVoteAverage()));
        posterShowViews.setText("(" + show.getVoteCount() + ")");
        episodeRuntime(show.getEpisodeRunTime().toString());

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
    public void showSimilarTVShowList(List<MovieLite> similarShows) {
        if(similarShows.isEmpty()) {
            similarShowsCard.setVisibility(View.GONE);
        }
        similarShowsAdapter.addAllMovies(similarShows);
    }

    @Override
    public void showBottomSeasonDialog(int position) {
        SeasonBottomDialog seasonBottomDialog = new SeasonBottomDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("SEASON", seasonAdapter.getSeason(position));
        seasonBottomDialog.setArguments(bundle);
        seasonBottomDialog.show(getChildFragmentManager(), null);
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
    public void showTVShowScreen() {
        errorScreen.setVisibility(View.GONE);
        showScreen.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorScreen() {
        errorScreen.setVisibility(View.VISIBLE);
        showScreen.setVisibility(View.GONE);
    }

    private void episodeRuntime(String episodeRuntime) {
        String updatedText = episodeRuntime.substring(1, episodeRuntime.length() - 1);
        posterShowDuration.setText(String.format("%s %s", updatedText, getResources().getString(R.string.minutes)));
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
