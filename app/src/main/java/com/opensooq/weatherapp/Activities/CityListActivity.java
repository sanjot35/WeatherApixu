package com.opensooq.weatherapp.Activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.opensooq.weatherapp.R;
import com.opensooq.weatherapp.api.ApiClient;
import com.opensooq.weatherapp.api.ApiInterface;
import com.opensooq.weatherapp.common.Const;
import com.opensooq.weatherapp.common.Pref;
import com.opensooq.weatherapp.model.Weather;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;


public class CityListActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter adapter;
    private int currentSelection;
    private ApiInterface apiService;
    private Weather weather;
    private Pref tinySharedPreferences;
    private FloatingActionButton floatingActionButton;
    private ProgressBar progressBar;
    private TextView jokeMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        tinySharedPreferences = new Pref();
        apiService = ApiClient.getClient().create(ApiInterface.class);
        getSupportActionBar().setHomeButtonEnabled(true);
        listView = (ListView) findViewById(R.id.list_cities);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        progressBar = (ProgressBar) findViewById(R.id.prog);
        jokeMessage= (TextView) findViewById(R.id.joke_txt);
        final String PREFS_NAME = "MyPrefsFile";
        final ActionMode.Callback modeCallBack = new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
                mode = null;
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.setTitle("Options");
                mode.getMenuInflater().inflate(R.menu.listoptions_menu, menu);
                return true;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

                int id = item.getItemId();
                switch (id) {
                    case R.id.delete: {

                        tinySharedPreferences.removeFavorite(getApplicationContext(), listView.getItemAtPosition(currentSelection).toString());
                        adapter.remove(adapter.getItem(currentSelection));
                        mode.finish();
                        return true;


                    }
                    default:
                        return false;

                }
            }
        };

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);


        if (settings.getBoolean("my_first_time", true)) {
            //the app is being launched for first time, do something
            tinySharedPreferences.addFavorite(this, "Amman");
            tinySharedPreferences.addFavorite(this, "new york");
            tinySharedPreferences.addFavorite(this, "barcelona");
            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).apply();
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tinySharedPreferences.loadFavorites(this));
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tinySharedPreferences.storeLastSelected(getApplicationContext(),listView.getItemAtPosition(i).toString());
                finish();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView parent, View view, int position, long id) {
                System.out.println("Long click");
                currentSelection = position;
                startActionMode(modeCallBack);
                view.setSelected(true);
                return true;
            }
        });
floatingActionButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        final EditText input = new EditText(CityListActivity.this);

        new AlertDialog.Builder(CityListActivity.this)
                .setTitle("Add entry")
                .setMessage("Please enter the city name correctly")
                 .setView(input)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String cityName = input.getText().toString();
                        Log.d("cityNAme:","yeah"+cityName);
                             new WeatherAsync().execute(cityName);

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
});
    }

    class WeatherAsync extends AsyncTask<String, Void, Void> {

       String cityName ;
        @Override
        protected void onPreExecute() {
      listView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            jokeMessage.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... city) {
            cityName = city[0];
            Log.d("background",cityName+"h");
            Response<Weather> response = null;
            Call<Weather> call = apiService.getWeather(Const.API_KEY, city[0], "5");
            try {
                response = call.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (response != null) {
                weather = response.body();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            listView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            jokeMessage.setVisibility(View.GONE);
            if (weather == null || weather.getCurrent()==null) {
             Toast.makeText(CityListActivity.this,"bad entry ! , please try enter city name again",Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(CityListActivity.this," Successfully Added!",Toast.LENGTH_LONG).show();
                tinySharedPreferences.addFavorite(CityListActivity.this,cityName);
                adapter.clear();
                adapter.addAll(tinySharedPreferences.loadFavorites(CityListActivity.this));
                adapter.notifyDataSetChanged();
            }
        }
    }
}
