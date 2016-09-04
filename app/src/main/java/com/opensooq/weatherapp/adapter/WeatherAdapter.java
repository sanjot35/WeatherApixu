package com.opensooq.weatherapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.opensooq.weatherapp.R;
import com.opensooq.weatherapp.common.Const;
import com.opensooq.weatherapp.common.Util;
import com.opensooq.weatherapp.model.Forecast;
import com.opensooq.weatherapp.model.ForecastDay;
import com.opensooq.weatherapp.model.Hour;
import com.squareup.picasso.Picasso;

/**
 * Created by Omar AlTamimi on 9/1/2016.
 */
public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherHolder>  {
   public Forecast mForecast;
    int mType;

    private int selectedPosition;
    /*
    type for making the recycler more reusable

    type = 0  it's for daily items
    type = 1  it's for hourly items

     */
    public WeatherAdapter(Forecast forecast,int type,int selectedDay) {
        this.mForecast = forecast;
        this.mType= type;
        this.selectedPosition = selectedDay;
    }

    @Override
    public WeatherHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_forcast, parent, false);

        // Return a new holder instance
        WeatherHolder viewHolder = new WeatherHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WeatherHolder holder, int position) {
        holder.bind(position,mType,mForecast,selectedPosition);
    }

    @Override
    public int getItemCount() {
        return mType== Const.DAILY?mForecast.getForecastDay().size():mForecast.getForecastDay().get(0).getHour().size();
    }
    private static OnItemClickListener listener;
    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        WeatherAdapter.listener = listener;
    }
    public static class WeatherHolder extends RecyclerView.ViewHolder {

        private ImageView icConditiotion;
        private TextView dateItem,conditionItem,maxTempItem,minTempItem;
        Hour hour;
        ForecastDay forecastDay;
        // Your holder should contain a member variable
        // for any view that will be set as you render a row

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview


        public WeatherHolder(final View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            icConditiotion = (ImageView) itemView.findViewById(R.id.ic_condition_item);
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
        public void bind(int position,int type,Forecast forecast,int selectedDay){
            if(type==Const.DAILY) {
                forecastDay = forecast.getForecastDay().get(position);
                Picasso.with(itemView.getContext()).load("http://".concat(forecastDay.getDay().getCondition().getIconLink().substring(2,forecastDay.getDay().getCondition().getIconLink().length()))).fit().centerCrop().into(icConditiotion);
                dateItem.setText(Util.ConvertDateToDay(forecastDay.getDate()));
                conditionItem.setText(forecastDay.getDay().getCondition().getConditionText());
                maxTempItem.setText(forecastDay.getDay().getMaxtempC()+""+" \u00B0");
                minTempItem.setText(forecastDay.getDay().getMintempC()+""+" \u00B0");

            }
                else
            {
                hour  = forecast.getForecastDay().get(selectedDay).getHour().get(position);
                Picasso.with(itemView.getContext()).load("http://".concat(hour.getCondition().getIconLink().substring(2,hour.getCondition().getIconLink().length()))).fit().centerCrop().into(icConditiotion);
                dateItem.setText(
                        hour.getTime().split(" ")[1]);
                conditionItem.setText(hour.getCondition().getConditionText());
                maxTempItem.setText(hour.getTempC()+""+" \u00B0");
                minTempItem.setText("");

            }

        }
    }
}
