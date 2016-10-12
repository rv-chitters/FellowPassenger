package com.example.raghu.fellowpassenger;


import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.raghu.fellowpassenger.fragments.MainFragment;
import com.example.raghu.fellowpassenger.interfaces.IListenerFunctions;
import com.example.raghu.fellowpassenger.interfaces.IServiceFunctions;


public class MainActivity extends FragmentActivity {

    private IServiceFunctions service = null;

    private ServiceConnection svcConn = new ServiceConnection() {

        // We register ourselves to the
        // service so that we can receive
        // updates
        public void onServiceConnected(ComponentName className, IBinder binder) {
            service = (IServiceFunctions) binder;

            try {
                service.registerActivity(MainActivity.this, listener);
            } catch (Throwable t) {
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            service = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataHandler.setContext(getBaseContext());
        DataHandler.initialiseLocationManager((LocationManager) getSystemService(LOCATION_SERVICE));
        DataHandler.initialiseLocationsArray();
        DataHandler.initialiseFragmentManager(getFragmentManager());
        DataHandler.initialiseDbHandler(getBaseContext());
        DataHandler.loadLocationData();
        setContentView(R.layout.activity_main);
        getFragmentManager().beginTransaction().replace(R.id.content_main, new MainFragment()).commit();

        startService(new Intent(this, FPService.class));
        bindService(new Intent(this, FPService.class), svcConn, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void setInterface(double lati, double loni) {
        Toast.makeText(getBaseContext(),Double.toString(lati) + " " + Double.toString(loni),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Deactivate updates to us so that we dont get callbacks no more.
        if(service != null)
            service.unregisterActivity(this);

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

