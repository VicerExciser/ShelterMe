package edu.gatech.cs2340.shelterme.controllers;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import edu.gatech.cs2340.shelterme.R;
import edu.gatech.cs2340.shelterme.model.Model;

//import com.google.firebase.auth.FirebaseAuth;

public class LoginPage extends AppCompatActivity {

    private EditText mUsernameView;
    private EditText mPasswordView;
//    private FirebaseAuth mAuth;
    Model model = Model.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mUsernameView=(EditText) findViewById(R.id.editText);
        mPasswordView=(EditText) findViewById(R.id.editText2);
        //mAuth = FirebaseAuth.getInstance();

        ImageButton logo = findViewById(R.id.log);
        ImageButton cancel = findViewById(R.id.actualcancel);
        ImageButton forgotpass = findViewById(R.id.forget);
        logo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(attemptLogin()){
                    android.content.Intent myIntent1 = new android.content.Intent(view.getContext(),
                            HomePage.class);
                    startActivityForResult(myIntent1, 0);
                }}
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.content.Intent myIntent2 = new android.content.Intent(view.getContext(),
                        MainActivity.class);
                startActivityForResult(myIntent2, 0);
            }
        });
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.content.Intent myIntent3 = new android.content.Intent(view.getContext(),
                        HomePage.class);
                startActivityForResult(myIntent3, 0);
            }
        });
    }
    private boolean attemptLogin() {
        String username = mUsernameView.getText().toString().trim();
        int password = mPasswordView.getText().toString().hashCode();
        Context context = getApplicationContext();
        if (password == 0 || TextUtils.isEmpty(username)) {
            displayErrorMessage("This field is required");
            return false;
        } else if (model.getAccountByIndex(0).validatePassword(password)
                || model.getAccountByIndex(0).getUsername().equals(username)) {
            displayErrorMessage("Pervasive data test successful! Welcome "+username+" !");
        } else if (!username.equals("user")) {
            displayErrorMessage("Your username or login is incorrect.");
            return false;
        }
        else if (password != "pass".hashCode()) {
            displayErrorMessage("Your username or login is incorrect.");
            return false;
        }
        return true;
    }
    private void displayErrorMessage(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Log.e("error", "displayErrorMessage: " + error);
        builder.setTitle("Error")
                .setMessage(error)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // co nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}

