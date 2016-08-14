package com.example.raghu.fellowpassenger.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.example.raghu.fellowpassenger.MainActivity;
import com.example.raghu.fellowpassenger.R;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by raghu on 07/08/16.
 */
public class LocationData {
    String LocationName = "";
    LatLng latLng;
    public Location location;
    public float distance;
    public Context context;
    public static LocationManager mLocationManager;

    public LocationData(String name, LatLng l, Context cntxt) {
        LocationName = name;
        latLng = l;
        location = new Location(name);
        location.setLatitude(l.latitude);
        location.setLongitude(l.longitude);
        context = cntxt;
    }

    public void getPosition() {
        mLocationManager = (LocationManager) MainActivity.mLocationManager;
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast toast = Toast.makeText(context, "failed", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        Toast toast = Toast.makeText(context, LocationName + " in getPosition ", Toast.LENGTH_SHORT);
        toast.show();
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                0, (LocationListener) mLocationListener);
    }

    @Override
    public String toString() {
        return LocationName + " " + distance + "Km";
    }


    public final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location currentLocation) {
            //your code here
            if (location.getAccuracy() < 100) {
                distance = currentLocation.distanceTo(location) / 1000;
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mLocationManager.removeUpdates(mLocationListener);
                if(MainActivity.currentFragmnet == "Main")
                    MainActivity.fm.beginTransaction().replace(R.id.content_main,new MainFragment()).commit();
            }
            Toast toast = Toast.makeText(context, location.getAccuracy() +" onLocationChanged " ,Toast.LENGTH_SHORT);
            toast.show();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            Toast toast = Toast.makeText(context, s +" onStatusChanged " +i,Toast.LENGTH_SHORT);
            toast.show();

        }

        @Override
        public void onProviderEnabled(String s) {
            Toast toast = Toast.makeText(context, s +" onProviderEnabled " ,Toast.LENGTH_SHORT);
            toast.show();
        }

        @Override
        public void onProviderDisabled(String s) {
            Toast toast = Toast.makeText(context, s +" onProviderDisabled " ,Toast.LENGTH_SHORT);
            toast.show();
        }
    };

}
