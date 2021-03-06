package edu.gatech.cs2340.shelterme.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.gatech.cs2340.shelterme.R;
import edu.gatech.cs2340.shelterme.model.Model;
import edu.gatech.cs2340.shelterme.model.Shelter;
import edu.gatech.cs2340.shelterme.model.User;

/**
 * The type Shelter details page.
 */
public class ShelterDetailsPage extends AppCompatActivity {
    private int vacancyRaw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_details_page);

        Button backButton = findViewById(R.id.backButton);
        Button stayRequest = findViewById(R.id.stayRequest);
        TextView header = findViewById(R.id.header);
//        ScrollView deets = findViewById(R.id.details);
        TextView capacity = findViewById(R.id.capacity);
        TextView restrictions = findViewById(R.id.restrictions);
        TextView address = findViewById(R.id.address);
        TextView phone = findViewById(R.id.phone);
        TextView longitude = findViewById(R.id.longitude);
        TextView latitude = findViewById(R.id.latitude);
        TextView notes = findViewById(R.id.notes);
        TextView vacancies = findViewById(R.id.vacancies);

        final Intent intent = getIntent();
        final Shelter shelterParcel = intent.getParcelableExtra("Shelter");
        final Model modelInstance = Model.getInstance();
        final Shelter shelter = modelInstance.verifyShelterParcel(shelterParcel);
        vacancyRaw = shelter.getVacancies();
        String vacancyString = String.valueOf(vacancyRaw);

        // Add new Shelter fields to Parcel
        header.setText(shelter.getShelterName());
        String capText = "Capacity: \n\t\t\t\t"+shelter.getCapacityStr();
        capacity.setText(capText);
        String accptText = "Accepts: \n\t\t\t\t"+shelter.getRestrictions();
        restrictions.setText(accptText);
        String CITY = "Atlanta";
        String shelterAddress = shelter.getAddress();
        String streetCity[] = shelterAddress.split(CITY);
        String addrText = "Address: \n\t\t\t\t"+streetCity[0]+"\n\t\t\t\t"+ CITY +streetCity[1];
        address.setText(addrText);
        String phoneText = "Phone: \n\t\t\t\t"+shelter.getPhone();
        phone.setText(phoneText);
        String longText = "Longitude: \n\t\t\t\t" +shelter.getLongitude();
        longitude.setText(longText);
        String latText = "Latitude: \n\t\t\t\t"+shelter.getLatitude();
        latitude.setText(latText);
        String noteText = shelter.getNotes();
        notes.setText(noteText);
        String vacText = "Vacancies: \n\t\t\t\t" + vacancyString;
        vacancies.setText(vacText);

        stayRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = intent.getStringExtra("UserEmail");
                if (userEmail == null && modelInstance.getCurrUser() != null) {
                    userEmail = modelInstance.getCurrUser().getEmail();
                }
                if (userEmail != null && modelInstance.emailExists(userEmail)) {
                    User user = (User)(Model.getAccountByEmail(userEmail));
                    if (!(user.isOccupyingBed())) {
                        if ((vacancyRaw > 0) && shelter.hasOpenBed(user.generateKey())) {
                            Intent request = new Intent(ShelterDetailsPage.this,
                                    RequestStayReport.class);
                            request.putExtra("Shelter", shelter);
                            startActivity(request);
                        } else {
                            modelInstance.displayErrorMessage("This shelter has " +
                                            "no available beds.",
                                    ShelterDetailsPage.this);
                        }
                    } else {
                        modelInstance.displayErrorMessage("Your account already " +
                                        "has beds claimed at another shelter.",
                                ShelterDetailsPage.this);
                    }
                } else {
                    modelInstance.displayErrorMessage("Must be a registered user " +
                                    "to request a stay.",
                            ShelterDetailsPage.this);
                }
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShelterDetailsPage.this,
                        ViewSheltersPage.class));
            }
        });
    }

}

/*
private void displayDetailView(int position) {
        Shelter selected = shelters.get(adapter.getItem(position));
        Intent intent = new Intent(ViewSheltersPage.this, ShelterDetailsPage.class);
        intent.putExtra("Shelter", selected);
        startActivity(intent);
    }
 */