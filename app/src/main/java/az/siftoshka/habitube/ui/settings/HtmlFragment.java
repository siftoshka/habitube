package az.siftoshka.habitube.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import az.siftoshka.habitube.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

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
            } else if (bundle.getInt("Web") == 1) {
                index = 1;
            } else if (bundle.getInt("Web") == 2) {
                index = 2;
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
        else if (index == 2)
            webView.loadUrl("file:///android_asset/licenses_output.html");
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
