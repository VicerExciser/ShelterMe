package edu.gatech.cs2340.shelterme.controllers;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Model model = Model.getInstance();
    private List<Shelter> shelters = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //populate hashmap of shelters
        for(Shelter shelter : Model.getShelterListPointer()){
            shelters.add(shelter);
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng loc = new LatLng(shelters.get(0).getLatitude(), shelters.get(0).getLongitude());
        populateMap();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        List<Marker> markers = populateMap();

    }

    public List<Marker> populateMap(){
        List<Marker> markers = new ArrayList<>();
        for(int i = 0; i < shelters.size();i++) {
            LatLng loc = new LatLng(shelters.get(i).getLatitude(), shelters.get(i).getLongitude());
            Marker marker = mMap.addMarker(new MarkerOptions().position(loc).title(shelters.get(i).getShelterName()));
            markers.add(marker);
        }
        return markers;
    }
}
