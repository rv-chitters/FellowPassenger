package com.example.raghu.fellowpassenger.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.R.layout;

import com.example.raghu.fellowpassenger.MainActivity;
import com.example.raghu.fellowpassenger.R;


/**
 * Created by raghu on 07/08/16.
 */
public class MainFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_main,container,false);
        int id = android.R.layout.simple_list_item_1;


        final Button ToMapButton = (Button) rootView.findViewById(R.id.toMapButton);
        final EditText EditLocation = (EditText) rootView.findViewById(R.id.editLocation);
        final ListView lv = (ListView) rootView.findViewById(R.id.listView);

        ArrayAdapter<LocationData> adapter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            adapter = new ArrayAdapter<LocationData>(getContext(), layout.simple_list_item_1, MainActivity.locations);
        }

        lv.setAdapter(adapter);




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
                MainActivity.currentFragmnet = "Map";
            }
        });



        return  rootView;
    }


}
