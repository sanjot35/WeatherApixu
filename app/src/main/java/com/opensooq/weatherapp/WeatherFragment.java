package com.opensooq.weatherapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.opensooq.weatherapp.model.Weather;


public class WeatherFragment extends Fragment {
    private static final String WEATHER = "weather";

    private Weather weather;

    private OnItemClickedListener mListener;

    public WeatherFragment() {
        // Required empty public constructor
    }
   public static WeatherFragment newInstance(Weather weather) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putSerializable(WEATHER, weather);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            weather = (Weather) getArguments().getSerializable(WEATHER);
            if (weather != null) {
                Log.e("see",weather.getForecast().getForecastday().get(1).getDate());
            }
            else {
          Log.e("weather is ",weather+"");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onItemClicked(Uri uri) {
        if (mListener != null) {
            mListener.showDetails(uri);
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
        void showDetails(Uri uri);
    }
}
