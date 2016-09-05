package com.opensooq.weatherapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Omar AlTamimi on 9/1/2016.
 */
public class Day implements Parcelable {
    public static final Parcelable.Creator<Day> CREATOR = new Parcelable.Creator<Day>() {
        @Override
        public Day createFromParcel(Parcel source) {
            return new Day(source);
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };
    @SerializedName("maxtemp_c")
    private double maxtempC;
    @SerializedName("mintemp_c")
    private double mintempC;
    @SerializedName("condition")
    private Condition condition;

    public Day() {
    }

    protected Day(Parcel in) {
        this.maxtempC = in.readDouble();
        this.mintempC = in.readDouble();
        this.condition = in.readParcelable(Condition.class.getClassLoader());
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.maxtempC);
        dest.writeDouble(this.mintempC);
        dest.writeParcelable(this.condition, flags);
    }
}
