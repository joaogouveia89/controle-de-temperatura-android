package io.github.joaogouveia89.ui.base;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import io.github.joaogouveia89.interfaces.CallbackBasicViewModel;

public class BaseDialogFragment extends DialogFragment implements CallbackBasicViewModel {


    @Override
    public void showError(Throwable t) {

    }


    @Override
    public void finish(int resultCode) {

    }

    @Override
    public FragmentManager getDialogFragmentManager() {
        return getFragmentManager();
    }
}
