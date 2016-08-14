package com.example.raghu.fellowpassenger;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import android.os.Bundle;

import android.widget.Toast;
import android.location.LocationListener;

import com.example.raghu.fellowpassenger.fragments.LocationData;
import com.example.raghu.fellowpassenger.fragments.MainFragment;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    public static List<LocationData> locations;
    public static LocationManager mLocationManager;
    public static  android.app.FragmentManager fm;
    public static String currentFragmnet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locations = new ArrayList<LocationData>();
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        fm = getFragmentManager();
        setContentView(R.layout.activity_main);
        fm.beginTransaction().replace(R.id.content_main,new MainFragment()).commit();
        currentFragmnet = "Main";
    }


}
