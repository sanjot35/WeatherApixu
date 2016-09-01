package com.opensooq.weatherapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Omar AlTamimi on 9/1/2016.
 */
public class Current {
    @SerializedName("condition")
    private Condition condition;
    @SerializedName("temp_c")
    private int tempC;

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public int getTempC() {
        return tempC;
    }

    public void setTempC(int tempC) {
        this.tempC = tempC;
    }
}
