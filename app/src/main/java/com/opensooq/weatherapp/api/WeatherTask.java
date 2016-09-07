package com.opensooq.weatherapp.api;

import android.os.AsyncTask;
import android.util.Log;

import com.opensooq.weatherapp.common.Const;
import com.opensooq.weatherapp.model.WeatherResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Omar AlTamimi on 9/5/2016.
 */
public class WeatherTask extends AsyncTask<String, Void, WeatherResponse> {
    final private ApiInterface apiService;
    private boolean isSuccessful;
    private WeatherResponse weatherResponse;
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
            mListener.onPreExecuteTask();
    }

    @Override
    protected WeatherResponse doInBackground(String... city) {
        Log.e("city name", city[0]);
        Response<WeatherResponse> response = null;
        Call<WeatherResponse> call = apiService.getWeather(Const.API_KEY, city[0], "5");
        try {
            if (isCancelled())
                return null;
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (isCancelled())
            return null;
        if (response != null) {
            weatherResponse = response.body();
            isSuccessful = response.isSuccessful();
        }
        return weatherResponse;


    }

    @Override
    protected void onPostExecute(WeatherResponse weatherResponse) {
        if (mListener != null)
            mListener.onPostExecuteTask(weatherResponse, isSuccessful);
    }

    public interface MyAsyncTaskListener {
        void onPreExecuteTask();

        void onPostExecuteTask(WeatherResponse result, boolean isSuccessful);
    }
}
