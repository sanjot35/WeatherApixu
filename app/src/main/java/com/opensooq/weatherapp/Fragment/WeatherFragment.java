package com.opensooq.weatherapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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

import com.opensooq.weatherapp.Activities.CityListActivity;
import com.opensooq.weatherapp.Activities.MainActivity;
import com.opensooq.weatherapp.R;
import com.opensooq.weatherapp.adapter.WeatherAdapter;
import com.opensooq.weatherapp.api.WeatherTask;
import com.opensooq.weatherapp.common.PreferencesManager;
import com.opensooq.weatherapp.common.Util;
import com.opensooq.weatherapp.model.ForecastDay;
import com.opensooq.weatherapp.model.WeatherResponse;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherFragment extends Fragment implements WeatherTask.MyAsyncTaskListener {
    private static final String WEATHER = "weatherResponse";
    private static final String COUNTRY_NAME = "country";
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
    @BindView(R.id.refresh)
    FloatingActionButton refreshButton;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    private String cityName;
    private OnItemClickedListener mListener;
    private WeatherResponse weatherResponse;
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

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        return view;
    }

    public void fetchDataCard() {
        String conditionStr;
        conditionStr = weatherResponse.getCurrent().getCondition().getConditionText();
        conditionStr = conditionStr.toLowerCase();
        Picasso.with(getActivity()).load(Util.weatherConditionBackground(conditionStr)).fit().centerCrop().into(cardBackground);
        Picasso.with(getContext()).load("http://".concat(weatherResponse.getCurrent().getCondition().getIconLink().substring(2, weatherResponse.getCurrent().getCondition().getIconLink().length()))).into(conditionIcon);
        dateTxt.setText(R.string.today);
        countryNameTxt.setText(PreferencesManager.getInstance(getContext()).getLastSelectedCity());
        conditionTxt.setText(conditionStr);
        maxTempTxt.setText(String.format("%s°", weatherResponse.getForecast().getForecastDay().get(0).getDay().getMaxtempC()));
        minTempTxt.setText(String.format("%s°", weatherResponse.getForecast().getForecastDay().get(0).getDay().getMintempC()));


    }

    public void fetchList() {
        if (adapter == null) {
            adapter = new WeatherAdapter(weatherResponse.getForecast());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter.setOnItemClickListener(new WeatherAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View itemView, int position) {
                    onItemClicked(position);

                }
            });
        } else {
            adapter.setForecast(weatherResponse.getForecast());
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            weatherResponse = savedInstanceState.getParcelable(WEATHER);
            cityName = savedInstanceState.getString("city");
        }
        if (weatherResponse == null)
            startWeatherTask(cityName);
        else {
            fetchList();
            fetchDataCard();
        }
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startWeatherTask(cityName);
            }
        });
        recyclerView.setBackgroundColor(Color.parseColor("#303F9F"));

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(WEATHER, weatherResponse);
        outState.putString("city", cityName);
        super.onSaveInstanceState(outState);

    }

    public void startWeatherTask(String cityName) {
        task = new WeatherTask();
        task.setListener(this);
        task.execute(cityName);
    }

    public void onItemClicked(int pos) {
        if (mListener != null) {
            mListener.showDetails(weatherResponse.getForecast().getForecastDay().get(pos));
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
    public void onPreExecuteTask() {
        ((MainActivity) getActivity()).progressBar.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).container.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPostExecuteTask(WeatherResponse result, boolean isSuccessful) {
        this.weatherResponse = result;


        /*
        to make the code more readable , I separate the ui into two methods



        */
        LoadDate(weatherResponse, isSuccessful);
        /*
        if(weatherResponse==nu)
          fetchDataCard();
        fetchList();
        ((MainActivity)getActivity()).progressBar.setVisibility(View.INVISIBLE);
        ((MainActivity)getActivity()).container.setVisibility(View.VISIBLE);
   */
    }

    public void LoadDate(WeatherResponse weatherResponse, boolean isSuccessful) {
        ((MainActivity) getActivity()).progressBar.setVisibility(View.INVISIBLE);
        ((MainActivity) getActivity()).container.setVisibility(View.VISIBLE);
        if (isSuccessful) {
            if (weatherResponse.getCurrent() != null) {
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
    public void onDestroy() {
        if (task != null && task.getStatus() == AsyncTask.Status.RUNNING)
            task.cancel(true);
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_add_city:
                getActivity().startActivity(new Intent(getActivity(), CityListActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public interface OnItemClickedListener {
        // TODO: Update argument type and name
        void showDetails(ForecastDay pos);

        void onRefreshAnchorButton();
    }
}
