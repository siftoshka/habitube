package az.siftoshka.habitube.ui.star;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
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

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import az.siftoshka.habitube.Constants;
import az.siftoshka.habitube.R;
import az.siftoshka.habitube.adapters.CastPersonAdapter;
import az.siftoshka.habitube.adapters.CrewPersonAdapter;
import az.siftoshka.habitube.di.modules.SearchModule;
import az.siftoshka.habitube.entities.person.Person;
import az.siftoshka.habitube.entities.personcredits.Cast;
import az.siftoshka.habitube.entities.personcredits.Crew;
import az.siftoshka.habitube.model.system.MessageListener;
import az.siftoshka.habitube.presentation.star.StarPresenter;
import az.siftoshka.habitube.presentation.star.StarView;
import az.siftoshka.habitube.ui.credits.CastPersonBottomDialog;
import az.siftoshka.habitube.ui.credits.CrewPersonBottomDialog;
import az.siftoshka.habitube.utils.DateChanger;
import az.siftoshka.habitube.utils.ImageLoader;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Scope;
import toothpick.Toothpick;

import static android.content.Context.MODE_PRIVATE;
import static az.siftoshka.habitube.Constants.DI.APP_SCOPE;
import static az.siftoshka.habitube.Constants.DI.POST_SCOPE;

public class StarFragment extends MvpAppCompatFragment implements StarView {

    @InjectPresenter StarPresenter starPresenter;

    @BindView(R.id.star_toolbar) Toolbar toolbar;
    @BindView(R.id.person_screen) RelativeLayout personScreen;
    @BindView(R.id.loading_screen) View loadingScreen;
    @BindView(R.id.error_screen) View errorScreen;
    @BindView(R.id.empty_screen) View emptyScreen;
    @BindView(R.id.poster_person_post) ImageView posterPerson;
    @BindView(R.id.poster_person_title) TextView posterPersonName;
    @BindView(R.id.poster_person_birthdate) TextView posterPersonBirthDate;
    @BindView(R.id.poster_person_location) TextView posterPersonLocation;
    @BindView(R.id.poster_person_bio) TextView posterPersonBio;
    @BindView(R.id.bio_person_card_layout) LinearLayout personBioCard;
    @BindView(R.id.imdb_button) MaterialButton imdbButton;
    @BindView(R.id.social_layout) LinearLayout socialLayout;
    @BindView(R.id.refresh) ImageView refreshButton;
    @BindView(R.id.tab_info) MaterialButton tabInfo;
    @BindView(R.id.tab_movies) MaterialButton tabMovies;
    @BindView(R.id.tab_shows) MaterialButton tabShows;
    @BindView(R.id.cast_movie_button) MaterialButton castMovieButton;
    @BindView(R.id.crew_movie_button) MaterialButton crewMovieButton;
    @BindView(R.id.cast_show_button) MaterialButton castShowButton;
    @BindView(R.id.crew_show_button) MaterialButton crewShowButton;
    @BindView(R.id.cast_movie_text) TextView castMovieText;
    @BindView(R.id.crew_movie_text) TextView crewMovieText;
    @BindView(R.id.cast_show_text) TextView castShowText;
    @BindView(R.id.crew_show_text) TextView crewShowText;
    @BindView(R.id.tab_info_layout) LinearLayout tabInfoCard;
    @BindView(R.id.tab_credits_movie_layout) LinearLayout tabMovieCreditsCard;
    @BindView(R.id.tab_credits_show_layout) LinearLayout tabShowCreditsCard;
    @BindView(R.id.recycler_view_movie_crew) RecyclerView recyclerViewMovieCrew;
    @BindView(R.id.recycler_view_movie_cast) RecyclerView recyclerViewMovieCast;
    @BindView(R.id.recycler_view_show_crew) RecyclerView recyclerViewShowCrew;
    @BindView(R.id.recycler_view_show_cast) RecyclerView recyclerViewShowCast;


    private DateChanger dateChanger = new DateChanger();
    private Unbinder unbinder;
    private MessageListener messageListener;
    private CastPersonAdapter movieCastAdapter, showCastAdapter;
    private CrewPersonAdapter movieCrewAdapter, showCrewAdapter;

    @ProvidePresenter
    StarPresenter starPresenter() {
        final Bundle searchBundle = getArguments();
        final Integer postId = searchBundle.getInt("postId");
        final Integer mediaType = searchBundle.getInt("mediaType");
        final Scope temporaryPostScope = Toothpick.openScopes(APP_SCOPE, POST_SCOPE);
        temporaryPostScope.installModules(new SearchModule(postId, mediaType));
        final StarPresenter starPresenter = temporaryPostScope.getInstance(StarPresenter.class);
        Toothpick.closeScope(POST_SCOPE);
        return starPresenter;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MessageListener) this.messageListener = (MessageListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toothpick.inject(this, Toothpick.openScope(APP_SCOPE));
        movieCastAdapter = new CastPersonAdapter(id -> starPresenter.goToDetailedMovieScreen(id));
        movieCrewAdapter = new CrewPersonAdapter(id -> starPresenter.goToDetailedMovieScreen(id));
        showCastAdapter = new CastPersonAdapter(id -> starPresenter.goToDetailedShowScreen(id));
        showCrewAdapter = new CrewPersonAdapter(id -> starPresenter.goToDetailedShowScreen(id));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_star, container, false);
        unbinder = ButterKnife.bind(this, view);
        posterPersonName.setSelected(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        toolbar.setNavigationOnClickListener(v -> starPresenter.goBack());
        checkTabs();
        initTabs();
        LinearLayoutManager linearLayoutMovieCast = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewMovieCast.setLayoutManager(linearLayoutMovieCast);
        recyclerViewMovieCast.setItemAnimator(new DefaultItemAnimator());
        recyclerViewMovieCast.setHasFixedSize(true);
        recyclerViewMovieCast.setAdapter(movieCastAdapter);
        LinearLayoutManager linearLayoutMovieCrew = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewMovieCrew.setLayoutManager(linearLayoutMovieCrew);
        recyclerViewMovieCrew.setItemAnimator(new DefaultItemAnimator());
        recyclerViewMovieCrew.setHasFixedSize(true);
        recyclerViewMovieCrew.setAdapter(movieCrewAdapter);
        LinearLayoutManager linearLayoutShowCast = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewShowCast.setLayoutManager(linearLayoutShowCast);
        recyclerViewShowCast.setItemAnimator(new DefaultItemAnimator());
        recyclerViewShowCast.setHasFixedSize(true);
        recyclerViewShowCast.setAdapter(showCastAdapter);
        LinearLayoutManager linearLayoutShowCrew = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewShowCrew.setLayoutManager(linearLayoutShowCrew);
        recyclerViewShowCrew.setItemAnimator(new DefaultItemAnimator());
        recyclerViewShowCrew.setHasFixedSize(true);
        recyclerViewShowCrew.setAdapter(showCrewAdapter);
    }

    @Override
    public void showPerson(Person person) {
        toolbar.setTitle(person.getName());
        ImageLoader.load(getContext(), person.getProfilePath(), posterPerson);
        posterPersonName.setText(person.getName());
        posterPersonBirthDate.setText(dateChanger.changeDate(person.getBirthday()));
        posterPersonLocation.setText(person.getPlaceOfBirth());
        posterPersonBio.setText(person.getBiography());
        checkImdbAvailability(person);
    }

    private void initTabInfo() {
        tabInfo.setTextColor(getResources().getColor(R.color.colorPrimary));
        tabMovies.setTextColor(getResources().getColor(R.color.dark_800));
        tabShows.setTextColor(getResources().getColor(R.color.dark_800));
        tabInfoCard.setVisibility(View.VISIBLE);
        tabMovieCreditsCard.setVisibility(View.GONE);
        tabShowCreditsCard.setVisibility(View.GONE);
        checkDescription();
    }

    private void initTabMovies() {
        tabInfo.setTextColor(getResources().getColor(R.color.dark_800));
        tabMovies.setTextColor(getResources().getColor(R.color.colorPrimary));
        tabShows.setTextColor(getResources().getColor(R.color.dark_800));
        tabInfoCard.setVisibility(View.GONE);
        tabMovieCreditsCard.setVisibility(View.VISIBLE);
        tabShowCreditsCard.setVisibility(View.GONE);
        checkMoviesExist();
    }

    private void initTabShows() {
        tabInfo.setTextColor(getResources().getColor(R.color.dark_800));
        tabMovies.setTextColor(getResources().getColor(R.color.dark_800));
        tabShows.setTextColor(getResources().getColor(R.color.colorPrimary));
        tabInfoCard.setVisibility(View.GONE);
        tabMovieCreditsCard.setVisibility(View.GONE);
        tabShowCreditsCard.setVisibility(View.VISIBLE);
        checkShowsExist();
    }

    private void initTabs() {
        tabInfo.setOnClickListener(view -> {
            initTabInfo();
            SharedPreferences.Editor editor = requireContext().getSharedPreferences("Star-Tab", MODE_PRIVATE).edit();
            editor.putInt("StarTab", 100).apply();
        });
        tabMovies.setOnClickListener(view -> {
            initTabMovies();
            SharedPreferences.Editor editor = requireContext().getSharedPreferences("Star-Tab", MODE_PRIVATE).edit();
            editor.putInt("StarTab", 101).apply();
        });
        tabShows.setOnClickListener(view -> {
           initTabShows();
            SharedPreferences.Editor editor = requireContext().getSharedPreferences("Star-Tab", MODE_PRIVATE).edit();
            editor.putInt("StarTab", 102).apply();
        });
    }

    private void checkTabs() {
        SharedPreferences prefs = requireContext().getSharedPreferences("Star-Tab", MODE_PRIVATE);
        int idTheme = prefs.getInt("StarTab", 0);
        switch (idTheme) {
            case 100: initTabInfo(); break;
            case 101: initTabMovies(); break;
            case 102: initTabShows(); break;
        }
    }

    private void checkMoviesExist() {
        if (movieCastAdapter.getItemCount() == 0 && movieCrewAdapter.getItemCount() == 0)
            emptyScreen.setVisibility(View.VISIBLE);
        else emptyScreen.setVisibility(View.GONE);
    }

    private void checkShowsExist() {
        if (showCastAdapter.getItemCount() == 0 && showCrewAdapter.getItemCount() == 0)
            emptyScreen.setVisibility(View.VISIBLE);
        else emptyScreen.setVisibility(View.GONE);
    }

    private void checkDescription() {
        if (posterPersonBio.getText().toString().equals("")) {
            personBioCard.setVisibility(View.GONE);
            emptyScreen.setVisibility(View.VISIBLE);
        }
    }

    private void checkImdbAvailability(Person person) {
        if (!person.getImdbId().equals("")) {
            socialLayout.setVisibility(View.VISIBLE);
            showImdbWeb(person.getImdbId());
        }
    }

    private void showImdbWeb(String imdbId) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(Constants.SYSTEM.IMDB_PERSON + imdbId));
        imdbButton.setOnClickListener(v -> startActivity(intent));
        imdbButton.setOnLongClickListener(view -> {
            ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(getString(R.string.copied), Constants.SYSTEM.IMDB_PERSON + imdbId);
            if (clipboard != null) {
                clipboard.setPrimaryClip(clip);
                messageListener.showText(getString(R.string.copied));
            }
            return true;
        });
    }

    @Override
    public void showMovieCast(List<Cast> casts) {
        if (casts == null || casts.size() == 0) castMovieText.setVisibility(View.GONE);
        movieCastAdapter.addAllPersons(casts);
    }

    @Override
    public void showTVShowCast(List<Cast> casts) {
        if (casts == null || casts.size() == 0) castShowText.setVisibility(View.GONE);
        showCastAdapter.addAllPersons(casts);
    }

    @Override
    public void showMovieCrew(List<Crew> crews) {
        if (crews == null || crews.size() == 0) crewMovieText.setVisibility(View.GONE);
        movieCrewAdapter.addAllPersons(crews);
    }

    @Override
    public void showTVShowCrew(List<Crew> crews) {
        if (crews == null || crews.size() == 0) crewShowText.setVisibility(View.GONE);
        showCrewAdapter.addAllPersons(crews);
    }

    @Override
    public void showMovieCastExpandButton(List<Cast> casts) {
        castMovieButton.setVisibility(View.VISIBLE);
        castMovieButton.setOnClickListener(view -> {
            CastPersonBottomDialog castBottomDialog = new CastPersonBottomDialog(starPresenter.getRouter());
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("MCAST", (ArrayList<? extends Parcelable>) casts);
            castBottomDialog.setArguments(bundle);
            castBottomDialog.show(getChildFragmentManager(), null);
        });
    }

    @Override
    public void showTVShowCastExpandButton(List<Cast> casts) {
        castShowButton.setVisibility(View.VISIBLE);
        castShowButton.setOnClickListener(view -> {
            CastPersonBottomDialog castBottomDialog = new CastPersonBottomDialog(starPresenter.getRouter());
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("SCAST", (ArrayList<? extends Parcelable>) casts);
            castBottomDialog.setArguments(bundle);
            castBottomDialog.show(getChildFragmentManager(), null);
        });
    }

    @Override
    public void showMovieCrewExpandButton(List<Crew> crews) {
        crewMovieButton.setVisibility(View.VISIBLE);
        crewMovieButton.setOnClickListener(view -> {
            CrewPersonBottomDialog crewBottomDialog = new CrewPersonBottomDialog(starPresenter.getRouter());
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("MCREW", (ArrayList<? extends Parcelable>) crews);
            crewBottomDialog.setArguments(bundle);
            crewBottomDialog.show(getChildFragmentManager(), null);
        });
    }

    @Override
    public void showTVShowCrewExpandButton(List<Crew> crews) {
        crewShowButton.setVisibility(View.VISIBLE);
        crewShowButton.setOnClickListener(view -> {
            CrewPersonBottomDialog crewBottomDialog = new CrewPersonBottomDialog(starPresenter.getRouter());
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("SCREW", (ArrayList<? extends Parcelable>) crews);
            crewBottomDialog.setArguments(bundle);
            crewBottomDialog.show(getChildFragmentManager(), null);
        });
    }

    @Override
    public void showProgress(boolean loadingState) {
        if (loadingState && loadingScreen != null) loadingScreen.setVisibility(View.VISIBLE);
        else if (loadingScreen != null) loadingScreen.setVisibility(View.GONE);
    }

    @Override
    public void showPersonScreen() {
        errorScreen.setVisibility(View.GONE);
        personScreen.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorScreen() {
        personScreen.setVisibility(View.GONE);
        errorScreen.setVisibility(View.VISIBLE);
        refreshButton.setOnClickListener(view -> starPresenter.onFirstViewAttach());
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
