package edu.gatech.cs2340.shelterme.controllers;


import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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

public class DBUtil {

    public static final DBUtil ourInstance = new DBUtil();

    private final Map<String, User> users = new HashMap<String, User>();
    private final Map<String, Admin> admins = new HashMap<String, Admin>();
    private final Map<String, Employee> employees = new HashMap<>();
    private final Map<String, Account> accountList;// = Model.getAccountListPointer();
    private final Map<String, Shelter> shelterList;// = Model.getShelterListPointer();

    // Write a message to the database
    private static FirebaseDatabase database;// = FirebaseDatabase.getInstance();
//    const root = firebase.database().ref();
    private static DatabaseReference rootRef;// = database.getReference(/*"message"*/);

    private static DatabaseReference usersRef;// = rootRef.child("users");

    private static DatabaseReference employeesRef;// = rootRef.child("employees");
    private static DatabaseReference adminsRef;// = rootRef.child("admins");
    private static DatabaseReference sheltersRef;// = rootRef.child("shelters");


    // TODO: Add ChildUpdateListeners, etc. to DBReferences


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
//        database.setPersistenceEnabled(true);
        rootRef = database.getReference();
//        rootRef.keepSynced(true);
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
                try {
                    for (DataSnapshot child : children) {
                        User user = child.getValue(User.class);
                        users.put(user.getEmail(), user);
                        accountList.put(user.getEmail(), user);
                    }
                } catch (com.google.firebase.database.DatabaseException dbe) {
                    Log.e("userDataChange", dbe.getMessage());
                    dbe.printStackTrace();
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
                    employees.put(emp.getEmail(), emp);
                    accountList.put(emp.getEmail(), emp);
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
                    admins.put(admin.getEmail(), admin);
                    accountList.put(admin.getEmail(), admin);
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
                         shelterList.put(shelter.getShelterName(), shelter);
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
        if (branch.equals("users")) {
            usersRef.child(key).child("stayReports").setValue(((User)newAccount).getStayReports());
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

//    FirebaseDatabase ref = FirebaseDatabase.getInstance("https://shelterme-2340.firebaseio.com/");

    public void updateShelterVacanciesAndBeds(Shelter s, HashMap<String, Collection<Bed>> reserved, User u) {
        String key = s.getShelterKey() + "_" + s.getShelterName();
        String bedTypeKey = s.findValidBedType(u.generateKey());
        DatabaseReference ref = sheltersRef.child(key);

        // TODO: Figure out how to update a Shelter's Beds hashmap in Firebase (fuck Android seriously)
        // ids map contains <bedTypeKey, bedIDNumber>

        // get all bed_ids for each bedTypeKey
        // use s.getBeds()

//        String updateKey = "beds/" + u.generateKey();
//        for (int i = 0; i < ids.length; i++) {
//            updateKey += "/"+ids[i];
//        }

        // may need to update individual beds...
        Map<String, Object> update = new HashMap<>();
        for (String bedKey : reserved.keySet()) {
            String jsonPath;
            for (Bed bed : reserved.get(bedKey)) {
                jsonPath = String.format("/%s/%s", bedKey, bed.getId());
                ref.child(jsonPath).setValue(bed);
//                update.put(jsonPath, bed);
            }
        }

        try {
//            ref.updateChildren(update);
            ref.child("vacancies").setValue(s.getVacancies());
/*
            String mapkey = key.trim() + "/vacancies";
            mapkey = mapkey.replace(' ', '%');
            update.put(mapkey, s.getVacancies());
            sheltersRef.updateChildren(update);
            update = new HashMap<>();
            mapkey = key.trim() + "/beds";
            mapkey = mapkey.replace(' ', '%');
            update.put(mapkey, s.getBeds());
*/
//            sheltersRef.setValue(update);

//            ref.child(updateKey).setValue(null);
//            ref.child(updateKey).setValue(s.getBeds().get(u.generateKey()));
//            ref.child("beds/O").setValue(s.getBeds().get("O"));

//            ref.child("vacancies").setValue(s.getVacancies());
//            ref.child("beds").setValue(s.getBeds());

        } catch (Exception e) {
            Log.e("UpdateShelters: ", e.getMessage());
            updateShelterInfo(s);
        }
    }

    public void updateShelterInfo(Shelter s) {
        String key = s.getShelterKey() + "_" + s.getShelterName();
        sheltersRef.child(key).setValue(s);
    }

    public void updateUserOccupancyAndStayReports(User u) {
        String key = u.getEmail().substring(0, u.getEmail().indexOf('@'));
        usersRef.child(key).child("isOccupyingBed").setValue(u.isOccupyingBed());
        // TODO: Figure out how to make a User's StayReportList readable into Firebase
       /* ObjectMapper mapper = new ObjectMapper();
        GenericTypeIndicator<Map<String,Object>> indicator = new GenericTypeIndicator<Map<String, Object>>() {};
        TestChild value = mapper.convertValue(dataSnapshot.getValue(indicator), TestChild.class);
        GenericTypeIndicator<List<StayReport>>  = new GenericTypeIndicator<List<StayReport>>() {};
        List<StayReport> srlist = new Stack<>();
        srlist.addAll(u.getStayReports());
        usersRef.child(key).child("stayReports").setValue(srlist); */

//        Map<String, Object> update = new HashMap<>();
//        update.put("isOccupyingBed", u.isOccupyingBed());
//        update.put("stayReports", (u.getStayReports()));
//        Map<String, Map<String, Object>> userUpdate = new HashMap<>();
//        userUpdate.put(key, update);
//        usersRef.setValue(userUpdate);
    }

    public void updateUserInfo(User u) {
        String key = u.getEmail().substring(0, u.getEmail().indexOf('@'));
        usersRef.child(key).setValue(u);
    }

}
