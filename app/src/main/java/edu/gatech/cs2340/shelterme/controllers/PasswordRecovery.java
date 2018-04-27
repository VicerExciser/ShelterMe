/*
 * This activity allows a User to submit a request to Admins for resetting a password
 */
package edu.gatech.cs2340.shelterme.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.cs2340.shelterme.R;
import edu.gatech.cs2340.shelterme.model.Account;
import edu.gatech.cs2340.shelterme.model.Employee;
import edu.gatech.cs2340.shelterme.model.Message;
import edu.gatech.cs2340.shelterme.model.Model;
import edu.gatech.cs2340.shelterme.model.UnlockRequest;
import edu.gatech.cs2340.shelterme.model.User;

/**
 * The type Password recovery.
 */
public class PasswordRecovery extends AppCompatActivity {
    Model model = Model.getInstance();
//    List<String> questions;
    String[] questions;
    static final int MAX_QUESTIONS = 4;
    ArrayAdapter<String> questionsAdapter;
    Spinner securitySpin;
    boolean questionSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);
        final TextView emailInput = findViewById(R.id.acctEmail);
        final TextView answerInput = findViewById(R.id.securityAnswer);
        Button submitPasswordRequest = findViewById(R.id.submitReq);

        securitySpin = (Spinner) findViewById(R.id.spinnerQuestions);
        assert(securitySpin != null);

        updateQuestions(null);

        questionsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, questions);
        securitySpin.setAdapter(questionsAdapter);
        securitySpin.setSelection(0);

        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) questionSet = false;
                if (Model.isValidEmailAddress(s)) {
                    String email = s.toString();
                    if (model.emailExists(email)) {
                        updateQuestions(email);
                    }
//                    else {
//                        Toast.makeText(PasswordRecovery.this, "No account found for this" +
//                                "email", Toast.LENGTH_SHORT).show();
//                    }
                }
//                else {
//                    Toast.makeText(PasswordRecovery.this, "Invalid email",
//                            Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Model.isValidEmailAddress(s)) {
                    String email = s.toString();
                    if (model.emailExists(email)) {
                        updateQuestions(email);
                    }
//                    else {
//                        Toast.makeText(PasswordRecovery.this, "No account found for this" +
//                                "email", Toast.LENGTH_SHORT).show();
//                    }
                }
//                else {
//                    Toast.makeText(PasswordRecovery.this, "Invalid email",
//                            Toast.LENGTH_SHORT).show();
//                }
            }
        });

        submitPasswordRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String email = emailInput.getText().toString();
            String answer = answerInput.getText().toString();
            if (email.isEmpty() || answer.isEmpty()) {
                model.displayErrorMessage("All fields must be filled out!",
                        PasswordRecovery.this);
            } else {
                Account account = Model.getAccountByEmail(email);
                if (account == null) {
                    model.displayErrorMessage("No user found for the entered email!",
                            PasswordRecovery.this);
                } else {
                    if (answer.equalsIgnoreCase(account.getSecAns())) {
                        Message unlockReq = new UnlockRequest((User) account);
                        model.addToMessages(unlockReq);
                        String successText = "Password successfully reset! " +
                                "\nA request has been submitted for your account to be unlocked" +
                                " and a new password will be sent to your email soon.";
//                            Toast.makeText(PasswordRecovery.this, successText,
//                                    Toast.LENGTH_LONG);
                        model.displaySuccessMessage(successText,
                                PasswordRecovery.this);
                        final Intent nextIntent = new Intent(PasswordRecovery.this,
                                MainActivity.class);
//                            if (account.getAccountType() != Account.Type.USER) {
//                                switch (account.getAccountType()) {
//                                    case EMP:
//                                        nextIntent = new Intent(PasswordRecovery.this,
//                                                EmployeeHomePage.class);
//                                        break;
//                                    case ADMIN:
//                                        nextIntent = new Intent(PasswordRecovery.this,
//                                                AdminHomePage.class);
//                                        break;
//                                }
//                            }
                        Thread thread = new Thread() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(3500);
                                    startActivity(nextIntent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        thread.start();
//                            startActivity(nextIntent);
                    } else {
                        model.displayErrorMessage("Incorrect answer to the security" +
                                " question!", PasswordRecovery.this);
                    }
                }
            }
            }
        });
    }

    private void updateQuestions(String accountEmail) {
        if (accountEmail == null) {
//            questions = new ArrayList<>(MAX_QUESTIONS);
            questions = new String[MAX_QUESTIONS];
            int i = 0;
            for (Account.Question q : Account.Question.values()) {
//                questions.add(q.toString());
                questions[i++] = q.toString();
            }
        } else {
            Account account = Model.getAccountByEmail(accountEmail);
            if (account != null) {
//                questions = new ArrayList<>(1);
                questions = new String[1];
//                questions.add(account.getSecQuest().toString());
                questions[0] = account.getSecQuest().toString();
                questionSet = true;
            }
        }
        questionsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, questions);
        securitySpin.setAdapter(questionsAdapter);
        securitySpin.setSelection(0);
    }


}
