package az.siftoshka.habitube.presentation.star;

import az.siftoshka.habitube.entities.person.Person;
import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface StarView extends MvpView {
    void showPerson(Person person);
    void showProgress(boolean loadingState);
    void showPersonScreen();
    void showErrorScreen();
}
