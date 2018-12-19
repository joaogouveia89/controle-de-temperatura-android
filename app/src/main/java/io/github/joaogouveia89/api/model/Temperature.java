package com.example.joogouveia.joaogouveia89.api.model;

/**
 * Created by João Gouveia on 20/10/2017.
 */

public class Temperature {
    private String temperature;
    private String date;
    private String hour;

    public Temperature() {
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    @Override
    public String toString() {
        return "Temperature{" +
                "temperature='" + temperature + '\'' +
                ", date='" + date + '\'' +
                ", hour='" + hour + '\'' +
                '}';
    }
}
