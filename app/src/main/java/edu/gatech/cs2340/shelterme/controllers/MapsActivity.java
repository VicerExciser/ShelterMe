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
import edu.gatech.cs2340.shelterme.model.GenderAccepted;
import edu.gatech.cs2340.shelterme.model.Model;
import edu.gatech.cs2340.shelterme.model.Shelter;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Model model = Model.getInstance();
    private ArrayAdapter<String> adapter;
    private HashMap<String, Shelter> shelters;
    //private Model model = Model.getInstance();
    private boolean showAll = true;
    private GenderAccepted selectedGender = GenderAccepted.ANY;
    //private ListView shelterList;
    private boolean updated = false;
    private boolean ready = false;

    int updateCounter = 0;
    final int updatesForInit = 2;   // signifies initialization is complete (to be compared against updateCounter)
    boolean ignoreUpdate = false;   // to avoid unnecessary subroutine calls

//    private enum GenderAccepted {
//        ANY("Any gender"),
//        MEN("Men only"),
//        WOMEN("Women only");
//
//        private final String _msg;
//        GenderAccepted(String msg) { _msg = msg; }
//        public String toString() { return _msg; }
//    }


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

        CompoundButton.OnCheckedChangeListener checkListen = new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showAll = updateCounter >= updatesForInit ? isChecked : showAll;
                updated = true;
                Log.e("OnCheckedChangeListener", "showAll checked = "+showAll);
                if (updateCounter != 0)
                    updateSearch(familyCheck.isChecked());
                else
                    updateCounter++;
            }
        };
        // For initially displaying all shelters:
        showAllCheck.post(new Runnable() {
            @Override
            public void run() {
                showAllCheck.setChecked(true);
                Log.e("Maps runnable", "showAll should be checked");
            }
        });
        showAllCheck.setOnCheckedChangeListener(checkListen);

        ArrayAdapter<String> adapterGen = new ArrayAdapter(this,android.R.layout.simple_spinner_item, GenderAccepted.values());
        adapterGen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinMap.setAdapter(adapterGen);

        familyCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO: Incorporate other search criteria to be applied?
                if (isChecked && showAllCheck.isChecked()) {
                    if (updateCounter >= updatesForInit) {
                        ignoreUpdate = true;
                        showAllCheck.setChecked(false);
                        ignoreUpdate = false;
                    }
                }
                updated = true;
                if (updateCounter != 0)
                    updateSearch(isChecked);
                else
                    updateCounter++;
            }
        });

        genderSpinMap.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (updateCounter >= updatesForInit) {
                    ignoreUpdate = true;
                    showAllCheck.setChecked(false);
                    ignoreUpdate = false;
                }
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
                if (updateCounter != 0)
                    updateSearch(familyCheck.isChecked());
                else
                    updateCounter++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng loc = new LatLng(33.7490, -84.3880);
        List<Marker> markers = populateMap();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(11));
        ready = true;
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

        updated = ignoreUpdate = false;
        return markers;
    }


    private void updateSearch(boolean isChecked) {
        if (!ignoreUpdate) {
            shelters.clear();
            updateCounter++;
            if (showAll) {
                for (Shelter s : Model.getShelterListPointer().values()) {
                    shelters.put(s.getShelterName(), s);
                }
            } else {
                for (Shelter s : Model.getShelterListPointer().values()) {
                    for (String key : s.getBeds().keySet()) {
                        if (key.length() > 1) {
                            if (familyChoiceMatchesKey(key, isChecked) && s.getVacancies() > 0) {
                                if (genderChoiceMatchesKey(key)) {
                                    shelters.put(s.getShelterName(), s);
                                }
                            }
                        }
                    }
                }
            }
            if (ready) {
                populateMap();
            }
        }
        Log.e("updateSearch", "updateCounter = "+updateCounter);
    }

    private boolean genderChoiceMatchesKey(String key) {
        boolean match = false;
        if ((selectedGender.equals(GenderAccepted.MEN) && key.charAt(1) == 'T')
                ^ (selectedGender.equals(GenderAccepted.WOMEN) && key.charAt(2) == 'T')) {
            match = true;
        }
        else if (selectedGender.equals(GenderAccepted.ANY)
                && (key.charAt(1) == 'F' && key.charAt(2) == 'F')) {
            match = true;
        }
        return match;
    }

    private boolean familyChoiceMatchesKey(String key, boolean famChecked) {
        boolean match = false;
        if (famChecked && key.charAt(0) == 'T') {
            match = true;
        }
        else if (!famChecked && key.charAt(0) == 'F') {
            match = true;
        }
        return match;
    }

}