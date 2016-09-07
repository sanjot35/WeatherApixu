package com.opensooq.weatherapp.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.opensooq.weatherapp.Activities.MainActivity;
import com.opensooq.weatherapp.R;
import com.opensooq.weatherapp.adapter.HourlyAdapter;
import com.opensooq.weatherapp.common.PreferencesManager;
import com.opensooq.weatherapp.common.Util;
import com.opensooq.weatherapp.model.ForecastDay;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherDetailsFragment extends Fragment {

    private static final String FORECAST = "forecast";
    @BindView(R.id.bg_img)
    ImageView cardBackground;
    @BindView(R.id.condition_ic)
    ImageView conditionIcon;
    @BindView(R.id.condition_txt)
    TextView conditionTxt;
    @BindView(R.id.date)
    TextView dateTxt;
    @BindView(R.id.max_temp)
    TextView maxTempTxt;
    @BindView(R.id.min_temp)
    TextView minTempTxt;
    @BindView(R.id.countryName)
    TextView countryNameTxt;
    @BindView(R.id.hourly_list)
    RecyclerView hourlyRecycler;
    private ForecastDay forecastDay;
    private HourlyAdapter hourlyAdapter;
    private ActionBar ab;

    public WeatherDetailsFragment() {
        // Required empty public constructor
    }

    public static WeatherDetailsFragment newInstance(ForecastDay forcastDay) {
        WeatherDetailsFragment fragment = new WeatherDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(FORECAST, forcastDay);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            forecastDay = getArguments().getParcelable(FORECAST);

        }
        setHasOptionsMenu(true);
        ab = ((MainActivity) getActivity()).getSupportActionBar();
        if (ab != null)
            ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather_details, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        fetchDataCard();
        hourlyAdapter = new HourlyAdapter(forecastDay);
        hourlyRecycler.setAdapter(hourlyAdapter);
        hourlyRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

    }

    public void fetchDataCard() {
        String conditionStr;
        conditionStr = forecastDay.getDay().getCondition().getConditionText();
        conditionStr = conditionStr.toLowerCase();
        Picasso.with(getActivity()).load(Util.weatherConditionBackground(conditionStr)).fit().centerCrop().into(cardBackground);
        Picasso.with(getContext()).load("http://".concat(forecastDay.getDay().getCondition().getIconLink().substring(2, forecastDay.getDay().getCondition().getIconLink().length()))).into(conditionIcon);
        dateTxt.setText(Util.ConvertDateToDay(forecastDay.getDate()));
        countryNameTxt.setText(PreferencesManager.getInstance(getContext()).getLastSelectedCity());
        conditionTxt.setText(conditionStr);
        maxTempTxt.setText(String.format("%s°", forecastDay.getDay().getMaxtempC()));
        minTempTxt.setText(String.format("%s°", forecastDay.getDay().getMintempC()));


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menumain, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                ab.setDisplayHomeAsUpEnabled(false);
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
