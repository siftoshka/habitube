package az.siftoshka.habitube.presentation.explore.dialog.apple;

import javax.inject.Inject;

import az.siftoshka.habitube.Screens;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class AppleDialogPresenter extends MvpPresenter<AppleDialogView> {

    private final Router router;

    @Inject
    public AppleDialogPresenter(Router router) {
        this.router = router;
    }

    public void showApplePopular() {
        goToAppleScreen(0);
    }

    public void showAppleBest() {
        goToAppleScreen(1);
    }

    public void showAppleNew() {
        goToAppleScreen(2);
    }

    private void goToAppleScreen(int index) { router.navigateTo(new Screens.AppleDiscoverScreen(index)); }

    public void goBack() {
        router.exit();
    }
}
