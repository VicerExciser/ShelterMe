/*
 * This activity allows Admins to create a new shelter.
 */
package edu.gatech.cs2340.shelterme.controllers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import edu.gatech.cs2340.shelterme.R;
import edu.gatech.cs2340.shelterme.model.Model;
import edu.gatech.cs2340.shelterme.model.Shelter;

public class CreateShelter extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shelter);
        EditText shelterName = (EditText) findViewById(R.id.inputName);
        EditText shelterAddress = (EditText) findViewById(R.id.inputAddress);
        EditText shelterPhone = (EditText) findViewById(R.id.inputPhone);
        EditText shelterRestrictions = (EditText) findViewById(R.id.inputRestriction);
        EditText shelterCapacity = (EditText) findViewById(R.id.inputCapacity);



        final String realShelterName = shelterName.toString();
        final String realShelterAddress = shelterAddress.toString();
        final String realShelterPhone = shelterPhone.toString();
        final String realShelterRestriction = shelterRestrictions.toString();
        final String realShelterCapacity = shelterCapacity.toString();

        Button create = findViewById(R.id.createButton);


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Model.getInstance().addShelter(new Shelter(null, realShelterName,
                        realShelterCapacity, realShelterRestriction, 0, 0,
                        realShelterAddress, null, realShelterPhone));

            }
        });

    }
}
