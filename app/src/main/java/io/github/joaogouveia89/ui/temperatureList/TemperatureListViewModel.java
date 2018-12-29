package io.github.joaogouveia89.ui.temperatureList;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import java.util.List;
import java.util.Objects;

import io.github.joaogouveia89.api.RetrofitBase;
import io.github.joaogouveia89.api.model.Temperature;
import io.github.joaogouveia89.interfaces.CallbackBasicViewModel;
import io.github.joaogouveia89.ui.base.BaseViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TemperatureListViewModel extends BaseViewModel {

    public ObservableField<TemperatureListAdapter> adapter = new ObservableField<>();
    public ObservableBoolean hasFetchedTemps = new ObservableBoolean();


    public TemperatureListViewModel(CallbackBasicViewModel callback,
                                    CompositeDisposable compositeDisposable) {
        super(callback, compositeDisposable);
        init();
    }

    private void init() {
        adapter.set(new TemperatureListAdapter());
    }

    public void fetchTemperatures(){
        hasFetchedTemps.set(false);
        Disposable disposable = RetrofitBase.getInterfaceRetrofit()
                .getTemperatures()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(temperatures -> {
                    for(Temperature temperature : temperatures){
                        Objects.requireNonNull(adapter.get()).addTemperature(temperature);
                    }
                    Objects.requireNonNull(adapter.get()).notifyDataSetChanged();
                    hasFetchedTemps.set(true);
                }, this::showError);
        compositeDisposable.add(disposable);
    }
}
