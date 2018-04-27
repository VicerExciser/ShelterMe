package edu.gatech.cs2340.shelterme.model;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.gatech.cs2340.shelterme.controllers.MessageBoard;
import edu.gatech.cs2340.shelterme.util.DBUtil;
import edu.gatech.cs2340.shelterme.util.MessageAdapter;

/* Facade Controller */

/**
 * The type Model.
 */
// SINGLETON -- Updated to be Thread-Safe for Synchronization
@SuppressWarnings("AssignmentToStaticFieldFromInstanceMethod")
public final class Model {
    private static volatile Model modelInstance; // Volatile so multiple threads can access
    private static volatile DBUtil dbUtil;
    private static final String TAG = "Model";

    private static Map<String, Account> accounts;
    private static Map<String, Shelter> shelters;
    private static Map<String, Message> messages;
    private Account currUser;
    private Account currEmployee;  // both of these are for testing purposes, not thread-safe

    private Model() {
        while (dbUtil == null) {
            dbUtil = DBUtil.getInstance();
        }
        while (accounts == null) {
            accounts = DBUtil.getAccountListPointer();
        }
        while (shelters == null) {
            shelters = DBUtil.getShelterListPointer();
        }
        while (messages == null) {
            messages = DBUtil.getMessageListPointer();
        }
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static Model getInstance() {
        if (modelInstance == null) {
            synchronized (Model.class) {
                if (modelInstance == null) {
                    modelInstance = new Model();
                }
            }
        }
        return modelInstance;
    }


    /**
     * Gets account list pointer.
     *
     * @return the account list pointer
     */
// Map: <Email, Account>
    public static Map<String, Account> getAccountListPointer() {
        return accounts;
    }

    /**
     * Gets shelter list pointer.
     *
     * @return the shelter list pointer
     */
// Map: <ShelterName, Shelter>
    public static Map<String, Shelter> getShelterListPointer() {
        return shelters;
    }

    /**
     * Gets message list pointer.
     *
     * @return the message list pointer
     */
    public static Map<String, Message> getMessageListPointer() { return messages; }

    /**
     * Init messages.
     */
    public static void initMessages() {
        dbUtil.initMessages();
    }

    /**
     * Maintain messages.
     *
     * @param messagesRecyclerView the messages recycler view
     * @param mb              the calling MessageBoard activity
     */
    public static void maintainMessages(RecyclerView messagesRecyclerView, MessageBoard mb) {
        dbUtil.maintainMessages(messagesRecyclerView, mb);
    }

    /**
     * Test message.
     */
    public void testMessage() {
        addToMessages(new UnlockRequest());
    }

    public void addToMessages(Message msg) {
        dbUtil.addMessage(msg);
    }

    /**
     * Verify shelter parcel shelter.
     *
     * @param shelter the shelter
     * @return the shelter
     */
    public Shelter verifyShelterParcel(Shelter shelter) {
        if (shelters.containsValue(shelter)) {
            return shelters.get(shelter.getShelterName());
        }
        return shelter;
    }

    /**
     * Locks/unlocks User account and updates Firebase and all synchronized data.
     *
     * @param u User to ban or grant access to
     * @param locked whether the User account should be locked/unlocked
     */
    public static void updateUserAccountStatus(Account u, boolean locked) {
//        u.setAccountLocked(locked);
        dbUtil.updateUserAccountStatus(u, locked, u.getEmail());
    }

    /**
     * Gets curr user.
     *
     * @return the curr user
     */
    public Account getCurrUser() {return currUser;}

    /**
     * Sets curr user.
     *
     * @param email the email
     * @param usr   the usr
     */
    @SuppressWarnings("unused")
    public void setCurrUser(String email, User usr) { this.currUser = usr;}

    /**
     * Sets curr user.
     *
     * @param email the email
     */
    public void setCurrUser(String email) {
        Account found = getAccountByEmail(email);
        if (found != null) {
            String empString = Account.Type.EMP.toString();
            String adminString = Account.Type.ADMIN.toString();
            String fat = found.getAccountType();
            if (empString.equals(fat) || "EMP".equals(fat)) {
                setCurrEmployee(found);
                return;
            } else if (adminString.equals(fat) || "ADMIN".equals(fat)) {
                return;  // no actions taken for logged in administrators
            }
        }
        currUser = found;
    }

    /**
     * Add to accounts.
     *
     * @param acct the acct
     */
    public void addToAccounts(Account acct) {
        accounts.put(acct.getEmail(), acct);
    }

    /**
     * Email exists boolean.
     *
     * @param email the email
     * @return the boolean
     */
    public boolean emailExists(String email) {
        return accounts.containsKey(email);
    }

    /**
     * Gets account by email.
     *
     * @param email the email
     * @return the account by email
     */
    public static Account getAccountByEmail(String email) {
        if (email == null) {
            return null;
        }
        Account toRet = accounts.get(email);
        /*Account.Type*/ String type = toRet.getAccountType();
        return (
                (type.equals(Account.Type.USER.toString())
                        || type.equals("USER"))
                        ? (User)toRet
                : ((type.equals(Account.Type.ADMIN.toString())
                        || type.equals("ADMIN")))
                        ? (Admin)toRet
                : (((type.equals(Account.Type.EMP.toString())
                        || type.equals("EMP")))
                        ? (Employee)toRet
                : toRet)
        );
    }

    /**
     * Gets email associated with username.
     *
     * @param username the username
     * @return the email associated with username
     */
    public static String getEmailAssociatedWithUsername(String username) {
        if (username != null) {
            for (Account a : accounts.values()) {
                if (username.equalsIgnoreCase(a.getUsername())) {
                    return a.getEmail();
                }
            }
        }
        return null;
    }

    /**
     * Gets curr employee.
     *
     * @return the curr employee
     */
    public Account getCurrEmployee() {return currEmployee;}

    /**
     * Sets curr employee.
     *
     * @param emp the employee currently sending an administrator request
     */
//    public void setCurrEmployee(String email) {currEmployee = getAccountByEmail(email);}
    private void setCurrEmployee(Account emp) { currEmployee = emp; }

    /**
     * Mark message as addressed.
     *
     * @param oldMessage the message being resolved
     */
    static void markMessageAsAddressed(Message oldMessage) {
        dbUtil.markMessageAsAddressed(oldMessage);
    }

//    public static User

//    public Account getAccountByIndex(int indx) {
//        return accounts.get(indx);
//    }

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public void removeAccountOfEmail(String email) {
////        Account toRem = accounts.get(email);
////        for (Account a : accounts) {
////            if (a.getEmail().equals(email)) {
////                toRem = a;
////
////            }
////        }
//        accounts.remove(email);
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)

    /**
     * Add shelter.
     *
     * @param s the s
     */
    public void addShelter(Shelter s) {
        shelters.put(s.getShelterName(), s);
//        dbUtil.addShelter(s);
    }

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public void removeShelter(String name) {
//        shelters.remove(name);
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)

    /**
     * Update vacancy.
     *
     * @param s the s
     */
    public void updateVacancy(Shelter s) {
        dbUtil.updateVacancy(s);
    }


    private void getShelterDetails(Shelter s) {
        Log.v(TAG, s.detail());
    }

    /**
     * Update user occupancy and stay reports.
     *
     * @param u the u
     */
    public void updateUserOccupancyAndStayReports(User u) {
        dbUtil.updateUserOccupancyAndStayReports(u);
    }

    /**
     * Update shelter vacancies and beds.
     *
     * @param s         the s
     * @param reserved  the reserved
     * @param reserving the reserving
     */
    public void updateShelterVacanciesAndBeds(Shelter s, Map<String, List<Bed>> reserved,
                                              boolean reserving) {
        dbUtil.updateShelterVacanciesAndBeds(s, reserved, reserving);
    }

    /**
     * Gets all shelter details.
     */
    public void getAllShelterDetails() {
        for (Shelter s : shelters.values()) {
            getShelterDetails(s);
        }
    }

    /**
     * Find shelter by name shelter.
     *
     * @param name the name
     * @return the shelter
     */
    public static Shelter findShelterByName(String name) {
        return shelters.get(name);
    }

    /**
     * Is valid email address boolean.
     *
     * @param email the email
     * @return the boolean
     */
    public static boolean isValidEmailAddress(CharSequence email) {
        String ePattern = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]" +
                "+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.find();
    }

    /**
     * Display success message.
     *
     * @param message     the message
     * @param callerClass the caller class
     */
    @SuppressWarnings("ChainedMethodCall")
    public void displaySuccessMessage(CharSequence message, Context callerClass) {
        AlertDialog.Builder builder = new AlertDialog.Builder(callerClass);
        builder.setTitle("Success!").setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * Display error message.
     *
     * @param error       the error
     * @param callerClass the caller class
     */
    @SuppressWarnings("ChainedMethodCall")
    public void displayErrorMessage(String error, Context callerClass) {
        AlertDialog.Builder builder = new AlertDialog.Builder(callerClass);
        Log.e("error", "displayErrorMessage: " + error);
        builder.setTitle("Error")
                .setMessage(error)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
