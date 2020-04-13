package az.siftoshka.habitube.ui.explore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.xw.repo.BubbleSeekBar;

import az.siftoshka.habitube.Constants;
import az.siftoshka.habitube.R;
import az.siftoshka.habitube.presentation.explore.DiscoverDialogPresenter;
import az.siftoshka.habitube.presentation.explore.DiscoverDialogView;
import az.siftoshka.habitube.utils.moxy.MvpBottomSheetDialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class DiscoverDialog extends MvpBottomSheetDialogFragment implements DiscoverDialogView {

    @InjectPresenter DiscoverDialogPresenter discoverPresenter;

    @BindView(R.id.bottom_dialog_layout) LinearLayout linearLayout;
    @Nullable @BindView(R.id.sort_buttons) MaterialButtonToggleGroup toggleGroup;
    @Nullable @BindView(R.id.popularity) MaterialButton sortPopularity;
    @Nullable @BindView(R.id.revenue) MaterialButton sortRevenue;
    @BindView(R.id.slider_year) BubbleSeekBar sliderYear;
    @BindView(R.id.slider_vote) BubbleSeekBar sliderVote;
    @BindView(R.id.text_year) TextView textYear;
    @BindView(R.id.text_vote) TextView textVote;
    @BindView(R.id.discover) MaterialButton discoverButton;

    private Unbinder unbinder;
    private short index;
    private int voteIndex;
    private String yearIndex;
    private String sortSelection;

    @ProvidePresenter
    DiscoverDialogPresenter discoverPresenter() {
        return Toothpick.openScope(Constants.DI.APP_SCOPE).getInstance(DiscoverDialogPresenter.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetTheme);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            if (bundle.getInt("DISCOVER") == 0)
                index = 0;
            else if (bundle.getInt("DISCOVER") == 1)
                index = 1;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        if (index == 0) view = inflater.inflate(R.layout.dialog_discover, container, false);
        else if (index == 1) view = inflater.inflate(R.layout.dialog_show_discover, container, false);
        unbinder = ButterKnife.bind(this, view);
        linearLayout.setClipToOutline(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setDialog(index);
    }

    private void setDialog(int index) {
        if (index == 0) {
            defaultSorting();
            sortPopularity.addOnCheckedChangeListener((button, isChecked) -> {
                if (isChecked) {
                    button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    button.setTextColor(getResources().getColor(R.color.white_is_white));
                    sortRevenue.setBackgroundColor(getResources().getColor(R.color.background));
                    sortRevenue.setTextColor(getResources().getColor(R.color.dark_800));
                    sortSelection = "popularity.desc";
                }
            });
            sortRevenue.addOnCheckedChangeListener((button, isChecked) -> {
                if (isChecked) {
                    button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    button.setTextColor(getResources().getColor(R.color.white_is_white));
                    sortPopularity.setBackgroundColor(getResources().getColor(R.color.background));
                    sortPopularity.setTextColor(getResources().getColor(R.color.dark_800));
                    sortSelection = "revenue.desc";
                }
            });
            sliderYear.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
                @Override
                public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                    textYear.setText(String.valueOf(progress));
                    yearIndex = String.valueOf(progress);
                }

                @Override
                public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) { }

                @Override
                public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) { }
            });
            sliderVote.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
                @Override
                public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                    textVote.setText(String.valueOf(progress));
                    voteIndex = progress;
                }

                @Override
                public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) { }

                @Override
                public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) { }
            });

            discoverButton.setOnClickListener(view -> discoverPresenter.discoverMovies(sortSelection, yearIndex + "-01-01", voteIndex));
        }
        if (index == 1) {
            defaultShowSorting();
            sliderYear.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
                @Override
                public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                    textYear.setText(String.valueOf(progress));
                    yearIndex = String.valueOf(progress);
                }

                @Override
                public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                }

                @Override
                public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                }
            });
            sliderVote.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
                @Override
                public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                    textVote.setText(String.valueOf(progress));
                    voteIndex = progress;
                }

                @Override
                public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                }

                @Override
                public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                }
            });

            discoverButton.setOnClickListener(view -> discoverPresenter.discoverShows(yearIndex + "-01-01", voteIndex));
        }
    }

    private void defaultSorting() {
        sortPopularity.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        sortPopularity.setTextColor(getResources().getColor(R.color.white_is_white));
        sortRevenue.setBackgroundColor(getResources().getColor(R.color.background));
        sortRevenue.setTextColor(getResources().getColor(R.color.dark_800));
        sortSelection = "popularity.desc";
        textYear.setText(String.valueOf(sliderYear.getProgress()));
        textVote.setText(String.valueOf(sliderVote.getProgress()));
        yearIndex = String.valueOf(sliderYear.getProgress());
        voteIndex = sliderVote.getProgress();
    }

    private void defaultShowSorting() {
        textYear.setText(String.valueOf(sliderYear.getProgress()));
        textVote.setText(String.valueOf(sliderVote.getProgress()));
        yearIndex = String.valueOf(sliderYear.getProgress());
        voteIndex = sliderVote.getProgress();
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
