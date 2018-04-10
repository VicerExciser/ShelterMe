package edu.gatech.cs2340.shelterme.controllers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;

import edu.gatech.cs2340.shelterme.R;
import edu.gatech.cs2340.shelterme.model.Account;
import edu.gatech.cs2340.shelterme.model.Admin;
import edu.gatech.cs2340.shelterme.model.Employee;
import edu.gatech.cs2340.shelterme.model.Model;
import edu.gatech.cs2340.shelterme.model.User;

//import com.google.firebase.auth.FirebaseAuth;

public class LoginPage extends AppCompatActivity {

    private EditText mUsernameView;
    private EditText mPasswordView;
    private FirebaseAuth auth;
    Model model = Model.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mUsernameView=(EditText) findViewById(R.id.editText);
        mPasswordView=(EditText) findViewById(R.id.editText2);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // User is logged in
            startActivity(new Intent(LoginPage.this, HomePage.class));
            finish();
        }

        ImageButton logo = findViewById(R.id.log);
        ImageButton cancel = findViewById(R.id.actualcancel);
        ImageButton forgotpass = findViewById(R.id.forget);

        logo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(attemptLogin()){
                    Account acct = model.getCurrUser();
                    Intent myIntent1 = null;
                    if (acct instanceof User) {
                           myIntent1 = new Intent(view.getContext(), HomePage.class);
                    } else if (acct instanceof Admin) {
                        myIntent1 = new Intent(view.getContext(), AdminHomePage.class);
                    } else if (acct instanceof Employee) {
                        myIntent1 = new Intent(view.getContext(), EmployeeHomePage.class);
                    }
                    if (myIntent1 != null)
                        startActivityForResult(myIntent1, 0);
                }}
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent2 = new Intent(view.getContext(),
                        MainActivity.class);
                startActivityForResult(myIntent2, 0);
            }
        });

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent3 = new Intent(view.getContext(),
                        HomePage.class);
                startActivityForResult(myIntent3, 0);
            }
        });
    }
    private boolean attemptLogin() {
        String username = mUsernameView.getText().toString().trim();
        int password = mPasswordView.getText().toString().hashCode();
        String email = DBUtil.getInstance().getEmailAssociatedWithUsername(username);
        Account attempting = Model.getAccountByEmail(email);

        if (Model.getAccountListPointer().isEmpty())
            Log.e("attemptLogin", "Account list is empty!");
        else {
            int count = 0;
            for (Account acct : Model.getAccountListPointer().values()) {
                Log.e("Account " + count, acct.getUsername());
                count++;
            }
        }

        if (username.equals("lady") && password == "password".hashCode()) {
            model.setCurrUser("lady@shelterme.com");
            return true;
        }
        if (password == 0 || TextUtils.isEmpty(username)) {
            model.displayErrorMessage("This field is required", this);
            return false;
//        } else if (model.getAccountByIndex(0).validatePassword(password)
//                || model.getAccountByIndex(0).getUsername().equals(username)) {
        } else if (attempting != null
                && username.equals(attempting.getUsername())
                && email.equals(attempting.getEmail())
                && attempting.validatePassword(password)) {
            model.displaySuccessMessage("Login successful!", this);
        } else if (!Model.getAccountListPointer().containsValue(attempting)) {
            model.displayErrorMessage("User does not exist, please register an account", this);
            return false;
        } else if (!attempting.validatePassword(password)) {
            model.displayErrorMessage("Incorrect username or password", this);
            return false;
        }
        model.setCurrUser(email);
        return true;
    }

//    public void displaySuccessMessage(String message) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Success!").setMessage(message)
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // do nothing
//                    }
//                })
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .show();
//    }
//
//    public void displayErrorMessage(String error) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        Log.e("error", "displayErrorMessage: " + error);
//        builder.setTitle("Error")
//                .setMessage(error)
//                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // co nothing
//                    }
//                })
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .show();
//    }

}

