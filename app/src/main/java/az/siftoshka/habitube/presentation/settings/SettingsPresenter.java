package az.siftoshka.habitube.presentation.settings;

import javax.inject.Inject;

import az.siftoshka.habitube.Screens;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class SettingsPresenter extends MvpPresenter<SettingsView> {

    private final Router router;

    @Inject
    public SettingsPresenter(Router router) {
        this.router = router;
    }

    public void goToSearchScreen() {
        router.navigateTo(new Screens.SearchScreen());
    }
}
