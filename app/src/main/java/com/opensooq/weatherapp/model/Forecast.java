package com.opensooq.weatherapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Omar AlTamimi on 9/1/2016.
 */
public class Forecast {
    @SerializedName("forecastday")
    private List<Forecastday> forecastday = new ArrayList<Forecastday>();

    public List<Forecastday> getForecastday() {
        return forecastday;
    }

    public void setForecastday(List<Forecastday> forecastday) {
        this.forecastday = forecastday;
    }
}
