package com.opensooq.weatherapp.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.opensooq.weatherapp.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Omar AlTamimi on 9/1/2016.
 */
public class Util {
    public  static String ConvertDateToDay(String input_date){
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date dt1= null;
        try {
            dt1 = format1.parse(input_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat format2 = new SimpleDateFormat("EEEE", Locale.getDefault());
        return format2.format(dt1);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static int weatherConditionBackground(String condition) {
        if (condition.contains("cloudy") || condition.contains("cloud"))
            return R.drawable.bg_cloudy;
        else if (condition.contains("rainy") || condition.contains("rain"))
            return R.drawable.bg_rainy;
        else
            return R.drawable.bg_sunny;
    }
}
