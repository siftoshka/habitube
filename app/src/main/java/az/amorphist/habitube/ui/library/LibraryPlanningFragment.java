package az.amorphist.habitube.ui.library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import az.amorphist.habitube.R;
import az.amorphist.habitube.presentation.library.LibraryPlanningPresenter;
import az.amorphist.habitube.presentation.library.LibraryPlanningView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.amorphist.habitube.Constants.DI.APP_SCOPE;

public class LibraryPlanningFragment extends MvpAppCompatFragment implements LibraryPlanningView {

    @InjectPresenter LibraryPlanningPresenter planningPresenter;
    private Unbinder unbinder;

    @ProvidePresenter
    LibraryPlanningPresenter planningPresenter() {
        return Toothpick.openScope(APP_SCOPE).getInstance(LibraryPlanningPresenter.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_library_planned, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
