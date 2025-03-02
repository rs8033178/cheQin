package com.cheqin.booking.gcm;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.cheqin.booking.Bean.NotificationBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by user on 04-03-2016.
 */
public class NotifySharedPreference {

    public static final String PREFS_NAME = "PRODUCT_API";
    public static final String FAVORITES = "Product_Favorite";

    public NotifySharedPreference() {
        super();
    }

    // This four methods are used for maintaining favorites.
    public void saveFavorites(Context context, List<NotificationBean> favorites) {
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

    public void addFavorite(Context context, NotificationBean product) {
        List<NotificationBean> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<NotificationBean>();
        favorites.add(product);
        saveFavorites(context, favorites);
    }

    public void removeFavorite(Context context, NotificationBean product) {
        ArrayList<NotificationBean> favorites = getFavorites(context);
        if (favorites != null) {
            favorites.remove(product);
            saveFavorites(context, favorites);
        }
    }

    public ArrayList<NotificationBean> getFavorites(Context context) {
        SharedPreferences settings;
        List<NotificationBean> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            NotificationBean[] favoriteItems = gson.fromJson(jsonFavorites,
                    NotificationBean[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<NotificationBean>(favorites);
        } else
            return null;

        return (ArrayList<NotificationBean>) favorites;
    }




    public void clearSharedPrefrence(Context context) {
        SharedPreferences settings;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        settings.edit().clear().commit();
    }
}
