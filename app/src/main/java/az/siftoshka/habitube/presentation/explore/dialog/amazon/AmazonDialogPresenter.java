package az.siftoshka.habitube.presentation.explore.dialog.amazon;

import javax.inject.Inject;

import az.siftoshka.habitube.Screens;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class AmazonDialogPresenter extends MvpPresenter<AmazonDialogView> {

    private final Router router;

    @Inject
    public AmazonDialogPresenter(Router router) {
        this.router = router;
    }

    public void showAmazonPopular() {
        goToAmazonScreen(0);
    }

    public void showAmazonBest() {
        goToAmazonScreen(1);
    }

    public void showAmazonNew() {
        goToAmazonScreen(2);
    }

    private void goToAmazonScreen(int index) { router.navigateTo(new Screens.AmazonDiscoverScreen(index)); }

    public void goBack() {
        router.exit();
    }
}
