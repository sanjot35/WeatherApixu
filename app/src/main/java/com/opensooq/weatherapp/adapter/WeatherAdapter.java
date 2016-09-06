package com.opensooq.weatherapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.opensooq.weatherapp.R;
import com.opensooq.weatherapp.common.Util;
import com.opensooq.weatherapp.model.Forecast;
import com.opensooq.weatherapp.model.ForecastDay;
import com.squareup.picasso.Picasso;

/**
 * Created by Omar AlTamimi on 9/1/2016.
 */
public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherHolder> {
    private static OnItemClickListener listener;
    public Forecast mForecast;

    public WeatherAdapter(Forecast forecast) {
        this.mForecast = forecast;
    }

    public Forecast getForecast() {
        return mForecast;
    }

    public void setForecast(Forecast mForecast) {
        this.mForecast = mForecast;
    }

    @Override
    public WeatherHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_forcast, parent, false);
        // Return a new holder instance
        return new WeatherHolder(contactView);
    }

    @Override
    public void onBindViewHolder(WeatherHolder holder, int position) {
        holder.bind(position, mForecast);
    }

    @Override
    public int getItemCount() {
        return mForecast.getForecastDay().size();
    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        WeatherAdapter.listener = listener;
    }

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public static class WeatherHolder extends RecyclerView.ViewHolder {

        ForecastDay forecastDay;
        private ImageView conditionIcon;
        private TextView dateItem, conditionItem, maxTempItem, minTempItem;
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview

        public WeatherHolder(final View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            conditionIcon = (ImageView) itemView.findViewById(R.id.ic_condition_item);
            dateItem = (TextView) itemView.findViewById(R.id.date_item);
            conditionItem = (TextView) itemView.findViewById(R.id.condition_item_txt);
            maxTempItem = (TextView) itemView.findViewById(R.id.max_temp_item);
            minTempItem = (TextView) itemView.findViewById(R.id.min_temp_item);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null)
                        listener.onItemClick(itemView, getLayoutPosition());
                }
            });
        }

        public void bind(int position, Forecast forecast) {
            forecastDay = forecast.getForecastDay().get(position);
            Picasso.with(itemView.getContext()).load("http://".concat(forecastDay.getDay().getCondition().getIconLink().substring(2, forecastDay.getDay().getCondition().getIconLink().length()))).fit().centerCrop().into(conditionIcon);
            dateItem.setText(Util.ConvertDateToDay(forecastDay.getDate()));
            conditionItem.setText(forecastDay.getDay().getCondition().getConditionText());
            maxTempItem.setText(String.format("%s °", forecastDay.getDay().getMaxtempC()));
            minTempItem.setText(String.format("%s °", forecastDay.getDay().getMintempC()));

        }
    }
}
