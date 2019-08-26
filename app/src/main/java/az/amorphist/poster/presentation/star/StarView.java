package az.amorphist.poster.presentation.star;

import az.amorphist.poster.entities.person.Person;
import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface StarView extends MvpView {
    void getPerson(Person person);
    void showProgress(boolean loadingState);
    void showPersonScreen();
    void showErrorScreen();
}
