package az.siftoshka.habitube.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.List;

import az.siftoshka.habitube.Constants;
import az.siftoshka.habitube.R;
import az.siftoshka.habitube.adapters.DiscoverAdapter;
import az.siftoshka.habitube.entities.movielite.MovieLite;
import az.siftoshka.habitube.presentation.explore.DiscoverPresenter;
import az.siftoshka.habitube.presentation.explore.DiscoverView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class HtmlFragment extends Fragment {

    @BindView(R.id.web) WebView webView;

    private Unbinder unbinder;
    private int index;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            if (bundle.getInt("Web") == 0) {
                index = 0;
            }
            else if (bundle.getInt("Web") == 1) {
                index = 1;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_web, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        showWeb(index);
    }

    private void showWeb(int index) {
        if (index == 0)
            webView.loadUrl("file:///android_asset/privacy_policy.html");
        else if (index == 1)
            webView.loadUrl("file:///android_asset/terms_of_service.html");
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
