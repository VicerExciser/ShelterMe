package edu.gatech.cs2340.shelterme;

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

public class LoginPage extends AppCompatActivity {

    private EditText mUsernameView;
    private EditText mPasswordView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mUsernameView=(EditText) findViewById(R.id.editText);
        mPasswordView=(EditText) findViewById(R.id.editText2);

        ImageButton logo = findViewById(R.id.log);
        logo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(attemptLogin()){
                    android.content.Intent myIntent1 = new android.content.Intent(view.getContext(), MainActivity.class);
                    startActivityForResult(myIntent1, 0);
                }}
        });
    }
    private boolean attemptLogin() {
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();
        Context context = getApplicationContext();
        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(username)) {
            displayErrorMessage("This field is required");
            return false;
        }
        if (!password.equals("pass")) {
            return false;
        }
        if (!username.equals("user")) {
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

