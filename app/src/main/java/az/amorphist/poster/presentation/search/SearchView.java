package az.amorphist.poster.presentation.search;

import java.util.List;

import az.amorphist.poster.entities.movielite.MovieLite;
import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.OneExecutionStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface SearchView extends MvpView {
    void getSearchedMediaList(List<MovieLite> searchResult);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void unsuccessfulQueryError();
}
