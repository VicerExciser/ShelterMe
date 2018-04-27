package edu.gatech.cs2340.shelterme.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.Locale;
import java.util.Map;

import edu.gatech.cs2340.shelterme.R;
import edu.gatech.cs2340.shelterme.model.Account;
import edu.gatech.cs2340.shelterme.model.Message;
import edu.gatech.cs2340.shelterme.model.Model;
import edu.gatech.cs2340.shelterme.model.ReportUserRequest;
import edu.gatech.cs2340.shelterme.model.Shelter;

/**
 * The type Employee home page.
 */
public class EmployeeHomePage extends AppCompatActivity {
    private Model model = Model.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_home_page);

        Button logOut = findViewById(R.id.logout);
        logOut.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("ChainedMethodCall")
            @Override
            public void onClick(View view) {
               startActivity(new Intent(EmployeeHomePage.this, LoginPage.class));
            }
        });

//----------------------- Demo functionality -----------------------------------

        Button editShelter = findViewById(R.id.editShelter);
        editShelter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to EditShelter activity or display TextView for Shelter and vacancy input

                // hardcoded test to decrement vacancies:
                Shelter selected = Model.findShelterByName("Nicholas House");
                int pastVacancies = selected.getVacancies();
                int newVacancies = pastVacancies - 1;
                selected.setVacancies(newVacancies);
                model.updateVacancy(selected);
                String successText = String.format(Locale.US, "Vacancy for Nicholas House " +
                        "changed from %d to %d.", pastVacancies, newVacancies);
                model.displaySuccessMessage(successText,
                        EmployeeHomePage.this);
            }
        });

        Button reportUser = findViewById(R.id.sendRequest);
        reportUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to SendAdminRequest activity or display TextView for violating User input

                // hardcoded test to ban an existing Account:
                Account toSuspend = Model.getAccountByEmail("acondict3@gatech.edu");
                assert (toSuspend != null);
                Account sendingEmployee = model.getCurrEmployee();
                Message userReport = new ReportUserRequest(sendingEmployee, toSuspend);
                model.addToMessages(userReport);
                String successText = "User " + toSuspend.getUsername() + " has been reported.";
                model.displaySuccessMessage(successText,
                        EmployeeHomePage.this);
            }
        });

    }

}
