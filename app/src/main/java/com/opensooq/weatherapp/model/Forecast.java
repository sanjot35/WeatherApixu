package com.opensooq.weatherapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Omar AlTamimi on 9/1/2016.
 */
public class Forecast implements Serializable{
    @SerializedName("forecastday")
    private List<ForecastDay> forecastDay = new ArrayList<ForecastDay>();

    public List<ForecastDay> getForecastDay() {
        return forecastDay;
    }

    public void setForecastDay(List<ForecastDay> forecastDay) {
        this.forecastDay = forecastDay;
    }
}
