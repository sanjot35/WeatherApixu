package com.opensooq.weatherapp.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Omar AlTamimi on 9/1/2016.
 */
public class Pref {
    public static final String PREFS_NAME = "openSooqApp";
    public static final String FAVORITES = "Favorite";
    public static final String SELECTED = "lastSelected";
    private static SharedPreferences settings;
    private static ArrayList<String> favorites;

    /*


    this singleton is inspired by Picasso Library

     */
    private static volatile Pref singleton = null;

    private Pref(Context context) {
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

    }

    public static Pref with(Context context) {
        if (singleton == null) {
            singleton = new Pref(context);
        }
        return singleton;
    }

    public void storeLastSelected(String lastSelected) {
        settings.edit().putString(SELECTED, lastSelected).apply();
    }

    public void storeFavorites(List<String> favorites) {
        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);
        settings.edit().putString(FAVORITES, jsonFavorites).apply();
    }

    public String getLastSelected() {
        return settings.getString(SELECTED, "Amman");
    }

    public ArrayList<String> loadFavorites() {
        //singleton approach
        if (favorites == null)
            if (settings.contains(FAVORITES)) {
                String jsonFavorites = settings.getString(FAVORITES, null);
                Gson gson = new Gson();
                String[] favoriteItems = gson.fromJson(jsonFavorites, String[].class);
                favorites = (ArrayList<String>) Arrays.asList(favoriteItems);
                //favorites = new ArrayList(favorites);
            } else
                return null;
        return favorites;
    }

    public void addFavorite(String beanSampleList) {
        List<String> favorites = loadFavorites();
        if (favorites == null)
            favorites = new ArrayList<>();
        favorites.add(beanSampleList);
        storeFavorites(favorites);
    }

    public void removeFavorite(String beanSampleList) {
        ArrayList<String> favorites = loadFavorites();
        if (favorites != null) {
            favorites.remove(beanSampleList);
            storeFavorites(favorites);
        }
    }
}