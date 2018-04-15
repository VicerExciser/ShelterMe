package edu.gatech.cs2340.shelterme.model;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.Map;

import edu.gatech.cs2340.shelterme.util.DBUtil;
import edu.gatech.cs2340.shelterme.util.MessageAdapter;

/* Facade Controller */

// SINGLETON -- Updated to be Thread-Safe for Synchronization
@SuppressWarnings("AssignmentToStaticFieldFromInstanceMethod")
//@Singleton
public final class Model {
    private static volatile Model modelInstance; // Volatile so multiple threads can access
    private static volatile DBUtil dbUtil;
    private static final String TAG = "Model";

    private static Map<String, Account> accounts;
    private static Map<String, Shelter> shelters;
    private static Map<String, Message> messages;
    private Account currUser;

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


    // Map: <Email, Account>
    public static Map<String, Account> getAccountListPointer() {
        return accounts;
    }

    // Map: <ShelterName, Shelter>
    public static Map<String, Shelter> getShelterListPointer() {
        return shelters;
    }

    public static Map<String, Message> getMessageListPointer() { return messages; }

    public static void initMessages() {
        dbUtil.initMessages();
    }

    public static void maintainMessages(RecyclerView messagesRecyclerView, MessageAdapter adapter) {
        dbUtil.maintainMessages(messagesRecyclerView, adapter);
    }

    public void testMessage() {
        addToMessages(new Message());
    }

    private void addToMessages(Message msg) {
        dbUtil.addMessage(msg);
    }

    public Shelter verifyShelterParcel(Shelter shelter) {
        if (shelters.containsValue(shelter)) {
            return shelters.get(shelter.getShelterName());
        }
        return shelter;
    }

    public Account getCurrUser() {return currUser;}
    @SuppressWarnings("unused")
    public void setCurrUser(String email, User usr) { this.currUser = usr;}
    public void setCurrUser(String email) {currUser = getAccountByEmail(email);}

    public void addToAccounts(Account acct) {
        accounts.put(acct.getEmail(), acct);
    }

    public static Account getAccountByEmail(String email) {
        if (email == null) {
            return null;
        }
        Account toRet = accounts.get(email);
        Account.Type type = toRet.getAccountType();
        return (type == Account.Type.USER) ? (User) toRet
                : ((type == Account.Type.ADMIN) ? (Admin) toRet
                : ((type == Account.Type.EMP) ? (Employee) toRet : toRet));
    }

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

    public void addShelter(Shelter s) {
        shelters.put(s.getShelterName(), s);
//        dbUtil.addShelter(s);
    }

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public void removeShelter(String name) {
//        shelters.remove(name);
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)


    private void getShelterDetails(Shelter s) {
        Log.v(TAG, s.detail());
    }

    public void getAllShelterDetails() {
        for (Shelter s : shelters.values()) {
            getShelterDetails(s);
        }
    }

    public static Shelter findShelterByName(String name) {
        return shelters.get(name);
    }

    public static boolean isValidEmailAddress(CharSequence email) {
        String ePattern = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]" +
                "+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.find();
    }

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
