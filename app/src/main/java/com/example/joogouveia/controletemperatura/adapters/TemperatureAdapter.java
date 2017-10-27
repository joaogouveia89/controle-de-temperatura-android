package com.example.joogouveia.controletemperatura.adapters;

import android.content.Context;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.joogouveia.controletemperatura.R;
import com.example.joogouveia.controletemperatura.api.model.Temperature;

import java.util.List;

/**
 * Created by João Gouveia on 26/10/2017.
 */

public class TemperatureAdapter extends RecyclerView.Adapter<TemperatureAdapter.MyViewHolder>{

    private List<Temperature> mList;
    private LayoutInflater mLayoutInflater;

    public TemperatureAdapter(Context c, List<Temperature> l){
        mList = l;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.item_temperature, parent, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.temperature.setText(mList.get(position).getTemperature() + " ºC");
        holder.date.setText(mList.get(position).getDate());
        holder.hour.setText(mList.get(position).getHour());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder{

        public TextView temperature;
        public TextView date;
        public TextView hour;

        public MyViewHolder(View itemView) {
            super(itemView);

            temperature = (TextView) itemView.findViewById(R.id.tv_list_temperature);
            date = (TextView) itemView.findViewById(R.id.tv_list_date);
            hour = (TextView) itemView.findViewById(R.id.tv_list_hour);
        }
    }
}
