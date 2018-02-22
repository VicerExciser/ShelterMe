package edu.gatech.cs2340.shelterme.controllers;

import android.content.DialogInterface;
import android.graphics.ColorSpace;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.text.TextWatcher;
import android.widget.TextView;

import java.util.Calendar;

import edu.gatech.cs2340.shelterme.R;
import edu.gatech.cs2340.shelterme.model.Account;
import edu.gatech.cs2340.shelterme.controllers.MainActivity;
import edu.gatech.cs2340.shelterme.model.Admin;
import edu.gatech.cs2340.shelterme.model.Employee;
import edu.gatech.cs2340.shelterme.model.Shelter;
import edu.gatech.cs2340.shelterme.model.User;
import edu.gatech.cs2340.shelterme.model.Model;


public class RegistrationPage extends AppCompatActivity {

    private EditText fullNameField;
    private EditText emailField;
    private EditText userNameField;
    private EditText passwordField;
    private EditText passwordConfirmField;
    private EditText securityAnswerField;
    private EditText dateOfBirthField;
    private EditText workplaceField;
    private Spinner securityQuestionSpinner;
    private Spinner selectSexSpinner;
    private Spinner accountTypeSpinner;
    private Button submitButton;
    private Button cancelButton;
    private TextView sexPrompt;
    private TextView dobPrompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        fullNameField = findViewById(R.id.fullName);
        fullNameField.addTextChangedListener(nameFormatWatcher);

        emailField = findViewById(R.id.email);
        userNameField = findViewById(R.id.userName);
        workplaceField = findViewById(R.id.workplace);

        passwordField = findViewById(R.id.pass);
        passwordConfirmField = findViewById(R.id.passConfirm);
        passwordField.addTextChangedListener(passwordFormatWatcher);
        passwordConfirmField.addTextChangedListener(passwordFormatWatcher);

        securityAnswerField = findViewById(R.id.securityAnswer);
        dateOfBirthField = findViewById(R.id.DOB);
        dateOfBirthField.addTextChangedListener(dobEntryWatcher);

        securityQuestionSpinner = findViewById(R.id.securityQuestionSpinner);
        ArrayAdapter<Account.Question> adapter0 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,
                Account.Question.values());
        adapter0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        securityQuestionSpinner.setAdapter(adapter0);
        securityQuestionSpinner.setSelection(0);

        selectSexSpinner = findViewById(R.id.sexSpinner);
        ArrayAdapter<Account.Sex> adapter1 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,
                Account.Sex.values());
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectSexSpinner.setAdapter(adapter1);
        selectSexSpinner.setSelection(0);

        accountTypeSpinner = findViewById(R.id.userTypeSpinner);
        ArrayAdapter<Account.Type> adapter2 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,
                Account.Type.values());
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountTypeSpinner.setAdapter(adapter2);
        accountTypeSpinner.setSelection(0);

        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateInputs();
            }
        });
        cancelButton = findViewById(R.id.cancelButten);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationPage.this, MainActivity.class));
            }
        });

        sexPrompt = findViewById(R.id.textView7);
        dobPrompt = findViewById(R.id.textView9);

        dateOfBirthField.setVisibility(View.GONE);
        selectSexSpinner.setVisibility(View.GONE);
        workplaceField.setVisibility(View.GONE);
        sexPrompt.setVisibility(View.GONE);
        dobPrompt.setVisibility(View.GONE);

        accountTypeSpinner.setOnItemSelectedListener(typeWatcher);

    }

    private AdapterView.OnItemSelectedListener typeWatcher = new AdapterView.OnItemSelectedListener() {
    // TODO: Will need to add some sort of authentication input for verifying Admin accounts
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // If account type User is selected, display DOB & Sex input fields
            // If account type Employee is selected, display Workplace input field
            // Else, hide those EditText fields
            if (position == 0) { // User
                dateOfBirthField.setVisibility(View.VISIBLE);
                selectSexSpinner.setVisibility(View.VISIBLE);
                sexPrompt.setVisibility(View.VISIBLE);
                dobPrompt.setVisibility(View.VISIBLE);
                workplaceField.setVisibility(View.GONE);
            } else if (position == 1) { // Shelter Employee
                workplaceField.setVisibility(View.VISIBLE);
                dateOfBirthField.setVisibility(View.GONE);
                selectSexSpinner.setVisibility(View.GONE);
                sexPrompt.setVisibility(View.GONE);
                dobPrompt.setVisibility(View.GONE);
            }
            else if (position == 2) { //Admin
                workplaceField.setVisibility(View.GONE);
                dateOfBirthField.setVisibility(View.GONE);
                selectSexSpinner.setVisibility(View.GONE);
                sexPrompt.setVisibility(View.GONE);
                dobPrompt.setVisibility(View.GONE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    };

    private TextWatcher dobEntryWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            /*
            String working = s.toString();
            boolean isValid = true;
            if (working.length() == 2 && before == 0) {
                if (Integer.parseInt(working) < 1 || Integer.parseInt(working) > 12) {
                    isValid = false;
                } else {
                    working += "/";
                    dateOfBirthField.setText(working);
                    dateOfBirthField.setSelection(working.length());
                }
            } else if (working.length() == 7 && before == 0) {
                String enteredYear = working.substring(3);
                int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                if (Integer.parseInt(enteredYear) < currentYear) {
                    isValid = false;
                }
            } else if (working.length() < 7) {
                isValid = false;
            } else {
                isValid = true;
            }

            if (!isValid) {
                dateOfBirthField.setError("Enter a valid date: MM/YYYY");
            } else {
                dateOfBirthField.setError(null);
            }
            */
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void afterTextChanged(Editable text) {
            if (text.length() == 2) {
                text.append('/');
            }
        }
    };

    private TextWatcher nameFormatWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable text) {
            if (text.length() > 0) {
                int delim = text.toString().indexOf(' ');
                if (!Character.isUpperCase(text.charAt(0))) {
                    CharSequence fixLetter =  String.valueOf(Character.toUpperCase(text.charAt(0)));
                    text.replace(0, 1, (CharSequence) fixLetter);
                }
                else if (delim != -1 && text.toString().length() > delim + 1) {
                    char lName = text.charAt(delim + 1);
                    if (!Character.isUpperCase(lName)) {
                        CharSequence fixLetter = String.valueOf(Character.toUpperCase(lName));
                        text.replace(delim+1, delim+2, fixLetter);
                    }
                }
            }
        }
    };

    private TextWatcher passwordFormatWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().length() < 4) {
                dateOfBirthField.setError("Password must be at least 4 characters");
                // TODO: Obviously, we can set stricter password format constraints
            } else {
                dateOfBirthField.setError(null);
            }
        }

        @Override
        public void afterTextChanged(Editable text) {}
    };

    private void validateInputs() {
        boolean allFieldsComplete = true;
        // Process name:
        String name = fullNameField.getText().toString().trim();
        allFieldsComplete &= (!name.isEmpty() && !name.equals(fullNameField.getHint().toString()));
        int delim = name.indexOf(' ');
        if (!allFieldsComplete || delim < 0) {
            displayErrorMessage("First and last name are required");
            return;
        }


        name = Character.toUpperCase(name.charAt(0)) + name.substring(1, delim + 1)
                + Character.toUpperCase(name.charAt(delim + 1)) + name.substring(delim + 2);


        // Validate email:
        String email = emailField.getText().toString().trim();
        // TODO: if (email does not already exist in database)
        allFieldsComplete &= (!email.isEmpty() && !email.equals(emailField.getHint().toString()));
        if (!allFieldsComplete) {
            displayErrorMessage("Email address is required");
            return;
        } else if (!isValidEmailAddress(email)) {
            displayErrorMessage("Invalid email address");
            return;
        }

        // Register username:
        String username = userNameField.getText().toString().trim();
        // TODO: if (username does not already exist in database)
        if (username.isEmpty() || username.equals(userNameField.getHint().toString())
                || username.equals(email)) {
            // Set username as an alias to email to avoid having to distinguish
            // between valid username or email for login validity checks
            username = email;
        }

        // Validate password & confirmation:
        int password = passwordField.getText().toString().trim().hashCode();
        int confirmPass = passwordConfirmField.getText().toString().trim().hashCode();
        if (password == 0 || passwordField.getText().length() < 4
                || password == passwordField.getHint().toString().hashCode()) {
            displayErrorMessage("Valid password is required");
            return;
        } else if (confirmPass != password) {
            displayErrorMessage("Password inputs must match");
            return;
        }


        // Process user type:
        Account.Type type = (Account.Type) accountTypeSpinner.getSelectedItem();

        // Process DOB and Sex if regular User:
        String dob;
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int age = 0;
        Account.Sex sex = Account.Sex.MALE;
        String enteredWorkplace;
        Shelter workplace = null;

        if (type == Account.Type.USER) {
            dob = dateOfBirthField.getText().toString();
            if (!dob.isEmpty() && !dob.equals(dateOfBirthField.getHint().toString())) {
                delim = dob.indexOf('/');
                int enteredYear = Integer.parseInt(dob.substring(delim + 1));
                // TODO: Calculate for month of birth as well for more accurate age read-in
                age = currentYear - enteredYear;
                if (age == 0)
                    return;
            } else {
                displayErrorMessage("Month and year of birth are required");
                return;
            }

            sex = (Account.Sex) selectSexSpinner.getSelectedItem();

        } else if (type == Account.Type.EMP) {
            enteredWorkplace = workplaceField.getText().toString();
            allFieldsComplete &= !enteredWorkplace.isEmpty()
                    && !workplace.equals(workplaceField.getHint().toString());
            if (!allFieldsComplete) {
                displayErrorMessage("At which shelter do you work?");
                return;
            }
            // TODO: Search thru archived Shelters to verify this workplace exists
            // This is temporary placeholder for future code
            workplace = new Shelter();
        }


        // Process security question & response:
        Account.Question secQuest = (Account.Question) securityQuestionSpinner.getSelectedItem();
        String secAns = securityAnswerField.getText().toString();
        allFieldsComplete &= (!secAns.isEmpty()
                && !secAns.equals(securityAnswerField.getHint().toString()));
        if (!allFieldsComplete) {
            displayErrorMessage("A response is required for your security question");
            return;
        }


        if (allFieldsComplete) {
            createAccount(name, username, email, password, sex, age, type, secQuest, secAns, workplace);

            startActivity(new Intent(RegistrationPage.this, LoginPage.class));
        }
    }

    private boolean isValidEmailAddress(String email) {
        String ePattern = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]" +
                "+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.find();
    }

    private void displayErrorMessage(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Log.e("error", "displayErrorMessage: " + error);
        builder.setTitle("Error")
                .setMessage(error)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void createAccount(String nm, String un, String em, int pw, Account.Sex sx,
                               int age, Account.Type at, Account.Question sq, String qa, Shelter wp) {
        Account newAccount = null;
        switch (at) {
            case USER:
                newAccount = new User(nm, un, em, pw, sx, age, sq, qa);
                break;
            case EMP:
                newAccount = new Employee(nm, un, em, pw, wp, sq, qa);
                break;
            case ADMIN:
              newAccount = new Admin(nm, un, em, pw, sq, qa);
                break;
        }
        // TODO: Save new account in DB
        Model model = Model.getInstance();
        if (newAccount != null) {
            model.addToAccounts(newAccount);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Success!").setMessage("Account successfully created!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            displayErrorMessage("An error occured while registering your account");
            return;
        }



    }

}
