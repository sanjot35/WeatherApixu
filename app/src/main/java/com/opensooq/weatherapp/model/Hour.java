package com.opensooq.weatherapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Omar AlTamimi on 9/1/2016.
 */
public class Hour implements Serializable {
    @SerializedName("condition")
    private Condition condition;
    @SerializedName("time")
    private String time;

    @SerializedName("temp_c")
    private double tempC;

    public double getTempC() {
        return tempC;
    }

    public void setTempC(double tempC) {
        this.tempC = tempC;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
