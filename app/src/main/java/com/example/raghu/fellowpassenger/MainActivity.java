package com.example.raghu.fellowpassenger;

import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.raghu.fellowpassenger.fragments.MainFragment;


public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataHandler.initialiseLocationManager((LocationManager) getSystemService(LOCATION_SERVICE));
        DataHandler.initialiseLocationsArray();
        DataHandler.initialiseFragmentManager(getFragmentManager());
        Log.d("check","before setting activity_main");
        setContentView(R.layout.activity_main);
        Log.d("check","before calling fragment manager");
        Log.d("check",getResources().getResourceName(R.id.content_main));
        Log.d("check","bye");
        getFragmentManager().beginTransaction().replace(R.id.content_main,new MainFragment()).commit();
    }
}
