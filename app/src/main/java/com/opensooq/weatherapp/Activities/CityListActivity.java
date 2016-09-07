package com.opensooq.weatherapp.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
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
import com.opensooq.weatherapp.api.WeatherTask;
import com.opensooq.weatherapp.common.PreferencesManager;
import com.opensooq.weatherapp.common.Util;
import com.opensooq.weatherapp.model.WeatherResponse;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CityListActivity extends AppCompatActivity implements WeatherTask.MyAsyncTaskListener {
    @BindView(R.id.list_cities)
    ListView listView;
    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.prog)
    ProgressBar progressBar;
    @BindView(R.id.joke_txt)
    TextView jokeMessage;
    private ArrayAdapter<String> adapter;
    private int currentSelection;
    private PreferencesManager preferencesManager;
    private String cityName;
    private WeatherTask weatherTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        ButterKnife.bind(this);
        preferencesManager = PreferencesManager.getInstance(this);
        ActionBar ab = getSupportActionBar();
        if (ab != null)
            ab.setDisplayHomeAsUpEnabled(true);
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
                        preferencesManager.removeItem(listView.getItemAtPosition(currentSelection).toString());
                        adapter.notifyDataSetChanged();
                        mode.finish();
                        return true;


                    }
                    default:
                        return false;

                }
            }
        };
        if (preferencesManager.isFirstTime()) {
            //the app is being launched for first time, do something
            preferencesManager.addItem(getString(R.string.amman));
            preferencesManager.addItem(getString(R.string.new_york));
            preferencesManager.addItem(getString(R.string.barcelona));
            // record the fact that the app has been started at least once
            preferencesManager.setFirstTime(false);
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, preferencesManager.loadList());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = listView.getItemAtPosition(i).toString();
                if (item.equals(preferencesManager.getLastSelectedCity()))
                    finish();
                else {
                    Log.d("item", "the item is " + item);
                    notifyChangedCity(item);
                    finish();
                }

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
                                cityName = input.getText().toString();
                                Log.d("cityNAme:", "yeah" + cityName);
                                if (Util.isNetworkAvailable(CityListActivity.this)) {
                                    weatherTask = new WeatherTask();
                                    weatherTask.setListener(CityListActivity.this);
                                    weatherTask.execute(cityName);
                                } else {
                                    Toast.makeText(CityListActivity.this, "NO internet connection", Toast.LENGTH_SHORT).show();
                                }
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

    private void notifyChangedCity(String item) {
        preferencesManager.storeLastSelectedCity(item);
        Intent intent = new Intent("city-changed-event");
        // You can also include some extra data.
        intent.putExtra("city", item);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPreExecuteTask() {
        listView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        jokeMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPostExecuteTask(WeatherResponse result, boolean isSuccessful) {
        listView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        jokeMessage.setVisibility(View.GONE);
        if (result == null || result.getError() != null) {
            Toast.makeText(CityListActivity.this, "bad entry ! , please try enter city name again", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(CityListActivity.this, " Successfully Added!", Toast.LENGTH_LONG).show();
            preferencesManager.addItem(cityName);
            adapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onDestroy() {
        if (weatherTask != null && weatherTask.getStatus() == AsyncTask.Status.RUNNING)
            weatherTask.cancel(true);
        super.onDestroy();
    }
}
