package com.example.raghu.fellowpassenger.fragments;

import android.app.Fragment;
import android.content.Context;
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
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.raghu.fellowpassenger.activities.ActivityDataHandler;
import com.example.raghu.fellowpassenger.definations.LocationData;
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

        Log.d("check","in onCreate view");

        ActivityDataHandler.setCurrentFragment("Main");

        rootView = inflater.inflate(R.layout.fragment_front,container,false);

        final Button ToMapButton = (Button) rootView.findViewById(R.id.toMapButton);
        final EditText EditLocation = (EditText) rootView.findViewById(R.id.editLocation);
        final ListView lv = (ListView) rootView.findViewById(R.id.listView);

        ArrayAdapter<LocationData> adapter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            //adapter = new ArrayAdapter<LocationData>(getContext(), layout.simple_list_item_1, MainActivity.locations);
            adapter = new locationAdapter(getContext(),R.layout.location_row, ActivityDataHandler.getLocations());
        }

        lv.setAdapter(adapter);

        EditLocation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });

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
            loc_dist.setText( locationList.get(position).distance + " Kms");

            Button refresh = (Button) convertView.findViewById(R.id.refreshButton);
            refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    locationList.get(position).getPosition();
                }
            });

            Button delete = (Button) convertView.findViewById(R.id.deleteButton);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //ActivityDataHandler.getLocations().remove(position);
                    int id = locationList.get(position).id;
                    ActivityDataHandler.delete(id);
                }
            });

            Switch active  = (Switch) convertView.findViewById(R.id.activateSwitch);
            active.setChecked(locationList.get(position).isActive);
            active.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LocationData locationData = locationList.get(position);
                    ActivityDataHandler.updateStatus(locationData.id,!(locationData.isActive));
                }
            });


            return  convertView;
        }
    }
}
