package com.example.raghu.fellowpassenger;

import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.example.raghu.fellowpassenger.fragments.MainFragment;


public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataHandler.initialiseLocationManager((LocationManager) getSystemService(LOCATION_SERVICE));
        DataHandler.initialiseLocationsArray();
        DataHandler.initialiseFragmentManager(getFragmentManager());
        setContentView(R.layout.activity_main);
        getFragmentManager().beginTransaction().replace(R.id.content_main,new MainFragment()).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
