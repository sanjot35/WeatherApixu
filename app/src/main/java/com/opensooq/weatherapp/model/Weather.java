package com.opensooq.weatherapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Omar AlTamimi on 9/1/2016.
 */
public class Weather implements Serializable {
    @SerializedName("current")
    private Current current;

    @SerializedName("forecast")
    private Forecast forecast;

    public Current getCurrent() {
        return current;
    }

    public Forecast getForecast() {
        return forecast;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }

}
