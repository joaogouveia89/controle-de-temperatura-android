package io.github.joaogouveia89.ui.base;

import io.github.joaogouveia89.interfaces.CallbackBasicViewModel;
import io.reactivex.disposables.CompositeDisposable;

public class BaseViewModel implements ViewModel {

    protected CallbackBasicViewModel callback;
    protected CompositeDisposable compositeDisposable;

    public BaseViewModel(CallbackBasicViewModel callback, CompositeDisposable compositeDisposable) {
        this.callback = callback;
        compositeDisposable = new CompositeDisposable();
        this.compositeDisposable = compositeDisposable;
    }

    protected void showProgress() {
        if (callback != null) callback.showDialogProgress();
    }

    protected void hideProgress() {
        if (callback != null) callback.hideDialogProgress();
    }

    protected void showError(Throwable t) {
        hideProgress();
        if (callback != null) callback.showError(t);
    }

    protected void finish(int resultCode) {
        if (callback != null) callback.finish(resultCode);
    }

    @Override
    public void resume() {

    }

    @Override
    public void destroy() {

    }
}
