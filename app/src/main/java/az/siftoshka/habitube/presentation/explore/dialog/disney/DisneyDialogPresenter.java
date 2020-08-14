package az.siftoshka.habitube.presentation.explore.dialog.disney;

import javax.inject.Inject;

import az.siftoshka.habitube.Screens;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class DisneyDialogPresenter extends MvpPresenter<DisneyDialogView> {

    private final Router router;

    @Inject
    public DisneyDialogPresenter(Router router) {
        this.router = router;
    }

    public void showDisneyPopular() {
        goToDisneyScreen(0);
    }

    public void showDisneyBest() {
        goToDisneyScreen(1);
    }

    public void showDisneyNew() {
        goToDisneyScreen(2);
    }

    private void goToDisneyScreen(int index) {
        router.navigateTo(new Screens.DisneyDiscoverScreen(index));
    }

    public void goBack() {
        router.exit();
    }
}
