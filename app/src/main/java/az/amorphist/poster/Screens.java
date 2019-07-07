package az.amorphist.poster;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import az.amorphist.poster.ui.MainListFragment;
import az.amorphist.poster.ui.PostFragment;
import ru.terrakok.cicerone.android.support.SupportAppScreen;

public final class Screens {

    public static final class MainListScreen extends SupportAppScreen {
        @Override
        public Fragment getFragment() {
            return new MainListFragment();
        }
    }

    public static final class PostScreen extends SupportAppScreen {
        private final String id;

        public PostScreen(String id) {
            this.id = id;
        }

        @Override
        public Fragment getFragment() {
            final PostFragment fragment = new PostFragment();
            final Bundle bundle = new Bundle();
            bundle.putString("postID", id);
            fragment.setArguments(bundle);
            return fragment;
        }
    }
}
