package com.opensooq.weatherapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Omar AlTamimi on 9/1/2016.
 */
public class Current implements Parcelable {
    public static final Parcelable.Creator<Current> CREATOR = new Parcelable.Creator<Current>() {
        @Override
        public Current createFromParcel(Parcel source) {
            return new Current(source);
        }

        @Override
        public Current[] newArray(int size) {
            return new Current[size];
        }
    };
    @SerializedName("condition")
    private Condition condition;
    @SerializedName("temp_c")
    private double tempC;

    public Current() {
    }

    protected Current(Parcel in) {
        this.condition = in.readParcelable(Condition.class.getClassLoader());
        this.tempC = in.readDouble();
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.condition, flags);
        dest.writeDouble(this.tempC);
    }
}
