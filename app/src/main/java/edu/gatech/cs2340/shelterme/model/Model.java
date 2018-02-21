package edu.gatech.cs2340.shelterme.model;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

import edu.gatech.cs2340.shelterme.R;

/**
 * Created by austincondict on 2/18/18.
 */

// SINGLETON
public class Model {
    private static final Model ourInstance = new Model();
    // ^ when exactly does this get executed?

    private static final String TAG = "Model";

    private ArrayList<Account> accounts;
    private ArrayList<Shelter> shelters;

    public static Model getInstance() {
        return ourInstance;
    }

    private Model() {
        accounts = new ArrayList<>();
//        loadShelters();
        shelters = new ArrayList<>();
    }

    public void addToAccounts(Account acct) {
        accounts.add(acct);
    }

    public Account getAccountOfEmail(String email) {
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

}
