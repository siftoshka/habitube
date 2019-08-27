package az.amorphist.poster.adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import az.amorphist.poster.R;
import az.amorphist.poster.ui.library.LibraryPlanningFragment;
import az.amorphist.poster.ui.library.LibraryWatchedFragment;

public class LibraryPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    public LibraryPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new LibraryWatchedFragment();
            case 1:
                return new LibraryPlanningFragment();
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
