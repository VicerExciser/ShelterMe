package edu.gatech.cs2340.shelterme.controllers;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.gatech.cs2340.shelterme.R;
import edu.gatech.cs2340.shelterme.model.Model;
import edu.gatech.cs2340.shelterme.model.Shelter;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Model model = Model.getInstance();
    //private List<Shelter> shelters = new ArrayList<>();

    private ArrayAdapter<String> adapter;
    private HashMap<String, Shelter> shelters;
    //private Model model = Model.getInstance();
    private boolean showAll;
    private GenderAccepted selectedGender = GenderAccepted.ANY;
    //private ListView shelterList;
    private boolean updated = false;
    private boolean ready = false;

    private enum GenderAccepted {
        ANY("Any gender"),
        MEN("Men only"),
        WOMEN("Women only");

        private final String _msg;
        GenderAccepted(String msg) { _msg = msg; }
        public String toString() { return _msg; }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_activity);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final Spinner genderSpinMap = (Spinner) findViewById(R.id.genderSpinMaps);
        final CheckBox familyCheck = (CheckBox) findViewById(R.id.familyCheckMap);
        final CheckBox showAllCheck = (CheckBox) findViewById(R.id.showAllMap);

        shelters = new HashMap<>(Model.getShelterListPointer());
        System.out.print(shelters);
        showAllCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showAll = isChecked;
                updated = true;
                updateSearch(familyCheck.isChecked());
            }
        });
        ArrayAdapter<String> adapterGen = new ArrayAdapter(this,android.R.layout.simple_spinner_item, MapsActivity.GenderAccepted.values());
        adapterGen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinMap.setAdapter(adapterGen);

        // Omit shelters where this user is ineligible to stay  <- nah


        //adapter = new ArrayAdapter<>(MapsActivity.this, R.layout.list_item, R.id.shelter_name,
          //      shelters.keySet().toArray(new String[shelters.keySet().size()]));
        //shelterList.setAdapter(adapter);



        familyCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO: Incorporate other search criteria to be applied
//                showAll = false;
                showAllCheck.setChecked(false);
                updated = true;
                updateSearch(isChecked);
            }
        });


        genderSpinMap.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showAllCheck.setChecked(false);
                switch (position) {
                    case 0:
                        selectedGender = GenderAccepted.ANY;
                        break;
                    case 1:
                        selectedGender = GenderAccepted.MEN;
                        break;
                    case 2:
                        selectedGender = GenderAccepted.WOMEN;
                        break;
                }
                updated = true;
                updateSearch(familyCheck.isChecked());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        //SearchView inputSearch = findViewById(R.id.inputSearch);
        //inputSearch.setQueryHint("Search by Name");
//        inputSearch.setSelected(true);
        //inputSearch.setIconifiedByDefault(false);
        //inputSearch.setSelected(false);
        //inputSearch.setImeOptions(1);
        //inputSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //@Override
            //public boolean onQueryTextSubmit(String query) {
              //  MapsActivity.this.adapter.getFilter().filter(query);
               // showAllCheck.setChecked(false);
                //return false;
            //}

            //@Override
            //public boolean onQueryTextChange(String newText) {
                //MapsActivity.this.adapter.getFilter().filter(newText);
                //return false;
            //}
        //});


        if (!showAllCheck.isChecked())
            showAllCheck.setChecked(true);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng loc = new LatLng(33.7490, 84.3880);
        List<Marker> markers = populateMap();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        ready = true;
        //mMap.setOnMarkerClickListener()
    }

    public List<Marker> populateMap(){
        mMap.clear();
        List<Marker> markers = new ArrayList<>();
        for(Shelter s: shelters.values()) {
                if (s != null) {
                    LatLng loc = new LatLng(s.getLatitude(), s.getLongitude());
                    Marker marker = mMap.addMarker(new MarkerOptions().position(loc).title(s.getShelterName()).snippet(s.getPhone()));
                    markers.add(marker);
                }
        }
        //for(Marker m: markers) {
          //  if (!shelters.containsKey(m.getTitle())) {
            //    m.remove();
              //  System.out.println("TRYING TO REMOVE");
            //}
            //System.out.println("IN THE LOOOOOOOOOOOOOOOOOOP");
        //}
        updated = false;
        return markers;
    }


    //filling spinners



    private void updateSearch(boolean isChecked) {
        shelters.clear();
        if (showAll) {
            for (Shelter s : Model.getShelterListPointer().values()) {
                shelters.put(s.getShelterName(), s);
            }
        } else {
            for (Shelter s : Model.getShelterListPointer().values()) {
                for (String key : s.getBeds().keySet()) {
                    if (key.length() > 1) {
                        //Log.e("ViewShelters", "key = " + key);
                        if (familyChoiceMatchesKey(key, isChecked) && s.getVacancies() > 0) {
                            if (genderChoiceMatchesKey(key)) {
                                shelters.put(s.getShelterName(), s);
                            }
                        }
                    }
                }
            }
        }
        if(ready) {
            populateMap();
        }
        //updateResults();
    }

    /*private void updateResults() {
        adapter = new ArrayAdapter<>(MapsActivity.this, R.layout.list_item, R.id.shelter_name,
                shelters.keySet().toArray(new String[shelters.keySet().size()]));
        shelterList.setAdapter(adapter);
    }*/
    private boolean genderChoiceMatchesKey(String key) {
//        Log.e("ViewShelters", key);
        boolean match = false;
        if ((selectedGender.equals(GenderAccepted.MEN) && key.charAt(1) == 'T')
                ^ (selectedGender.equals(GenderAccepted.WOMEN) && key.charAt(2) == 'T')) {
            match = true;
        }
        else if (selectedGender.equals(GenderAccepted.ANY)
                /*||*/ && (key.charAt(1) == 'F' && key.charAt(2) == 'F')) {
            match = true;
        }
        return match;

    }
    private boolean familyChoiceMatchesKey(String key, boolean famChecked) {
        boolean match = false;
        if (famChecked && key.charAt(0) == 'T') {
            match = true;
        }
        else if (famChecked == false && key.charAt(0) == 'F')//key.charAt(1) == 'F' &&  key.charAt(2) == 'F') // FFF000_200_F
            match = true;
        return match;
    }


}
