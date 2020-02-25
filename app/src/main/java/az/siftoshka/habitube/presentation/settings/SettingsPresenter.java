package az.siftoshka.habitube.presentation.settings;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import az.siftoshka.habitube.Screens;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class SettingsPresenter extends MvpPresenter<SettingsView> {

    private final Router router;

    @Override
    public void onFirstViewAttach() {
        super.onFirstViewAttach();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user !=null) {
            getViewState().showUser(user);
        } else {
            getViewState().showGoogleSignIn();
        }
    }

    @Inject
    public SettingsPresenter(Router router) {
        this.router = router;
    }

    public void goToSearchScreen() {
        router.navigateTo(new Screens.SearchScreen());
    }
}
