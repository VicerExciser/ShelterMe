package edu.gatech.cs2340.shelterme.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Map;

import edu.gatech.cs2340.shelterme.R;
import edu.gatech.cs2340.shelterme.model.Account;
import edu.gatech.cs2340.shelterme.model.Model;

//import com.google.firebase.auth.FirebaseAuth;

public class LoginPage extends AppCompatActivity {

    private EditText mUsernameView;
    private EditText mPasswordView;
    private final Model model = Model.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mUsernameView= findViewById(R.id.editText);
        mPasswordView= findViewById(R.id.editText2);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // User is logged in
            startActivity(new Intent(LoginPage.this, HomePage.class));
            finish();
        }

        ImageButton logo = findViewById(R.id.log);
        ImageButton cancel = findViewById(R.id.actualcancel);
        ImageButton forgotPass = findViewById(R.id.forget);

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(attemptLogin()){
                    Account acct = model.getCurrUser();
                    Intent myIntent1 = null;
                    Account.Type type = acct.getAccountType();
                    switch (type) {
                        case USER:
                            myIntent1 = new Intent(view.getContext(), HomePage.class);
                            break;
                        case ADMIN:
                            myIntent1 = new Intent(view.getContext(), AdminHomePage.class);
                            break;
                        case EMP:
                            myIntent1 = new Intent(view.getContext(), EmployeeHomePage.class);
                            break;
                    }
                    if (myIntent1 != null) {
                        startActivityForResult(myIntent1, 0);
                    }
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

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent3 = new Intent(view.getContext(),
                        HomePage.class);
                startActivityForResult(myIntent3, 0);
            }
        });
    }
    private boolean attemptLogin() {
        String username = mUsernameView.getText().toString();
        username = username.trim();
        String passString =  mPasswordView.getText().toString();
        int password = passString.hashCode();
        String email = Model.getEmailAssociatedWithUsername(username);
        Account attempting = Model.getAccountByEmail(email);

        Map<String, Account> accountMap = Model.getAccountListPointer();
        if (accountMap.isEmpty()) {
            Log.e("attemptLogin", "Account list is empty!");
        } else {
            int count = 0;
            Map<String, Account> accountCollection = Model.getAccountListPointer();
            for (Account acct : accountCollection.values()) {
                Log.e("Account " + count, acct.getUsername());
                count++;
            }
        }

//        if ("lady".equals(username) && (password == "password".hashCode())) {
//            model.setCurrUser("lady@shelterme.com");
//            return true;
//        }
        if ((password == 0) || TextUtils.isEmpty(username)) {
            model.displayErrorMessage("This field is required", this);
            return false;
//        } else if (model.getAccountByIndex(0).validatePassword(password)
//                || model.getAccountByIndex(0).getUsername().equals(username)) {
        } else {
            if (email == null) {
                model.displayErrorMessage("No existing user found", this);
                return false;
            }
            if ((attempting != null)
                    && username.equals(attempting.getUsername())
                    && email.equals(attempting.getEmail())
                    && attempting.validatePassword(password)) {
                model.displaySuccessMessage("Login successful!", this);
            } else //noinspection ChainedMethodCall
                if (!Model.getAccountListPointer().containsValue(attempting)) {
                model.displayErrorMessage("User does not exist, please register an account", this);
                return false;
            } else {
                assert attempting != null;
                if (!attempting.validatePassword(password)) {
                    model.displayErrorMessage("Incorrect username or password", this);
                    return false;
                }
            }
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

