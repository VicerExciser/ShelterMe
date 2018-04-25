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
import edu.gatech.cs2340.shelterme.model.User;

//import com.google.firebase.auth.FirebaseAuth;

/**
 * The type Login page.
 */
public class LoginPage extends AppCompatActivity {

    private EditText mUsernameView;
    private EditText mPasswordView;
    private final Model model = Model.getInstance();
    private int incorrectLogins;
    private static final int MAX_ATTEMPTS = 3;

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
                    if (acct == null) {
                        String username = mUsernameView.getText().toString();
                        String email = Model.getEmailAssociatedWithUsername(username);
                        acct = Model.getAccountByEmail(email);
                    }
                    Intent myIntent1 = null;
                    /*Account.Type*/ String type = acct.getAccountType();
//                    switch (type) {
                    if (type.equals(Account.Type.USER.toString()) || type.equals("USER"))
                            myIntent1 = new Intent(view.getContext(), HomePage.class);
//                            break;
//                        case ADMIN:
                    else if (type.equals(Account.Type.ADMIN.toString()) || type.equals("ADMIN"))
                            myIntent1 = new Intent(view.getContext(), AdminHomePage.class);
//                            break;
//                        case EMP:
                    else if (type.equals(Account.Type.EMP.toString()) || type.equals("EMP"))
                            myIntent1 = new Intent(view.getContext(), EmployeeHomePage.class);
//                            break;
//                    }
                    assert(myIntent1 != null);
                    startActivity/*ForResult*/(myIntent1 /*, 0*/);
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
                        PasswordRecovery.class);
                startActivityForResult(myIntent3, 0);
            }
        });
    }
    private boolean attemptLogin() {
        String username = mUsernameView.getText().toString();
        username = username.trim();
        String passString =  mPasswordView.getText().toString();
        int password = passString.hashCode();
        String email;
        if (Model.isValidEmailAddress(username) && model.emailExists(username)) {
            email = username;
        } else {
            email = Model.getEmailAssociatedWithUsername(username);
        }
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
                model.displayErrorMessage("No account email entered", this);
                return false;
            }
            if (!model.emailExists(email)) {
                model.displayErrorMessage("No existing user found", this);
                return false;
            }
            if ((attempting == null || !Model.getAccountListPointer().containsValue(attempting))) {
                model.displayErrorMessage("User does not exist, please register an account",
                        this);
                return false;
            }
            if (attempting.isAccountLocked()) {  // Account is locked
                model.displayErrorMessage("This account is locked! Proceed to the Password " +
                        "Recovery page ('forgot my password') to unlock with a new password",
                        this);
                return false;
            }
            if (username.equals(attempting.getUsername()) && email.equals(attempting.getEmail())) {
                if (attempting.validatePassword(password)) {
                    model.displaySuccessMessage("Login successful!", this);
                } else {
                    incorrectLogins++;
                    if (incorrectLogins >= MAX_ATTEMPTS) {
//                        attempting.setAccountLocked(true);
                        Model.updateUserAccountStatus((User)attempting, true);
                        model.displayErrorMessage("Incorrect password for user " +
                                attempting.getUsername() + ".\nThis account is now locked!", this);
                    } else {
                        model.displayErrorMessage("Incorrect password for user " +
                                attempting.getUsername() + ".\nOnly " + (MAX_ATTEMPTS - incorrectLogins) +
                                " attempts remaining before lockout!", this);
                    }
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

