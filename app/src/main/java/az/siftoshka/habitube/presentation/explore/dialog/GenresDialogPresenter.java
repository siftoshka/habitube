package az.siftoshka.habitube.presentation.explore.dialog;

import javax.inject.Inject;

import az.siftoshka.habitube.Screens;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class GenresDialogPresenter extends MvpPresenter<GenresDialogView> {

    private final Router router;

    @Inject
    public GenresDialogPresenter(Router router) {
        this.router = router;
    }

    public void goToDiscoverScreen(String yearIndexUp, String yearIndexDown, int voteIndexUp, int voteIndexDown, String genre) {
        router.navigateTo(new Screens.GenreScreen(yearIndexUp, yearIndexDown, voteIndexUp, voteIndexDown, genre));
    }

    public void goToDiscoverShowScreen(String yearIndexUp, String yearIndexDown, int voteIndexUp, int voteIndexDown, String genre) {
        router.navigateTo(new Screens.GenreShowScreen(yearIndexUp, yearIndexDown, voteIndexUp, voteIndexDown, genre));
    }

    public void goBack() {
        router.exit();
    }
}
