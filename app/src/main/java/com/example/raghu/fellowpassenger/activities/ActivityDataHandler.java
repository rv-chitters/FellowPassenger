package com.example.raghu.fellowpassenger.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.location.LocationManager;
import android.app.FragmentManager;
import android.os.IBinder;

import com.example.raghu.fellowpassenger.R;
import com.example.raghu.fellowpassenger.definations.DbHandler;
import com.example.raghu.fellowpassenger.definations.LocationData;
import com.example.raghu.fellowpassenger.fragments.MainFragment;
import com.example.raghu.fellowpassenger.interfaces.IServiceFunctions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raghu on 22/09/16.
 */

/*Contains static variables
data here can be shared across all classes*/

public class ActivityDataHandler {
    public static List<LocationData> locations = null;
    public static LocationManager locationManager = null;
    public static String currentFragment = null;
    public static DbHandler dbHandler = null;
    public static FragmentManager fragmentManager = null;
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

    public static void setCurrentFragment(String fName){
        currentFragment = fName;
    }

    public static void initialiseLocationsArray(){
        locations = new ArrayList<>();
    }

    public static LocationManager getLocationManager(){
        return locationManager;
    }

    public static String getCurrentFragment(){
        return currentFragment;
    }

    public static void initialiseFragmentManager(FragmentManager fm){
        fragmentManager = fm;
    }

    public static FragmentManager getFragmentManager(){
        return fragmentManager;
    }

    public static List<LocationData> getLocations(){
        if(locations == null){
            loadLocationData();
        }
        return locations;
    }

    public static void loadLocationData(){
        locations = dbHandler.getAllRecords();
    }

    public static void initialiseDbHandler(Context context){
        dbHandler = new DbHandler(context);
    }

    /*public static DbHandler getDbHandler(){
        return dbHandler;
    }*/

    public  static void  setService(IBinder binder){
        service = (IServiceFunctions)binder;

    }

    public static IServiceFunctions getService(){
        return  service;
    }

    public static void delete(int id){
        dbHandler.delete(id);
        loadLocationData();
        if(currentFragment  == "Main")
            fragmentManager.beginTransaction().replace(R.id.content_main,new MainFragment()).commit();
        service.reloadData();
    }

    public static void updateStatus(int id,boolean val){
        dbHandler.updateStatus(id,val);
        loadLocationData();
        if(currentFragment  == "Main")
            fragmentManager.beginTransaction().replace(R.id.content_main,new MainFragment()).commit();
        service.reloadData();
    }

    public static void insert(int id, String name, Double lat, Double lng, int status, Float distance){
        dbHandler.insert(id,name,lat,lng,status,distance);
        loadLocationData();
        service.reloadData();
    }
}
