package io.github.joaogouveia89.ui.base;

import android.content.DialogInterface;
import android.databinding.ViewDataBinding;

import io.reactivex.disposables.CompositeDisposable;

public class BaseDialogFragmentViewModel <T extends ViewDataBinding, V extends ViewModel> extends BaseDialogFragment {
    protected T mBinding;
    protected V mViewModel;
    protected CompositeDisposable compositeDisposable;

    @Override
    public void onDismiss(DialogInterface dialog) {
        if(compositeDisposable != null)
            compositeDisposable.dispose();
        if (mViewModel != null){
            mViewModel.destroy();
            mViewModel = null;
        }
        super.onDismiss(dialog);
    }
}
