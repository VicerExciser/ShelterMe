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

import java.util.Collection;
import java.util.HashMap;

import edu.gatech.cs2340.shelterme.R;
import edu.gatech.cs2340.shelterme.model.Bed;
import edu.gatech.cs2340.shelterme.model.Model;
import edu.gatech.cs2340.shelterme.model.Shelter;
import edu.gatech.cs2340.shelterme.model.User;

public class RequestStayReport extends AppCompatActivity {

    DBUtil dbUtil = DBUtil.getInstance();

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
                    HashMap<String, Collection<Bed>> reserved = shelter.reserveBed(/*numBedsReserved*/);
                    for (String s : reserved.keySet()) {
                        Log.e("BED_KEY", s);
                        for (Bed b : reserved.get(s)) {
                            Log.e("\nKEY_ID", b.getId());
                        }
                    }
                    dbUtil.updateShelterVacanciesAndBeds(shelter, reserved, true);
                    dbUtil.updateUserOccupancyAndStayReports((User)Model.getInstance().getCurrUser());
                } catch (IllegalArgumentException iae) {
                    Model.getInstance().displayErrorMessage(iae.getMessage(), RequestStayReport.this);
                }
            }
        });

//        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
//        fab2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

    }

}

// Check that vacancies > numBedsReserved
// Use Shelter.reserveBed for checking in, and Shelter.undoReservation for checking out