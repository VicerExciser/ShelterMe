package edu.gatech.cs2340.shelterme.model;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import edu.gatech.cs2340.shelterme.R;
import edu.gatech.cs2340.shelterme.controllers.DBUtil;


// SINGLETON
public class Model {
    private static final Model ourInstance = new Model();
    // ^ when exactly does this get executed?

    private static final String TAG = "Model";

    private DBUtil dbUtil;// = DBUtil.getInstance();

    private static HashMap<String, Account> accounts;
    private static HashMap<String, Shelter> shelters;
    private Account currUser;

    public static Model getInstance() {
        return ourInstance;
    }

    private Model() {
        dbUtil = DBUtil.getInstance();

//        accounts = new HashMap<>();
//        DBUtil.initAccounts();
//        shelters = new HashMap<>();
//        DBUtil.initShelters();

        accounts = DBUtil.getAccountListPointer();
        shelters = DBUtil.getShelterListPointer();
    }

    // Map: <Email, Account>
    public static HashMap<String, Account> getAccountListPointer() {
        return accounts;
    }

    // Map: <ShelterName, Shelter>
    public static HashMap<String, Shelter> getShelterListPointer() {
        return shelters;
    }

    public Shelter verifyShelterParcel(Shelter shelter) {
        if (shelters.containsValue(shelter))
            return shelters.get(shelter.getShelterName());
        return shelter;
    }

    public Account getCurrUser() {return currUser;}
    public void setCurrUser(String email) {currUser = getAccountByEmail(email);}

    public void addToAccounts(Account acct) {
        accounts.put(acct.getEmail(), acct);
    }

    public static Account getAccountByEmail(String email) {
        Account toRet = null;
        if (email == null) return toRet;
//        for (Account a : accounts) {
//            if (a.getEmail().equals(email)) {
//                toRet = a;
//            }
//        }
        toRet = accounts.get(email);
        return toRet instanceof User ? (User)toRet
                : (toRet instanceof Admin ? (Admin)toRet
                : (toRet instanceof Employee ? (Employee)toRet : toRet));
    }

//    public static User

//    public Account getAccountByIndex(int indx) {
//        return accounts.get(indx);
//    }

    public void removeAccountOfEmail(String email) {
//        Account toRem = accounts.get(email);
//        for (Account a : accounts) {
//            if (a.getEmail().equals(email)) {
//                toRem = a;
//
//            }
//        }
        accounts.remove(email);
    }

    public void addShelter(Shelter s) {
        shelters.put(s.getShelterName(), s);
//        dbUtil.addShelter(s);
        System.out.println(s.toString());
    }


    public void getShelterDetails(Shelter s) {
        Log.v(TAG, s.detail());
    }

    public void getAllShelterDetails() {
        for (Shelter s : shelters.values()) {
            getShelterDetails(s);
        }
    }

    public static Shelter findShelterByName(String name) {
//        for (Shelter s : shelters.values()) {
//            if (s.getShelterName().equals(name))
//                return s;
//        }
//        return null;
        return shelters.get(name);
    }

    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]" +
                "+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.find();
    }

    public void displaySuccessMessage(String message, Context callerClass) {
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
//        sleepForSecond();
    }

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
//        sleepForSecond();
    }

    private void sleepForSecond() {
        try {
            java.util.concurrent.TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ie) {
            Log.e("StayReport success", "sleep() threw an InterruptedException");
        }
    }
}
