package com.opensooq.weatherapp.Activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.opensooq.weatherapp.R;
import com.opensooq.weatherapp.common.TinySharedPreferences;

import java.util.ArrayList;

public class CityListActivity extends AppCompatActivity {
    ListView listView;
    TinySharedPreferences tinySharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        getSupportActionBar().setHomeButtonEnabled(true);
        listView = (ListView) findViewById(R.id.list_cities);
        final String PREFS_NAME = "MyPrefsFile";

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        TinySharedPreferences tinySharedPreferences = new TinySharedPreferences();
        if (settings.getBoolean("my_first_time", true)) {
            //the app is being launched for first time, do something
            tinySharedPreferences.addFavorite(this, "Amman");
            tinySharedPreferences.addFavorite(this, "new york");
            tinySharedPreferences.addFavorite(this, "barcelona");
            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).apply();
        }
        listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,tinySharedPreferences.loadFavorites(this)));

    }
}
