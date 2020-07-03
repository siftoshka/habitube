package az.siftoshka.habitube.presentation.library;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

import javax.inject.Inject;

import az.siftoshka.habitube.R;
import az.siftoshka.habitube.Screens;
import az.siftoshka.habitube.entities.firebase.Media;
import az.siftoshka.habitube.entities.movie.Movie;
import az.siftoshka.habitube.entities.show.Show;
import az.siftoshka.habitube.model.interactor.PlannedInteractor;
import az.siftoshka.habitube.model.interactor.RemotePostInteractor;
import az.siftoshka.habitube.utils.ImageLoader;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

import static az.siftoshka.habitube.Constants.FIREBASE.PLANNING_MOVIE;
import static az.siftoshka.habitube.Constants.FIREBASE.PLANNING_SHOW;
import static az.siftoshka.habitube.Constants.SYSTEM.IMAGE_URL;

@InjectViewState
public class LibraryPlanningPresenter extends MvpPresenter<LibraryPlanningView> {

    private final Router router;
    private final Context context;
    private DatabaseReference databaseReference;
    private final RemotePostInteractor postInteractor;
    private final PlannedInteractor plannedInteractor;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public LibraryPlanningPresenter(Router router, Context context,
                                    RemotePostInteractor postInteractor,
                                    PlannedInteractor plannedInteractor) {
        this.router = router;
        this.context = context;
        this.postInteractor = postInteractor;
        this.plannedInteractor = plannedInteractor;
    }

    @Override
    protected void onFirstViewAttach() {
        getMovies();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (haveNetworkConnection() && user != null) {
            checkMovies();
            checkShows();
            addMoviesToFirebase();
            addShowsToFirebase();
        }
    }

    public void checkMovies() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            databaseReference = FirebaseDatabase.getInstance()
                    .getReference()
                    .child(PLANNING_MOVIE)
                    .child(user.getUid());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        compositeDisposable.add(plannedInteractor.isMovieExists(postSnapshot.getValue(Media.class).getId())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe((existInLocal) -> syncMovie(postSnapshot.getValue(Media.class), existInLocal), Throwable::printStackTrace));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }});
        }
    }

    private void syncMovie(Media mediaFromFirebase, Boolean existsInLocal) {
        if (!existsInLocal) {
            compositeDisposable.add(postInteractor.getMovie(mediaFromFirebase.getId(),
                    context.getResources().getString(R.string.language))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess(movie -> movie.setAddedDate(new Date()))
                    .subscribe((movie) -> Glide.with(context)
                            .load(IMAGE_URL + movie.getPosterPath())
                            .apply(new RequestOptions().override(200, 300))
                            .error(R.drawable.ic_missing)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) { return false; }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    ImageLoader.saveToInternalStorage(movie.getPosterPath(), context, resource);
                                    plannedInteractor.addMovie(movie);
                                    return true;
                                }}).submit(), Throwable::printStackTrace));
        }
    }

    public void checkShows() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            databaseReference = FirebaseDatabase.getInstance()
                    .getReference()
                    .child(PLANNING_SHOW)
                    .child(user.getUid());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        compositeDisposable.add(plannedInteractor.isShowExists(postSnapshot.getValue(Media.class).getId())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe((existInLocal) -> syncShows(postSnapshot.getValue(Media.class), existInLocal), Throwable::printStackTrace));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }});
        }
    }

    private void syncShows(Media mediaFromFirebase, Boolean existsInLocal) {
        if (!existsInLocal) {
            compositeDisposable.add(postInteractor.getTVShow(mediaFromFirebase.getId(),
                    context.getResources().getString(R.string.language))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess(show -> show.setAddedDate(new Date()))
                    .subscribe((show) -> Glide.with(context)
                            .load(IMAGE_URL + show.getPosterPath())
                            .apply(new RequestOptions().override(200, 300))
                            .error(R.drawable.ic_missing)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) { return false; }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    ImageLoader.saveToInternalStorage(show.getPosterPath(), context, resource);
                                    plannedInteractor.addShow(show);
                                    return true;
                                }}).submit(), Throwable::printStackTrace));
        }
    }

    public void getMovies() {
        compositeDisposable.add(plannedInteractor.getAllMovies()
                .subscribe((movies, throwable) -> getViewState().showPlannedMovies(movies)));
    }

    public void getShows() {
        compositeDisposable.add(plannedInteractor.getAllShows()
                .subscribe((shows, throwable) -> getViewState().showPlannedShows(shows)));
    }

    private void addMoviesToFirebase() {
        compositeDisposable.add(plannedInteractor.getAllMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movies -> {
                    for (Movie movie: movies)
                        plannedInteractor.addMovieFB(movie);
                }));
    }

    private void addShowsToFirebase() {
        compositeDisposable.add(plannedInteractor.getAllShows()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(shows -> {
                    for (Show show: shows)
                        plannedInteractor.addShowFB(show);
                }));
    }

    public void removeFromLocal(Movie movie, int position) {
        plannedInteractor.deleteMovie(movie);
        getViewState().screenWatcher(position);
    }

    public void removeFromLocal(Show show, int position) {
        plannedInteractor.deleteShow(show);
        getViewState().screenWatcher(position);
    }

    public void goToDetailedMovieScreen(int postId) {
        if (haveNetworkConnection()) {
            router.navigateTo(new Screens.PostMovieScreen(postId));
        } else {
            compositeDisposable.add(plannedInteractor.getMovie(postId)
                    .subscribe(movie -> getViewState().showOfflineCard(movie)));
        }
    }

    public void goToDetailedShowScreen(int postId) {
        if (haveNetworkConnection()) {
            router.navigateTo(new Screens.PostShowScreen(postId));
        } else {
            compositeDisposable.add(plannedInteractor.getShow(postId)
                    .subscribe(show -> getViewState().showOfflineCard(show)));
        }
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm != null ? cm.getAllNetworkInfo() : new NetworkInfo[0];
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    @Override
    public void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }
}
