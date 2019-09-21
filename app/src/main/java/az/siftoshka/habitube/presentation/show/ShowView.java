package az.siftoshka.habitube.presentation.show;

import java.util.List;

import az.siftoshka.habitube.entities.movielite.MovieLite;
import az.siftoshka.habitube.entities.show.Show;
import az.siftoshka.habitube.entities.video.Video;
import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface ShowView extends MvpView {

    void showTVShow(Show show);

    void showSimilarTVShowList(List<MovieLite> similarShows);
    void showVideos(List<Video> videos);
    void showBottomSeasonDialog(int position);

    void showTVShowScreen();
    void showProgress(boolean loadingState);
    void showErrorScreen();

}
