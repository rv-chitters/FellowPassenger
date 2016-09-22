package com.example.raghu.fellowpassenger.fragments;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raghu.fellowpassenger.LocationData;
import com.example.raghu.fellowpassenger.MainActivity;
import com.example.raghu.fellowpassenger.R;

import java.util.List;


/**
 * Created by raghu on 07/08/16.
 */
public class MainFragment extends Fragment{

    public View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_front,container,false);
        int id = android.R.layout.simple_list_item_1;


        final Button ToMapButton = (Button) rootView.findViewById(R.id.toMapButton);
        final EditText EditLocation = (EditText) rootView.findViewById(R.id.editLocation);
        final ListView lv = (ListView) rootView.findViewById(R.id.listView);

        ArrayAdapter<LocationData> adapter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            //adapter = new ArrayAdapter<LocationData>(getContext(), layout.simple_list_item_1, MainActivity.locations);
            adapter = new locationAdapter(getContext(),R.layout.location_row, MainActivity.locations);
        }

        lv.setAdapter(adapter);

        /*EditLocation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });*/

        ToMapButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                // Perform action on click
                String searchTerm = EditLocation.getText().toString();

                Bundle bundle = new Bundle();

                android.app.FragmentManager fm = getFragmentManager();

                MapHandlerFragment mp = new MapHandlerFragment();

                bundle.putString("searchTerm",searchTerm);
                mp.setArguments(bundle);
                fm.beginTransaction().replace(R.id.content_main,mp).commit();
                MainActivity.currentFragment = "Map";
            }
        });



        return  rootView;
    }

    public class locationAdapter extends ArrayAdapter<LocationData>{

        private List<LocationData> locationList;
        private int resource;
        private LayoutInflater inflater;

        public locationAdapter(Context context, int resource, List<LocationData> objects) {
            super(context, resource, objects);
            locationList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
/*
            return super.getView(position, convertView, parent);
*/

            if(convertView == null) {
                convertView = inflater.inflate(R.layout.location_row, null);
            }

            TextView loc_name = (TextView) convertView.findViewById(R.id.locationName);
            TextView loc_dist = (TextView) convertView.findViewById(R.id.locationDistance);

            loc_name.setText(locationList.get(position).LocationName);
            loc_dist.setText((int) locationList.get(position).distance + " Kms");

            Button btn = (Button) convertView.findViewById(R.id.refreshButton);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    locationList.get(position).getPosition();
                }
            });

            Button btn2 = (Button) convertView.findViewById(R.id.deleteButton);
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity.locations.remove(position);
                    if(MainActivity.currentFragment == "Main")
                        MainActivity.fm.beginTransaction().replace(R.id.content_main,new MainFragment()).commit();
                }
            });

            Switch s = (Switch) convertView.findViewById(R.id.activateSwitch);
            s.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    locationList.get(position).isActive = !(locationList.get(position).isActive);
                    Toast.makeText(getContext(),String.valueOf(locationList.get(position).isActive) , Toast.LENGTH_SHORT).show();
                }
            });


            return  convertView;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //setContentView(R.layout.myLayout);
        Log.d("tag","config changed");
    }

}
