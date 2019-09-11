package az.siftoshka.habitube.ui.library;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import javax.inject.Inject;

import az.siftoshka.habitube.R;
import az.siftoshka.habitube.adapters.LibraryPagerAdapter;
import az.siftoshka.habitube.presentation.library.LibraryPresenter;
import az.siftoshka.habitube.presentation.library.LibraryView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.siftoshka.habitube.Constants.DI.APP_SCOPE;

public class LibraryFragment extends MvpAppCompatFragment implements LibraryView, Toolbar.OnMenuItemClickListener {

    @InjectPresenter
    LibraryPresenter libraryPresenter;

    @Inject Context context;
    @BindView(R.id.library_toolbar) Toolbar toolbar;
    @BindView(R.id.library_tab) TabLayout tabLayout;
    @BindView(R.id.library_pager) ViewPager viewPager;

    private Unbinder unbinder;

    @ProvidePresenter
    LibraryPresenter libraryPresenter() {
        return Toothpick.openScope(APP_SCOPE).getInstance(LibraryPresenter.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toothpick.inject(this, Toothpick.openScope(APP_SCOPE));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_library, container, false);
        unbinder = ButterKnife.bind(this, view);
        FragmentPagerAdapter pagerAdapter = new LibraryPagerAdapter(context, getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
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

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
