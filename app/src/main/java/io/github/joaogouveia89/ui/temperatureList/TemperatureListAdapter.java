package io.github.joaogouveia89.ui.temperatureList;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.joogouveia.joaogouveia89.R;
import com.example.joogouveia.joaogouveia89.databinding.ItemTemperatureBinding;

import java.util.ArrayList;
import java.util.List;

import io.github.joaogouveia89.api.model.Temperature;

public class TemperatureListAdapter extends RecyclerView.Adapter<TemperatureListAdapter.TemperatureListViewHolder> {
    private List<Temperature> temperatures = new ArrayList<>();
    private TemperatureItemViewModel mViewModel;

    public TemperatureListAdapter(){

    }

    @NonNull
    @Override
    public TemperatureListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        ItemTemperatureBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_temperature, viewGroup, false);
        return new TemperatureListViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull TemperatureListViewHolder temperatureListViewHolder, int i) {
        mViewModel = new TemperatureItemViewModel(temperatures.get(i));
        temperatureListViewHolder.binding.setViewModel(mViewModel);
    }

    @Override
    public int getItemCount() {
        return temperatures.size();
    }

    void addTemperature(Temperature temperature){
        temperatures.add(temperature);
    }

    class TemperatureListViewHolder extends RecyclerView.ViewHolder{
        public ItemTemperatureBinding binding;
        public TemperatureListViewHolder(@NonNull ItemTemperatureBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
