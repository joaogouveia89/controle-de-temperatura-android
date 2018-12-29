package io.github.joaogouveia89.interfaces;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

public interface CallbackBasicViewModel {

    void showError(Throwable t);

    void finish(int resultCode);

    FragmentManager getDialogFragmentManager();

    String getString(int resString);
}
