package com.opensooq.weatherapp.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.opensooq.weatherapp.Fragment.ErrorMessageFragment;
import com.opensooq.weatherapp.Fragment.WeatherDetailsFragment;
import com.opensooq.weatherapp.Fragment.WeatherFragment;
import com.opensooq.weatherapp.R;
import com.opensooq.weatherapp.common.PreferencesManager;
import com.opensooq.weatherapp.common.Util;
import com.opensooq.weatherapp.model.ForecastDay;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ErrorMessageFragment.onRefreshButtonListener, WeatherFragment.OnItemClickedListener {
    private final String WEATHER_FRAGMENT_TAG = "weatherFragment";
    @BindView(R.id.loading_progress)
    public
    ProgressBar progressBar;
    @BindView(R.id.fragmentContainer)
    public
    FrameLayout container;
    private WeatherFragment weatherFragment;
    private ErrorMessageFragment errorMessageFragment;
    private String cityName;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            cityName = intent.getStringExtra("city");
            if (weatherFragment == null) {
                weatherFragment = (WeatherFragment) getSupportFragmentManager().findFragmentByTag(WEATHER_FRAGMENT_TAG);
            }
            weatherFragment.setCityName(cityName);
            weatherFragment.startWeatherTask(cityName);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        cityName = PreferencesManager.getInstance(this).getLastSelectedCity();
        if (savedInstanceState == null) {
            attachFragment();
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("city-changed-event"));
    }

    private void attachFragment() {
        if (!Util.isNetworkAvailable(this)) {
            errorMessageFragment = ErrorMessageFragment.newInstance(getResources().getString(R.string.check_your_internet_connection));
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, errorMessageFragment).commit();
        } else {
            weatherFragment = WeatherFragment.newInstance(cityName);
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragmentContainer, weatherFragment, WEATHER_FRAGMENT_TAG).
                    commit();
        }
    }

    @Override
    public void onRefresh() {
        attachFragment();
    }

    @Override
    public void showDetails(ForecastDay forecastDay) {
        WeatherDetailsFragment weatherDetailsFragment = WeatherDetailsFragment.newInstance(forecastDay);
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, weatherDetailsFragment).addToBackStack(null).commit();

    }

    @Override
    public void onRefreshAnchorButton() {
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
}
