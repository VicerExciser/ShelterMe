package edu.gatech.cs2340.shelterme.model;

/**
 * Created by austincondict on 2/20/18.
 */

public class Bed {
    // Had to change Bed id to a String in the form: "bed_1" for the sake of serialization

    private String id;
//    private User occupant;
    private String occupantEmail;
    private boolean isOccupied;
    private boolean isFamily;       //designates if the bed is of "family type" or for a single person
    private boolean menOnly;        //designates if there is a gender restriction against women
    private boolean womenOnly;      //designates if there is a gender restriction against men
    private Age minAge;             //designates minimum age that applies to an individual or the children of a family
    private Age maxAge;             //designates maximum age that applies to an individual or the children of a family
    private boolean veteranOnly;    //designates if the bed must have at least one veteran occupying it
    private String savedBedKey;     //bed Key saved so it can be easily grouped with other beds of its type
    public String associatedShelterName;    // stored for preservation of uniqueness when comparisons made

    /**
     * A constructor for Bed class
     * @param id the id of the bed
     * @param isFamily the family restriction of the bed type
     * @param menOnly the sex (men) restriction of the bed type
     * @param womenOnly the sex (women) restriction of the bed type
     * @param minAge the minimum age restriction of the bed type
     * @param maxAge the maximum age restriction of the bed type
     * @param veteranOnly the veteran restriction of the bed type
     * @param savedBedKey the bed key
     * @param associatedShelterName the name of the shelter that holds the bed
     */
    public Bed(String id, boolean isFamily, boolean menOnly, boolean womenOnly, Age minAge, Age maxAge,
               boolean veteranOnly, String savedBedKey, String associatedShelterName) {
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
        this.associatedShelterName = associatedShelterName;
    }

    /**
     * A no-arg constructor for Bed class
     */
    public Bed() {
        this("bed_1001", false, false, false, Age.MIN_AGE, Age.MAX_AGE,
                false, "FFF000_200_F", "Hope Atlanta");
    }

//    public User currentOccupant() {
//        return ((User)Model.getAccountByEmail(this.occupantEmail));
//    }

    /**
     * A getter for occupantEmail
     * @return the email of the bed occupant
     */
    public String getOccupantEmail() {
        return this.occupantEmail;
    }

    /**
     * A setter for the occupant's email
     * @param email the email of the occupant
     */
    public void setOccupantEmail(String email) {
        this.occupantEmail = email;
        if (!this.isOccupied) {
            modifyOccupant(email);
        }
    }

    /**
     * A method that changes the bed's status depending on whether there's a user account
     * associated with the bed or not
     * @param occupantEmail the occupant's email
     */
    private void modifyOccupant(String occupantEmail) {
        User occupant = (User) Model.getAccountByEmail(occupantEmail);
        if (occupant != null)
            occupant.setIsOccupyingBed(true);
        this.isOccupied = true;
    }

    /**
     * A method that clears a bed's occupied status and the previous occupant's email
     * @param email the email of the previuos occupant
     */
    public void removeOccupant(String email) {
        ((User)Model.getAccountByEmail(email)).clearOccupiedBed();
        this.occupantEmail = null;
        this.isOccupied = false;
    }

    /**
     * A getter for bed ID
     * @return the bed's ID
     */
    public String getId() {
        return id;
    }

//    public User getOccupant() {
//        return occupant;
//    }

    /**
     * A getter for the bed's occupation status
     * @return true if the bed is occupied, false otherwise
     */
    public boolean getIsOccupied() {
        return isOccupied;
    }

    /**
     * A getter for the bed's family status
     * @return true if it's a family bed, false otherwise
     */
    public boolean getIsFamily() {
        return isFamily;
    }

    /**
     * A getter for the bed's sex restriction
     * @return true if the bed is only for men, false otherwise
     */
    public boolean isMenOnly() {
        return menOnly;
    }

    /**
     * A getter for the bed's sex restriction
     * @return true if the bed is only for women, false otherwise
     */
    public boolean isWomenOnly() {
        return womenOnly;
    }

    /**
     * A getter for the bed's age restriction
     * @return the minimum age the bed allows for is occupant
     */
    public Age getMinAge() {
        return minAge;
    }

    /**
     * A getter for the bed's age restriction
     * @return the maximum age the bed allows for is occupant
     */
    public Age getMaxAge() {
        return maxAge;
    }

    /**
     * A getter for the bed's veteran restriction
     * @return true if the bed is only for veterans, false otherwise
     */
    public boolean isVeteranOnly() {
        return veteranOnly;
    }

    /**
     * A setter for the bed's sex restriction
     * @param onlyMale a boolean that represents whether the bed is for males only
     */
    public void setIsMenOnly(boolean onlyMale) {
        this.menOnly = onlyMale;
    }

    /**
     * A setter for the bed's sex restriction
     * @param onlyFemale a boolean that represents whether the bed is for females only
     */
    public void setIsWomenOnly(boolean onlyFemale) {
        this.womenOnly = onlyFemale;
    }

    /**
     * A setter for the bed's sex restriction
     * @param onlyVets a boolean that represents whether the bed is for veterans only
     */
    public void setIsVeteranOnly(boolean onlyVets) {
        this.veteranOnly = onlyVets;
    }

    /**
     * A setter for the bed's sex restriction
     * @param isFam a boolean that represents whether the bed is for families only
     */
    public void setIsFamily(boolean isFam) {
        this.isFamily = isFam;
    }

    /**
     * A setter for the bed's min age restriction
     * @param min the minimum age for the bed's occupant
     */
    public void setMinAge(Age min) {
        this.minAge = min;
    }

    /**
     * A setter for the bed's min age restriction
     * @param max the maximum age for the bed's occupant
     */
    public void setMaxAge(Age max) {
        this.maxAge = max;
    }

    /**
     * A setter for the Bed's ID
     * @param bedNumber the number of the bed
     */
    public void setId(String bedNumber) {
        String newId = bedNumber;
        if (bedNumber != null && !bedNumber.contains("bed_"))
                newId = "bed_" + bedNumber;
        for (Shelter s : Model.getShelterListPointer().values()) {
            if (!s.getBeds().containsKey(newId)) {
                this.id = newId;
            }
        }
    }

    /**
     * A getter for savedBedKey
     * @return the bed's key
     */
    public String getSavedBedKey() {
        return savedBedKey;
    }

    /**
     * A setter for the bed's key
     * @param savedBedKey the bed's key
     */
    public void setSavedBedKey(String savedBedKey) {
        this.savedBedKey = savedBedKey;
    }

    /**
     * Checks if two beds equal eachother based on zhelter name, bed key, and ID
     * @param other a bed object
     * @return true if two beds equal eachother, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof Bed)) {
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

    /**
     * A method that created a hashcode
     * @return the hashcode as an integer
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.id.toLowerCase().hashCode();
        result = 31 * result + this.associatedShelterName.toLowerCase().hashCode();
        result = 31 * result + this.savedBedKey.toLowerCase().hashCode();
        return result;
    }
}





