package edu.gatech.cs2340.shelterme.util;


import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import edu.gatech.cs2340.shelterme.model.Age;
import edu.gatech.cs2340.shelterme.model.Bed;
import edu.gatech.cs2340.shelterme.model.CapacityStruct;
import edu.gatech.cs2340.shelterme.model.Model;
import edu.gatech.cs2340.shelterme.model.Shelter;

/**
 * The type Bed manager.
 */
public class BedManager {

    private final Shelter currentShelter;
//    private final String currShelterName;

    /**
     * Instantiates a new Bed manager.
     *
     * @param shelter the shelter
     */
    public BedManager(Shelter shelter) {
        if (shelter == null) {
            throw new IllegalArgumentException("No BedManager can exist for a null Shelter.");
        }
        if (shelter.getBeds() != null) {
            this.currentShelter = shelter;
        } else {
            Model model = Model.getInstance();
            this.currentShelter = model.verifyShelterParcel(shelter); //.findShelterByName(shelter);
        }
    }


    /**
     * Add new beds.
     *
     * @param struct       the information holder for Shelter -> Bed data
     * @param numberOfBeds the number of beds being added
     */
//Bed Handling
// Ex. bedKey: 'FFF026_200_T'  <-- single bed, not men only, not women only, minAge = 26 (ADULT),
//                                   maxAge = 200 (MAX_AGE), is veterans only
// CALLED FROM SHELTER BUILDER
//    public void addNewBeds(int numberOfBeds, boolean isFamily, boolean menOnly, boolean womenOnly,
//                            Age minAge, Age maxAge, boolean veteranOnly) {
    public void addNewBeds(CapacityStruct struct, int numberOfBeds) {
        if (currentShelter == null) {
//            Model.getInstance().displayErrorMessage("Cannot add beds to a null shelter!",
//                    BedManager.this);
            // Display a Toast message: "Cannot add beds to a null shelter!"
            return;
        }
        //create unique bed key that encodes all of the bed's restrictions into the key
        // Note for searches: if !menOnly && !womenOnly, then this Shelter takes Anyone
        String bedKey = "";
        bedKey += struct.family ? "T" : "F";
        bedKey += struct.menOnly ? "T" : "F";
        bedKey += struct.womenOnly ? "T" : "F";
        Age minAge = struct.fromAge;
        bedKey += minAge.getAgeKeyVal();
        Age maxAge = struct.toAge;
        bedKey += maxAge.getAgeKeyVal();
        bedKey += struct.vetsOnly ? "T" : "F";
        int lastId = 0;
        Bed lastBedAdded = currentShelter.getLastBedAdded();
        if (lastBedAdded != null) {
            String bid = lastBedAdded.getId();
            bid = bid.substring(4);
            lastId = Integer.valueOf(bid);
        }
        // trimming off the "bed_" part of Id ^

        Map<String, Bed> bedType;
        Map<String, Map<String, Bed>> shelterBeds = currentShelter.getBeds();
        if (shelterBeds.containsKey(bedKey)) { // if this type of bed already exists,
            // add it to the existing bed list
            bedType = shelterBeds.get(bedKey);
        } else {
            // if this is a new bed type, create the new bed type and add it to the beds hashmap
            bedType = new HashMap<>();
            shelterBeds.put(bedKey, bedType);
        }
        for (int i = lastId + 1; i < (lastId + numberOfBeds + 1); i++) {
            String newId = String.format(Locale.US, "bed_%d", i);
//            Bed newBed = new Bed(newId, isFamily, menOnly, womenOnly, minAge, maxAge,
//                    veteranOnly, bedKey, currentShelter.getShelterName());
            Bed newBed = new Bed(newId, bedKey, struct);
            bedType.put(newId, newBed);
            int curVacancy = currentShelter.getVacancies();
            currentShelter.setVacancies(curVacancy + 1);
            if (struct.family) {
                int famCap = currentShelter.getFamilyCapacity();
                currentShelter.setFamilyCapacity(famCap + 1);
            } else {
                int singCap = currentShelter.getSingleCapacity();
                currentShelter.setSingleCapacity(singCap + 1);
            }
            currentShelter.setLastBedAdded(newBed);
        }
    }

//    public boolean hasOpenBed(String userKey) {
//        // Ex key: 'FM25F'  <-- not family acct, male, 25 yrs old, not veteran
//        return findValidBedType(userKey) != null;
//    }

    /**
     * Find valid bed type string.
     *
     * @param userKey the user key
     * @return the string
     */
//----------------------------------------------------------------------------------
// CALLED FROM RESERVATION MANAGER
    public String findValidBedType(String userKey) {
        //moved everything in hasOpenBed to more convenient and flexible private method
        if (userKey == null) {
            throw new IllegalArgumentException("User Key cannot be null");
        } else {
            char isFamilyChar = userKey.charAt(0);
            char genderChar = userKey.charAt(1);
            String ageString = userKey.substring(2, userKey.length() - 1);  // ageString = "25"
            char isVeteranChar = userKey.charAt(userKey.length() - 1);

//            Map<String, Shelter> shelterMap = Model.getShelterListPointer();
//            Shelter curShelter = shelterMap.get(currShelterName);
            Map<String, Map<String, Bed>> bedMap = currentShelter.getBeds();

            for (String bedKey : bedMap.keySet()) {
                if (bedKey.length() > 1) {
                    String restrict = currentShelter.getRestrictions();
                    if ("anyone".equals(restrict.toLowerCase())) {
                        return bedKey;
                    }
                    int keyLength = bedKey.length();
                    boolean thisBedOpen;
                    boolean bedCheck1;
                    boolean bedCheck2;
                    boolean bedCheck3 = true;
                    //attributes of Bed
                    // Ex. bedKey: 'TFF000_200_F'  <-- family, men, women, minage, maxage, vets
                    boolean isFamilyBed = bedKey.charAt(0) == 'T';
                    boolean menOnlyBed = bedKey.charAt(1) == 'T';
                    boolean womenOnlyBed = bedKey.charAt(2) == 'T';
                    boolean veteranOnlyBed = bedKey.charAt(keyLength - 1) == 'T';

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
                    boolean[] bedAttributes = {isFamilyBed, menOnlyBed, womenOnlyBed,
                            veteranOnlyBed};
                    boolean[] userAttributes = {isFamilyUser, isFemaleUser, isMaleUser,
                            isNonBinaryUser, isVeteranUser};

//                    String minString = bedKey.substring(3, 6);
//                    int minAge = Integer.parseInt(minString);
//                    String maxString = bedKey.substring(7, 10);
//                    int maxAge = Integer.parseInt(maxString);
//                    int userAge = Integer.parseInt(ageString);
//                    if (isFamilyBed ^ isFamilyUser) {
//                        //make sure family type matches between user and bed
//                        thisBedOpen = false;
//                    }
//                    if (menOnlyBed && (isFemaleUser)) {
//                        //make sure women don't get into men only shelters
//                        thisBedOpen = false;
//                    }
//                    if (womenOnlyBed && (isMaleUser)) {
//                        //make sure men don't get into women only shelters
//                        thisBedOpen = false;
//                    }
//                    if ((menOnlyBed ^ womenOnlyBed) && isNonBinaryUser) {
//                        //makes sure non-binary users can access shelters that
//                        // exclude nobody OR BOTH genders
//                        thisBedOpen = false;
//                    }
//                    if (veteranOnlyBed && !(isVeteranUser)) {
//                        //exclude veteran beds from non-veterans
//                        thisBedOpen = false;
//                    }
//                    if ((userAge > maxAge) || (userAge < minAge)) {
//                        //make sure user is within the appropriate age range
//                        thisBedOpen = false;
//                    }
//                    Map<String, Bed> bedType = bedMap.get(bedKey);
//                    if (bedType.isEmpty()) {
//                        //cannot have 0 vacancies of this bed type to be valid for use
//                        thisBedOpen = false;
//                    }

                    bedCheck1 = userCompatible(userAttributes, bedAttributes);
                    bedCheck2 = ageCompatible(ageString, bedKey);

                    Map<String, Bed> bedType = bedMap.get(bedKey);
                    if (bedType.isEmpty()) {
                        //cannot have 0 vacancies of this bed type to be valid for use
                        bedCheck3 = false;
                    }
                    thisBedOpen = bedCheck1 && bedCheck2 && bedCheck3;
                    if (thisBedOpen) {
                        return bedKey;
                    }
                }
            }
        }
        return null;
    }


    private boolean userCompatible(boolean[] user, boolean[] bed) {
        boolean thisBedOpen = true;
//        if (isFamilyBed ^ isFamilyUser) {
        if (bed[0] ^ user[0]) {
            //make sure family type matches between user and bed
            thisBedOpen = false;
        }
//        if (menOnlyBed && (isFemaleUser)) {
        if (bed[1] && user[1]) {
            //make sure women don't get into men only shelters
            thisBedOpen = false;
        }
//        if (womenOnlyBed && (isMaleUser)) {
        if (bed[2] && user[2]) {
            //make sure men don't get into women only shelters
            thisBedOpen = false;
        }
//        if ((menOnlyBed ^ womenOnlyBed) && isNonBinaryUser) {
        if ((bed[1] ^ bed[2]) && user[3]) {
            //makes sure non-binary users can access shelters that
            // exclude nobody OR BOTH genders
            thisBedOpen = false;
        }
//        if (veteranOnlyBed && !(isVeteranUser)) {
        if (bed[3] && !user[4]) {
            //exclude veteran beds from non-veterans
            thisBedOpen = false;
        }
        return thisBedOpen;
    }

    private boolean ageCompatible(String ageString, String bedKey) {
        String minString = bedKey.substring(3, 6);
        int minAge = Integer.parseInt(minString);
        String maxString = bedKey.substring(7, 10);
        int maxAge = Integer.parseInt(maxString);
        int userAge = Integer.parseInt(ageString);

        return !((userAge > maxAge) || (userAge < minAge));
    }

}
/*
    // Clears all occupied beds for this shelter
    public void clearOccupiedBeds() {
        Shelter curShelter = Model.getInstance().verifyShelterParcel(this);
//        DBUtil dbUtil = DBUtil.getInstance();
        for (String bedId : curShelter.beds.get("O").keySet()) {
            Bed bed = curShelter.beds.get("O").remove(bedId);
            User occupant = (User)Model.getAccountByEmail(bed.getOccupantEmail());
            bed.removeOccupant(occupant.getEmail());
            occupant.clearOccupiedBed();
            StayReport curStay = occupant.getCurrentStayReport();
            if (curStay != null && curStay.isActive())
                curStay.checkOut();
//            dbUtil.updateUserOccupancyAndStayReports(occupant);
            String bedKey = bed.getSavedBedKey();
            if (curShelter.beds.get(bedKey) != null) {
                curShelter.beds.get(bedKey).put(bedKey, bed);
            }
            curShelter.vacancies++;
        }
    }
    */

