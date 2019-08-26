package az.amorphist.poster;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import az.amorphist.poster.ui.AccountFragment;
import az.amorphist.poster.ui.ExploreFragment;
import az.amorphist.poster.ui.LibraryFragment;
import az.amorphist.poster.ui.MovieFragment;
import az.amorphist.poster.ui.NavbarFragment;
import az.amorphist.poster.ui.SearchFragment;
import az.amorphist.poster.ui.ShowFragment;
import az.amorphist.poster.ui.StarFragment;
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

    public static final class SearchdItemScreen extends SupportAppScreen {
        private Integer postId, mediaType;
        final Bundle bundle = new Bundle();
        final MovieFragment movieFragment = new MovieFragment();
        final ShowFragment showFragment = new ShowFragment();
        final StarFragment starFragment = new StarFragment();

        public SearchdItemScreen(int postId, int mediaType) {
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

    public static final class SearchScreen extends SupportAppScreen {
        @Override
        public Fragment getFragment() {
            return new SearchFragment();
        }
    }

    public static final class AccountScreen extends SupportAppScreen {

        @Override
        public Fragment getFragment() {
            return new AccountFragment();
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

}
