package edu.gatech.cs2340.shelterme.model;

import java.util.ArrayList;

/**
 * Created by austincondict on 2/18/18.
 */

public class Model {
    private static final Model ourInstance = new Model();

    public ArrayList<Account> accounts;

    public static Model getInstance() {
        return ourInstance;
    }

    private Model() {
        accounts = new ArrayList<>();
    }

}
