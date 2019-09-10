package az.amorphist.habitube.presentation.library;

import javax.inject.Inject;

import az.amorphist.habitube.Screens;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class LibraryPresenter extends MvpPresenter<LibraryView> {

    private final Router router;
    @Inject
    public LibraryPresenter(Router router) {
        this.router = router;
    }

    public void goToSearchScreen() {
        router.navigateTo(new Screens.SearchScreen());
    }
}
