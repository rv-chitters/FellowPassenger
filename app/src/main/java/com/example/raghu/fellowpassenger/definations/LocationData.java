package com.example.raghu.fellowpassenger.definations;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.example.raghu.fellowpassenger.R;
import com.example.raghu.fellowpassenger.activities.MainActivity;
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
    public long  waitTime = 300000;
    public Thread thread = null;
    Handler mHandler = null;
    private Runnable posGetter = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                return;
            }
            if(Thread.interrupted()){
                return;
            }
            Message message = mHandler.obtainMessage(1, "bar");
            message.sendToTarget();
        }
    };
    public final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location currentLocation) {
            //your code here
            if (location.getAccuracy() < 500) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context,"lol maxx",Toast.LENGTH_SHORT).show();
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
                distance = currentLocation.distanceTo(location) / 1000;
                ServiceDataHandler.updateDistance(id,distance);
                waitTime = getWaitTime(distance);
                if(!isActive){
                    return;
                }
                if(distance < 1){
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(context)
                                    .setSmallIcon(R.drawable.cast_ic_notification_small_icon)
                                    .setContentTitle("Fellow Passenger")
                                    .setContentText(LocationName + " " + String.format("%.2g%n", distance)+"Kms !!!")
                                    .setSound(notification)
                                    .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
                    Intent resultIntent = new Intent(context, MainActivity.class);
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                    stackBuilder.addParentStack(MainActivity.class);
                    stackBuilder.addNextIntent(resultIntent);
                    PendingIntent resultPendingIntent =
                            stackBuilder.getPendingIntent(
                                    0,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );
                    mBuilder.setContentIntent(resultPendingIntent);
                    NotificationManager mNotificationManager =
                            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(id, mBuilder.build());
                    Log.d("some_xyz","here 4");
                    ServiceDataHandler.updateStatus(id,!isActive);
                }else {
                    Log.d("some_xyz","here 1");
                    thread = new Thread(posGetter);
                    thread.start();
                }
            }
            /*Toast toast = Toast.makeText(context, location.getAccuracy() +" onLocationChanged " ,Toast.LENGTH_SHORT);
            toast.show();*/


        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            /*Toast toast = Toast.makeText(context, s +" onStatusChanged " +i,Toast.LENGTH_SHORT);
            toast.show();*/

        }

        @Override
        public void onProviderEnabled(String s) {
            /*Toast toast = Toast.makeText(context, s +" onProviderEnabled " ,Toast.LENGTH_SHORT);
            toast.show();*/
            getPosition();
        }

        @Override
        public void onProviderDisabled(String s) {
            /*Toast toast = Toast.makeText(context, s +" onProviderDisabled " ,Toast.LENGTH_SHORT);
            toast.show();*/
        }
    };

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
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                // This is where you do your work in the UI thread.
                // Your worker tells you in the message what to do.
                getPosition();
            }
        };
    }

    public void stopThread(){
        if(thread != null){
            thread.interrupt();
        }
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
            /*Toast toast = Toast.makeText(context, "failed", Toast.LENGTH_SHORT);
            toast.show();*/
            return;
        }
        /*Toast toast = Toast.makeText(context, LocationName + " in getPosition ", Toast.LENGTH_SHORT);
        toast.show();*/
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                0,  mLocationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                0,  mLocationListener);
    }

    @Override
    public String toString() {
        return LocationName + " " + distance + "Km";
    }

    public long getWaitTime(float d){
        if(d <= 2){
            return 60 * 1000;
        }
        if(d >= 100){
            return 30 * 60 * 1000;
        }
        return (long) ((1 + (d - 2)*(29/98)) * 60 * 100);
    }

}
