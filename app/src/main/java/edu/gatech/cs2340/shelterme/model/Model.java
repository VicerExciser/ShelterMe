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

    private static ArrayList<Account> accounts;
    private static ArrayList<Shelter> shelters;

    public static Model getInstance() {
        return ourInstance;
    }

    private Model() {
        dbUtil = DBUtil.getInstance();

        accounts = new ArrayList<>();
//        loadShelters();
        shelters = new ArrayList<>();
    }

    public static  List<Account> getAccountListPointer() {
        return accounts;
    }

    public static List<Shelter> getShelterListPointer() {
        return shelters;
    }

    public void addToAccounts(Account acct) {
        accounts.add(acct);
    }

    public static Account getAccountByEmail(String email) {
        Account toRet = null;
        for (Account a : accounts) {
            if (a.getEmail().equals(email)) {
                toRet = a;
            }
        }
        return toRet;
    }

    public Account getAccountByIndex(int indx) {
        return accounts.get(indx);
    }

    public boolean removeAccountOfEmail(String email) {
        Account toRem = null;
        for (Account a : accounts) {
            if (a.getEmail().equals(email)) {
                toRem = a;

            }
        }
        return  accounts.remove(toRem);
    }

    public void addShelter(Shelter s) {
        shelters.add(s);
//        dbUtil.addShelter(s);
        System.out.println(s.toString());
    }


    public void getShelterDetails(Shelter s) {
        Log.v(TAG, s.detail());
    }

    public void getAllShelterDetails() {
        for (Shelter s : shelters) {
            getShelterDetails(s);
        }
    }

    public static Shelter findShelterByName(String name) {
        for (Shelter s : shelters) {
            if (s.getShelterName().equals(name))
                return s;
        }
        return null;
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
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void displayErrorMessage(String error, Context callerClass) {
        AlertDialog.Builder builder = new AlertDialog.Builder(callerClass);
        Log.e("error", "displayErrorMessage: " + error);
        builder.setTitle("Error")
                .setMessage(error)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // co nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


}
