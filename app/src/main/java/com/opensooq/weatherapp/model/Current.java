package com.opensooq.weatherapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Omar AlTamimi on 9/1/2016.
 */
public class Current implements Serializable {
    @SerializedName("condition")
    private Condition condition;
    @SerializedName("temp_c")
    private double tempC;

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public double getTempC() {
        return tempC;
    }

    public void setTempC(double tempC) {
        this.tempC = tempC;
    }
}
