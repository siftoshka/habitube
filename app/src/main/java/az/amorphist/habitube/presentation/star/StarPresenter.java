package az.amorphist.habitube.presentation.star;

import javax.inject.Inject;

import az.amorphist.habitube.di.qualifiers.PostId;
import az.amorphist.habitube.model.interactor.RemotePostInteractor;
import io.reactivex.disposables.CompositeDisposable;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class StarPresenter extends MvpPresenter<StarView> {

    private final Router router;
    private final Integer postId;
    private final RemotePostInteractor remotePostInteractor;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public StarPresenter(Router router, RemotePostInteractor remotePostInteractor, @PostId Integer postId) {
        this.router = router;
        this.remotePostInteractor = remotePostInteractor;
        this.postId = postId;
    }

    @Override
    protected void onFirstViewAttach() {
        getMovieStar();
    }

    private void getMovieStar() {
        compositeDisposable.add(remotePostInteractor.getStar(postId)
                .doOnSubscribe(disposable -> getViewState().showProgress(true))
                .doAfterTerminate(() -> getViewState().showProgress(false))
                .doAfterSuccess(person -> getViewState().showPersonScreen())
                .subscribe(person -> getViewState().showPerson( person),
                        throwable -> getViewState().showErrorScreen()));
    }

    public void goBack() {
        router.exit();
    }

    @Override
    public void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }
}
