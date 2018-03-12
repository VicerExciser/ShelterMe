package edu.gatech.cs2340.shelterme.model;

/**
 * Created by austincondict on 2/20/18.
 */

public class Bed {
    // Had to change Bed id to a String in the form: "bed_1" for the sake of serialization

    private String id;
    private User occupant;
    private boolean isOccupied;
    private boolean isFamily;       //designates if the bed is of "family type" or for a single person
    private boolean menOnly;        //designates if there is a gender restriction against women
    private boolean womenOnly;      //designates if there is a gender restriction against men
    private Age minAge;             //designates minimum age that applies to an individual or the children of a family
    private Age maxAge;             //designates maximum age that applies to an individual or the children of a family
    private boolean veteranOnly;    //designates if the bed must have at least one veteran occupying it
    private String savedBedKey;     //bed Key saved so it can be easily grouped with other beds of its type

    public Bed(String id, boolean isFamily, boolean menOnly, boolean womenOnly, Age minAge, Age maxAge,
               boolean veteranOnly, String savedBedKey) {
        this.id = id;
        if (id != null && !id.contains("bed_"))
            this.id = "bed_" + id;
        this.isOccupied = false;
        this.isFamily = isFamily;
        this.menOnly = menOnly;
        this.womenOnly = womenOnly;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.veteranOnly = veteranOnly;
        this.savedBedKey = savedBedKey;
    }

    public Bed() {
        this("bed_1001", false, false, false, Age.MINAGE, Age.MAXAGE,
                false, "FFF000_200_F");
    }

    public void setOccupant(User occupant) {
        this.occupant = occupant;
        this.isOccupied = true;
    }

    public void removeOccupant() {
        this.occupant = null;
        this.isOccupied = false;
    }


    public String getId() {
        return id;
    }

    public User getOccupant() {
        return occupant;
    }

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
        if (bedNumber != null && !bedNumber.contains("bed_"))
                newId = "bed_" + bedNumber;
        for (Shelter s : Model.getShelterListPointer()) {
            if (!s.getBeds().containsKey(newId)) {
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
}
