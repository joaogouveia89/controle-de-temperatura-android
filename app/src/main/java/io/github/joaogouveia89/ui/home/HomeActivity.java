package io.github.joaogouveia89.ui.home;


import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.example.joogouveia.joaogouveia89.R;
import com.example.joogouveia.joaogouveia89.databinding.ActivityHomeBinding;

import io.github.joaogouveia89.ui.base.BaseActivityViewModel;


public class HomeActivity extends BaseActivityViewModel<ActivityHomeBinding, HomeViewModel> {

    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        mViewModel = new HomeViewModel(this, compositeDisposable);
        mBinding.setViewModel(mViewModel);
        init();
    }

    private void init() {
        showDialogProgress();
        mViewModel.init();
    }
}
