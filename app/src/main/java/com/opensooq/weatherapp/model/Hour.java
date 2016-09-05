package com.opensooq.weatherapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Omar AlTamimi on 9/1/2016.
 */
public class Hour implements Parcelable {
    public static final Parcelable.Creator<Hour> CREATOR = new Parcelable.Creator<Hour>() {
        @Override
        public Hour createFromParcel(Parcel source) {
            return new Hour(source);
        }

        @Override
        public Hour[] newArray(int size) {
            return new Hour[size];
        }
    };
    @SerializedName("condition")
    private Condition condition;
    @SerializedName("time")
    private String time;
    @SerializedName("temp_c")
    private double tempC;

    public Hour() {
    }

    protected Hour(Parcel in) {
        this.condition = in.readParcelable(Condition.class.getClassLoader());
        this.time = in.readString();
        this.tempC = in.readDouble();
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.condition, flags);
        dest.writeString(this.time);
        dest.writeDouble(this.tempC);
    }
}
