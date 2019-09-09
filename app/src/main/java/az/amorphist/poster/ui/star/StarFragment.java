package az.amorphist.poster.ui.star;

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

import az.amorphist.poster.R;
import az.amorphist.poster.di.modules.SearchModule;
import az.amorphist.poster.entities.person.Person;
import az.amorphist.poster.presentation.star.StarPresenter;
import az.amorphist.poster.presentation.star.StarView;
import az.amorphist.poster.utils.DateChanger;
import az.amorphist.poster.utils.GlideLoader;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Scope;
import toothpick.Toothpick;

import static az.amorphist.poster.Constants.DI.APP_SCOPE;
import static az.amorphist.poster.Constants.DI.POST_SCOPE;

public class StarFragment extends MvpAppCompatFragment implements StarView {

    @InjectPresenter StarPresenter starPresenter;

    @BindView(R.id.star_toolbar) Toolbar toolbar;
    @BindView(R.id.person_screen) RelativeLayout personScreen;
    @BindView(R.id.loading_screen) View loadingScreen;
    @BindView(R.id.error_screen) View errorScreen;
    @BindView(R.id.poster_person_post) ImageView posterPerson;
    @BindView(R.id.poster_person_title) TextView posterPersonName;
    @BindView(R.id.poster_person_birthdate) TextView posterPersonBirthDate;
    @BindView(R.id.poster_person_location) TextView posterPersonLocation;
    @BindView(R.id.poster_person_popularity) TextView posterPersonPopularity;
    @BindView(R.id.poster_person_bio) TextView posterPersonBio;
    @BindView(R.id.bio_person_card_layout) LinearLayout personBioCard;

    private DateChanger dateChanger = new DateChanger();
    private Unbinder unbinder;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toothpick.inject(this, Toothpick.openScope(APP_SCOPE));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_star, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        toolbar.setNavigationOnClickListener(v -> starPresenter.goBack());
    }

    @Override
    public void showPerson(Person person) {
        toolbar.setTitle(person.getName());
        GlideLoader.load(getContext(), person.getProfilePath(), posterPerson);
        posterPersonName.setText(person.getName());
        posterPersonBirthDate.setText(dateChanger.changeDate(person.getBirthday()));
        posterPersonLocation.setText(person.getPlaceOfBirth());
        posterPersonPopularity.setText(String.valueOf(person.getPopularity()));
        posterPersonBio.setText(person.getBiography());

        if(person.getBiography().equals("")) {
            personBioCard.setVisibility(View.GONE);
        }
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
    public void showPersonScreen() {
        errorScreen.setVisibility(View.GONE);
        personScreen.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorScreen() {
        personScreen.setVisibility(View.GONE);
        errorScreen.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
