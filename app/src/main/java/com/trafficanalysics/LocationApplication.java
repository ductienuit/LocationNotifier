package com.trafficanalysics;


import android.app.Application;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.here.android.mpa.common.GeoCoordinate;
import com.trafficanalysics.modal.ArrayFavorite;
import com.trafficanalysics.modal.Favorite;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class LocationApplication extends Application {
    SharedPreferences  mInstanceShared;
    private String sharedPrefFile = "com.locationnotifier";
    public GeoCoordinate geoCoordinate;

    public static final String TAG = LocationApplication.class
            .getSimpleName();

    public ArrayFavorite arrFavotites = new ArrayFavorite();

    private static LocationApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mInstanceShared = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        loadDataFromShared();

    }

    public static synchronized LocationApplication getInstance() {
        return mInstance;
    }

    public void loadDataFromShared()
    {
        String json = null;
        json = mInstanceShared.getString("favorites","0");
        if(json!=null){
            if(json.equals("0")){
            }
            else {
                arrFavotites = new Gson().fromJson(json, ArrayFavorite.class);
            }
        }

    }

}
