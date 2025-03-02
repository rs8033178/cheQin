package com.cheqin.booking.gcm;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.cheqin.booking.Bean.Amenitiesbean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



/**
 * Created by VaibhaV on 08-Jan-16.
 */
public class SharedPreference {

    public static final String PREFS_NAME = "PRODUCT_AP";
    public static final String FAVORITES = "Product_Favorit";
    public static final String PREFS_API_KEY = "APIKEY";
    public static final String PREFS_SPLASH_KEY = "SPLASH";

    public SharedPreference() {
        super();
    }

    // This four methods are used for maintaining favorites.
    public void saveFavorites(Context context, List<Amenitiesbean> favorites) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FAVORITES, jsonFavorites);

        editor.commit();
    }

    // This four methods are used for maintaining favorites.
    public void setApiKey(Context context, String apikey) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_API_KEY,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString(PREFS_API_KEY, apikey);

        editor.commit();
    }
    public String getApiKey(Context context) {
        SharedPreferences settings;

        settings = context.getSharedPreferences(PREFS_API_KEY,
                Context.MODE_PRIVATE);

            return settings.getString(PREFS_API_KEY, null);

    }

    // This four methods are used for maintaining favorites.
    public void setSplashKey(Context context) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_SPLASH_KEY,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putBoolean(PREFS_SPLASH_KEY, true);

        editor.commit();
    }
    public boolean getSplashKey(Context context) {
        SharedPreferences settings;

        settings = context.getSharedPreferences(PREFS_SPLASH_KEY,
                Context.MODE_PRIVATE);

        return settings.getBoolean(PREFS_SPLASH_KEY, false);

    }
            public void addFavorite(Context context, Amenitiesbean product) {
        List<Amenitiesbean> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<Amenitiesbean>();
        favorites.add(product);
        saveFavorites(context, favorites);
    }

    public void removeFavorite(Context context, Amenitiesbean product) {
        ArrayList<Amenitiesbean> favorites = getFavorites(context);
        if (favorites != null) {
            favorites.remove(product);
            saveFavorites(context, favorites);
        }
    }

    public ArrayList<Amenitiesbean> getFavorites(Context context) {
        SharedPreferences settings;
        List<Amenitiesbean> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            Amenitiesbean[] favoriteItems = gson.fromJson(jsonFavorites,
                    Amenitiesbean[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<Amenitiesbean>(favorites);
        } else
            return null;

        return (ArrayList<Amenitiesbean>) favorites;
    }
}
