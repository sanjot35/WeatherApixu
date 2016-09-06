package com.opensooq.weatherapp;

import android.os.AsyncTask;
import android.util.Log;

import com.opensooq.weatherapp.api.ApiClient;
import com.opensooq.weatherapp.api.ApiInterface;
import com.opensooq.weatherapp.common.Const;
import com.opensooq.weatherapp.model.Weather;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Omar AlTamimi on 9/5/2016.
 */
public class WeatherTask extends AsyncTask<String, Void, Weather> {
    private boolean isSuccesfull;
    private Weather weather;
    private ApiInterface apiService;
    private MyAsyncTaskListener mListener;

    public WeatherTask() {
        apiService = ApiClient.getClient().create(ApiInterface.class);
    }

    public void setListener(MyAsyncTaskListener listener) {
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        if (mListener != null)
            mListener.onPreExecuteConcluded();
    }

    @Override
    protected Weather doInBackground(String... city) {
        if (isCancelled())
            return null;
        Log.e("city name", city[0]);
        Response<Weather> response = null;
        Call<Weather> call = apiService.getWeather(Const.API_KEY, city[0], "5");
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response != null) {
            weather = response.body();
            isSuccesfull = response.isSuccessful();
        }
        return weather;


    }

    @Override
    protected void onPostExecute(Weather weather) {
        if (mListener != null)
            mListener.onPostExecuteConcluded(weather, isSuccesfull);
    }

    public interface MyAsyncTaskListener {
        void onPreExecuteConcluded();

        void onPostExecuteConcluded(Weather result, boolean isSuccesfull);
    }
}
