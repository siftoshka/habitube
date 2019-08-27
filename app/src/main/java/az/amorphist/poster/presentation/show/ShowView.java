package az.amorphist.poster.presentation.show;

import java.util.List;

import az.amorphist.poster.entities.movielite.MovieLite;
import az.amorphist.poster.entities.show.Show;
import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface ShowView extends MvpView {

    void showTVShow(Show show);

    void showSimilarTVShowList(List<MovieLite> similarShows);

    void showBottomSeasonDialog(int position);

    void showTVShowScreen();
    void showProgress(boolean loadingState);
    void showErrorScreen();

}
