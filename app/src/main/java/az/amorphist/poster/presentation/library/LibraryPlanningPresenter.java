package az.amorphist.poster.presentation.library;

import javax.inject.Inject;

import moxy.InjectViewState;
import moxy.MvpPresenter;

@InjectViewState
public class LibraryPlanningPresenter extends MvpPresenter<LibraryPlanningView> {

    @Inject
    public LibraryPlanningPresenter() {
    }
}
