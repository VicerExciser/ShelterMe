package edu.gatech.cs2340.shelterme.model;

import android.support.annotation.Nullable;

import java.util.Collection;
import java.util.HashMap;

public class Bed {
    // Had to change Bed id to a String in the form: "bed_1" for the sake of serialization

    private String id;
//    private User occupant;
    @Nullable
    private String occupantEmail;
    private boolean isOccupied;
    private final String savedBedKey;
    //bed Key saved so it can be easily grouped with other beds of its type
    private final String associatedShelterName;
    // stored for preservation of uniqueness when comparisons made

    public Bed(String id, boolean isFamily, boolean menOnly, boolean womenOnly, Age minAge,
               Age maxAge,
               boolean veteranOnly, String savedBedKey, String associatedShelterName) {
        this.id = id;
        if ((id != null) && !id.contains("bed_")) {
            this.id = "bed_" + id;
        }
        this.isOccupied = false;
        boolean isFamily1 = isFamily;
        boolean menOnly1 = menOnly;
        boolean womenOnly1 = womenOnly;
        Age minAge1 = minAge;
        Age maxAge1 = maxAge;
        boolean veteranOnly1 = veteranOnly;
        this.savedBedKey = savedBedKey;
        this.associatedShelterName = associatedShelterName;
    }

    public Bed() {
        this("bed_1001", false, false, false, Age.MIN_AGE, Age.MAX_AGE,
                false, "FFF000_200_F", "Hope Atlanta");
    }

    @Nullable
    public String getOccupantEmail() {
        return this.occupantEmail;
    }


    public void setOccupantEmail(@Nullable String email) {
        this.occupantEmail = email;
        if (!this.isOccupied) {
            modifyOccupant(email);
        }
    }

    private void modifyOccupant(String occupantEmail) {
        User occupant = (User) Model.getAccountByEmail(occupantEmail);
        if (occupant != null) {
            occupant.setIsOccupyingBed(true);
        }
        this.isOccupied = true;
    }

    public void removeOccupant(String email) {
        ((User)Model.getAccountByEmail(email)).clearOccupiedBed();
        this.occupantEmail = null;
        this.isOccupied = false;
    }


    public String getId() {
        return id;
    }

//    public User getOccupant() {
//        return occupant;
//    }

    public boolean getIsOccupied() {
        return isOccupied;
    }

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public boolean getIsFamily() {
//        return isFamily;
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public boolean isMenOnly() {
//        return menOnly;
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public boolean isWomenOnly() {
//        return womenOnly;
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public Age getMinAge() {
//        return minAge;
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public Age getMaxAge() {
//        return maxAge;
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public boolean isVeteranOnly() {
//        return veteranOnly;
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public void setIsMenOnly(boolean onlyMale) {
//        this.menOnly = onlyMale;
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public void setIsWomenOnly(boolean onlyFemale) {
//        this.womenOnly = onlyFemale;
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public void setIsVeteranOnly(boolean onlyVets) {
//        this.veteranOnly = onlyVets;
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public void setIsFamily(boolean isFam) {
//        this.isFamily = isFam;
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public void setMinAge(Age min) {
//        this.minAge = min;
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public void setMaxAge(Age max) {
//        this.maxAge = max;
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)

    public void setId(String bedNumber) {
        String newId = bedNumber;
        if ((bedNumber != null) && !bedNumber.contains("bed_")) {
            newId = "bed_" + bedNumber;
        }
        HashMap<String, Shelter> shelterHashMap = Model.getShelterListPointer();
        for (Shelter s : shelterHashMap.values()) {
            HashMap<String, HashMap<String, Bed>> bedHashMap = s.getBeds();
            if (!bedHashMap.containsKey(newId)) {
                this.id = newId;
            }
        }
    }

    public String getSavedBedKey() {
        return savedBedKey;
    }

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public void setSavedBedKey(String savedBedKey) {
//        this.savedBedKey = savedBedKey;
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)

    @Override
    public boolean equals(Object other) {
        if ((other == null) || !(other instanceof Bed)) {
            return false;
        }
        if (this == other) {
            return true;
        }
        Bed b = (Bed) other;
        return this.associatedShelterName.equalsIgnoreCase(b.associatedShelterName)
                && this.savedBedKey.equalsIgnoreCase(b.savedBedKey)
                && this.id.equals(b.id);
    }

    @SuppressWarnings("ChainedMethodCall")
    @Override
    public int hashCode() {
        int result = 17;
        result = (31 * result) + this.id.toLowerCase().hashCode();
        result = (31 * result) + this.associatedShelterName.toLowerCase().hashCode();
        result = (31 * result) + this.savedBedKey.toLowerCase().hashCode();
        return result;
    }
}





