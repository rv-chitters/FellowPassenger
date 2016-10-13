package com.example.raghu.fellowpassenger.activities;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.raghu.fellowpassenger.services.FPService;
import com.example.raghu.fellowpassenger.R;
import com.example.raghu.fellowpassenger.fragments.MainFragment;
import com.example.raghu.fellowpassenger.interfaces.IListenerFunctions;


public class MainActivity extends FragmentActivity {

    private BroadcastReceiver mReceiver;

    private ServiceConnection svcConn = new ServiceConnection() {

        // We register ourselves to the
        // service so that we can receive
        // updates
        public void onServiceConnected(ComponentName className, IBinder binder) {
            ActivityDataHandler.setService(binder);

            try {
                ActivityDataHandler.getService().registerActivity(MainActivity.this, listener);
            } catch (Throwable t) {
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            ActivityDataHandler.setService(null);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDataHandler.setContext(getBaseContext());
        ActivityDataHandler.initialiseLocationManager((LocationManager) getSystemService(LOCATION_SERVICE));
        ActivityDataHandler.initialiseLocationsArray();
        ActivityDataHandler.initialiseFragmentManager(getFragmentManager());
        ActivityDataHandler.initialiseDbHandler(getBaseContext());
        ActivityDataHandler.loadLocationData();
        setContentView(R.layout.activity_main);
        getFragmentManager().beginTransaction().replace(R.id.content_main, new MainFragment()).commit();

        startService(new Intent(this, FPService.class));
        bindService(new Intent(this, FPService.class), svcConn, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(
                "android.intent.action.MAIN");

        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                //extract our message from intent
                String msg_for_me = intent.getStringExtra("some_msg");
                //log our message value
                Log.i("InchooTutorial", msg_for_me);
                ActivityDataHandler.loadLocationData();
                if(ActivityDataHandler.getCurrentFragment() == "Main"){
                    ActivityDataHandler.getFragmentManager().beginTransaction().replace(R.id.content_main,new MainFragment()).commit();
                }

            }
        };
        //registering our receiver
        this.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(this.mReceiver);
    }

    private void setInterface(double lati, double loni) {
        Toast.makeText(getBaseContext(),Double.toString(lati) + " " + Double.toString(loni),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Deactivate updates to us so that we dont get callbacks no more.
        if(ActivityDataHandler.getService() != null)
            ActivityDataHandler.getService().unregisterActivity(this);

        // Finally stop the service
        unbindService(svcConn);
    }

    // This is essentially the callback that the service uses to notify us about
    // changes.
    private IListenerFunctions listener = new IListenerFunctions() {
        public void setLocation(double lat, double lon) {
            setInterface(lat, lon);
        }
    };
}

