package edu.gatech.cs2340.shelterme.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import edu.gatech.cs2340.shelterme.R;
import edu.gatech.cs2340.shelterme.model.Bed;
import edu.gatech.cs2340.shelterme.model.Model;
import edu.gatech.cs2340.shelterme.model.Shelter;
import edu.gatech.cs2340.shelterme.model.StayReport;
import edu.gatech.cs2340.shelterme.model.User;

public class HomePage extends AppCompatActivity {

    Model model = Model.getInstance();
    DBUtil dbUtil = DBUtil.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Button map = findViewById(R.id.mapButton);
        map.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, MapsActivity.class));
            }
        });
        Button logout = findViewById(R.id.logoutbutton);
        logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                android.content.Intent myIntent1 = new android.content.Intent(view.getContext(), MainActivity.class);
                Model.getInstance().setCurrUser(null);
                startActivityForResult(myIntent1, 0);
            }
        });
        Button viewShelters = findViewById(R.id.viewShelters);
        viewShelters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this, ViewSheltersPage.class));
            }
        });

        final User curUser = ((User)(model.getCurrUser()));
        final Button checkOut = findViewById(R.id.checkOut);
        if (curUser == null) {
            checkOut.setVisibility(View.GONE);
            Log.e("HomePage", "curUser == null");
        } else if (!curUser.isOccupyingBed() || curUser.getCurrentStayReport() == null) {
            checkOut.setVisibility(View.GONE);
            Log.e("HomePage", "curUser is not occupying a bed or has no active stay report");
        } else {
            checkOut.setVisibility(View.VISIBLE);
            final StayReport curStay = curUser.getCurrentStayReport();
            final String shelterName = curStay.getShelterName();
            if (shelterName == null) {
                model.displayErrorMessage("No current shelter found.", HomePage.this);
                checkOut.setVisibility(View.GONE);
                return;
            }
            final Shelter shelter = Model.findShelterByName(shelterName);

//          Process for a User to check out of their current shelter stay:
            checkOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        HashMap<String, Collection<Bed>> reserved = shelter.undoReservation(curStay);
//
                        dbUtil.updateShelterVacanciesAndBeds(shelter, reserved, false);
                        dbUtil.updateUserOccupancyAndStayReports(curUser);
                    } catch (IllegalArgumentException iae) {
                        model.displayErrorMessage(iae.getMessage(), HomePage.this);
                        return;
                    } catch (Exception e) {
                        model.displayErrorMessage(e.getMessage(), HomePage.this);
                        return;
                    }
                    model.displaySuccessMessage("Hope you enjoyed your stay at " + shelterName
                            + "!", HomePage.this);
                    if (!curUser.isOccupyingBed) checkOut.setVisibility(View.GONE);
                }
            });
        }




    }
}
