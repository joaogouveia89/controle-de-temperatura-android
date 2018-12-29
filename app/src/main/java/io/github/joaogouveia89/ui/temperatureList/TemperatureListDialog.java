package io.github.joaogouveia89.ui.temperatureList;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.joogouveia.joaogouveia89.R;
import com.example.joogouveia.joaogouveia89.databinding.DialogTemperaturesListBinding;

import org.parceler.Parcels;

import java.util.List;

import io.github.joaogouveia89.api.model.Temperature;
import io.github.joaogouveia89.ui.base.BaseDialogFragmentViewModel;

public class TemperatureListDialog extends BaseDialogFragmentViewModel<DialogTemperaturesListBinding, TemperatureListViewModel> {

    public TemperatureListDialog(){}

    public static TemperatureListDialog newInstance(){
        //TODO: If is necessary to pass any arguments to the dialog
//        Bundle args = new Bundle();
//        args.putParcelable(ARGS_TEMPERATURES_LIST, Parcels.wrap(temperatures));
//        temperatureListDialog.setArguments(args);
        return new TemperatureListDialog();
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_temperatures_list, container, false);
        mViewModel = new TemperatureListViewModel(this, compositeDisposable);
        mBinding.setViewModel(mViewModel);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel.fetchTemperatures();
    }

    @Override
    public void showError(Throwable t) {
        Log.e("TemperatureListDialog", t.getMessage());
    }
}
