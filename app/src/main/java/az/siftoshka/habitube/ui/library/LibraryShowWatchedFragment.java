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

import java.util.Collections;
import java.util.List;

import az.siftoshka.habitube.R;
import az.siftoshka.habitube.adapters.LibraryShowAdapter;
import az.siftoshka.habitube.entities.movie.Movie;
import az.siftoshka.habitube.entities.show.Show;
import az.siftoshka.habitube.presentation.library.LibraryWatchedPresenter;
import az.siftoshka.habitube.presentation.library.LibraryWatchedView;
import az.siftoshka.habitube.ui.library.dialogs.OfflineShowCardDialog;
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

public class LibraryShowWatchedFragment extends MvpAppCompatFragment implements LibraryWatchedView {

    @InjectPresenter LibraryWatchedPresenter watchedPresenter;

    @BindView(R.id.recycler_view_watched) RecyclerView recyclerViewWatched;
    @BindView(R.id.empty_screen) View emptyScreen;
    private LibraryShowAdapter libraryAdapter;
    private Unbinder unbinder;

    @ProvidePresenter
    LibraryWatchedPresenter watchedPresenter() {
        return Toothpick.openScope(APP_SCOPE).getInstance(LibraryWatchedPresenter.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toothpick.inject(this, Toothpick.openScope(APP_SCOPE));
        libraryAdapter = new LibraryShowAdapter(postId -> watchedPresenter.goToDetailedShowScreen(postId), this::showOptionMenu);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_library_watched, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerViewWatched.setLayoutManager(layoutManager);
        recyclerViewWatched.setItemAnimator(new DefaultItemAnimator());
        recyclerViewWatched.setHasFixedSize(true);
        recyclerViewWatched.setAdapter(libraryAdapter);
        watchedPresenter.getShows();
        libraryAdapter.getItemCount();
    }

    @Override
    public void showWatchedShows(List<Show> shows) {
        SharedPreferences prefs = requireContext().getSharedPreferences("Radio-Sort", MODE_PRIVATE);
        int id = prefs.getInt("Radio", 0);
        switch (id) {
            case 200: Collections.sort(shows, (o1, o2) -> o2.getAddedDate().compareTo(o1.getAddedDate()));break;
            case 201: Collections.sort(shows, (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));break;
            case 202: Collections.sort(shows, (o1, o2) -> o2.getFirstAirDate().compareTo(o1.getFirstAirDate()));break;
            case 203: Collections.sort(shows, (o1, o2) -> o1.getAddedDate().compareTo(o2.getAddedDate()));break;
            case 204: Collections.sort(shows, (o1, o2) -> o1.getFirstAirDate().compareTo(o2.getFirstAirDate()));break;
            case 205: Collections.sort(shows, (o1, o2) -> Double.compare(o2.getVoteAverage(), o1.getVoteAverage()));break;
        }
        libraryAdapter.addAllShows(shows);
        watcher();
    }

    @Override
    public void showOfflineCard(Show show) {
        OfflineShowCardDialog offlineShowCardDialog = new OfflineShowCardDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("Shows", show);
        offlineShowCardDialog.setArguments(bundle);
        offlineShowCardDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void screenWatcher(int position) {
        libraryAdapter.dataChanged(position);
        watcher();
    }

    private void watcher() {
        if (libraryAdapter.getItemCount() != 0) {
            emptyScreen.setVisibility(View.GONE);
            recyclerViewWatched.setVisibility(View.VISIBLE);
        } else {
            emptyScreen.setVisibility(View.VISIBLE);
            recyclerViewWatched.setVisibility(View.GONE);
        }
    }

    private void showOptionMenu(Show show, int position) {
        OptionMenuDialog menuDialog = new OptionMenuDialog(null, watchedPresenter);
        Bundle bundle = new Bundle();
        bundle.putParcelable("show-library-watched", show);
        bundle.putInt("position", position);
        menuDialog.setArguments(bundle);
        menuDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void showOfflineCard(Movie movie) { }

    @Override
    public void showWatchedMovies(List<Movie> movies) { }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
