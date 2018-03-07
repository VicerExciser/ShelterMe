package edu.gatech.cs2340.shelterme.controllers;


import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.cs2340.shelterme.model.Account;
import edu.gatech.cs2340.shelterme.model.Admin;
import edu.gatech.cs2340.shelterme.model.Employee;
import edu.gatech.cs2340.shelterme.model.Model;
import edu.gatech.cs2340.shelterme.model.Shelter;
import edu.gatech.cs2340.shelterme.model.User;

import static android.content.ContentValues.TAG;

public class DBUtil {

    private static final DBUtil ourInstance = new DBUtil();

    private final List<User> users = new ArrayList<User>();
    private final List<Admin> admins = new ArrayList<Admin>();
    private final List<Employee> employees = new ArrayList<Employee>();
    private final List<Account> accountList;// = Model.getAccountListPointer();
    private final List<Shelter> shelterList;// = Model.getShelterListPointer();

    // Write a message to the database
    private static FirebaseDatabase database;// = FirebaseDatabase.getInstance();
//    const root = firebase.database().ref();
    private static DatabaseReference rootRef;// = database.getReference(/*"message"*/);

    private static DatabaseReference usersRef;// = rootRef.child("users");

    private static DatabaseReference employeesRef;// = rootRef.child("employees");
    private static DatabaseReference adminsRef;// = rootRef.child("admins");
    private static DatabaseReference sheltersRef;// = rootRef.child("shelters");



//    myRef.setValue("Hello, World!");

    // Read from the database
//myRef.addValueEventListener(new ValueEventListener() {
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//            // This method is called once with the initial value and again
//            // whenever data at this location is updated.
//            String value = dataSnapshot.getValue(String.class);
//            Log.d(TAG, "Value is: " + value);
//        }
//
//        @Override
//        public void onCancelled(DatabaseError error) {
//            // Failed to read value
//            Log.w(TAG, "Failed to read value.", error.toException());
//        }
//    });

    private DBUtil() {
        accountList = Model.getAccountListPointer();
        shelterList = Model.getShelterListPointer();

        database = FirebaseDatabase.getInstance();
        rootRef = database.getReference();
        usersRef = rootRef.child("users");
        employeesRef = rootRef.child("employees");
        adminsRef = rootRef.child("admins");
        sheltersRef = rootRef.child("shelters");

        /* The following onDataChange methods are demonstrative of how to configure Firebase
           to update the database contents whenever data is changed
         */

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot child : children) {
                    User user = child.getValue(User.class);
                    users.add(user);
                    accountList.add(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
         });

        employeesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot child : children) {
                    Employee emp = child.getValue(Employee.class);
                    employees.add(emp);
                    accountList.add(emp);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
         });

         adminsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot child : children) {
                    Admin admin = child.getValue(Admin.class);
                    admins.add(admin);
                    accountList.add(admin);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
         });

         sheltersRef.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                 try {
                     for (DataSnapshot child : children) {
                         Shelter shelter = child.getValue(Shelter.class);
                         shelterList.add(shelter);
                     }
                 } catch (com.google.firebase.database.DatabaseException dbe) {
                     Log.e("shelterDataChange", dbe.getMessage());
                     dbe.printStackTrace();
                 }
             }

             @Override
             public void onCancelled(DatabaseError databaseError) { }
         });
    }


    public static DBUtil getInstance() {
        return ourInstance;
    }

    public static DatabaseReference getRef() {
        return database.getReference();
    }

    // Primary key = email (String up to '@' symbol)
    public void addAccount(Account newAccount) {
//        String branch = "";
//        if (newAccount instanceof User) {
//            branch = "users";
//        } else if (newAccount instanceof Employee) {
//            branch = ""
//        } else if (newAccount instanceof Admin) {
//
//        }
        String branch = newAccount instanceof User ? "users"
                : (newAccount instanceof Employee ? "employees"
                : (newAccount instanceof Admin ? "admins" : ""));
        if (branch.isEmpty()) return;
//        DatabaseReference newAcctRef = rootRef.child(branch).push();
//        newAccount.setID(newAcctRef.getKey());
        String key = newAccount.getEmail().substring(0, newAccount.getEmail().indexOf('@'));
        rootRef.child(branch).child(key).setValue(newAccount,
//        newAcctRef.setValue(newAccount,
                new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.v("DBUtil", "Data could not be saved " + databaseError.getMessage());
                } else {
                    Log.v("DBUtil", "Data saved successfully.");
                }
            }
        });
//        newAccount.setAccountID(newAcctRef);

    }

    public Account getAccountByEmail(String emailAddress) {
//        Account target = null;
////        Query userQuery = usersRef.orderByChild("email").equalTo(emailAddress);
////        Query empQuery = employeesRef.orderByChild("email").equalTo(emailAddress);
////        Query adminQuery = adminsRef.orderByChild("email").equalTo(emailAddress);
////        DataSnapshot request = usersRef.
//        for (User u : users) {
//            if (u.getEmail().equals(emailAddress)) {
//                target = u;
//                break;
//            }
//        }
//        if (target == null) {
//            for (Employee e : employees) {
//                if (e.getEmail().equals(emailAddress)) {
//                    target = e;
//                    break;
//                }
//            }
//        }
//        if (target == null) {
//            for (Admin a : admins) {
//                if (a.getEmail().equals(emailAddress)) {
//                    target = a;
//                    break;
//                }
//            }
//        }
//        return target;
        return Model.getAccountByEmail(emailAddress);
    }

    public String getEmailAssociatedWithUsername(String username) {
        if (Model.isValidEmailAddress(username)) return username;
        List<Account> accounts = new ArrayList<Account>(users);
        accounts.addAll(employees);
        accounts.addAll(admins);
        for (Account a : accounts) {
            if (a.getUsername().equals(username))
                return a.getEmail();
        }
        return null;
    }

    // Primary key = shelterKey_shelterName
    public void addShelter(Shelter newShelter) {
//        DatabaseReference newSheltRef = sheltersRef.push();
//        newShelter.setDbID(newSheltRef.getKey());
        String key = newShelter.getShelterKey() + "_" + newShelter.getShelterName();
//        newSheltRef.setValue(newShelter,
        sheltersRef.child(key).setValue(newShelter,
                new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.v("DBUtil", "Data could not be saved " + databaseError.getMessage());
                } else {
                    Log.v("DBUtil", "Data saved successfully.");
                }
            }
        });
        sheltersRef.child(key).child("beds").setValue(newShelter.getBeds());

    }

}
