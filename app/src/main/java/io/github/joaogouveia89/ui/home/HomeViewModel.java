package io.github.joaogouveia89.ui.home;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.util.Log;
import android.view.View;

import io.github.joaogouveia89.api.RetrofitBase;
import io.github.joaogouveia89.interfaces.CallbackBasicViewModel;
import io.github.joaogouveia89.ui.base.BaseViewModel;
import io.github.joaogouveia89.ui.temperatureList.TemperatureListDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends BaseViewModel {

    public ObservableField<String> lastTemperature = new ObservableField<>();
    public ObservableField<String> lastTemperatureTimestamp = new ObservableField<>();
    public ObservableBoolean hasFetchedTemperature = new ObservableBoolean(false);

    public HomeViewModel(CallbackBasicViewModel callback, CompositeDisposable compositeDisposable) {
        super(callback, compositeDisposable);
    }

    public void init() {
        fetchLastTemperature();
    }

    private void fetchLastTemperature(){
        hasFetchedTemperature.set(false);
        Disposable disposable = RetrofitBase
                .getInterfaceRetrofit()
                .getLastTemperature()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    hasFetchedTemperature.set(true);
                    Log.i("DEUUUU", response.getTemperature());
                    lastTemperature.set(response.getTemperature());
                    lastTemperatureTimestamp.set(response.getDate() +  " " + response.getHour());
                },this::handleError);
        compositeDisposable.add(disposable);
    }

    public void openTemperaturesListDialog(View view){
        TemperatureListDialog dialog = TemperatureListDialog.newInstance();
        showDialogFragment(dialog);
    }

    private void handleError(Throwable t){
        Log.i("ERRORRRR", t.getMessage());
    }
}
