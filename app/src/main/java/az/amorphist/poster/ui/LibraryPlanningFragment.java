package az.amorphist.poster.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import az.amorphist.poster.R;
import az.amorphist.poster.presentation.library.LibraryPlanningPresenter;
import az.amorphist.poster.presentation.library.LibraryPlanningView;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.amorphist.poster.di.DI.APP_SCOPE;

public class LibraryPlanningFragment extends MvpAppCompatFragment implements LibraryPlanningView {

    @InjectPresenter LibraryPlanningPresenter planningPresenter;

    @ProvidePresenter
    LibraryPlanningPresenter planningPresenter() {
        return Toothpick.openScope(APP_SCOPE).getInstance(LibraryPlanningPresenter.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_library_planned, container, false);
    }

}
