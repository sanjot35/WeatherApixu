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
public class PreferencesManager {
    private static final String PREFS_NAME = "openSooqApp";
    private static final String FAVORITES = "Favorite";
    private static final String SELECTED = "lastSelected";
    private static final String FIRST_TIME = "my_first_time";
    private static SharedPreferences settings;
    private static List<String> favorites;
    private static PreferencesManager singleton = null;

    private PreferencesManager(Context context) {
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

    }

    public static synchronized PreferencesManager getInstance(Context context) {
        if (singleton == null) {
            synchronized (PreferencesManager.class) {
                if (singleton == null) {
                    singleton = new PreferencesManager(context);
                }
            }
        }
        return singleton;
    }

    public void storeLastSelectedCity(String lastSelected) {
        settings.edit().putString(SELECTED, lastSelected).apply();
    }

    public boolean isFirstTime() {
        return settings.getBoolean(FIRST_TIME, true);
    }

    public void setFirstTime(boolean flag) {
        settings.edit().putBoolean(FIRST_TIME, flag).apply();
    }

    public void storeList(List<String> favorites) {
        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);
        settings.edit().putString(FAVORITES, jsonFavorites).apply();
    }

    public String getLastSelectedCity() {
        return settings.getString(SELECTED, "Amman");
    }

    public List<String> loadList() {
        //singleton approach
        if (favorites == null)
            if (settings.contains(FAVORITES)) {
                String jsonFavorites = settings.getString(FAVORITES, null);
                Gson gson = new Gson();
                String[] listItems = gson.fromJson(jsonFavorites, String[].class);
                favorites = Arrays.asList(listItems);
                favorites = new ArrayList<>(favorites);
            }
        return favorites;
    }

    public void addItem(String item) {
        favorites = loadList();
        if (favorites == null)
            favorites = new ArrayList<>();
        favorites.add(item);
        storeList(favorites);
    }

    public void removeItem(String item) {
        favorites = loadList();
        if (favorites != null) {
            favorites.remove(item);
            storeList(favorites);
        }
    }
}