package edu.gatech.cs2340.shelterme.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by austincondict on 2/18/18.
 */

public class Shelter implements Parcelable{
    private String shelterName;
    private int shelterKey;
//    private String dbID;
//    private int capacity;
    private String capacity;
    private String restrictions;

//    private Location location;
    private double latitude;
    private double longitude;
    private String notes;
    private ContactsContract.CommonDataKinds.Phone phoneNumber;
    private String phone;
    private String address;

    private HashMap<String, LinkedList<Bed>> beds;
    // TODO: Make occupiedBeds list within beds HashMap
    // ^ perhaps try using a LinkedHashMap?
    private Bed lastBedAdded;
    private int vacancies;

    // TODO: Constructors, getters, setters

    public Shelter() {
        this("Atlanta Mission: The Shephard's Inn");
    }

    public Shelter(String name) {
        shelterName = name;
        shelterKey = 1;
        setCapacity("500");
    }

    public Shelter(int key, String name, String capacity, String restrictions, double longitude,
                   double latitude, String address, String specNotes, String num) {
        shelterKey = key;
        shelterName = name;
        this.setCapacity(capacity);
        this.setRestrictions(restrictions);
        this.setLongitude(longitude);
        this.setLatitude(latitude);
        this.setAddress(address);
        setNotes(specNotes);
        phone = num;
//        processRestrictions();
    }

    public Shelter(Parcel parcel) {
        shelterName = parcel.readString();
        capacity = parcel.readString();
        restrictions = parcel.readString();
        longitude = parcel.readDouble();
        latitude = parcel.readDouble();
        address = parcel.readString();
        phone = parcel.readString();
        notes = parcel.readString();
    }

    public String toString() {
        return String.format("%d | %s | %s | %s | %2.6f | %2.6f | %s | %s | %s\n", shelterKey, shelterName, getCapacity(),
                getRestrictions(), getLongitude(), getLatitude(), getAddress(), getNotes(), phone);
    }

    public String detail() {
        String buff = "============================================================================================\n";
        String description = String.format(" Shelter no. %d\n %s: %s\n Located at %s (%2.6f, %2.6f)\n Phone: %s\n " +
                        "Currently accepting: %s",
                shelterKey, shelterName, getNotes(), getAddress(), getLongitude(), getLatitude(), phone, getRestrictions());
        if (!this.getCapacity().equals("N/A")) {
            description += " with a capacity of " + this.getCapacity();
        }
        return buff + description + "\n" + buff;
    }

    // TODO: IGNORE DIS
    // parse Capacity strings to extrapolate integers & bed types
    private ArrayList parseCapacity(String cp) {
        ArrayList vals = new ArrayList();
        if (cp != null && !cp.isEmpty()) {
            // split up strings by spaces
            String[] tokens = cp.split(" ");
            if (cp.indexOf(' ') < 0 || tokens.length == 1) {
                if (getInt(tokens[0]) >= 0)
                    vals.add(getInt(tokens[0]));
                // if only 1 capacity value provided, determine what type of users
                // this shelter will accept and associate capacity value w/ Bed type

            } else {

                for (int i = 0; i < tokens.length; i++) {

                }
            }
        } else {
            vals.add(100);
        }
        return vals;
    }

    private int getInt(String text) {
        int val = -1;
        try {
            val = Integer.valueOf(text.trim());
        } catch (NumberFormatException nfe) { }
        return val;
    }

//    public void setDbID(String id) {
//        this.dbID = id;
//    }
//
//    public String getDbID() {
//        return this.dbID;
//    }

    public String getShelterName() {
        return this.shelterName;
    }

    public int getShelterKey() {
        return this.shelterKey;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(String restrictions) {
        this.restrictions = restrictions;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ContactsContract.CommonDataKinds.Phone getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(ContactsContract.CommonDataKinds.Phone phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getPhone() { return this.phone; }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.shelterName);
        dest.writeString(this.capacity);
        dest.writeString(this.restrictions);
        dest.writeDouble(this.longitude);
        dest.writeDouble(this.latitude);
        dest.writeString(this.address);
        dest.writeString(this.phone);
        dest.writeString(this.notes);
    }

    public static final Parcelable.Creator<Shelter> CREATOR = new Parcelable.Creator<Shelter>() {
        @Override
        public Shelter createFromParcel(Parcel parcel) {
            return new Shelter(parcel);
        }

        @Override
        public Shelter[] newArray(int size) {
            return new Shelter[0];
        }
    };

//    TODO: Make enum type for restrictions/allowances

    //Bed Handling
    public void addNewBeds(int numberOfBeds, boolean isFamily, boolean noAdultMen, Age minAge, Age maxAge, boolean veteranOnly) {
        //create unique bed key that encodes all of the bed's restrictions into the key
        String bedKey = "";
        if (isFamily) {
            bedKey += "T";
        } else {
            bedKey += "F";
        }
        if (noAdultMen) {
            bedKey += "T";
        } else {
            bedKey += "F";
        }
        bedKey += minAge.getAgeString();
        bedKey += maxAge.getAgeString();
        if (veteranOnly) {
            bedKey += "T";
        } else {
            bedKey += "F";
        }
        int lastId;
        if (lastBedAdded == null) {
            lastId = 0;
        } else {
            lastId = lastBedAdded.getId();
        }
        LinkedList<Bed> bedType;
        if (beds.containsKey(bedKey)) { // if this type of bed already exists, add it to the existing bed list
            bedType = beds.get(bedKey);
        } else {    // if this is a new bed type, create the new bed type and add it to the beds hashmap
            bedType = new LinkedList<>();
            beds.put(bedKey, bedType);
        }
        for (int i = lastId + 1; i < lastId + numberOfBeds + 1; i++) {
            Bed newBed = new Bed(i, isFamily, noAdultMen, minAge, maxAge, veteranOnly);
            bedType.add(newBed);
            vacancies++;
            // TODO: overall capacity++, lastBedAdded needs to be updated
        }
    }
    //TODO hasOpenBed method
    public boolean hasOpenBed(String userKey) {
        if (userKey == null) {
            throw new IllegalArgumentException("User Key cannot be null");
        }
        char isFamilyChar = userKey.charAt(0);
        char genderChar = userKey.charAt(1);
        String ageString = userKey.substring(2, userKey.length() - 1);
        char isVeteranChar = userKey.charAt(userKey.length() - 1);
        for (String bedKey : beds.keySet()) {
            boolean thisBedOpen = true;
            boolean isFamilyBed = false; //attributes of Bed
            boolean noAdultMenBed = false;
            boolean veteranOnlyBed = false;
            boolean isFamilyUser = false; //attributes of User
            boolean isMaleorOtherUser = false;
            boolean isVeteranUser = false;
            if (bedKey.charAt(0) == 'T') {
                isFamilyBed = true;
            }
            if (bedKey.charAt(1) == 'T') {
                noAdultMenBed = true;
            }
            if (bedKey.charAt(bedKey.length() - 1) == 'T') {
                veteranOnlyBed = true;
            }
            if (isFamilyChar == 'T') {
                isFamilyUser = true;
            }
            if (genderChar != 'F') {
                isMaleorOtherUser = true;
            }
            if (isVeteranChar == 'T') {
                isVeteranUser = true;
            }
            int minAge = Integer.parseInt(bedKey.substring(2, 5));
            int maxAge = Integer.parseInt(bedKey.substring(6, 9));
            int userAge = Integer.parseInt(ageString.substring(0, 3));
            if(isFamilyBed ^ isFamilyUser) { //make sure family type matches between user and bed
                thisBedOpen = false;
            }
            if (noAdultMenBed && (isMaleorOtherUser && (userAge > 15))) { //make sure adult males don't get into women only shelters
                thisBedOpen = false;
            }
            if (veteranOnlyBed && !(isVeteranUser)) { //exclude veteran beds from non-veterans
                thisBedOpen = false;
            }
            if (thisBedOpen) {
                return true;
            }
        }
        return false;
    }

    public void reserveBed() {

    }
}
