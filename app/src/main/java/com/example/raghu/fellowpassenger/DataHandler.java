package com.example.raghu.fellowpassenger;

import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raghu on 22/09/16.
 */

/*Contains static variables
data here can be shared across all classes*/

public class DataHandler {
    public static List<LocationData> locations = null;
    public static LocationManager locationManager = null;
    public static String currentFragment = null;
    public static DbHandler dbHandler = null;
    public static FragmentManager fragmentManager = null;


    public static void initialiseLocationManager(LocationManager locManager){
        locationManager = locManager;
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
        return locations;
    }


}
