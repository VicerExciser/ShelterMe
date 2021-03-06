package edu.gatech.cs2340.shelterme.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import edu.gatech.cs2340.shelterme.R;
import edu.gatech.cs2340.shelterme.model.Bed;
import edu.gatech.cs2340.shelterme.model.Model;
import edu.gatech.cs2340.shelterme.model.Shelter;
import edu.gatech.cs2340.shelterme.model.StayReport;
import edu.gatech.cs2340.shelterme.model.User;
import edu.gatech.cs2340.shelterme.util.DBUtil;
import edu.gatech.cs2340.shelterme.util.ReservationManager;

/**
 * The type Home page.
 */
public class HomePage extends AppCompatActivity {

    private final Model model = Model.getInstance();
    private final DBUtil dbUtil = DBUtil.getInstance();
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        userEmail = getIntent().getStringExtra("UserEmail");
        assert (userEmail != null);

        Button map = findViewById(R.id.mapButton);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, MapsActivity.class)
                        .putExtra("UserEmail", userEmail));
            }
        });
        Button logout = findViewById(R.id.logoutButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.content.Intent myIntent1 = new android.content.Intent(view.getContext(),
                        MainActivity.class);
                model.setCurrUser(null);
                startActivityForResult(myIntent1, 0);
            }
        });
        Button viewShelters = findViewById(R.id.viewShelters);
        viewShelters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this, ViewSheltersPage.class)
                        .putExtra("UserEmail", userEmail));
            }
        });

        final User curUser = ((User)(model.getCurrUser()));
        final Button checkOut = findViewById(R.id.checkOut);
        if (curUser == null) {
            checkOut.setVisibility(View.GONE);
            Log.e("HomePage", "curUser == null");
            return;
        }
        assert (userEmail.equals(curUser.getEmail()));
        if (!curUser.isOccupyingBed() || (curUser.getCurrentStayReport() == null)) {
            checkOut.setVisibility(View.GONE);
            Log.e("HomePage", "curUser is not occupying a bed or has " +
                    "no active stay report");
        } else {
            checkOut.setVisibility(View.VISIBLE);
            final StayReport curStay = curUser.getCurrentStayReport();
            final String shelterName = curStay.getShelterName();
            if (shelterName == null) {
                model.displayErrorMessage("No current shelter found.",
                        HomePage.this);
                checkOut.setVisibility(View.GONE);
                return;
            }
            final Shelter shelter = Model.findShelterByName(shelterName);

//          Process for a User to check out of their current shelter stay:
            checkOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        ReservationManager reservationManager
                                = new ReservationManager(shelter, curUser);
                        Map<String, List<Bed>> reserved
                                = reservationManager.undoReservation(curStay);
//
                        model.updateShelterVacanciesAndBeds(shelter, reserved, false);
                        model.updateUserOccupancyAndStayReports(curUser);
                    } catch (IllegalArgumentException iae) {
                        model.displayErrorMessage(iae.getMessage(), HomePage.this);
                        return;
                    } catch (Exception e) {
                        model.displayErrorMessage(e.getMessage(), HomePage.this);
                        return;
                    }
                    model.displaySuccessMessage("Hope you enjoyed your stay at "
                            + shelterName
                            + "!", HomePage.this);
                    if (!curUser.isOccupyingBed) {
                        checkOut.setVisibility(View.GONE);
                    }
                }
            });
        }




    }
}
