package az.amorphist.habitube.presentation.search;

import java.util.List;

import az.amorphist.habitube.entities.movielite.MovieLite;
import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.OneExecutionStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface SearchView extends MvpView {
    void showSearchedMediaList(List<MovieLite> searchResult);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void unsuccessfulQueryError();
}
