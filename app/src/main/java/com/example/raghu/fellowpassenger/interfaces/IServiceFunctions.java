package com.example.raghu.fellowpassenger.interfaces;

import android.app.Activity;
import android.location.Location;

/**
 * Created by raghu on 12/10/16.
 */
public interface IServiceFunctions {
    void registerActivity(Activity activity, IListenerFunctions callback);

    void unregisterActivity(Activity activity);

    Location getLocation();

}
