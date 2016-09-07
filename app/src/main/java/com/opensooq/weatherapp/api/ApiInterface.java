package com.opensooq.weatherapp.api;

import com.opensooq.weatherapp.model.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Omar AlTamimi on 9/1/2016.
 */
public interface ApiInterface {
    @GET("forecast.json ")
    Call<WeatherResponse> getWeather(@Query("key") String key, @Query("q") String city, @Query("days") String days);
}
