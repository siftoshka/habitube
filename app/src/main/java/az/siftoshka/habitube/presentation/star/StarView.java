package az.siftoshka.habitube.presentation.star;

import java.util.List;

import az.siftoshka.habitube.entities.person.Person;
import az.siftoshka.habitube.entities.personcredits.Cast;
import az.siftoshka.habitube.entities.personcredits.Crew;
import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface StarView extends MvpView {

    void showPerson(Person person);
    void showProgress(boolean loadingState);

    void showMovieCrew(List<Crew> crews);
    void showMovieCast(List<Cast> casts);
    void showTVShowCrew(List<Crew> crews);
    void showTVShowCast(List<Cast> casts);

    void showMovieCastExpandButton(List<Cast> casts);
    void showMovieCrewExpandButton(List<Crew> crews);
    void showTVShowCastExpandButton(List<Cast> casts);
    void showTVShowCrewExpandButton(List<Crew> crews);
    void showPersonScreen();
    void showErrorScreen();
}
