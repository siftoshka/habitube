package az.siftoshka.habitube.adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import az.siftoshka.habitube.R;
import az.siftoshka.habitube.ui.library.LibraryShowPlanningFragment;
import az.siftoshka.habitube.ui.library.LibraryShowWatchedFragment;

public class LibraryShowPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    public LibraryShowPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new LibraryShowWatchedFragment();
            case 1:
                return new LibraryShowPlanningFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.watched_tab);
            case 1:
                return context.getString(R.string.planning_tab);
            default:
                return null;
        }
    }
}
