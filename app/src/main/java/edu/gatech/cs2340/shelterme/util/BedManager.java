package edu.gatech.cs2340.shelterme.util;


import java.util.HashMap;

import edu.gatech.cs2340.shelterme.model.Age;
import edu.gatech.cs2340.shelterme.model.Bed;
import edu.gatech.cs2340.shelterme.model.Model;
import edu.gatech.cs2340.shelterme.model.Shelter;

public class BedManager {

    private Shelter currentShelter;

    public BedManager(Shelter shelter){
        currentShelter = shelter;
    }


    //Bed Handling
// Ex. bedKey: 'FFF026_200_T'  <-- single bed, not men only, not women only, minAge = 26 (ADULT),
//                                   maxAge = 200 (MAX_AGE), is veterans only
    public void addNewBeds(int numberOfBeds, boolean isFamily, boolean menOnly, boolean womenOnly,
                            Age minAge, Age maxAge, boolean veteranOnly) {
        if (currentShelter == null) {
            Model.getInstance().displayErrorMessage("Cannot add beds to a null shelter!",
                    this);
        }
        //create unique bed key that encodes all of the bed's restrictions into the key
        // Note for searches: if !menOnly && !womenOnly, then this Shelter takes Anyone
        String bedKey = "";
        bedKey += isFamily ? "T" : "F";
        bedKey += menOnly ? "T" : "F";
        bedKey += womenOnly ? "T" : "F";
        bedKey += minAge.getAgeKeyVal();
        bedKey += maxAge.getAgeKeyVal();
        bedKey += veteranOnly ? "T" : "F";
        int lastId = 0;
        if (currentShelterlastBedAdded != null) {
            String bid = lastBedAdded.getId();
            lastId = Integer.valueOf(bid.substring(4));
        }
        // trimming off the "bed_" part of Id ^


        HashMap<String, Bed> bedType;
        if (getBeds().containsKey(bedKey)) { // if this type of bed already exists,
            // add it to the existing bed list
            bedType = getBeds().get(bedKey);
        } else {
            // if this is a new bed type, create the new bed type and add it to the beds hashmap
            bedType = new HashMap<>();
            getBeds().put(bedKey, bedType);
        }
        for (int i = lastId + 1; i < (lastId + numberOfBeds + 1); i++) {
            Bed newBed = new Bed("bed_" + i, isFamily, menOnly, womenOnly, minAge, maxAge,
                    veteranOnly, bedKey, this.shelterName);
            bedType.put(String.valueOf(newBed.getId()), newBed);
            this.vacancies++;
            if (isFamily) {
                setFamilyCapacity(getFamilyCapacity() + 1);
            } else {
                setSingleCapacity(getSingleCapacity() + 1);
            }
            this.lastBedAdded = newBed;
        }
    }

    public boolean hasOpenBed(String userKey) {
        // Ex key: 'FM25F'  <-- not family acct, male, 25 yrs old, not veteran
        return findValidBedType(userKey) != null;
    }

    public String findValidBedType(String userKey) {
        //moved everything in hasOpenBed to more convenient and flexible private method
        if (userKey == null) {
            throw new IllegalArgumentException("User Key cannot be null");
        } else {
            char isFamilyChar = userKey.charAt(0);
            char genderChar = userKey.charAt(1);
            String ageString = userKey.substring(2, userKey.length() - 1);  // ageString = "25"
            char isVeteranChar = userKey.charAt(userKey.length() - 1);
            HashMap<String, Shelter> shelterHashMap = Model.getShelterListPointer();
            Shelter curShelter = shelterHashMap.get(this.getShelterName());
            HashMap<String, HashMap<String, Bed>> bedHashMap = curShelter.getBeds();
            for (String bedKey : bedHashMap.keySet()) {
                if (bedKey.length() > 1) {
                    if ("anyone".equals(this.restrictions.toLowerCase())) {
                        return bedKey;
                    }

                    //attributes of Bed
                    boolean thisBedOpen = true;
                    // Ex. bedKey: 'TFF000_200_F'  <-- family, men, women, minage, maxage, vets
                    boolean isFamilyBed = bedKey.charAt(0) == 'T';
                    boolean menOnlyBed = bedKey.charAt(1) == 'T';
                    boolean womenOnlyBed = bedKey.charAt(2) == 'T';
                    boolean veteranOnlyBed = bedKey.charAt(bedKey.length() - 1) == 'T';

                    //attributes of User
                    boolean isMaleUser = false;
                    boolean isFemaleUser = false;
                    boolean isNonBinaryUser = false;
                    boolean isFamilyUser = isFamilyChar == 'T';
                    switch (genderChar) {
                        case 'M':
                            isMaleUser = true;
                            break;
                        case 'F':
                            isFemaleUser = true;
                            break;
                        case 'O':
                            isNonBinaryUser = true;
                            break;
                    }
                    boolean isVeteranUser = isVeteranChar == 'T';
                    int minAge = Integer.parseInt(bedKey.substring(3, 6));
                    int maxAge = Integer.parseInt(bedKey.substring(7, 10));
                    int userAge = Integer.parseInt(ageString/*.substring(0, 3)*/);
                    if (isFamilyBed ^ isFamilyUser) {
                        //make sure family type matches between user and bed
                        thisBedOpen = false;
                    }
                    if (menOnlyBed && (isFemaleUser)) {
                        //make sure women don't get into men only shelters
                        thisBedOpen = false;
                    }
                    if (womenOnlyBed && (isMaleUser)) {
                        //make sure men don't get into women only shelters
                        thisBedOpen = false;
                    }
                    if ((menOnlyBed ^ womenOnlyBed) && isNonBinaryUser) {
                        //makes sure non-binary users can access shelters that
                        // exclude nobody OR BOTH genders
                        thisBedOpen = false;
                    }
                    if (veteranOnlyBed && !(isVeteranUser)) {
                        //exclude veteran beds from non-veterans
                        thisBedOpen = false;
                    }
                    if ((userAge > maxAge) || (userAge < minAge)) {
                        //make sure user is within the appropriate age range
                        thisBedOpen = false;
                    }
                    if (curShelter.beds.get(bedKey).isEmpty()) {
                        //cannot have 0 vacancies of this bed type to be valid for use
                        thisBedOpen = false;
                    }
                    if (thisBedOpen) {
                        return bedKey;
                    }
                }
            }
        }
        return null;
    }
}
