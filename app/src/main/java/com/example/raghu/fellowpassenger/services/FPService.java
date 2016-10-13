package com.example.raghu.fellowpassenger.services;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

import com.example.raghu.fellowpassenger.interfaces.IListenerFunctions;
import com.example.raghu.fellowpassenger.interfaces.IServiceFunctions;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by raghu on 02/10/16.
 */

public class FPService  extends Service {
    private int count = 0;
    private static final int  ONE_SECOND = 1000;
    private Map<Activity, IListenerFunctions> clients = new ConcurrentHashMap<Activity, IListenerFunctions>();
    private final Binder binder = new LocalBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        ServiceDataHandler.setContext(getBaseContext());
        ServiceDataHandler.initialiseLocationManager((LocationManager) getSystemService(LOCATION_SERVICE));
        ServiceDataHandler.initialiseLocationsArray();
        ServiceDataHandler.initialiseDbHandler(getBaseContext());
        ServiceDataHandler.loadLocationData();

        // Starts the thread that sends the Location regularly to the clients
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        count++;
        //Toast.makeText(getBaseContext(), count , Toast.LENGTH_SHORT).show();

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        /*Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);*/

        // If we get killed, after returning from here, restart

        Log.d("service_asdf",Integer.toString(count));
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return (binder);
    }


    // You might want to change that thing :)
    private Location getRealLocation() {
        Location dummyLocation = new Location("dummy provider");
        dummyLocation.setLatitude(Math.random());
        dummyLocation.setLongitude(Math.random());
        return dummyLocation;
    }


    public class LocalBinder extends Binder implements IServiceFunctions {

        // Registers a Activity to receive updates
        public void registerActivity(Activity activity, IListenerFunctions callback) {
            clients.put(activity, callback);
        }

        public void unregisterActivity(Activity activity) {
            clients.remove(activity);
        }

        public Location getLocation() {
            return getRealLocation();
        }

        public void reloadData(){
            ServiceDataHandler.loadLocationData();
        }
    }
}
