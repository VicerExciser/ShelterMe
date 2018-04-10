package edu.gatech.cs2340.shelterme.controllers;


import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import edu.gatech.cs2340.shelterme.model.Account;
import edu.gatech.cs2340.shelterme.model.Admin;
import edu.gatech.cs2340.shelterme.model.Bed;
import edu.gatech.cs2340.shelterme.model.Employee;
import edu.gatech.cs2340.shelterme.model.Model;
import edu.gatech.cs2340.shelterme.model.Shelter;
import edu.gatech.cs2340.shelterme.model.StayReport;
import edu.gatech.cs2340.shelterme.model.User;

import static android.content.ContentValues.TAG;

// Properly configured implementation of Runnable interface so that not all work done on main thread
public class DBUtil implements Runnable {

    private static volatile DBUtil dbUtilInstance;// = new DBUtil();

    private static volatile Map<String, User> users = new HashMap<String, User>();
    private static volatile Map<String, Admin> admins = new HashMap<String, Admin>();
    private static volatile Map<String, Employee> employees = new HashMap<>();
    private static volatile Map<String, Account> accountList = new HashMap<>();// = Model.getAccountListPointer();
    private static volatile Map<String, Shelter> shelterList = new HashMap<>();// = Model.getShelterListPointer();

    // Write a message to the database
    private static volatile FirebaseDatabase database;// = FirebaseDatabase.getInstance();
    private static volatile DatabaseReference rootRef;// = database.getReference(/*"message"*/);
    private static volatile DatabaseReference usersRef;// = rootRef.child("users");
    private static volatile DatabaseReference employeesRef;// = rootRef.child("employees");
    private static volatile DatabaseReference adminsRef;// = rootRef.child("admins");
    private static volatile DatabaseReference sheltersRef;// = rootRef.child("shelters");

    public static DBUtil getInstance() {
        if (dbUtilInstance == null) {
            synchronized (DBUtil.class) {
                if (dbUtilInstance == null) {
                    dbUtilInstance = new DBUtil();
                }
            }
        }
        return dbUtilInstance;
    }

    /*  A note on ensuring Firebase read/writes are thread-safe:
        Enclose variables that can be accessed by more than one thread in a synchronized block.
        This approach will prevent one thread from reading the variable while another is writing to it.
     */

    private DBUtil() {
        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);    // Enables persistent data caching if user goes offline or app restarts
        rootRef = database.getReference();
//        rootRef.keepSynced(true);
    }

    @Override
    public void run() {
        usersRef = rootRef.child("users");
        employeesRef = rootRef.child("employees");
        adminsRef = rootRef.child("admins");
        sheltersRef = rootRef.child("shelters");
        initAccounts();
        initShelters();
        maintainAccounts();
        maintainShelters();
    }

    /* The following onDataChange methods are demonstrative of how to configure Firebase
       to update the database contents whenever data is changed
     */
    private void maintainAccounts() {
        // Read from the database
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                try {
                    for (DataSnapshot child : children) {
                        User user = child.getValue(User.class);
                        if (user != null) {
                            synchronized (users) {
                            users.put(user.getEmail(), user);
                            }
                            synchronized (accountList) {
                            accountList.put(user.getEmail(), user);
                            }
                        }
                    }
                } catch (com.google.firebase.database.DatabaseException dbe) {
                    Log.e("userDataChange", dbe.getMessage());
                    dbe.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });

        employeesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                try {
                    for (DataSnapshot child : children) {
                        Employee emp = child.getValue(Employee.class);
                        if (emp != null) {
                            synchronized (employees) {
                            employees.put(emp.getEmail(), emp);
                            }
                            synchronized (accountList) {
                            accountList.put(emp.getEmail(), emp);
                            }
                        }
                    }
                } catch (com.google.firebase.database.DatabaseException dbe) {
                    Log.e("employeeDataChange", dbe.getMessage());
                    dbe.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        adminsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                try {
                    for (DataSnapshot child : children) {
                        Admin admin = child.getValue(Admin.class);
                        if (admin != null) {
                            synchronized (admins) {
                            admins.put(admin.getEmail(), admin);
                            }
                            synchronized (accountList) {
                            accountList.put(admin.getEmail(), admin);
                            }
                        }
                    }
                } catch (com.google.firebase.database.DatabaseException dbe) {
                    Log.e("adminDataChange", dbe.getMessage());
                    dbe.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    //
    private void maintainShelters() {
        sheltersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                try {
                    for (DataSnapshot child : children) {
                        Shelter shelter = child.getValue(Shelter.class);
                        if (shelter != null) {
                            synchronized (shelterList) {
                            shelterList.put(shelter.getShelterName(), shelter);
                            }
                        }
                    }
                } catch (com.google.firebase.database.DatabaseException dbe) {
                    Log.e("shelterDataChange", dbe.getMessage());
                    dbe.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private static void initAccounts() {
        Query usersQueryRef = usersRef.orderByKey();
        usersQueryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    synchronized (accountList) {
                    accountList.put(user.getEmail(), user);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    synchronized (accountList) {
                    accountList.put(user.getEmail(), user);
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && user != null) {
                    synchronized (accountList) {
                    accountList.remove(user.getEmail(), user);
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        Query adminsQueryRef = adminsRef.orderByKey();
        adminsQueryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Admin admin = dataSnapshot.getValue(Admin.class);
                if (admin != null) {
                    synchronized (accountList) {
                    accountList.put(admin.getEmail(), admin);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Admin admin = dataSnapshot.getValue(Admin.class);
                if (admin != null) {
                    synchronized (accountList) {
                    accountList.put(admin.getEmail(), admin);
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Admin admin = dataSnapshot.getValue(Admin.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && admin != null) {
                    synchronized (accountList) {
                    accountList.remove(admin.getEmail(), admin);
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        Query empsQueryRef = employeesRef.orderByKey();
        empsQueryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Employee emp = dataSnapshot.getValue(Employee.class);
                if (emp != null) {
                    synchronized (accountList) {
                    accountList.put(emp.getEmail(), emp);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Employee emp = dataSnapshot.getValue(Employee.class);
                if (emp != null) {
                    synchronized (accountList) {
                    accountList.put(emp.getEmail(), emp);
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Employee emp = dataSnapshot.getValue(Employee.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && emp != null) {
                    synchronized (accountList) {
                    accountList.remove(emp.getEmail(), emp);
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private static void initShelters() {
        Query shelterQueryRef = sheltersRef.orderByKey();
        shelterQueryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Shelter shelter = dataSnapshot.getValue(Shelter.class);
                if (shelter != null) {
                    synchronized (shelterList) {
                    shelterList.put(shelter.getShelterName(), shelter);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Shelter shelter = dataSnapshot.getValue(Shelter.class);
                if (shelter != null) {
                    synchronized (shelterList) {
                    shelterList.put(shelter.getShelterName(), shelter);
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Shelter shelter = dataSnapshot.getValue(Shelter.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && shelter != null) {
                    synchronized (shelterList) {
                    shelterList.remove(shelter.getShelterName(), shelter);
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    // Map: <Email, Account>
    public static HashMap<String, Account> getAccountListPointer() {
        return (HashMap<String, Account>) accountList;
    }

    // Map: <ShelterName, Shelter>
    public static HashMap<String, Shelter> getShelterListPointer() {
        return (HashMap<String, Shelter>) shelterList;
    }

    public static DatabaseReference getRef() {
        return database.getReference();
    }



    // Primary key = email (String up to '@' symbol)
    public void addAccount(Account newAccount) {
        String branch = newAccount instanceof User ? "users"
                : (newAccount instanceof Employee ? "employees"
                : (newAccount instanceof Admin ? "admins" : ""));
        if (branch.isEmpty()) return;
        String key = newAccount.getEmail().substring(0, newAccount.getEmail().indexOf('@'));
        rootRef.child(branch).child(key).setValue(newAccount,
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
        if (branch.equals("users")) {
            usersRef.child(key).child("stayReports").setValue(((User) newAccount).getStayReports());
        }

    }

    //public Account getAccountByEmail(String emailAddress) {
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
    //  return Model.getAccountByEmail(emailAddress);
    // }

    public String getEmailAssociatedWithUsername(String username) {
        if (Model.isValidEmailAddress(username)) return username;
        List<Account> accounts = new ArrayList<Account>(users.values());
        accounts.addAll(employees.values());
        accounts.addAll(admins.values());
        for (Account a : accounts) {
            if (a.getUsername().equals(username))
                return a.getEmail();
        }
        return null;
    }

    // Primary key = shelterKey_shelterName
    public void addShelter(Shelter newShelter) {
        String key = newShelter.getShelterKey() + "_" + newShelter.getShelterName();
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

    public void updateShelterVacanciesAndBeds(Shelter s, HashMap<String, Collection<Bed>> reserved,
                                              boolean reserving) {
        String key = String.format("%s_%s", s.getShelterKey(), s.getShelterName());
        DatabaseReference ref = sheltersRef.child(key);
        String bedsPath = String.format("%s/beds", key);
        DatabaseReference bedsRef = sheltersRef.child(bedsPath);

        try {
            for (String bedKey : reserved.keySet()) {
                String jsonPath, occPath;
                for (Bed bed : reserved.get(bedKey)) {
                    jsonPath = String.format("%s/%s", bedKey, bed.getId());
                    occPath = String.format("O/%s", bed.getId());
                    Log.e("jsonPath = ", jsonPath);
                    if (reserving) {
                        bedsRef.child(jsonPath).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                                  Log.e("removeValue Complete?", databaseError.getMessage());
                            }
                        });
                        bedsRef.child(occPath).setValue(bed);
                    } else {
                        bedsRef.child(occPath).removeValue();
                        bedsRef.child(jsonPath).setValue(bed);
                    }
                }
            }

            ref.child("vacancies").setValue(s.getVacancies());
            ref.child("familyCapacity").setValue(s.familyCapacity);
            ref.child("singleCapacity").setValue(s.singleCapacity);
        } catch (Exception e) {
            Log.e("UpdateShelters: ", e.getMessage());
//            updateShelterInfo(s);
        }
    }

    public void updateShelterInfo(Shelter s) {
        String key = s.getShelterKey() + "_" + s.getShelterName();
        sheltersRef.child(key).setValue(s);
    }

    public void updateUserOccupancyAndStayReports(User u) {
        String key = u.getEmail().substring(0, u.getEmail().indexOf('@'));
        usersRef.child(key).child("isOccupyingBed").setValue(u.isOccupyingBed());
        List<StayReport> reports = u.getStayReports();
        if (reports == null) {
            Log.e("Getting StayReports: ", "User.getStayReports == null");
            reports = new Stack<StayReport>();
        }
        try {
            usersRef.child(key).child("stayReports").setValue(reports);
        } catch (DatabaseException de) {
            Log.e("UpdateStayReports: ", de.getMessage());
        }
    }

    public void updateUserInfo(User u) {
        String key = u.getEmail().substring(0, u.getEmail().indexOf('@'));
        usersRef.child(key).setValue(u);
    }

}

