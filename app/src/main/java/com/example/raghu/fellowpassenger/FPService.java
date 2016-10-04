package com.example.raghu.fellowpassenger;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by raghu on 02/10/16.
 */
public class FPService extends Service {
    public int create_count = 0;
    public int bind_count = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        bind_count++;
        Toast.makeText(getBaseContext(),bind_count + "in binder",Toast.LENGTH_SHORT).show();
        Log.d("serv", bind_count + "in binder");
        return null;
    }

    @Override
    public void onCreate() {
        create_count++;
        Toast.makeText(getBaseContext(),create_count + "in oncreate",Toast.LENGTH_SHORT).show();
        Log.d("serv", create_count + "in oncreate");
        super.onCreate();
    }
}
