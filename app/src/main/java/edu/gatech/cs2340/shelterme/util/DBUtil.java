package edu.gatech.cs2340.shelterme.util;


import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import edu.gatech.cs2340.shelterme.controllers.MessageBoard;
import edu.gatech.cs2340.shelterme.model.Account;
import edu.gatech.cs2340.shelterme.model.Admin;
import edu.gatech.cs2340.shelterme.model.Bed;
import edu.gatech.cs2340.shelterme.model.Employee;
import edu.gatech.cs2340.shelterme.model.Message;
import edu.gatech.cs2340.shelterme.model.ReportUserRequest;
import edu.gatech.cs2340.shelterme.model.Shelter;
import edu.gatech.cs2340.shelterme.model.StayReport;
import edu.gatech.cs2340.shelterme.model.UnlockRequest;
import edu.gatech.cs2340.shelterme.model.User;

import static android.content.ContentValues.TAG;

/**
 * The type Db util.
 */
// Properly configured implementation of Runnable interface so that not all work done on main thread
@SuppressWarnings("ALL")
// Declared volatile rather than final for sync.
//@Singleton
public final class DBUtil implements Runnable {

    private static volatile DBUtil dbUtilInstance;

    private static volatile Map<String, User> users = new HashMap<>();
    private static volatile Map<String, Admin> admins = new HashMap<>();
    private static volatile Map<String, Employee> employees = new HashMap<>();
    private static volatile Map<String, Account> accountList = new HashMap<>();
    private static volatile Map<String, Shelter> shelterList = new HashMap<>();
    private static volatile Map<String, Message> messageList = new HashMap<>();

    private static volatile DatabaseReference rootRef;
    private static volatile DatabaseReference usersRef;
    private static volatile DatabaseReference employeesRef;
    private static volatile DatabaseReference adminsRef;
    private static volatile DatabaseReference sheltersRef;
    private static volatile DatabaseReference messageRef;

    private DBUtil() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // TURN BACK ON ONCE ALL USAGE OF TEST DATA IS FINISHED
//        database.setPersistenceEnabled(true);    // Enables persistent data caching
//                                                 // if user goes offline or app restarts
        // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        rootRef = database.getReference();
//        rootRef.keepSynced(true);
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
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

    @Override
    public void run() {
        usersRef = rootRef.child("users");
        employeesRef = rootRef.child("employees");
        adminsRef = rootRef.child("admins");
        sheltersRef = rootRef.child("shelters");
        messageRef = rootRef.child("messages");
        initAccounts();
        initShelters();
        maintainAccounts();
        maintainShelters();
//        maintainMessages();
    }
//-------------------------------------------------------------------------------------------------
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
//-------------------------------------------------------------------------------------------------
    private static void initAccounts() {
        Query usersQueryRef = usersRef.orderByKey();
        usersQueryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        synchronized (accountList) {
                            accountList.put(user.getEmail(), user);
                        }
                    }
                } catch (DatabaseException dbe) {
                    Log.e("USER CHILD ADDED", dbe.getMessage());
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
                if ((user != null)) {
                    synchronized (accountList) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            accountList.remove(user.getEmail(), user);
                        } else {
                            accountList.remove(user.getEmail());
                        }
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
                if ((admin != null)) {
                    synchronized (accountList) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            accountList.remove(admin.getEmail(), admin);
                        } else {
                            accountList.remove(admin.getEmail());
                        }
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
                if ((emp != null)) {
                    synchronized (accountList) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            accountList.remove(emp.getEmail(), emp);
                        } else {
                            accountList.remove(emp.getEmail());
                        }
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
                if ((shelter != null)) {
                    synchronized (shelterList) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            shelterList.remove(shelter.getShelterName(), shelter);
                        } else {
                            shelterList.remove(shelter.getShelterName());
                        }
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
//-------------------------------------------------------------------------------------------------

    /**
     * Gets account list pointer.
     *
     * @return the account list pointer
     */
// Map: <Email, Account>
    public static Map<String, Account> getAccountListPointer() {
        return accountList;
    }

    /**
     * Gets shelter list pointer.
     *
     * @return the shelter list pointer
     */
// Map: <ShelterName, Shelter>
    public static Map<String, Shelter> getShelterListPointer() {
        return shelterList;
    }

    /**
     * Gets message list pointer.
     *
     * @return the message list pointer
     */
// Map<String, Message> messageList
    public static Map<String, Message> getMessageListPointer() {
        return messageList;
    }

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public static DatabaseReference getRef() {
//        return database.getReference();
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)


    /**
     * Add shelter.
     *
     * @param newShelter the new shelter
     */
//-------------------------------------------------------------------------------------------------
    // Primary key = shelterKey_shelterName
    @SuppressWarnings("unused")
    public void addShelter(Shelter newShelter) {
        String key = newShelter.getShelterKey() + "_" + newShelter.getShelterName();
        sheltersRef.child(key).setValue(newShelter,
                new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError,
                                           DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Log.v("DBUtil", "Data could not be saved "
                                    + databaseError.getMessage());
                        } else {
                            Log.v("DBUtil", "Data saved successfully.");
                        }
                    }
                });
        sheltersRef.child(key).child("beds").setValue(newShelter.getBeds());

    }

    /**
     * Update shelter vacancies and beds.
     *
     * @param s         the s
     * @param reserved  the reserved
     * @param reserving the reserving
     */
    public void updateShelterVacanciesAndBeds(Shelter s, Map<String, Collection<Bed>> reserved,
                                              boolean reserving) {
        String key = String.format("%s_%s", s.getShelterKey(), s.getShelterName());
        DatabaseReference ref = sheltersRef.child(key);
        String bedsPath = String.format("%s/beds", key);
        DatabaseReference bedsRef = sheltersRef.child(bedsPath);

        try {
            for (String bedKey : reserved.keySet()) {
                String jsonPath;
                String occPath;
                for (Bed bed : reserved.get(bedKey)) {
                    jsonPath = String.format("%s/%s", bedKey, bed.getId());
                    occPath = String.format("O/%s", bed.getId());
                    Log.e("jsonPath = ", jsonPath);
                    if (reserving) {
                        bedsRef.child(jsonPath).removeValue(
                                new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError,
                                                   DatabaseReference databaseReference) {
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
            ref.child("familyCapacity").setValue(s.getFamilyCapacity());
            ref.child("singleCapacity").setValue(s.getSingleCapacity());
        } catch (Exception e) {
            Log.e("UpdateShelters: ", e.getMessage());
//            updateShelterInfo(s);
        }
    }

    public void updateVacancy(Shelter s) {
        String key = String.format("%s_%s", s.getShelterKey(), s.getShelterName());
        DatabaseReference vacRef = sheltersRef.child(key).child("vacancies");
        vacRef.setValue(s.getVacancies());
    }

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public void updateShelterInfo(Shelter s) {
//        String key = s.getShelterKey() + "_" + s.getShelterName();
//        sheltersRef.child(key).setValue(s);
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)
//-------------------------------------------------------------------------------------------------

    /**
     * Add account.
     *
     * @param newAccount the new account
     */
// Primary key = email (String up to '@' symbol)
    public void addAccount(Account newAccount) {
        /*Account.Type*/ String type = newAccount.getAccountType();
        String branch = (type.equals(Account.Type.USER.toString()) || type.equals("USER")) ? "users"
                : ((type.equals(Account.Type.ADMIN.toString()) || type.equals("ADMIN")) ? "employees"
                : ((type.equals(Account.Type.EMP.toString()) || type.equals("EMP")) ? "admins" : ""));
        if (branch.isEmpty()) {
            return;
        }
        String key = newAccount.getEmail().substring(0, newAccount.getEmail().indexOf('@'));
        rootRef.child(branch).child(key).setValue(newAccount,
                new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError,
                                           DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Log.v("DBUtil", "Data could not be saved "
                                    + databaseError.getMessage());
                        } else {
                            Log.v("DBUtil", "Data saved successfully.");
                        }
                    }
                });
    //        newAccount.setAccountID(newAcctRef);
        if ("users".equals(branch)) {
            //noinspection ConstantConditions
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


    /*
    public String getEmailAssociatedWithUsername(String username) {
        if (Model.isValidEmailAddress(username)) {
            return username;
        }
        Collection<Account> accounts = new ArrayList<>();
        accounts.addAll(users.values());
        accounts.addAll(employees.values());
        accounts.addAll(admins.values());
        for (Account a : accounts) {
            if (a.getUsername().equals(username)) {
                return a.getEmail();
            }
        }
        return null;
    }
    */

    /**
     * Update user occupancy and stay reports.
     *
     * @param u the u
     */
    public void updateUserOccupancyAndStayReports(User u) {
        String key = u.getEmail().substring(0, u.getEmail().indexOf('@'));
        usersRef.child(key).child("isOccupyingBed").setValue(u.isOccupyingBed());
        List<StayReport> reports = u.getStayReports();
        if (reports == null) {
            Log.e("Getting StayReports: ", "User.getStayReports == null");
            reports = new Stack<>();
        }
        try {
            usersRef.child(key).child("stayReports").setValue(reports);
        } catch (DatabaseException de) {
            Log.e("UpdateStayReports: ", de.getMessage());
        }
    }

    /**
     * Update user account status.
     *
     * @param a               the account to update as locked/unlocked
     * @param accountIsLocked the account is locked
     * @param email           the email
     */
    public void updateUserAccountStatus(Account a, boolean accountIsLocked, String email) {
        User u = (User) a;
        String key = email.substring(0, email.indexOf('@'));
        usersRef.child(key).child("accountLocked").setValue(accountIsLocked);
        usersRef.child(key).child("password").setValue(u.getPassword());
    }

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public void updateUserInfo(User u) {
//        String key = u.getEmail().substring(0, u.getEmail().indexOf('@'));
//        usersRef.child(key).setValue(u);
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)
//-------------------------------------------------------------------------------------------------
    /*
    map of Messages:
    get DB ref: FirebaseReference messageRef = rootRef.child("messages")
    add listener for data change

    */

    /**
     * Init messages.
     */
    public void initMessages() {
        messageRef.child("unlockRequests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                try {
                    for (DataSnapshot child : children) {
                        Message message = child.getValue(UnlockRequest.class);
                        if (message != null) {
                            synchronized (messageList) {
                                messageList.put(message.getTimeSent(), message);
                            }
                        }
                    }
                } catch (com.google.firebase.database.DatabaseException dbe) {
                    Log.e("messageDataChange", dbe.getMessage());
                    dbe.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });

        messageRef.child("userReports").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                try {
                    for (DataSnapshot child : children) {
                        Message message = child.getValue(ReportUserRequest.class);
                        if (message != null) {
                            synchronized (messageList) {
                                messageList.put(message.getTimeSent(), message);
                            }
                        }
                    }
                } catch (com.google.firebase.database.DatabaseException dbe) {
                    Log.e("messageDataChange", dbe.getMessage());
                    dbe.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    /**
     * Maintain messages.
     *
     * @param messagesRecyclerView the messages recycler view
     * @param mb                   the mb
     */
    public void maintainMessages(final RecyclerView messagesRecyclerView,
                                 final MessageBoard mb) {
        Query unlockReqsQuery = messageRef.child("unlockRequests").orderByKey();
        unlockReqsQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(UnlockRequest.class);
                //Now add to message map
                synchronized (messageList) {
                    if (message != null) {
                        messageList.put(message.getTimeSent(), message);
                    }
                }
                //Now Add messageList into Adapter/RecyclerView
                messagesRecyclerView.setAdapter(mb.getMessageAdapter());
                mb.updateDataSet(messagesRecyclerView);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(UnlockRequest.class);
                synchronized (messageList) {
                    if (message != null) {
                        messageList.put(message.getTimeSent(), message);
                    }
                }
                messagesRecyclerView.setAdapter(mb.getMessageAdapter());
                mb.updateDataSet(messagesRecyclerView);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Message message = dataSnapshot.getValue(UnlockRequest.class);
                synchronized (messageList) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        if (message != null) {
                            messageList.remove(message.getTimeSent(), message);
                        }
                    } else {
                        assert message != null;
                        messageList.remove(message.getTimeSent());
                    }
                }
                messagesRecyclerView.setAdapter(mb.getMessageAdapter());
                mb.updateDataSet(messagesRecyclerView);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Query userReportsQuery = messageRef.child("userReports").orderByKey();
        userReportsQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(ReportUserRequest.class);
                //Now add to message map
                synchronized (messageList) {
                    if (message != null) {
                        messageList.put(message.getTimeSent(), message);
                    }
                }
                //Now Add messageList into Adapter/RecyclerView
//                messagesRecyclerView.setAdapter(mb.getMessageAdapter());
                mb.updateDataSet(messagesRecyclerView);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(ReportUserRequest.class);
                synchronized (messageList) {
                    if (message != null) {
                        messageList.put(message.getTimeSent(), message);
                    }
                }
//                messagesRecyclerView.setAdapter(mb.getMessageAdapter());
                mb.updateDataSet(messagesRecyclerView);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Message message = dataSnapshot.getValue(ReportUserRequest.class);
                synchronized (messageList) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        if (message != null) {
                            messageList.remove(message.getTimeSent(), message);
                        }
                    } else {
                        assert message != null;
                        messageList.remove(message.getTimeSent());
                    }
                }
//                messagesRecyclerView.setAdapter(mb.getMessageAdapter());
                mb.updateDataSet(messagesRecyclerView);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Add message.
     *
     * @param newMessage the new message
     */
    public void addMessage(Message newMessage) {
//        String key = new Date(newMessage.getTimeSent()).toString(); // likely redundant
        String key = String.valueOf(new Date(newMessage.getTimeSent()).getTime());
        String branch = newMessage.isAccountUnlockRequested() ? "unlockRequests" : "userReports";
        messageRef.child(branch).child(key).setValue(newMessage,
                new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError,
                                   DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.v("DBUtil", "Data could not be saved "
                            + databaseError.getMessage());
                } else {
                    Log.v("DBUtil", "Data saved successfully.");
                }
            }
        });
    }

    /**
     * Mark message as addressed.
     *
     * @param oldMessage the old message
     */
    public void markMessageAsAddressed(Message oldMessage) {
        String key = String.valueOf(new Date(oldMessage.getTimeSent()).getTime());
        String branch = oldMessage.isAccountUnlockRequested() ? "unlockRequests" : "userReports";
        messageRef.child(branch).child(key).child("addressed").setValue(oldMessage.isAddressed());
    }
}

