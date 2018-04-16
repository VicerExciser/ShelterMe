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

    /**
     * Instantiates a new Bed.
     *
     * @param id                    the id
     * @param isFamily              the is family
     * @param menOnly               the men only
     * @param womenOnly             the women only
     * @param minAge                the min age
     * @param maxAge                the max age
     * @param veteranOnly           the veteran only
     * @param savedBedKey           the saved bed key
     * @param associatedShelterName the associated shelter name
     */
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

    /**
     * Instantiates a new Bed.
     */
    public Bed() {
        this("bed_1001", false, false, false, Age.MIN_AGE, Age.MAX_AGE,
                false, "FFF000_200_F", "Hope Atlanta");
    }

    /**
     * Gets occupant email.
     *
     * @return the occupant email
     */
    @Nullable
    public String getOccupantEmail() {
        return this.occupantEmail;
    }


    /**
     * Sets occupant email.
     *
     * @param email the email
     */
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

    /**
     * Remove occupant.
     *
     * @param email the email
     */
    public void removeOccupant(String email) {
        ((User)Model.getAccountByEmail(email)).clearOccupiedBed();
        this.occupantEmail = null;
        this.isOccupied = false;
    }


    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

//    public User getOccupant() {
//        return occupant;
//    }

    /**
     * Gets is occupied.
     *
     * @return the is occupied
     */
    public boolean getIsOccupied() {
        return isOccupied;
    }

    /**
     * Gets is family.
     *
     * @return the is family
     */
    public boolean getIsFamily() {
        return isFamily;
    }

    /**
     * Is men only boolean.
     *
     * @return the boolean
     */
    public boolean isMenOnly() {
        return menOnly;
    }

    /**
     * Is women only boolean.
     *
     * @return the boolean
     */
    public boolean isWomenOnly() {
        return womenOnly;
    }

    /**
     * Gets min age.
     *
     * @return the min age
     */
    public Age getMinAge() {
        return minAge;
    }

    /**
     * Gets max age.
     *
     * @return the max age
     */
    public Age getMaxAge() {
        return maxAge;
    }

    /**
     * Is veteran only boolean.
     *
     * @return the boolean
     */
    public boolean isVeteranOnly() {
        return veteranOnly;
    }

    /**
     * Sets is men only.
     *
     * @param onlyMale the only male
     */
    public void setIsMenOnly(boolean onlyMale) {
        this.menOnly = onlyMale;
    }

    /**
     * Sets is women only.
     *
     * @param onlyFemale the only female
     */
    public void setIsWomenOnly(boolean onlyFemale) {
        this.womenOnly = onlyFemale;
    }

    /**
     * Sets is veteran only.
     *
     * @param onlyVets the only vets
     */
    public void setIsVeteranOnly(boolean onlyVets) {
        this.veteranOnly = onlyVets;
    }

    /**
     * Sets is family.
     *
     * @param isFam the is fam
     */
    public void setIsFamily(boolean isFam) {
        this.isFamily = isFam;
    }

    /**
     * Sets min age.
     *
     * @param min the min
     */
    public void setMinAge(Age min) {
        this.minAge = min;
    }

    /**
     * Sets max age.
     *
     * @param max the max
     */
    public void setMaxAge(Age max) {
        this.maxAge = max;
    }

    /**
     * Sets id.
     *
     * @param bedNumber the bed number
     */
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

    /**
     * Gets saved bed key.
     *
     * @return the saved bed key
     */
    public String getSavedBedKey() {
        return savedBedKey;
    }

    /**
     * Sets saved bed key.
     *
     * @param savedBedKey the saved bed key
     */
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





