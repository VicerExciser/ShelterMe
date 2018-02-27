package edu.gatech.cs2340.shelterme.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;

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

    // Restrictions:
    private boolean ageRestricted;
    private boolean sexRestricted;
    private boolean familyAllowed;
    private boolean veteransOnly;

//    private Location location;
    private double latitude;
    private double longitude;
    private String notes;
    private ContactsContract.CommonDataKinds.Phone phoneNumber;
    private String phone;
    private String address;

    private Bed[] beds;

    // TODO: Constructors, getters, setters

    public Shelter() {
        this("Atlanta Mission: The Shephard's Inn");
    }

    public Shelter(String name) {
        shelterName = name;
        shelterKey = 1;
        setCapacity("500");
        ageRestricted = false;
        sexRestricted = false;
        familyAllowed = true;
        veteransOnly = false;
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
        try {
            beds = new Bed[Integer.valueOf(capacity)];
        } catch (NumberFormatException nfe) {
            beds = null;
        }
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
}
