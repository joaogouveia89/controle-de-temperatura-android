package io.github.joaogouveia89.interfaces;

import android.support.v4.app.DialogFragment;

public interface CallbackBasicViewModel {
    void showDialogProgress();

    void hideDialogProgress();

    void showError(Throwable t);

    void showDialogFragment(DialogFragment dialogFragment);

    void finish(int resultCode);

    void showSimpleDialog(int msg);

    String getString(int resString);
}
