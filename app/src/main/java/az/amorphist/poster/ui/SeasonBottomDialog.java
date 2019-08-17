package az.amorphist.poster.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import az.amorphist.poster.R;
import az.amorphist.poster.entities.show.Season;
import az.amorphist.poster.presentation.season.SeasonPresenter;
import az.amorphist.poster.presentation.season.SeasonView;
import az.amorphist.poster.utils.GlideLoader;
import az.amorphist.poster.utils.moxy.MvpBottomSheetDialogFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.amorphist.poster.Constants.DI.APP_SCOPE;

public class SeasonBottomDialog extends MvpBottomSheetDialogFragment implements SeasonView {

    @InjectPresenter SeasonPresenter seasonPresenter;

    private Season season;
    private LinearLayout linearLayout;
    private ImageView seasonImage;
    private TextView seasonName, seasonDate, seasonEpisodes, seasonOverview;

    @ProvidePresenter
    SeasonPresenter seasonPresenter() {
       return Toothpick.openScope(APP_SCOPE).getInstance(SeasonPresenter.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            season = bundle.getParcelable("SEASON");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_season_bottom, container, false);
        seasonImage = view.findViewById(R.id.poster_season_post);
        seasonName = view.findViewById(R.id.poster_season_name);
        seasonDate = view.findViewById(R.id.poster_season_air_date);
        seasonEpisodes = view.findViewById(R.id.poster_season_episode);
        seasonOverview = view.findViewById(R.id.poster_season_overview);
        linearLayout = view.findViewById(R.id.bottom_dialog_layout);
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
}
