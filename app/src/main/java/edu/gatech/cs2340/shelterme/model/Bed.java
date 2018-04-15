package edu.gatech.cs2340.shelterme.model;

import android.support.annotation.Nullable;

import java.util.Map;

/**
 * Represents a Bed in a shelter.
 */

public class Bed {
    // Had to change Bed id to a String in the form: "bed_1" for the sake of serialization

    private String id;
//    private User occupant;
    @Nullable
    private String occupantEmail;
    private boolean isOccupied;
    private boolean isFamily;
    private boolean menOnly;
    private boolean womenOnly;
    private Age minAge;
    private Age maxAge;
    private boolean veteranOnly;
    private String savedBedKey;
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
        this.isFamily = isFamily;
        this.menOnly = menOnly;
        this.womenOnly = womenOnly;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.veteranOnly = veteranOnly;
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

    public boolean getIsFamily() {
        return isFamily;
    }

    public boolean isMenOnly() {
        return menOnly;
    }

    public boolean isWomenOnly() {
        return womenOnly;
    }

    public Age getMinAge() {
        return minAge;
    }

    public Age getMaxAge() {
        return maxAge;
    }

    public boolean isVeteranOnly() {
        return veteranOnly;
    }

    public void setIsMenOnly(boolean onlyMale) {
        this.menOnly = onlyMale;
    }

    public void setIsWomenOnly(boolean onlyFemale) {
        this.womenOnly = onlyFemale;
    }

    public void setIsVeteranOnly(boolean onlyVets) {
        this.veteranOnly = onlyVets;
    }

    public void setIsFamily(boolean isFam) {
        this.isFamily = isFam;
    }

    public void setMinAge(Age min) {
        this.minAge = min;
    }

    public void setMaxAge(Age max) {
        this.maxAge = max;
    }

    public void setId(String bedNumber) {
        String newId = bedNumber;
        if ((bedNumber != null) && !bedNumber.contains("bed_")) {
            newId = "bed_" + bedNumber;
        }
        Map<String, Shelter> shelterMap = Model.getShelterListPointer();
        for (Shelter s : shelterMap.values()) {
            Map<String, Map<String, Bed>> bedMap = s.getBeds();
            if (!bedMap.containsKey(newId)) {
                this.id = newId;
            }
        }
    }

    public String getSavedBedKey() {
        return savedBedKey;
    }

    public void setSavedBedKey(String savedBedKey) {
        this.savedBedKey = savedBedKey;
    }

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





