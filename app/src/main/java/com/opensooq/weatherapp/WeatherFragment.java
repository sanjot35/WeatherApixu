package com.opensooq.weatherapp;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
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
import com.opensooq.weatherapp.common.Util;
import com.opensooq.weatherapp.model.Weather;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherFragment extends Fragment implements WeatherTask.MyAsyncTaskListener {
    private static final String WEATHER = "weather";
    private static final String COUNTRY_NAME = "country";
    @BindView(R.id.bg_img)
    ImageView cardBackground;
    @BindView(R.id.condition_ic)
    ImageView conditionIcon;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
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
    @BindView(R.id.refresh)
    FloatingActionButton refreshButton;
    private String cityName;
    private OnItemClickedListener mListener;
    private Weather weather;
    private WeatherAdapter adapter;
    private WeatherTask task;

    /*  cardBackground = (ImageView) view.findViewById(R.id.bg_img);
    conditionIcon = (ImageView) view.findViewById(R.id.condition_ic);
    dateTxt = (TextView) view.findViewById(R.id.date);
    maxTempTxt = (TextView) view.findViewById(R.id.max_temp);
    minTempTxt = (TextView) view.findViewById(R.id.min_temp);
    conditionTxt = (TextView) view.findViewById(R.id.condition_txt);
    countryNameTxt = (TextView) view.findViewById(R.id.countryNameTxt);
    refreshButton = (FloatingActionButton) view.findViewById(R.id.refresh);
*/
    public WeatherFragment() {
        // Required empty public constructor
    }

    public static WeatherFragment newInstance(String cityName) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString(COUNTRY_NAME, cityName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cityName = getArguments().getString(COUNTRY_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        ButterKnife.bind(this, view);
        if (savedInstanceState != null) {
            weather = savedInstanceState.getParcelable(WEATHER);
        }
        if (weather == null)
            startWeatherTask();
        else {
            fetchList();
            fetchDataCard();
        }
        return view;
    }

    public void fetchDataCard() {
        String conditionStr;
        conditionStr = weather.getCurrent().getCondition().getConditionText();
        if (conditionStr.toLowerCase().contains("cloudy") || conditionStr.toLowerCase().contains("cloud"))
            Picasso.with(getActivity()).load(R.drawable.bg_cloudy).fit().centerCrop().into(cardBackground);
        else if (conditionStr.toLowerCase().contains("rainy") || conditionStr.toLowerCase().contains("rain"))
            Picasso.with(getContext()).load(R.drawable.bg_rainy).fit().centerCrop().into(cardBackground);
        else
            Picasso.with(getContext()).load(R.drawable.bg_sunny).fit().centerCrop().into(cardBackground);
        Picasso.with(getContext()).load("http://".concat(weather.getCurrent().getCondition().getIconLink().substring(2, weather.getCurrent().getCondition().getIconLink().length()))).into(conditionIcon);
        dateTxt.setText(R.string.today);
        conditionTxt.setText(weather.getCurrent().getCondition().getConditionText());
        maxTempTxt.setText(String.format("%s°", weather.getForecast().getForecastDay().get(0).getDay().getMaxtempC()));
        minTempTxt.setText(String.format("%s°", weather.getForecast().getForecastDay().get(0).getDay().getMintempC()));


    }

    public void fetchList() {
        if (adapter == null) {
            adapter = new WeatherAdapter(weather.getForecast());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter.setOnItemClickListener(new WeatherAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View itemView, int position) {
                    onItemClicked(position);

                }
            });
        } else {
            adapter.setForecast(weather.getForecast());
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startWeatherTask();
            }
        });
        countryNameTxt.setText(cityName);
        recyclerView.setBackgroundColor(Color.parseColor("#303F9F"));

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(WEATHER, weather);
        super.onSaveInstanceState(outState);

    }

    public void startWeatherTask() {
        task = new WeatherTask();
        task.setListener(this);
        task.execute(cityName);
    }

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
    public void onPreExecuteConcluded() {
        Log.e("onPreExecuteConcluded", "Hi!");
        ((MainActivity) getActivity()).progressBar.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).container.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPostExecuteConcluded(Weather result, boolean isSuccessful) {
        this.weather = result;
        Log.e("onPostExecuteConcluded", "Hi!");

        /*
        to make the code more readable , I separate the ui into two methods



        */
        LoadDate(weather, isSuccessful);
        /*
        if(weather==nu)
          fetchDataCard();
        fetchList();
        ((MainActivity)getActivity()).progressBar.setVisibility(View.INVISIBLE);
        ((MainActivity)getActivity()).container.setVisibility(View.VISIBLE);
   */
    }

    private void LoadDate(Weather weather, boolean isSuccessful) {
        ((MainActivity) getActivity()).progressBar.setVisibility(View.INVISIBLE);
        ((MainActivity) getActivity()).container.setVisibility(View.VISIBLE);
        if (isSuccessful) {
            if (weather.getCurrent() != null) {
                fetchDataCard();
                fetchList();
            } else {
                loadErrorMessage(getString(R.string.something_wrong));
            }
        } else {
            if (Util.isNetworkAvailable(getContext()))
                loadErrorMessage(getString(R.string.server_down));
            else {
                loadErrorMessage(getString(R.string.check_your_internet_connection));

            }
        }

    }

    public void loadErrorMessage(String message) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, ErrorMessageFragment.newInstance(message)).commit();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (task != null && task.getStatus() == AsyncTask.Status.RUNNING)
            task.cancel(true);
    }

    public interface OnItemClickedListener {
        // TODO: Update argument type and name
        void showDetails(int pos);

        void onRefreshAnchorButton();
    }
}
