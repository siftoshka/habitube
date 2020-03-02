package az.siftoshka.habitube.ui.library.dialogs;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;

import az.siftoshka.habitube.R;
import az.siftoshka.habitube.entities.movie.Movie;
import az.siftoshka.habitube.utils.CurrencyFormatter;
import az.siftoshka.habitube.utils.DateChanger;
import az.siftoshka.habitube.utils.ImageLoader;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class OfflineCardDialog extends DialogFragment {

    @BindView(R.id.poster_movie_post) ImageView posterMain;
    @BindView(R.id.poster_title) TextView posterTitle;
    @BindView(R.id.poster_date) TextView posterDate;
    @BindView(R.id.poster_rate) TextView posterRate;
    @BindView(R.id.poster_views) TextView posterViews;
    @BindView(R.id.poster_duration) TextView posterDuration;
    @BindView(R.id.poster_budget) TextView posterBudget;
    @BindView(R.id.poster_revenue) TextView posterRevenue;
    @BindView(R.id.poster_desc) TextView posterDesc;
    @BindView(R.id.close_button) MaterialButton closeButton;

    private Movie movie;
    private Unbinder unbinder;
    private DateChanger dateChanger = new DateChanger();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            movie = bundle.getParcelable("Movies");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_offline, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        setDialog(view);

        closeButton.setOnClickListener(view1 -> dismiss());
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void setDialog(View view) {
        ImageLoader.loadLocally(view, movie.getPosterImage(), posterMain);
        posterTitle.setText(movie.getTitle());
        posterDate.setText(dateChanger.changeDate(movie.getReleaseDate()));
        posterRate.setText(String.valueOf(movie.getVoteAverage()));
        posterViews.setText("(" + movie.getVoteCount() + ")");
        posterDuration.setText(movie.getRuntime() + " " + getResources().getString(R.string.minutes));
        posterBudget.setText("$" + CurrencyFormatter.format(movie.getBudget()));
        posterRevenue.setText("$" + CurrencyFormatter.format(movie.getRevenue()));
        posterDesc.setText(movie.getOverview());
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
