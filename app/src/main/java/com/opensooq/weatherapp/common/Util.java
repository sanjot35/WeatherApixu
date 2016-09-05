package com.opensooq.weatherapp.common;

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
        String finalDay=format2.format(dt1);
        return finalDay;
    }
}
