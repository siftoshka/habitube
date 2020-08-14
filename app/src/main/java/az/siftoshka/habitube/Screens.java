package az.siftoshka.habitube;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import az.siftoshka.habitube.ui.explore.AppleFragment;
import az.siftoshka.habitube.ui.explore.DiscoverFragment;
import az.siftoshka.habitube.ui.explore.DiscoverNetflixFragment;
import az.siftoshka.habitube.ui.explore.DisneyFragment;
import az.siftoshka.habitube.ui.explore.ExploreFragment;
import az.siftoshka.habitube.ui.explore.GenreFragment;
import az.siftoshka.habitube.ui.library.LibraryFragment;
import az.siftoshka.habitube.ui.movie.MovieFragment;
import az.siftoshka.habitube.ui.navbar.NavbarFragment;
import az.siftoshka.habitube.ui.search.SearchFragment;
import az.siftoshka.habitube.ui.settings.HtmlFragment;
import az.siftoshka.habitube.ui.settings.SettingsFragment;
import az.siftoshka.habitube.ui.show.ShowFragment;
import az.siftoshka.habitube.ui.star.StarFragment;
import ru.terrakok.cicerone.android.support.SupportAppScreen;

public final class Screens {

    public static final class ExploreScreen extends SupportAppScreen {
        @Override
        public Fragment getFragment() {
            return new ExploreFragment();
        }
    }

    public static final class PostMovieScreen extends SupportAppScreen {
        private Integer movieID;

        public PostMovieScreen(Integer movieID) {
            this.movieID = movieID;
        }

        @Override
        public Fragment getFragment() {
            final MovieFragment fragment = new MovieFragment();
            final Bundle bundle = new Bundle();
            bundle.putInt("postPosition", movieID);
            fragment.setArguments(bundle);
            return fragment;
        }
    }

    public static final class PostShowScreen extends SupportAppScreen {
        private Integer tvShowID;

        public PostShowScreen(Integer tvShowID) {
            this.tvShowID = tvShowID;
        }

        @Override
        public Fragment getFragment() {
            final ShowFragment fragment = new ShowFragment();
            final Bundle bundle = new Bundle();
            bundle.putInt("showPosition", tvShowID);
            fragment.setArguments(bundle);
            return fragment;
        }
    }

    public static final class PostUpcomingScreen extends SupportAppScreen {
        private Integer upcomingID;

        public PostUpcomingScreen(Integer tvShowID) {
            this.upcomingID = tvShowID;
        }

        @Override
        public Fragment getFragment() {
            final MovieFragment fragment = new MovieFragment();
            final Bundle bundle = new Bundle();
            bundle.putInt("upcomingPosition", upcomingID);
            fragment.setArguments(bundle);
            return fragment;
        }
    }

    public static final class SearchItemScreen extends SupportAppScreen {
        private Integer postId, mediaType;
        final Bundle bundle = new Bundle();
        final MovieFragment movieFragment = new MovieFragment();
        final ShowFragment showFragment = new ShowFragment();
        final StarFragment starFragment = new StarFragment();

        public SearchItemScreen(int postId, int mediaType) {
            this.postId = postId;
            this.mediaType = mediaType;
        }

        @Override
        public Fragment getFragment() {
            bundle.putInt("postId", postId);
            bundle.putInt("mediaType", mediaType);
            switch (mediaType) {
                case 1:
                    movieFragment.setArguments(bundle);
                    return movieFragment;
                case 2:
                    showFragment.setArguments(bundle);
                    return showFragment;
                case 3:
                    starFragment.setArguments(bundle);
                    return starFragment;
            }
            return getFragment();
        }
    }

    public static final class DiscoverScreen extends SupportAppScreen {
        private String sortSelection, yearIndexUp, yearIndexDown;
        private int voteIndexUp, voteIndexDown;

        public DiscoverScreen(String sortSelection, String yearIndexUp, String yearIndexDown, int voteIndexUp, int voteIndexDown) {
            this.sortSelection = sortSelection;
            this.yearIndexUp = yearIndexUp;
            this.yearIndexDown = yearIndexDown;
            this.voteIndexDown = voteIndexDown;
            this.voteIndexUp = voteIndexUp;
        }

        @Override
        public Fragment getFragment() {
            final Bundle bundle = new Bundle();
            final DiscoverFragment discoverFragment = new DiscoverFragment();
            bundle.putString("Discover-Ms", sortSelection);
            bundle.putString("Discover-MyUp", yearIndexUp);
            bundle.putString("Discover-MyDown", yearIndexDown);
            bundle.putInt("Discover-MvUp", voteIndexUp);
            bundle.putInt("Discover-MvDown", voteIndexDown);
            discoverFragment.setArguments(bundle);
            return discoverFragment;
        }
    }

    public static final class GenreScreen extends SupportAppScreen {
        private String yearIndexUp, yearIndexDown, genreId;
        private int voteIndexUp, voteIndexDown;

        public GenreScreen(String yearIndexUp, String yearIndexDown, int voteIndexUp, int voteIndexDown, String genreId) {
            this.yearIndexUp = yearIndexUp;
            this.yearIndexDown = yearIndexDown;
            this.voteIndexDown = voteIndexDown;
            this.voteIndexUp = voteIndexUp;
            this.genreId = genreId;
        }

        @Override
        public Fragment getFragment() {
            final Bundle bundle = new Bundle();
            final GenreFragment genreFragment = new GenreFragment();
            bundle.putString("Selection", "M");
            bundle.putString("Discover-Mg", genreId);
            bundle.putString("Discover-MyUp", yearIndexUp);
            bundle.putString("Discover-MyDown", yearIndexDown);
            bundle.putInt("Discover-MvUp", voteIndexUp);
            bundle.putInt("Discover-MvDown", voteIndexDown);
            genreFragment.setArguments(bundle);
            return genreFragment;
        }
    }

    public static final class DiscoverShowScreen extends SupportAppScreen {
        private String yearIndexUp, yearIndexDown;
        private int voteIndexUp, voteIndexDown;

        public DiscoverShowScreen(String yearIndexUp, String yearIndexDown, int voteIndexUp, int voteIndexDown) {
            this.yearIndexUp = yearIndexUp;
            this.yearIndexDown = yearIndexDown;
            this.voteIndexDown = voteIndexDown;
            this.voteIndexUp = voteIndexUp;
        }

        @Override
        public Fragment getFragment() {
            final Bundle bundle = new Bundle();
            final DiscoverFragment discoverFragment = new DiscoverFragment();
            bundle.putString("Discover-SyUp", yearIndexUp);
            bundle.putString("Discover-SyDown", yearIndexDown);
            bundle.putInt("Discover-SvUp", voteIndexUp);
            bundle.putInt("Discover-SvDown", voteIndexDown);
            discoverFragment.setArguments(bundle);
            return discoverFragment;
        }
    }

    public static final class GenreShowScreen extends SupportAppScreen {
        private String yearIndexUp, yearIndexDown, genreId;
        private int voteIndexUp, voteIndexDown;

        public GenreShowScreen(String yearIndexUp, String yearIndexDown, int voteIndexUp, int voteIndexDown, String genreId) {
            this.yearIndexUp = yearIndexUp;
            this.yearIndexDown = yearIndexDown;
            this.voteIndexDown = voteIndexDown;
            this.voteIndexUp = voteIndexUp;
            this.genreId = genreId;
        }

        @Override
        public Fragment getFragment() {
            final Bundle bundle = new Bundle();
            final GenreFragment genreFragment = new GenreFragment();
            bundle.putString("Discover-Sg", genreId);
            bundle.putString("Discover-SyUp", yearIndexUp);
            bundle.putString("Discover-SyDown", yearIndexDown);
            bundle.putInt("Discover-SvUp", voteIndexUp);
            bundle.putInt("Discover-SvDown", voteIndexDown);
            genreFragment.setArguments(bundle);
            return genreFragment;
        }
    }

    public static final class NetflixDiscoverScreen extends SupportAppScreen {
        private int index;

        public NetflixDiscoverScreen(int index) {
            this.index = index;
        }

        @Override
        public Fragment getFragment() {
            final Bundle bundle = new Bundle();
            final DiscoverNetflixFragment discoverFragment = new DiscoverNetflixFragment();
            if (index == 0) bundle.putInt("Discover-N", 0);
            if (index == 1) bundle.putInt("Discover-N", 1);
            if (index == 2) bundle.putInt("Discover-N", 2);
            discoverFragment.setArguments(bundle);
            return discoverFragment;
        }
    }

    public static final class AppleDiscoverScreen extends SupportAppScreen {
        private int index;

        public AppleDiscoverScreen(int index) {
            this.index = index;
        }

        @Override
        public Fragment getFragment() {
            final Bundle bundle = new Bundle();
            final AppleFragment discoverFragment = new AppleFragment();
            if (index == 0) bundle.putInt("Discover-A", 0);
            if (index == 1) bundle.putInt("Discover-A", 1);
            if (index == 2) bundle.putInt("Discover-A", 2);
            discoverFragment.setArguments(bundle);
            return discoverFragment;
        }
    }

    public static final class DisneyDiscoverScreen extends SupportAppScreen {
        private int index;

        public DisneyDiscoverScreen(int index) {
            this.index = index;
        }

        @Override
        public Fragment getFragment() {
            final Bundle bundle = new Bundle();
            final DisneyFragment discoverFragment = new DisneyFragment();
            if (index == 0) bundle.putInt("Discover-D", 0);
            if (index == 1) bundle.putInt("Discover-D", 1);
            if (index == 2) bundle.putInt("Discover-D", 2);
            discoverFragment.setArguments(bundle);
            return discoverFragment;
        }
    }

    public static final class SearchScreen extends SupportAppScreen {
        @Override
        public Fragment getFragment() {
            return new SearchFragment();
        }
    }

    public static final class AccountScreen extends SupportAppScreen {

        @Override
        public Fragment getFragment() {
            return new SettingsFragment();
        }
    }

    public static final class LibraryScreen extends SupportAppScreen {

        @Override
        public Fragment getFragment() {
            return new LibraryFragment();
        }
    }

    public static final class NavbarScreen extends SupportAppScreen {

        @Override
        public Fragment getFragment() {
            return new NavbarFragment();
        }
    }

    public static class WebFragmentScreen extends SupportAppScreen {
        private int index;

        public WebFragmentScreen(int index) {
            this.index = index;
        }

        @Override
        public Fragment getFragment() {
            final Bundle bundle = new Bundle();
            final HtmlFragment htmlFragment = new HtmlFragment();
            if (index == 0) bundle.putInt("Web", 0);
            if (index == 1) bundle.putInt("Web", 1);
            if (index == 2) bundle.putInt("Web", 2);
            htmlFragment.setArguments(bundle);
            return htmlFragment;
        }
    }
}
