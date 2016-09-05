package com.opensooq.weatherapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Omar AlTamimi on 9/1/2016.
 */
public class Forecast implements Parcelable {
    public static final Parcelable.Creator<Forecast> CREATOR = new Parcelable.Creator<Forecast>() {
        @Override
        public Forecast createFromParcel(Parcel source) {
            return new Forecast(source);
        }

        @Override
        public Forecast[] newArray(int size) {
            return new Forecast[size];
        }
    };
    @SerializedName("forecastday")
    private List<ForecastDay> forecastDay = new ArrayList<ForecastDay>();

    public Forecast() {
    }

    protected Forecast(Parcel in) {
        this.forecastDay = in.createTypedArrayList(ForecastDay.CREATOR);
    }

    public List<ForecastDay> getForecastDay() {
        return forecastDay;
    }

    public void setForecastDay(List<ForecastDay> forecastDay) {
        this.forecastDay = forecastDay;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.forecastDay);
    }
}
