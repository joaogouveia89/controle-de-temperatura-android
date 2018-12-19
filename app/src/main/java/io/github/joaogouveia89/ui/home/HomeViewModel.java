package io.github.joaogouveia89.ui.home;

import android.databinding.ObservableField;
import android.util.Log;

import io.github.joaogouveia89.api.RetrofitBase;
import io.github.joaogouveia89.interfaces.CallbackBasicViewModel;
import io.github.joaogouveia89.ui.base.BaseViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends BaseViewModel {

    public ObservableField<String> lastTemperature = new ObservableField<>();
    public ObservableField<String> lastTemperatureTimestamp = new ObservableField<>();

    public HomeViewModel(CallbackBasicViewModel callback) {
        super(callback);
    }

    public void init() {
        fetchLastTemperature();
    }

    private void fetchLastTemperature() {
        RetrofitBase
                .getInterfaceRetrofit()
                .getLastTemperature()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    Log.i("DEUUUU", response.getTemperature());
                    lastTemperature.set(response.getTemperature());
                    lastTemperatureTimestamp.set(response.getDate() +  " " + response.getHour());
                },this::handleError);

    }

    private void handleError(Throwable t){
        Log.i("ERRORRRR", t.getMessage());
    }
}
