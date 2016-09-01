package com.opensooq.weatherapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Omar AlTamimi on 9/1/2016.
 */
public class Day {
    @SerializedName("maxtemp_c")
    private double maxtempC;
    @SerializedName("mintemp_c")
    private double mintempC;

    @SerializedName("condition")
    private Condition condition;

    public double getMaxtempC() {
        return maxtempC;
    }

    public void setMaxtempC(double maxtempC) {
        this.maxtempC = maxtempC;
    }

    public double getMintempC() {
        return mintempC;
    }

    public void setMintempC(double mintempC) {
        this.mintempC = mintempC;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }
}
