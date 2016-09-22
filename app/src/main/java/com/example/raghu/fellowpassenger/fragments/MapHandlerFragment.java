package com.example.raghu.fellowpassenger.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.raghu.fellowpassenger.DataHandler;
import com.example.raghu.fellowpassenger.LocationData;
import com.example.raghu.fellowpassenger.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

/**
 * Created by raghu on 07/08/16.
 */
public class MapHandlerFragment extends Fragment implements OnMapReadyCallback {

    String searchTerm;
    Marker marker;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        searchTerm = getArguments().getString("searchTerm");
        View rootView =   inflater.inflate(R.layout.fragment_map,container,false);

        final Button ToMainButton = (Button) rootView.findViewById(R.id.fixLocation);

        ToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = getFragmentManager();
                MainFragment mp = new MainFragment();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    LocationData ld = new LocationData(searchTerm, marker.getPosition(), getContext());
                    ld.getPosition();
                    DataHandler.getLocations().add(ld);
                }
                fm.beginTransaction().replace(R.id.content_main,mp).commit();
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DataHandler.setCurrentFragment("Maps");

        MapFragment fragment = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            fragment =  (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        }
        fragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        Log.d("check",searchTerm);
        Geocoder geocoder = new Geocoder(this.getActivity().getBaseContext());
        List<Address> addresses = null;
        try {
            // Find a maximum of 3 locations with the name
            addresses = geocoder.getFromLocationName(searchTerm, 3);
            Log.d("check","try success");
        } catch (IOException e) {
            Log.d("check","try fail");
            e.printStackTrace();
        }
        if (addresses != null) {
            MarkerOptions opts = null;
            for (Address loc : addresses) {
                opts = new MarkerOptions()
                        .position(new LatLng(loc.getLatitude(), loc.getLongitude()))
                        .title(loc.getAddressLine(0));
                marker = googleMap.addMarker(opts);
            }

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(opts.getPosition(),13));
        }

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                if(marker != null){
                    marker.remove();
                }

                 MarkerOptions opt = new MarkerOptions()
                        .position(latLng)
                        .title("Target Location");
                 marker = googleMap.addMarker(opt);
            }
        });
    }
}
