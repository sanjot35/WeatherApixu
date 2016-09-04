package com.opensooq.weatherapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.opensooq.weatherapp.adapter.WeatherAdapter;
import com.opensooq.weatherapp.common.Const;
import com.opensooq.weatherapp.common.Util;
import com.opensooq.weatherapp.model.Day;
import com.opensooq.weatherapp.model.Weather;
import com.squareup.picasso.Picasso;


public class WeatherFragment extends Fragment {
    private static final String WEATHER = "weather";
    private static final String TYPE = "type";
    private static final String POSITION = " position";
    private static final String COUNTRY_NAME="country";


    private Weather weather;
    private int mType;
    private int mPosition;
    private ImageView bg, icCondition;
    private RecyclerView recyclerView;
    private WeatherAdapter adapter;
    private TextView conditionTxt, dateTxt, MaxTemp, MinTemp,countryName;
    private Day  selectedDay;
    private String cityName;
    private OnItemClickedListener mListener;
    private FloatingActionButton refreshButton;

    public WeatherFragment() {
        // Required empty public constructor
    }

    public int getType() {
        return mType;
    }

    public void setType(int mType) {
        this.mType = mType;
    }

    public Day getSelectedDay() {
        return selectedDay;
    }

    public void setSelectedDay(Day selectedDay) {
        this.selectedDay = selectedDay;
    }

    public static WeatherFragment newInstance(Weather weather, int type, int i, String cityName) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putSerializable(WEATHER, weather);
        args.putInt(TYPE, type);
        args.putInt(POSITION,i);
         args.putString(COUNTRY_NAME,cityName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            weather = (Weather) getArguments().getSerializable(WEATHER);
            mType = getArguments().getInt(TYPE);
            mPosition = getArguments().getInt(POSITION);
    cityName = getArguments().getString(COUNTRY_NAME);
            if (weather != null) {
                Log.e("see", weather.getForecast().getForecastDay().get(1).getDate());
            } else {
                Log.e("weather is ", weather + "");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        bg = (ImageView) view.findViewById(R.id.bg_img);
        icCondition = (ImageView) view.findViewById(R.id.condition_ic);
        dateTxt = (TextView) view.findViewById(R.id.date);
        MaxTemp = (TextView) view.findViewById(R.id.max_temp);
        MinTemp = (TextView) view.findViewById(R.id.min_temp);
        conditionTxt = (TextView) view.findViewById(R.id.condition_txt);
        countryName = (TextView) view.findViewById(R.id.countryName);
        refreshButton = (FloatingActionButton) view.findViewById(R.id.refresh);
        if(mType!=Const.DAILY)
            refreshButton.setVisibility(View.GONE);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onRefreshAnchorButton();
            }
        });
        countryName.setText(cityName);
        String conditionstr = null;
        if (mType == Const.DAILY)
            conditionstr = weather.getCurrent().getCondition().getConditionText();
        else {
            setSelectedDay(weather.getForecast().getForecastDay().get(mPosition).getDay());
            conditionstr = getSelectedDay().getCondition().getConditionText();
        }
        if (conditionstr.toLowerCase().contains("cloudy")||conditionstr.toLowerCase().contains("cloud"))
            Picasso.with(getActivity()).load(R.drawable.bg_cloudy).fit().centerCrop().into(bg);
        else if (conditionstr.toLowerCase().contains("rainy")||conditionstr.toLowerCase().contains("rain"))
            Picasso.with(getContext()).load(R.drawable.bg_rainy).fit().centerCrop().into(bg);
        else Picasso.with(getContext()).load(R.drawable.bg_sunny).fit().centerCrop().into(bg);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setBackgroundColor(Color.parseColor("#303F9F"));
        if (mType == Const.DAILY)
            adapter = new WeatherAdapter(weather.getForecast(), Const.DAILY, 0);
        else
            adapter = new WeatherAdapter(weather.getForecast(), Const.HOURLY, mPosition);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.setOnItemClickListener(new WeatherAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                if (mType == Const.DAILY)
                    onItemClicked(position);

            }
        });
        if (mType == Const.DAILY) {
            Picasso.with(getContext()).load("http://".concat(weather.getCurrent().getCondition().getIconLink().substring(2, weather.getCurrent().getCondition().getIconLink().length()))).into(icCondition);
            dateTxt.setText("today");
            conditionTxt.setText(weather.getCurrent().getCondition().getConditionText());
            MaxTemp.setText(weather.getForecast().getForecastDay().get(0).getDay().getMaxtempC() + "" + "\u00B0");
            MinTemp.setText(weather.getForecast().getForecastDay().get(0).getDay().getMintempC() + "" + "\u00B0");
        } else {
            Picasso.with(getContext()).load("http://".concat(getSelectedDay().getCondition().getIconLink().substring(2, getSelectedDay().getCondition().getIconLink().length()))).into(icCondition);
            dateTxt.setText(Util.ConvertDateToDay(weather.getForecast().getForecastDay().get(mPosition).getDate()) + "");
            conditionTxt.setText(getSelectedDay().getCondition().getConditionText());
            MaxTemp.setText(getSelectedDay().getMaxtempC() + "" + "\u00B0");
            MinTemp.setText(getSelectedDay().getMintempC() + "" + "\u00B0");
        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onItemClicked(int pos) {
        if (mListener != null) {
            mListener.showDetails(pos);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnItemClickedListener) {
            mListener = (OnItemClickedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnItemClickedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnItemClickedListener {
        // TODO: Update argument type and name
        void showDetails(int pos);
        void onRefreshAnchorButton();
    }

}
