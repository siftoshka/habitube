package az.siftoshka.habitube.ui.library;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Fade;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import az.siftoshka.habitube.Constants;
import az.siftoshka.habitube.R;
import az.siftoshka.habitube.adapters.LibraryAdapter;
import az.siftoshka.habitube.entities.movie.Movie;
import az.siftoshka.habitube.entities.show.Show;
import az.siftoshka.habitube.presentation.library.LibraryPlanningPresenter;
import az.siftoshka.habitube.presentation.library.LibraryPlanningView;
import az.siftoshka.habitube.ui.library.dialogs.OfflineCardDialog;
import az.siftoshka.habitube.ui.library.dialogs.OptionMenuDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static android.content.Context.MODE_PRIVATE;
import static az.siftoshka.habitube.Constants.DI.APP_SCOPE;

public class LibraryPlanningFragment extends MvpAppCompatFragment implements LibraryPlanningView {

    @InjectPresenter LibraryPlanningPresenter planningPresenter;

    @BindView(R.id.recycler_view_planning) RecyclerView recyclerViewPlanning;
    @BindView(R.id.empty_screen) View emptyScreen;
    private LibraryAdapter libraryAdapter;
    private Unbinder unbinder;

    @ProvidePresenter
    LibraryPlanningPresenter planningPresenter() {
        return Toothpick.openScope(Constants.DI.APP_SCOPE).getInstance(LibraryPlanningPresenter.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toothpick.inject(this, Toothpick.openScope(APP_SCOPE));
        libraryAdapter = new LibraryAdapter(requireContext(), postId -> planningPresenter.goToDetailedMovieScreen(postId), this::showOptionMenu);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_library_planned, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerViewPlanning.setLayoutManager(layoutManager);
        recyclerViewPlanning.setItemAnimator(new DefaultItemAnimator());
        recyclerViewPlanning.setHasFixedSize(true);
        recyclerViewPlanning.setAdapter(libraryAdapter);
        planningPresenter.getMovies();
        libraryAdapter.getItemCount();
    }

    @Override
    public void showPlannedMovies(List<Movie> movies) {
        SharedPreferences prefs = requireContext().getSharedPreferences("Radio-Sort", MODE_PRIVATE);
        int id = prefs.getInt("Radio", 0);
        switch (id) {
            case 200: Collections.sort(movies, (o1, o2) -> o2.getAddedDate().compareTo(o1.getAddedDate()));break;
            case 201: Collections.sort(movies, (o1, o2) -> o1.getTitle().compareToIgnoreCase(o2.getTitle()));break;
            case 202: Collections.sort(movies, (o1, o2) -> o2.getReleaseDate().compareTo(o1.getReleaseDate()));break;
            case 203: Collections.sort(movies, (o1, o2) -> o1.getAddedDate().compareTo(o2.getAddedDate()));break;
            case 204: Collections.sort(movies, (o1, o2) -> o1.getReleaseDate().compareTo(o2.getReleaseDate()));break;
            case 205: Collections.sort(movies, (o1, o2) -> Double.compare(o2.getVoteAverage(), o1.getVoteAverage()));break;
        }
        libraryAdapter.addAllMovies(movies);
        watcher();
    }

    @Override
    public void showOfflineCard(Movie movie) {
        OfflineCardDialog offlineCardDialog = new OfflineCardDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("Movies", movie);
        offlineCardDialog.setArguments(bundle);
        offlineCardDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void showOfflineCard(Show show) {

    }

    @Override
    public void showPlannedShows(List<Show> shows) {

    }

    @Override
    public void screenWatcher(int position) {
        libraryAdapter.removedItem(position);
        watcher();
    }

    private void watcher() {
        if (libraryAdapter.getItemCount() != 0) {
            toggle(recyclerViewPlanning);
            emptyScreen.setVisibility(View.GONE);
            recyclerViewPlanning.setVisibility(View.VISIBLE);
        } else {
            emptyScreen.setVisibility(View.VISIBLE);
            recyclerViewPlanning.setVisibility(View.GONE);
        }
    }

    private void toggle(View target) {
        Transition transition = new Fade();
        transition.setDuration(1000);
        transition.addTarget(target);
        TransitionManager.beginDelayedTransition((ViewGroup) Objects.requireNonNull(getView()).getParent(), transition);
    }

    private void showOptionMenu(Movie movie, int position) {
        OptionMenuDialog menuDialog = new OptionMenuDialog(planningPresenter, null);
        Bundle bundle = new Bundle();
        bundle.putParcelable("movie-library-planning", movie);
        bundle.putInt("position", position);
        menuDialog.setArguments(bundle);
        menuDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
