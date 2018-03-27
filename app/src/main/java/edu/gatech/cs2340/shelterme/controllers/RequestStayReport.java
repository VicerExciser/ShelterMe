/*
 * Activity for User to request vacant bed(s) at a Shelter for a duration of time.
 */
package edu.gatech.cs2340.shelterme.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.HashMap;

import edu.gatech.cs2340.shelterme.R;
import edu.gatech.cs2340.shelterme.model.Model;
import edu.gatech.cs2340.shelterme.model.Shelter;
import edu.gatech.cs2340.shelterme.model.User;

public class RequestStayReport extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_stay_report);

        Intent intent = getIntent();
        final Shelter shelterParcel = intent.getParcelableExtra("Shelter");
        final Shelter shelter = Model.getInstance().verifyShelterParcel(shelterParcel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    HashMap<String, String> reservedBedIds = shelter.reserveBed(2/*numBedsReserved*/);
                    for (String s : reservedBedIds.keySet())
                        Log.e("BED_KEY", s);
                    for (String s : reservedBedIds.values()) {
                        Log.e("BED_ID", s);
                    }
                    User user = (User)Model.getInstance().getCurrUser();
                    DBUtil dbUtil = DBUtil.getInstance();
                    dbUtil.updateShelterVacanciesAndBeds(shelter, reservedBedIds, user);
                    dbUtil.updateUserOccupancyAndStayReports(user);
                } catch (IllegalArgumentException iae) {
                    Model.getInstance().displayErrorMessage(iae.getMessage(), RequestStayReport.this);
                }
            }
        });



    }

}

// Check that vacancies > numBedsReserved
// Use Shelter.reserveBed for checking in, and Shelter.clearOccupiedBeds for checking out