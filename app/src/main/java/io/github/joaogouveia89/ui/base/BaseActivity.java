package io.github.joaogouveia89.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;

import io.github.joaogouveia89.interfaces.CallbackBasicViewModel;

public class BaseActivity  extends AppCompatActivity implements CallbackBasicViewModel {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void showDialogProgress() {

    }

    @Override
    public void hideDialogProgress() {

    }

    @Override
    public void showError(Throwable t) {

    }

    @Override
    public void showDialogFragment(DialogFragment dialogFragment) {

    }

    @Override
    public void finish(int resultCode) {

    }

    @Override
    public void showSimpleDialog(int msg) {

    }
}