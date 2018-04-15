package edu.gatech.cs2340.shelterme.controllers;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.text.TextWatcher;
import android.widget.TextView;

import java.util.AbstractCollection;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Map;

import edu.gatech.cs2340.shelterme.R;
import edu.gatech.cs2340.shelterme.model.Account;
import edu.gatech.cs2340.shelterme.model.Admin;
import edu.gatech.cs2340.shelterme.model.Employee;
import edu.gatech.cs2340.shelterme.model.Shelter;
import edu.gatech.cs2340.shelterme.model.User;
import edu.gatech.cs2340.shelterme.model.Model;
import edu.gatech.cs2340.shelterme.util.DBUtil;


public class RegistrationPage extends AppCompatActivity {

    private DBUtil dbUtil;
//    DatabaseReference dbRootRef = FirebaseDatabase.getInstance().getReference();
//    DatabaseReference userRef = dbRootRef.child("users");

    private Model model;
    private EditText fullNameField;
    private EditText emailField;
    private EditText userNameField;
    private EditText passwordField;
    private EditText passwordConfirmField;
    private EditText securityAnswerField;
    private EditText dateOfBirthField;
//    private EditText workplaceField;
    private Spinner shelterSpinner;
    private Spinner securityQuestionSpinner;
    private Spinner selectSexSpinner;
    private Spinner accountTypeSpinner;
    private TextView sexPrompt;
    private TextView dobPrompt;
    private TextView powPrompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        dbUtil = DBUtil.getInstance();
//        dbRootRef =
//        dbRef =dbUtil.getRef();
        model = Model.getInstance();

        fullNameField = findViewById(R.id.fullName);
        fullNameField.addTextChangedListener(nameFormatWatcher);

        emailField = findViewById(R.id.email);
        userNameField = findViewById(R.id.userName);

//        workplaceField = findViewById(R.id.workplace);
        shelterSpinner = findViewById(R.id.shelterSpinner);
        powPrompt = findViewById(R.id.textView11);
        Map<String, Shelter> shelterList = Model.getShelterListPointer();
        AbstractCollection<String> shelterNames
                = new HashSet<>(shelterList.keySet());
//        for (Shelter s : Model.getShelterListPointer()) {
//            shelterNames.add(s.getShelterName());
//        }
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
            shelterNames.toArray(new String[shelterNames.size()]));
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shelterSpinner.setAdapter(adapter3);
        shelterSpinner.setSelection(0);

        passwordField = findViewById(R.id.pass);
        passwordConfirmField = findViewById(R.id.passConfirm);
        passwordField.addTextChangedListener(passwordFormatWatcher);
        passwordConfirmField.addTextChangedListener(passwordFormatWatcher);

        securityAnswerField = findViewById(R.id.securityAnswer);
        dateOfBirthField = findViewById(R.id.DOB);
        dateOfBirthField.addTextChangedListener(dobEntryWatcher);

        securityQuestionSpinner = findViewById(R.id.securityQuestionSpinner);
        ArrayAdapter<Account.Question> adapter0 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                Account.Question.values());
        adapter0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        securityQuestionSpinner.setAdapter(adapter0);
        securityQuestionSpinner.setSelection(0);

        selectSexSpinner = findViewById(R.id.sexSpinner);
        ArrayAdapter<Account.Sex> adapter1 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                Account.Sex.values());
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectSexSpinner.setAdapter(adapter1);
        selectSexSpinner.setSelection(0);

        accountTypeSpinner = findViewById(R.id.userTypeSpinner);
        ArrayAdapter<Account.Type> adapter2 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                Account.Type.values());
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountTypeSpinner.setAdapter(adapter2);
        accountTypeSpinner.setSelection(0);

        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateInputs();
            }
        });
        Button cancelButton = findViewById(R.id.cancelButton);
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
//        workplaceField.setVisibility(View.GONE);
        shelterSpinner.setVisibility(View.GONE);
        powPrompt.setVisibility(View.GONE);
        sexPrompt.setVisibility(View.GONE);
        dobPrompt.setVisibility(View.GONE);

        accountTypeSpinner.setOnItemSelectedListener(typeWatcher);

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
////        userRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String text = dataSnapshot.getValue(String.class);
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }

    private final AdapterView.OnItemSelectedListener typeWatcher
            = new AdapterView.OnItemSelectedListener() {
    // Will need to add some sort of authentication input for verifying Admin accounts
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // If account type User is selected, display DOB & Sex input fields
            // If account type Employee is selected, display Workplace input field
            // Else, hide those EditText fields
            switch (position) {
                case 0:  // User
                    dateOfBirthField.setVisibility(View.VISIBLE);
                    selectSexSpinner.setVisibility(View.VISIBLE);
                    sexPrompt.setVisibility(View.VISIBLE);
                    dobPrompt.setVisibility(View.VISIBLE);
//                workplaceField.setVisibility(View.GONE);
                    shelterSpinner.setVisibility(View.GONE);
                    powPrompt.setVisibility(View.GONE);
                    break;
                case 1:  // Shelter Employee
//                workplaceField.setVisibility(View.VISIBLE);
                    shelterSpinner.setVisibility(View.VISIBLE);
                    powPrompt.setVisibility(View.VISIBLE);
                    dateOfBirthField.setVisibility(View.GONE);
                    selectSexSpinner.setVisibility(View.GONE);
                    sexPrompt.setVisibility(View.GONE);
                    dobPrompt.setVisibility(View.GONE);
                    break;
                case 2:  //Admin
//                workplaceField.setVisibility(View.GONE);
                    shelterSpinner.setVisibility(View.GONE);
                    powPrompt.setVisibility(View.GONE);
                    dateOfBirthField.setVisibility(View.GONE);
                    selectSexSpinner.setVisibility(View.GONE);
                    sexPrompt.setVisibility(View.GONE);
                    dobPrompt.setVisibility(View.GONE);
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    };

    private final TextWatcher dobEntryWatcher = new TextWatcher() {
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

    private final TextWatcher nameFormatWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable text) {
            try {
                if (text.length() > 0) {
                    int delim = text.toString().indexOf(' ');
                    if (!Character.isUpperCase(text.charAt(0))) {
                        CharSequence fixLetter = String.valueOf(
                                Character.toUpperCase(text.charAt(0)));
                        text.replace(0, 1, fixLetter);
                    } else if ((delim != -1) && (text.toString().length() > (delim + 1))) {
                        char lName = text.charAt(delim + 1);
                        if (!Character.isUpperCase(lName)) {
                            CharSequence fixLetter = String.valueOf(Character.toUpperCase(lName));
                            text.replace(delim + 1, delim + 2, fixLetter);
                        }
                    }
                }
            } catch (Exception ignored) {
            }
        }
    };

    private final TextWatcher passwordFormatWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().length() < 4) {
                dateOfBirthField.setError("Password must be at least 4 characters");
                // Obviously, we can set stricter password format constraints
            } else {
                dateOfBirthField.setError(null);
            }
        }

        @Override
        public void afterTextChanged(Editable text) {}
    };

    private void validateInputs() {
        try {
            boolean allFieldsComplete;
            // Process name:
            String name = fullNameField.getText().toString();
            name = name.trim();
            allFieldsComplete = !name.isEmpty() && !name.equals(fullNameField.getHint().toString());
            int delimiter = name.indexOf(' ');
            if (!allFieldsComplete || (delimiter < 0)) {
                model.displayErrorMessage("First and last name are required", this);
                return;
            }

            name = Character.toUpperCase(name.charAt(0)) + name.substring(1, delimiter + 1)
                    + Character.toUpperCase(name.charAt(delimiter + 1))
                    + name.substring(delimiter + 2);

            // Validate email:
            String email = emailField.getText().toString();
            email = email.trim();
            allFieldsComplete = !email.isEmpty() && !email.equals(emailField.getHint().toString());
            if (!allFieldsComplete) {
                model.displayErrorMessage("Email address is required", this);
                return;
            } else if (!Model.isValidEmailAddress(email)) {
                model.displayErrorMessage("Invalid email address", this);
                return;
            }

            // Register username:
            String username = userNameField.getText().toString();
            username = username.trim();
            if (username.isEmpty() || username.equals(userNameField.getHint().toString())
                    || username.equals(email)) {
                // Set username as an alias to email to avoid having to distinguish
                // between valid username or email for login validity checks
                username = email;
            }
            Map<String, Account> accountHashMap = Model.getAccountListPointer();
            for (Account a : accountHashMap.values()) {
                String aEmail = a.getEmail();
                String aUsername = a.getUsername();
                if (aEmail.equals(email)) {
                    model.displayErrorMessage("Account already exists for this email",
                            this);
                    return;
                } else if (aUsername.equals(username)) {
                    model.displayErrorMessage("Account already exists for this username",
                            this);
                    return;
                }
            }

            // Validate password & confirmation:
            String passString = passwordField.getText().toString();
            passString = passString.trim();
            int password = passString.hashCode();
            String confirmPassString = passwordConfirmField.getText().toString();
            confirmPassString = confirmPassString.trim();
            int confirmPass = confirmPassString.hashCode();
            int passLength = passString.length();
            String passHint = passwordField.getHint().toString();
            int hintHash = passHint.hashCode();
            if ((password == 0) || (passLength < 4)
                    || (password == hintHash)) {
                model.displayErrorMessage("Valid password is required", this);
                return;
            } else if (confirmPass != password) {
                model.displayErrorMessage("Password inputs must match", this);
                return;
            }


            // Process user type:
            Account.Type type = (Account.Type) accountTypeSpinner.getSelectedItem();

            // Process DOB and Sex if regular User:
            String dob;
            @SuppressWarnings("ChainedMethodCall") int currentYear
                    = Calendar.getInstance().get(Calendar.YEAR);
            @SuppressWarnings("ChainedMethodCall") int currentMonth
                    = Calendar.getInstance().get(Calendar.MONTH);
            int age = 0;
            Account.Sex sex = Account.Sex.MALE;
            String selectedWorkplace; //enteredWorkplace;
            Shelter workplace = null;

            if (type == Account.Type.USER) {
                //noinspection ChainedMethodCall
                dob = dateOfBirthField.getText().toString();
                //noinspection ChainedMethodCall
                if (!dob.isEmpty() && !dob.equals(dateOfBirthField.getHint().toString())) {
                    delimiter = dob.indexOf('/');
                    int enteredYear = Integer.parseInt(dob.substring(delimiter + 1));
                    int enteredMonth = Integer.parseInt(dob.substring(0, delimiter));
                    int monthDiff = currentMonth - enteredMonth;
                    age = currentYear - enteredYear;
                    if (monthDiff < 0) {
                        age -= 1;
                    }
                    if (age == 0) {
                        return;
                    }
                } else {
                    model.displayErrorMessage("Month and year of birth are required",
                            this);
                    return;
                }

                sex = (Account.Sex) selectSexSpinner.getSelectedItem();

            } else if (type == Account.Type.EMP) {
//            enteredWorkplace = workplaceField.getText().toString();
//            allFieldsComplete &= !enteredWorkplace.isEmpty()
//                    && !enteredWorkplace.equals(workplaceField.getHint().toString());
//            if (!allFieldsComplete) {
//                model.displayErrorMessage("At which shelter do you work?", this);
//                return;
//            }
//            // This is temporary placeholder for future code
//            workplace = new Shelter();
                selectedWorkplace = String.valueOf(shelterSpinner.getSelectedItem());
                workplace = Model.findShelterByName(selectedWorkplace);
            }


            // Process security question & response:
            Account.Question secQuest
                    = (Account.Question) securityQuestionSpinner.getSelectedItem();
            @SuppressWarnings("ChainedMethodCall") String secAns
                    = securityAnswerField.getText().toString();
            //noinspection ChainedMethodCall
            allFieldsComplete = (!secAns.isEmpty()
                    && !secAns.equals(securityAnswerField.getHint().toString()));
            if (!allFieldsComplete) {
                model.displayErrorMessage("A response is required for your security question",
                        this);
                return;
            }

// False suggestion that allFieldsComplete
            //noinspection ConstantConditions
            if (allFieldsComplete) {
                createAccount(name, username, email, password, sex, age, type, secQuest,
                        secAns, workplace);

                startActivity(new Intent(RegistrationPage.this, LoginPage.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createAccount(String nm, String un, String em, int pw, Account.Sex sx, int age,
                               Account.Type at, Account.Question sq, String qa, Shelter wp) {
        Account newAccount = null;
        switch (at) {
            case USER:
                newAccount = new User(nm, un, em, pw, sx, age, false, sq, qa);
                ////// CHANGE FAMILY FALSE !!!!!!!!!!
//                DatabaseReference newUserRef = dbRootRef.child("users").push();
//                newUserRef.setValue(newAccount);
//                newAccount.setAccountID(newUserRef);
                break;
            case EMP:
                newAccount = new Employee(nm, un, em, pw, wp, sq, qa);
//                DatabaseReference newEmpRef = dbRootRef.child("employees").push();
//                newEmpRef.setValue(newAccount);
//                newAccount.setAccountID(newEmpRef);
                break;
            case ADMIN:
                newAccount = new Admin(nm, un, em, pw, sq, qa);
//                DatabaseReference newAdminRef = dbRootRef.child("admins").push();
//                newAdminRef.setValue(newAccount);
//                newAccount.setAccountID(newAdminRef);
                break;
        }

// False suggestion that newAccount will never be null
        //noinspection ConstantConditions
        if (newAccount != null) {
            model.addToAccounts(newAccount);
            dbUtil.addAccount(newAccount);
            model.displaySuccessMessage("Account successfully created!", this);
        } else {
            model.displayErrorMessage("An error occurred while registering your account",
                    this);
        }

    }

}
