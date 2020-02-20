package az.siftoshka.habitube.ui.library;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import az.siftoshka.habitube.R;
import az.siftoshka.habitube.adapters.LibraryAdapter;
import az.siftoshka.habitube.adapters.LibraryShowAdapter;
import az.siftoshka.habitube.entities.movie.Movie;
import az.siftoshka.habitube.entities.show.Show;
import az.siftoshka.habitube.presentation.library.LibraryWatchedPresenter;
import az.siftoshka.habitube.presentation.library.LibraryWatchedView;
import az.siftoshka.habitube.utils.animation.SwipeDecorator;
import az.siftoshka.habitube.utils.animation.VegaXLayoutManager;
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
        libraryAdapter = new LibraryShowAdapter(postId -> watchedPresenter.goToDetailedShowScreen(postId));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_library_watched, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerViewWatched.setLayoutManager(new VegaXLayoutManager());
        recyclerViewWatched.setItemAnimator(new DefaultItemAnimator());
        recyclerViewWatched.setHasFixedSize(true);
        recyclerViewWatched.setAdapter(libraryAdapter);
        watchedPresenter.getShows();
        libraryAdapter.getItemCount();

        ItemTouchHelper itemTouchHelper =
                new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                watchedPresenter.removeFromLocal(libraryAdapter.getShowAt(viewHolder.getAdapterPosition()));
                screenWatcher();
            }

            @Override
            public void onChildDraw(@NonNull Canvas c,
                                    @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new SwipeDecorator.Builder(requireContext(), c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(requireContext(), R.color.deleteColor))
                        .addActionIcon(R.drawable.ic_delete)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
                });
        itemTouchHelper.attachToRecyclerView(recyclerViewWatched);
    }

    @Override
    public void showWatchedMovies(List<Movie> movies) {

    }

    @Override
    public void showWatchedShows(List<Show> shows) {
        SharedPreferences prefs = requireContext().getSharedPreferences("Radio-Sort", MODE_PRIVATE);
        int id = prefs.getInt("Radio", 0);
        switch (id) {
            case 200:
                Collections.sort(shows, (o1, o2) -> o2.getAddedDate().compareTo(o1.getAddedDate()));
                break;
            case 201:
                Collections.sort(shows, (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
                break;
            case 202:
                Collections.sort(shows, (o1, o2) -> o2.getFirstAirDate().compareTo(o1.getFirstAirDate()));
        }
        libraryAdapter.addAllShows(shows);
        screenWatcher();
    }

    @Override
    public void showOfflineCard(Movie movie) {

    }

    @Override
    public void showOfflineCard(Show show) {
        OfflineShowCardDialog offlineShowCardDialog = new OfflineShowCardDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("Shows", show);
        offlineShowCardDialog.setArguments(bundle);
        offlineShowCardDialog.show(getChildFragmentManager(), null);
    }

    private void screenWatcher() {
        if (libraryAdapter.getItemCount() != 0) {
            emptyScreen.setVisibility(View.GONE);
            recyclerViewWatched.setVisibility(View.VISIBLE);
        } else {
            emptyScreen.setVisibility(View.VISIBLE);
            recyclerViewWatched.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
