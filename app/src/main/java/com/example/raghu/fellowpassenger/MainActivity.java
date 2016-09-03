package com.example.raghu.fellowpassenger;

import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;

import android.os.Bundle;

import com.example.raghu.fellowpassenger.fragments.MainFragment;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    public static List<LocationData> locations;
    public static LocationManager mLocationManager;
    public static  android.app.FragmentManager fm;
    public static String currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locations = new ArrayList<LocationData>();
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        fm = getFragmentManager();
        setContentView(R.layout.activity_main);
        fm.beginTransaction().replace(R.id.content_main,new MainFragment()).commit();
        currentFragment = "Main";
    }


}
