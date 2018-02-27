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
import edu.gatech.cs2340.shelterme.model.Model;

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
                    Intent myIntent1 = new Intent(view.getContext(),
                            HomePage.class);
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
        Account attempting = Model.getAccountByEmail(DBUtil.getInstance().getEmailAssociatedWithUsername(username));
        if (password == 0 || TextUtils.isEmpty(username)) {
            model.displayErrorMessage("This field is required", this);
            return false;
//        } else if (model.getAccountByIndex(0).validatePassword(password)
//                || model.getAccountByIndex(0).getUsername().equals(username)) {
        } else if (attempting != null && attempting.validatePassword(password)) {
                model.displaySuccessMessage("Login successful!", this);
        } else if (!username.equals("user")) {
            model.displayErrorMessage("Incorrect username or password", this);
            return false;
        }
        else if (password != "pass".hashCode()) {
            model.displayErrorMessage("Incorrect username or password", this);
            return false;
        }
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

