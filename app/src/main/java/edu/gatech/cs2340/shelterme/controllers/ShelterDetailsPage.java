package edu.gatech.cs2340.shelterme.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import edu.gatech.cs2340.shelterme.R;
import edu.gatech.cs2340.shelterme.model.Model;
import edu.gatech.cs2340.shelterme.model.Shelter;

public class ShelterDetailsPage extends AppCompatActivity {
    final String CITY = "Atlanta";
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

        Intent intent = getIntent();
        final Shelter shelter = intent.getParcelableExtra("Shelter");

        // TODO: Add new Shelter fields to Parcel
        header.setText(shelter.getShelterName());
        String capText = "Capacity: \n\t\t\t\t"+shelter.getCapacityStr();
        capacity.setText(capText);
        String accptText = "Accepts: \n\t\t\t\t"+shelter.getRestrictions();
        restrictions.setText(accptText);
        String streetCity[] = shelter.getAddress().split(CITY);
        String addrText = "Address: \n\t\t\t\t"+streetCity[0]+"\n\t\t\t\t"+CITY+streetCity[1];
        address.setText(addrText);
        String phoneText = "Phone: \n\t\t\t\t"+shelter.getPhone();
        phone.setText(phoneText);
        String longText = "Longitude: \n\t\t\t\t" +shelter.getLongitude();
        longitude.setText(longText);
        String latText = "Latitude: \n\t\t\t\t"+shelter.getLatitude();
        latitude.setText(latText);
        String noteText = shelter.getNotes();
        notes.setText(noteText);
        String vacText = "Vacancies: \n\t\t\t\t"+shelter.getVacancies();
        vacancies.setText(vacText);

        stayRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shelter.getVacancies() > 0)
                    startActivity(new Intent(ShelterDetailsPage.this, RequestStayReport.class));
                else
                    Model.getInstance().displayErrorMessage("This shelter has no available beds.", ShelterDetailsPage.this);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShelterDetailsPage.this, ViewSheltersPage.class));
            }
        });
    }

}
