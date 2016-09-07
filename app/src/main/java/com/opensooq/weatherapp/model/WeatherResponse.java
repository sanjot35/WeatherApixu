package com.opensooq.weatherapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Omar AlTamimi on 9/1/2016.
 */
public class WeatherResponse implements Parcelable {

    public static final Creator<WeatherResponse> CREATOR = new Creator<WeatherResponse>() {
        @Override
        public WeatherResponse createFromParcel(Parcel source) {
            return new WeatherResponse(source);
        }

        @Override
        public WeatherResponse[] newArray(int size) {
            return new WeatherResponse[size];
        }
    };
    @SerializedName("current")
    private Current current;
    @SerializedName("forecast")
    private Forecast forecast;
    @SerializedName("error")
    private Error error;

    public WeatherResponse() {
    }

    protected WeatherResponse(Parcel in) {
        this.current = in.readParcelable(Current.class.getClassLoader());
        this.forecast = in.readParcelable(Forecast.class.getClassLoader());
        this.error = in.readParcelable(Error.class.getClassLoader());
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    public Forecast getForecast() {
        return forecast;
    }

    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.current, flags);
        dest.writeParcelable(this.forecast, flags);
        dest.writeParcelable(this.error, flags);
    }
}
