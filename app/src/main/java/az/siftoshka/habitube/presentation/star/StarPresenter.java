package az.siftoshka.habitube.presentation.star;

import android.content.Context;

import javax.inject.Inject;

import az.siftoshka.habitube.R;
import az.siftoshka.habitube.di.qualifiers.PostId;
import az.siftoshka.habitube.model.interactor.RemotePostInteractor;
import io.reactivex.disposables.CompositeDisposable;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class StarPresenter extends MvpPresenter<StarView> {

    private final Router router;
    private final Context context;
    private final Integer postId;
    private final RemotePostInteractor remotePostInteractor;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public StarPresenter(Router router, Context context,
                         RemotePostInteractor remotePostInteractor, @PostId Integer postId) {
        this.router = router;
        this.context = context;
        this.remotePostInteractor = remotePostInteractor;
        this.postId = postId;
    }

    @Override
    protected void onFirstViewAttach() {
        getMovieStar(context.getResources().getString(R.string.language));
    }

    private void getMovieStar(String language) {
        compositeDisposable.add(remotePostInteractor.getStar(postId, language)
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
