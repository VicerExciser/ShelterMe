package edu.gatech.cs2340.shelterme.model;

import android.location.Location;
import android.provider.ContactsContract;

/**
 * Created by austincondict on 2/18/18.
 */

public class Shelter {
    private String shelterName;
    private int shelterKey;
//    private int capacity;
    private String capacity;
    String restrictions;

    // Restrictions:
    private boolean ageRestricted;
    private boolean sexRestricted;
    private boolean familyAllowed;
    private boolean veteransOnly;

//    private Location location;
    private double latitude, longitude;
    private String notes;
    private ContactsContract.CommonDataKinds.Phone phoneNumber;
    private String phone, address;

    private Bed[] beds;

    // TODO: Constructors, getters, setters

    public Shelter() {
        this("Atlanta Mission: The Shephard's Inn");
    }

    public Shelter(String name) {
        shelterName = name;
        shelterKey = 1;
        capacity = "500";
        ageRestricted = false;
        sexRestricted = false;
        familyAllowed = true;
        veteransOnly = false;
    }

    public Shelter(int key, String name, String capacity, String restrictions, double longitude,
                   double latitude, String address, String specNotes, String num) {
        shelterKey = key;
        shelterName = name;
        this.capacity = capacity;
        this.restrictions = restrictions;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        notes = specNotes;
        phone = num;
//        processRestrictions();
        try {
            beds = new Bed[Integer.valueOf(capacity)];
        } catch (NumberFormatException nfe) {
            beds = null;
        }
    }

    public String toString() {
        return String.format("%d | %s | %s | %s | %2.6f | %2.6f | %s | %s | %s\n", shelterKey, shelterName, capacity,
                restrictions, longitude, latitude, address, notes, phone);
    }

    public String detail() {
        String buff = "============================================================================================\n";
        String description = String.format(" Shelter no. %d\n %s: %s\n Located at %s (%2.6f, %2.6f)\n Phone: %s\n " +
                        "Currently accepting: %s",
                shelterKey, shelterName, notes, address, longitude, latitude, phone, restrictions);
        if (!this.capacity.equals("N/A")) {
            description += " with a capacity of " + this.capacity;
        }
        return buff + description + "\n" + buff;
    }

}
