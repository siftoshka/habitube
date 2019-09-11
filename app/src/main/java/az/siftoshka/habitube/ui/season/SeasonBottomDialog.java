package az.siftoshka.habitube.ui.season;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import az.siftoshka.habitube.R;
import az.siftoshka.habitube.entities.show.Season;
import az.siftoshka.habitube.presentation.season.SeasonPresenter;
import az.siftoshka.habitube.presentation.season.SeasonView;
import az.siftoshka.habitube.utils.GlideLoader;
import az.siftoshka.habitube.utils.moxy.MvpBottomSheetDialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.siftoshka.habitube.Constants.DI.APP_SCOPE;

public class SeasonBottomDialog extends MvpBottomSheetDialogFragment implements SeasonView {

    @InjectPresenter
    SeasonPresenter seasonPresenter;

    @BindView(R.id.bottom_dialog_layout) LinearLayout linearLayout;
    @BindView(R.id.poster_season_post) ImageView seasonImage;
    @BindView(R.id.poster_season_name) TextView seasonName;
    @BindView(R.id.poster_season_air_date) TextView seasonDate;
    @BindView(R.id.poster_season_episode) TextView seasonEpisodes;
    @BindView(R.id.poster_season_overview) TextView seasonOverview;

    private Season season;
    private Unbinder unbinder;

    @ProvidePresenter
    SeasonPresenter seasonPresenter() {
       return Toothpick.openScope(APP_SCOPE).getInstance(SeasonPresenter.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetTheme);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            season = bundle.getParcelable("SEASON");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_season_bottom, container, false);
        unbinder = ButterKnife.bind(this, view);
        linearLayout.setClipToOutline(true);
        setDialog();
        return view;
    }

    private void setDialog() {
        GlideLoader.load(getContext(), season.getPosterPath(), seasonImage);
        seasonName.setText(season.getName());
        seasonDate.setText(season.getAirDate());
        seasonEpisodes.setText(String.valueOf(season.getEpisodeCount()));
        seasonOverview.setText(season.getOverview());
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
