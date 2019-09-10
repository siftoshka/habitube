package az.amorphist.habitube.presentation.library;

import javax.inject.Inject;

import az.amorphist.habitube.Screens;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class LibraryPlanningPresenter extends MvpPresenter<LibraryPlanningView> {

    private final Router router;

    @Inject
    public LibraryPlanningPresenter(Router router) {
        this.router = router;
    }

    public void goToExploreScreen() {
        router.newRootScreen(new Screens.NavbarScreen());
    }
}
