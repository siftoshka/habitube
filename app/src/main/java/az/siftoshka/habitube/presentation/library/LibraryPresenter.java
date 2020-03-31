package az.siftoshka.habitube.presentation.library;

import javax.inject.Inject;

import moxy.InjectViewState;
import moxy.MvpPresenter;

@InjectViewState
public class LibraryPresenter extends MvpPresenter<LibraryView> {

    @Inject
    public LibraryPresenter() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
