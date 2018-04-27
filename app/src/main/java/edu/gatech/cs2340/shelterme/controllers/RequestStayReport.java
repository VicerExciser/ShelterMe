/*
 * Activity for User to request vacant bed(s) at a Shelter for a duration of time.
 */
package edu.gatech.cs2340.shelterme.controllers;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;
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
 * The type Request stay report.
 */
public class RequestStayReport extends AppCompatActivity {

    private final DBUtil dbUtil = DBUtil.getInstance();
    private final Model model = Model.getInstance();
    private static final int MAX_BEDS_ALLOWED = 5;
    private Shelter shelterParcel;
    private boolean agreed;//, acknowledged;
    private String selectedBedType;
    private Integer selctedNumber;
    private Spinner numBedsSpin;
    private ArrayAdapter<Integer> singleAdapter;
    private ArrayAdapter<Integer> famAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_stay_report);

        Intent intent = getIntent();
        shelterParcel = intent.getParcelableExtra("Shelter");
        final Shelter shelter = model.verifyShelterParcel(shelterParcel);

        TextView header = findViewById(R.id.header);
        header.setText(shelter.getShelterName());

        boolean isFam = false;
        boolean isSingle = false;
        final ArrayList<String> types = new ArrayList<>(1);
        if (shelter.getSingleCapacity() > 0) {
            types.add("Single");
            isSingle = true;
            if (shelter.getFamilyCapacity() > 0) {
                types.add("Family");
                isFam = true;
            }
        } else if (shelter.getFamilyCapacity() > 0) {
                types.add("Family");
                isFam = true;
        }

        Spinner bedTypeSpin = findViewById(R.id.bedType);
        SpinnerAdapter btsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, types.toArray(new String[types.size()]));
        bedTypeSpin.setAdapter(btsAdapter);
        bedTypeSpin.setSelection(0);
        selectedBedType = types.get(0);
        bedTypeSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedBedType = types.get(position);
                updateNumBeds();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        ArrayList<Integer> singleNums = new ArrayList<>(1);
        if (isSingle) {
            int sc = shelter.getSingleCapacity();
            int max = (sc < MAX_BEDS_ALLOWED) ? sc : MAX_BEDS_ALLOWED;
            for (int i = 1; i <= max; i++) {
                singleNums.add(i);
            }
        }
        singleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                singleNums.toArray(new Integer[singleNums.size()]));

        ArrayList<Integer> famNums = new ArrayList<>(1);
        if (isFam) {
            int fc = shelter.getFamilyCapacity();
            int max = (fc < MAX_BEDS_ALLOWED) ? fc : MAX_BEDS_ALLOWED;
            for (int i = 1; i <= max; i++) {
                famNums.add(i);
            }
        }
        famAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                famNums.toArray(new Integer[famNums.size()]));

        numBedsSpin = findViewById(R.id.numOfBeds);
        updateNumBeds();
        numBedsSpin.setSelection(0);
        selctedNumber = (Integer)numBedsSpin.getSelectedItem();
        numBedsSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selctedNumber = (Integer)numBedsSpin.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        agreed = false;
        CheckBox agreeCheck = findViewById(R.id.checkAgreement);
        agreeCheck.setChecked(agreed);
        agreeCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                agreed = isChecked;
            }
        });

        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("ChainedMethodCall")
            @Override
            public void onClick(View view) {
                boolean success = true;
                User user = (User)model.getCurrUser();
                if (agreed) {
                    if (shelter.getVacancies() >= selctedNumber) {
                        try {
                                ReservationManager reservationManager
                                    = new ReservationManager(shelter, user);
                                Map<String, List<Bed>> reserved
                                    = reservationManager.reserveBed(selectedBedType, selctedNumber);

                                model.updateShelterVacanciesAndBeds(shelter, reserved, true);
                                model.updateUserOccupancyAndStayReports(user);
                            } catch (IllegalArgumentException iae) {
                                model.displayErrorMessage(iae.getMessage(),
                                        RequestStayReport.this);
                                success = false;
                            }
                    } else {
                        model.displayErrorMessage("This shelter does not have "
                                + String.valueOf(selctedNumber) + " bed(s) available",
                                RequestStayReport.this);
                        success = false;
                    }
                } else {
                    model.displayErrorMessage("Must agree to shelter terms " +
                                    "to submit a request",
                            RequestStayReport.this);
                    success = false;
                }
                if (success) {
                    StayReport newStay = user.getCurrentStayReport();
                    @SuppressLint("DefaultLocale") String message = String.format
                            ("Your reservation for %d bed(s) "
                            + "at %s was confirmed for %s", selctedNumber, shelter.getShelterName(),
                            newStay.getCheckInDate());
                    AlertDialog.Builder dialog = new AlertDialog.Builder(
                            RequestStayReport.this);
                    dialog.setTitle("Success!").setMessage(message)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    returnToDetailsPage();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert).show();
                }
            }
        });

        Button cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToDetailsPage();
            }
        });

    }

    private void returnToDetailsPage() {
        Shelter shelter = model.verifyShelterParcel(shelterParcel);
        Intent newIntent = new Intent(RequestStayReport.this,
                ShelterDetailsPage.class);
        newIntent.putExtra("Shelter", shelter);
//        try {
//            java.util.concurrent.TimeUnit.SECONDS.sleep(1);
//        } catch (InterruptedException ie) {
//            Log.e("StayReport success", "sleep() threw an InterruptedException");
//        }
        startActivity(newIntent);
    }

    private void updateNumBeds() {
        if ("Single".equals(selectedBedType)) {
            numBedsSpin.setAdapter(singleAdapter);
        }
        else if ("Family".equals(selectedBedType)) {
            numBedsSpin.setAdapter(famAdapter);
        }
    }

}

// Check that vacancies > numBedsReserved
// Use Shelter.reserveBed for checking in, and Shelter.undoReservation for checking out