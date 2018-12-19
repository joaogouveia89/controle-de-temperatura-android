package io.github.joaogouveia89.ui.base;

import io.github.joaogouveia89.interfaces.CallbackBasicViewModel;

public class BaseViewModel implements ViewModel {

    protected CallbackBasicViewModel callback;

    public BaseViewModel(CallbackBasicViewModel callback) {
        this.callback = callback;
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
