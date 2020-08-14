package az.siftoshka.habitube.presentation.explore.dialog.genres;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface GenresDialogView extends MvpView {

}
