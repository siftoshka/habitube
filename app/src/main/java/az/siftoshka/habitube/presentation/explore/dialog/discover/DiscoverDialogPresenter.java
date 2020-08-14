package az.siftoshka.habitube.presentation.explore.dialog.discover;

import javax.inject.Inject;

import az.siftoshka.habitube.Screens;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class DiscoverDialogPresenter extends MvpPresenter<DiscoverDialogView> {

    private final Router router;

    @Inject
    public DiscoverDialogPresenter(Router router) {
        this.router = router;
    }

    public void discoverMovies(String sortSelection, String yearIndexUp, String yearIndexDown, int voteIndexUp, int voteIndexDown) {
        goToDiscoverScreen(sortSelection, yearIndexUp, yearIndexDown, voteIndexUp, voteIndexDown);
    }

    public void discoverShows(String yearIndexUp, String yearIndexDown, int voteIndexUp, int voteIndexDown) {
        goToDiscoverShowScreen(yearIndexUp, yearIndexDown, voteIndexUp, voteIndexDown);
    }

    private void goToDiscoverScreen(String sortSelection, String yearIndexUp, String yearIndexDown, int voteIndexUp, int voteIndexDown) {
        router.navigateTo(new Screens.DiscoverScreen(sortSelection, yearIndexUp, yearIndexDown, voteIndexUp, voteIndexDown));
    }

    private void goToDiscoverShowScreen(String yearIndexUp, String yearIndexDown, int voteIndexUp, int voteIndexDown) {
        router.navigateTo(new Screens.DiscoverShowScreen(yearIndexUp, yearIndexDown, voteIndexUp, voteIndexDown));
    }

    public void goBack() {
        router.exit();
    }
}
