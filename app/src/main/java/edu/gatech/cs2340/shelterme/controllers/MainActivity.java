package edu.gatech.cs2340.shelterme.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
//import android.widget.Button;
import android.widget.ImageButton;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import edu.gatech.cs2340.shelterme.model.Model;
import edu.gatech.cs2340.shelterme.R;
import edu.gatech.cs2340.shelterme.model.Shelter;
import edu.gatech.cs2340.shelterme.util.DBUtil;

/**
 * The type Main activity.
 */
public class MainActivity extends AppCompatActivity {

    private Model model; // = Model.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton login = findViewById(R.id.login);
        ImageButton proceed = findViewById(R.id.proceed);
        ImageButton signup = findViewById(R.id.signup);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(),
                        LoginPage.class);
                startActivityForResult(myIntent, 0);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent myIntent = new Intent(view.getContext(),
//                        RegistrationPage.class);
//                startActivityForResult(myIntent, 0);
                startActivity(new Intent(MainActivity.this, RegistrationPage.class));
            }
        } );

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(),
                        HomePage.class);
                model.setCurrUser(null);
                startActivityForResult(myIntent, 0);
            }
        });

        DBUtil dbUtil = DBUtil.getInstance();
        model = Model.getInstance();
        Thread backgroundThread = new Thread(dbUtil);
        backgroundThread.start();   // Init & run thread for maintaining database

        // MUST COMMENT THIS OUT TO PREVENT SHELTER DATA FROM BEING OVERWRITTEN
//        loadShelters();
        // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        model.getAllShelterDetails();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //noinspection ChainedMethodCall
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Reading in our CSV data for shelters
    private void loadShelters() {
        try {
            InputStream is = getResources().openRawResource(R.raw.homeless_shelter_database);
            BufferedReader br = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                // KitKat = Android 4.4 API
                br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            }
            br.readLine();
            String line;// = null;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(";");

                int length = tokens.length;
                for (int i = 0; i < length; i++) {
                    if (tokens[i].isEmpty() || " ".equals(tokens[i])) {
                        tokens[i] = "N/A";
                    } else {
                        tokens[i] = tokens[i].replace('"',' ');
                        tokens[i] = tokens[i].trim();
                    }
//                    if (tokens[i].charAt(0)=='"' && tokens[i].charAt(tokens[i].length())=='"') {
//                     tokens[i] = tokens[i].substring(1, tokens[i].length());
//                    }
                }
//                Shelter shelter;
// Format: key, name, capacity, restricts, long, lat, addr, notes, phone
                if (length == 9) {
//                    int key = Integer.valueOf(tokens[0]);
                    String key = tokens[0];
                    String name = tokens[1];
                    String capacity = tokens[2];
                    String restricts = tokens[3];
                    double longitude = Double.parseDouble(tokens[4]);
                    double latitude = Double.parseDouble(tokens[5]);
                    String addr = tokens[6];
                    String notes = tokens[7];
                    String phone = tokens[8];
                    Shelter newShelter = new Shelter(key, name, capacity, restricts, longitude,
 latitude,
                            addr, notes, phone);
                    model.addShelter(newShelter);
//                    dbUtil.addShelter(newShelter);
                }
            }
            br.close();

        } catch (FileNotFoundException fnf) {

        } catch (IOException ioe)

        {

        } catch (Exception e) {
            Log.e("LoadShelters", e.getMessage());
            Log.e("Error loading shelters", e.toString());
            e.printStackTrace();
        }
    }
}
