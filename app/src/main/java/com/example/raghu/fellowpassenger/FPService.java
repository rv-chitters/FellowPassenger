package com.example.raghu.fellowpassenger;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
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

        // Starts the thread that sends the Location regularly to the clients
        new Thread(gpsRunner).start();
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

    private void updateLocationOnClient(final Activity client) {
        // Get the location
        final Location dummyLocation = getRealLocation();
        try {

            // Call the setLocation in the main thread (ui thread) as it updates
            // the ui.
            // If we don't use the handler and just exec the code in the run() we
            // get a CalledFromWrongThreadException
            Handler lo = new Handler(Looper.getMainLooper());
            lo.post(new Runnable() {

                public void run() {
                    IListenerFunctions callback = clients.get(client);
                    callback.setLocation(dummyLocation.getLatitude(), dummyLocation.getLongitude());
                }
            });

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    // You might want to change that thing :)
    private Location getRealLocation() {
        Location dummyLocation = new Location("dummy provider");
        dummyLocation.setLatitude(Math.random());
        dummyLocation.setLongitude(Math.random());
        return dummyLocation;
    }

    private Runnable gpsRunner = new Runnable() {
        public void run() {

            while (true) {
                for (Activity client : clients.keySet()) {
                    updateLocationOnClient(client);
                }

                // Wait a little to NOT spam the
                // client activity
                SystemClock.sleep(ONE_SECOND * 2);
            }

        }
    };

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
    }
}
