package io.github.joaogouveia89.ui.home;


import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.example.joogouveia.joaogouveia89.R;
import com.example.joogouveia.joaogouveia89.databinding.ActivityHomeBinding;

import io.github.joaogouveia89.ui.base.BaseActivityViewModel;


public class HomeActivity extends BaseActivityViewModel<ActivityHomeBinding, HomeViewModel> {

    private static final String TAG = "HomeActivity";
    public static final String API_TOKEN = "u^[Y]e^v^KeQ]TV";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        mViewModel = new HomeViewModel(this);
    }

}
