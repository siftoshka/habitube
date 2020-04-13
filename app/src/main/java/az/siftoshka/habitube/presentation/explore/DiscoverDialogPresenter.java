package az.siftoshka.habitube.presentation.explore;

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

    public void discoverMovies(String sortSelection, String yearIndex, int voteIndex) {
        goToDiscoverScreen(sortSelection, yearIndex, voteIndex);
    }

    public void discoverShows(String yearIndex, int voteIndex) {
        goToDiscoverShowScreen(yearIndex, voteIndex);
    }

    private void goToDiscoverScreen(String sortSelection, String yearIndex, int voteIndex) {
        router.navigateTo(new Screens.DiscoverScreen(sortSelection, yearIndex, voteIndex));
    }

    private void goToDiscoverShowScreen(String yearIndex, int voteIndex) {
        router.navigateTo(new Screens.DiscoverShowScreen(yearIndex, voteIndex));
    }

    public void goBack() {
        router.exit();
    }
}
