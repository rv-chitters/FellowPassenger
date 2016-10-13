package com.example.raghu.fellowpassenger.definations;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.example.raghu.fellowpassenger.services.ServiceDataHandler;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by raghu on 07/08/16.
 */
public class LocationData {
    public int id;
    public String LocationName = "";
    public LatLng latLng;
    public Location location;
    public float distance;
    public Context context;
    public boolean isActive = true;


    public LocationData(int id,String name,Double lat,Double lng,int status,Float distance){
        this.id = id;
        this.LocationName = name;
        this.latLng = new LatLng(lat,lng);
        this.isActive = status != 0;
        this.distance = distance;
        context = ServiceDataHandler.getContext();
        location = new Location(LocationName);
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
    }



    public void getPosition() {
        LocationManager locationManager =  ServiceDataHandler.getLocationManager();
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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                0,  mLocationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                0,  mLocationListener);
    }

    @Override
    public String toString() {
        return LocationName + " " + distance + "Km";
    }


    public final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location currentLocation) {
            //your code here
            if (location.getAccuracy() < 500) {
                distance = currentLocation.distanceTo(location) / 1000;
                ServiceDataHandler.updateDistance(id,distance);
                if(distance < 1){
                    isActive = false;

                    try {
                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                        Ringtone r = RingtoneManager.getRingtone(context, notification);
                        r.play();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

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
                ServiceDataHandler.getLocationManager().removeUpdates(mLocationListener);

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
