package com.opensooq.weatherapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Omar AlTamimi on 9/1/2016.
 */
public class Forecastday {
    @SerializedName("date")
    private String date;
    @SerializedName("day")
    private Day day;
    @SerializedName("hour")
    private List<Hour> hour = new ArrayList<Hour>();

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public List<Hour> getHour() {
        return hour;
    }

    public void setHour(List<Hour> hour) {
        this.hour = hour;
    }
}
