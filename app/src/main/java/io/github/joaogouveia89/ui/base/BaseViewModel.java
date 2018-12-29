package io.github.joaogouveia89.ui.base;

import android.support.v4.app.DialogFragment;

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

    protected void showDialogFragment(DialogFragment dialogFragment) {
        dialogFragment.show(callback.getDialogFragmentManager(), "dialog");
    }

    protected void showError(Throwable t) {
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
