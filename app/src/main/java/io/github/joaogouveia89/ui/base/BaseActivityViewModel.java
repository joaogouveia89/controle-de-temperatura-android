package io.github.joaogouveia89.ui.base;

import android.databinding.ViewDataBinding;

import io.reactivex.disposables.CompositeDisposable;

public class BaseActivityViewModel<T extends ViewDataBinding, V extends ViewModel> extends BaseActivity {

protected T mBinding;
protected V mViewModel;
protected CompositeDisposable compositeDisposable;

    @Override
    protected void onDestroy() {
        if (mViewModel != null){
            mViewModel.destroy();
            mViewModel = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        if(compositeDisposable != null)
            compositeDisposable.dispose();
        super.onStop();
    }
}
