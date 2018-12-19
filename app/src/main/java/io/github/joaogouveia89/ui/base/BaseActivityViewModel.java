package io.github.joaogouveia89.ui.base;

import android.databinding.ViewDataBinding;

public class BaseActivityViewModel<T extends ViewDataBinding, V extends ViewModel> extends BaseActivity {

protected T mBinding;
protected V mViewModel;


@Override
protected void onDestroy() {
        if (mViewModel != null){
        mViewModel.destroy();
        mViewModel = null;
        }
        super.onDestroy();
        }
}
