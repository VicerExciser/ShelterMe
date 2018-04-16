/*
 * This activity allows a User to modify or add account information.
 */
package edu.gatech.cs2340.shelterme.controllers;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import edu.gatech.cs2340.shelterme.R;

/**
 * The type Edit profile.
 */
public class EditProfile extends AppCompatActivity {
    // --Commented out by Inspection (4/13/2018 6:17 PM):DBUtil dbUtil = DBUtil.getInstance();

    // USE THIS: dbUtil.updateUserInfo(user);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        @SuppressWarnings("unused") FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

}
