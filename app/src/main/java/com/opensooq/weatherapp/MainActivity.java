package com.opensooq.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.opensooq.weatherapp.Activities.CityListActivity;
import com.opensooq.weatherapp.common.PreferencesManager;
import com.opensooq.weatherapp.common.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ErrorMessageFragment.onRefreshButtonListener, WeatherFragment.OnItemClickedListener {
    private final String WEATHER_FRAGMENT_TAG = "weatherFragment";
    @BindView(R.id.loading_progress)
    ProgressBar progressBar;
    @BindView(R.id.fragmentContainer)
    FrameLayout container;
    private WeatherFragment weatherFragment;
    private ErrorMessageFragment errorMessageFragment;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            attachFragment();
        }

    }

    private void attachFragment() {
        if (!Util.isNetworkAvailable(this)) {
            errorMessageFragment = ErrorMessageFragment.newInstance(getResources().getString(R.string.check_your_internet_connection));
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, errorMessageFragment).commit();
        } else {
            weatherFragment = WeatherFragment.newInstance(PreferencesManager.getInstance(this).getLastSelectedCity());
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragmentContainer, weatherFragment, WEATHER_FRAGMENT_TAG).
                    commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_city) {
            startActivity(new Intent(this, CityListActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        attachFragment();
    }

    @Override
    public void showDetails(int position) {
    }

    @Override
    public void onRefreshAnchorButton() {
    }
}
