package com.cheqin.booking.gcm;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.cheqin.booking.Bean.Hoteltypebean;
import com.cheqin.booking.Bean.RoomTypeBean;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Suhas on 20-Feb-16.
 */
public class SharedPreferenceRoom {
    public static final String PREFS_NAME = "PRODUCT_ROOM";
    public static final String FAVORITES = "Product_ROOMFavorit1";

    public SharedPreferenceRoom() {
        super();
    }

    // This four methods are used for maintaining favorites.
    public void saveFavorites(Context context, String favorites) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putString(FAVORITES, favorites);
        editor.commit();
    }

   /* public void addFavorite(Context context, RoomTypeBean product) {
        List<RoomTypeBean> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<RoomTypeBean>();
        favorites.add(product);
        saveFavorites(context, favorites);
    }*/
/* public void removeFavorite(Context context, RoomTypeBean product) {
        ArrayList<RoomTypeBean> favorites = getFavorites(context);
        if (favorites != null) {
            favorites.remove(product);
            saveFavorites(context, favorites);
        }
    }*/


    public ArrayList<RoomTypeBean> getFavorites(Context context) {
        SharedPreferences settings;
        List<RoomTypeBean> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            RoomTypeBean[] favoriteItems = gson.fromJson(jsonFavorites,
                    RoomTypeBean[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<RoomTypeBean>(favorites);
        } else
            return null;

        return (ArrayList<RoomTypeBean>) favorites;
    }
}


