package io.github.joaogouveia89.ui.temperatureList;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

import io.github.joaogouveia89.api.model.Temperature;

public class TemperatureItemViewModel extends ViewModel {
    public ObservableField<String> temperature = new ObservableField<>();
    public ObservableField<String> date = new ObservableField<>();
    public ObservableField<String> hour = new ObservableField<>();

    public TemperatureItemViewModel(Temperature temperature){
        this.temperature.set(temperature.getTemperature());
        this.date.set(temperature.getDate());
        this.hour.set(temperature.getHour());
    }
}
