package az.amorphist.poster.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import az.amorphist.poster.R;
import az.amorphist.poster.presentation.library.LibraryPresenter;
import az.amorphist.poster.presentation.library.LibraryView;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.amorphist.poster.di.DI.APP_SCOPE;

public class LibraryFragment extends MvpAppCompatFragment implements LibraryView, Toolbar.OnMenuItemClickListener {

    @InjectPresenter LibraryPresenter libraryPresenter;

    private Toolbar toolbar;

    @ProvidePresenter
    LibraryPresenter libraryPresenter() {
        return Toothpick.openScope(APP_SCOPE).getInstance(LibraryPresenter.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        toolbar = view.findViewById(R.id.library_toolbar);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        toolbar.inflateMenu(R.menu.main_menu);
        toolbar.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.search_movies) {
            libraryPresenter.goToSearchScreen();
        }
        return false;
    }
}
