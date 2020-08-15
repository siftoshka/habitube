package az.siftoshka.habitube.presentation.explore;

import java.util.List;

import az.siftoshka.habitube.entities.movielite.MovieLite;
import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface AmazonView extends MvpView {

    void showMedia(List<MovieLite> media);
    void showMoreMedia(List<MovieLite> media);
}
