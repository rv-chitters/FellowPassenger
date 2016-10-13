package com.example.raghu.fellowpassenger.services;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

import com.example.raghu.fellowpassenger.definations.DbHandler;
import com.example.raghu.fellowpassenger.definations.LocationData;
import com.example.raghu.fellowpassenger.interfaces.IServiceFunctions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raghu on 13/10/16.
 */
public class ServiceDataHandler {
    public static List<LocationData> locations = null;
    public static LocationManager locationManager = null;
    public static DbHandler dbHandler = null;
    public static Context context = null;
    public static IServiceFunctions service = null;

    public static void initialiseLocationManager(LocationManager locManager){
        locationManager = locManager;
    }

    public static void setContext(Context context1){
        context = context1;
    }

    public static  Context getContext(){
        return context;
    }

    public static void initialiseLocationsArray(){
        locations = new ArrayList<>();
    }

    public static LocationManager getLocationManager(){
        return locationManager;
    }

    public static List<LocationData> getLocations(){
        if(locations == null){
            loadLocationData();
        }
        return locations;
    }

    public static void loadLocationData(){
        locations = dbHandler.getAllRecords();
        for (LocationData location:locations) {
            location.getPosition();
        }
    }

    public static void initialiseDbHandler(Context context){
        dbHandler = new DbHandler(context);
    }

    public static DbHandler getDbHandler(){
        return dbHandler;
    }

    public static IServiceFunctions getService(){
        return  service;
    }

    public static void updateDistance(int id,float distance){
        dbHandler.updateDistance(id,distance);
        Intent i = new Intent("android.intent.action.MAIN").putExtra("some_msg", "I will be sent!");
        context.sendBroadcast(i);
    }
}
