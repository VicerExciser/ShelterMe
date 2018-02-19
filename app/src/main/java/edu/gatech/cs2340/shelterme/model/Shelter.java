package edu.gatech.cs2340.shelterme.model;

import android.location.Location;
import android.provider.ContactsContract;

/**
 * Created by austincondict on 2/18/18.
 */

public class Shelter {
    private String shelterName;
    private int shelterKey;
    private int capacity;

    // Restrictions:
    private boolean ageRestricted;
    private boolean sexRestricted;
    private boolean familyAllowed;
    private boolean veteransOnly;

    private Location location;
    private String notes;
    private ContactsContract.CommonDataKinds.Phone phoneNumber;

    // TODO: Constructors, getters, setters

    public Shelter() {
        this("Atlanta Mission: The Shephard's Inn");
    }

    public Shelter(String name) {
        shelterName = name;
        shelterKey = 1;
        capacity = 500;
        ageRestricted = false;
        sexRestricted = false;
        familyAllowed = true;
        veteransOnly = false;
    }

}
