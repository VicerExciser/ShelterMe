/*
 * This activity allows Admins to create a new shelter.
 */
package edu.gatech.cs2340.shelterme.controllers;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import edu.gatech.cs2340.shelterme.R;
import edu.gatech.cs2340.shelterme.model.Shelter;

public class CreateShelter extends AppCompatActivity {

    private EditText shelterName;
    private EditText shelterAddress;
    private EditText shelterPhone;
    private EditText shelterRestrictions;
    private EditText shelterCapacity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shelter);
        shelterName = (EditText) findViewById(R.id.inputName);
        shelterAddress = (EditText) findViewById(R.id.inputAddress);
        shelterPhone = (EditText) findViewById(R.id.inputPhone);
        shelterRestrictions = (EditText) findViewById(R.id.inputRestriction);
        shelterCapacity = (EditText) findViewById(R.id.inputCapacity);



        final String realShelterName = shelterName.toString();
        final String realShelterAddress = shelterAddress.toString();
        final String realShelterPhone = shelterPhone.toString();
        final String realShelterRestriction = shelterRestrictions.toString();
        final String realShelterCapacity = shelterCapacity.toString();

        ImageButton create = findViewById(R.id.createButton);


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Shelter newShelter;
                newShelter = new Shelter(null, realShelterName, realShelterCapacity,
                        realShelterRestriction, 0, 0, realShelterAddress,
                        null, realShelterPhone);
            }
        });

    }
}
