package com.cheqin.booking.gcm;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.cheqin.booking.Bean.Hoteltypebean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Suhas on 20-Feb-16.
 */
public class SharedPreference1 {
    public static final String PREFS_NAME = "PRODUCT_AP1";
    public static final String FAVORITES = "Product_Favorit1";

    public SharedPreference1() {
        super();
    }

    // This four methods are used for maintaining favorites.
    public void saveFavorites(Context context, List<Hoteltypebean> favorites) {
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

    public void addFavorite(Context context, Hoteltypebean product) {
        List<Hoteltypebean> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<Hoteltypebean>();
        favorites.add(product);
        saveFavorites(context, favorites);
    }

    public void removeFavorite(Context context, Hoteltypebean product) {
        ArrayList<Hoteltypebean> favorites = getFavorites(context);
        if (favorites != null) {
            favorites.remove(product);
            saveFavorites(context, favorites);
        }
    }

    public ArrayList<Hoteltypebean> getFavorites(Context context) {
        SharedPreferences settings;
        List<Hoteltypebean> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            Hoteltypebean[] favoriteItems = gson.fromJson(jsonFavorites,
                    Hoteltypebean[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<Hoteltypebean>(favorites);
        } else
            return null;

        return (ArrayList<Hoteltypebean>) favorites;
    }
}


