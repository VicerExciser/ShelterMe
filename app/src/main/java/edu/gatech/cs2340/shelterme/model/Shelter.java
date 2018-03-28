package edu.gatech.cs2340.shelterme.model;

import edu.gatech.cs2340.shelterme.controllers.DBUtil;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.*;

/**
 * Created by austincondict on 2/18/18.
 */

public class Shelter implements Parcelable{
    // Had to change shelterKey to String in the form: "s_1" for the sake of serialization
    // (throws DatabaseException otherwise)

    private String shelterName;
    private String shelterKey;
    private int familyCapacity;     // # of family accommodations
    private int singleCapacity;     // # of single-person beds
    private String capacityStr;
    private String restrictions;
//    private boolean takesFamilies;

//    private Location location;
    private double latitude;
    private double longitude;
    private String notes;
    private String phone;
    private String address;

    private HashMap<String, LinkedHashMap<String, Bed>> beds;
    private Bed lastBedAdded;
    private int vacancies;

//    @interface IgnoreExtraProperties {}
//    @JsonIgnore
//    private DBUtil dbUtil = DBUtil.getInstance();

    public Shelter() {
        this("<Shelter Name Here>");
    }

    public Shelter(String name) {
        this("s_1001", name, "500", "Anyone", 0, 0, "123 Sesame St", "", "(800) 800-8008");
    }

    public Shelter(String key, String name, String capacity, String restrictions, double longitude,
                   double latitude, String address, String specNotes, String num) {
        shelterKey = key;
        if (key != null && !shelterKey.contains("s_"))
             shelterKey = "s_" + key;
        shelterName = name;
        this.capacityStr = capacity;
        this.restrictions = restrictions;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.notes = specNotes;
        this.phone = num;
        familyCapacity = 0;
        singleCapacity = 0;
        this.beds = new HashMap<>();
        LinkedHashMap<String, Bed> occupiedBeds = new LinkedHashMap<>();
        this.beds.put("O", occupiedBeds);
        if (restrictions != null)
            processRestrictions(this.restrictions);
        else
            Log.v("Shelter", "restrictions for "+name+" were not found\n"+this.toString());
    }

    public Shelter(Parcel parcel) {
        shelterKey = parcel.readString();
        shelterName = parcel.readString();
        capacityStr = parcel.readString();
        restrictions = parcel.readString();
        longitude = parcel.readDouble();
        latitude = parcel.readDouble();
        address = parcel.readString();
        phone = parcel.readString();
        notes = parcel.readString();
        vacancies = parcel.readInt();
//        beds = parcel.readHashMap();
    }

    public String toString() {
        return String.format("%s | %s | %s | %s | %2.6f | %2.6f | %s | %s | %s\n", shelterKey, shelterName,
                capacityStr, restrictions, longitude, latitude, address, notes, phone);
    }

    public String detail() {
        String buff = "============================================================================================\n";
        String description = String.format(" Shelter no. %s\n %s: %s\n Located at %s (%2.6f, %2.6f)\n Phone: %s\n " +
                "Currently accepting: %s", this.shelterKey, this.shelterName, this.notes,
                this.address, this.longitude, this.latitude, this.phone, this.restrictions);
        if (!this.capacityStr.equals("N/A")) {
            description += " with a capacity of " + this.capacityStr;
        }
        return buff + description + "\n" + buff;
    }


    private void processRestrictions(String rs) {
//        if (rs != null && !rs.isEmpty())
//            rs = "Anyone";
        String cp = this.capacityStr;
        boolean fam, anyone, exMen, exWomen, exVets, youngAdults;
        int ageFloor = 0;
        int ageCeiling = 200;
        rs = rs.toLowerCase();
        anyone = rs.contains("anyone");
        if (anyone) {
            fam = true;
            exMen = false;
            exWomen = false;
            exVets = false;
            ageFloor = 0;
            ageCeiling = 200;
            parseCapacity(cp, fam, exMen, exWomen, ageFloor, ageCeiling, exVets);
            return;
        } else {
            fam = rs.contains("families");
            exWomen = !fam && rs.contains("women");
            exMen = !fam && !exWomen && rs.contains("men");
        }
        if (exMen) ageFloor = 26;
        exVets = rs.contains("veterans");
        if (exVets) {
            ageFloor = 26;
            ageCeiling = 200;
        }
        youngAdults = rs.contains("young adults");
        if (rs.contains("children") || rs.contains("newborns")) {
            if (rs.contains("newborns") || rs.contains("under 5") || exWomen || exMen) {
                ageFloor = 0;
                ageCeiling = 5;
            } else {
                ageFloor = 6;
                ageCeiling = 15;
            }
            if (/*fam ||*/ exWomen || exMen) {
                ageCeiling = 200;
                fam = true;
                if (cp.split(" ").length < 2) {
                    cp = (getInt(cp) / 4) + " apartment";   // :,( RIP
                }
            } else if (youngAdults) {
                ageCeiling = 25;
            }
        } else if (youngAdults) {
            ageFloor = 16;
            ageCeiling = 25;
        }
        parseCapacity(cp, fam, exMen, exWomen, ageFloor, ageCeiling, exVets);
    }

    // parse Capacity strings to extrapolate integers & bed types
    // make this set totalCapacity & call:
    //   addNewBeds(int numberOfSingleBeds, boolean isFamily, boolean menOnly, boolean womenOnly,
    //       Age minAge, Age maxAge, boolean veteranOnly)
    private void parseCapacity(String cp, boolean fm, boolean mo, boolean wo, int mna, int mxa, boolean vo ) {
//        this.takesFamilies=fm;
        int singleBeds = 0;
        int familyBeds = 0;
        if (cp != null && !cp.trim().isEmpty() && !cp.equals("N/A")) {
            // split up Capacity strings by spaces
            String[] tokens = cp.toLowerCase().split(" ");
            int length = tokens.length;
            for (int i = 0; i < length; i++) {
                int val = getInt(tokens[i]);
                if (val > 0) {
                    if (length > 1) {
                        for (int j = 1; j <= 3; j++) {
                            if (i + j < length) {
                                if (tokens[i + j].contains("famil")) {
                                    familyBeds += val;
                                } else if (tokens[i + j].contains("singl")
                                        || tokens[i + j].contains("bed")) {
                                    singleBeds += val;
                                } else if (tokens[i + j].contains("apartment")) {
                                    familyBeds += val / 2;
                                    singleBeds += val * 2;      // Special case! (Sorry, Mom.)
                                }
                            }
                        }
                    } else {
                        if (fm) {
                            familyBeds = val;
                        } else {
                            singleBeds = val;
                        }
                    }
                }
            }
        } else {
            if (fm) {
                familyBeds = 50;
            } else {
                singleBeds = 50;
            }
        }
        Age minAge, maxAge;
        switch (mna) {
            case 0:  minAge = Age.MINAGE; break;
            case 6:  minAge = Age.CHILDREN_BASE; break;
            case 16: minAge = Age.YOUNGADULTS_BASE; break;
            case 26: minAge = Age.ADULTS_BASE; break;
            default: minAge = Age.MINAGE; break;
        }
        switch (mxa) {
            case 5:   maxAge = Age.BABIES; break;
            case 15:  maxAge = Age.CHILDREN_CAP; break;
            case 25:  maxAge = Age.YOUNGADULTS_CAP; break;
            case 65:  maxAge = Age.ADULTS_CAP; break;
            case 200: maxAge = Age.MAXAGE; break;
            default:  maxAge = Age.MAXAGE; break;
        }
        //this.setVacancies(singleBeds + familyBeds);
        if (singleBeds > 0)
            addNewBeds(singleBeds, false, mo, wo, minAge, maxAge, vo);
        if (familyBeds > 0)
            addNewBeds(familyBeds, true, mo, wo, minAge, maxAge, vo);
    }


    private int getInt(String text) {
        int val = -1;
        try {
            val = Integer.valueOf(text.trim());
        } catch (NumberFormatException nfe) { }
        return val;
    }

    //Bed Handling
    // Ex. bedKey:   'FFF026_200_T'  <-- single bed, not men only, not women only, minAge = 26 (ADULT),
    //                                   maxAge = 200 (MAXAGE), is veterans only
    public void addNewBeds(int numberOfBeds, boolean isFamily, boolean menOnly, boolean womenOnly,
                           Age minAge, Age maxAge, boolean veteranOnly) {
        //create unique bed key that encodes all of the bed's restrictions into the key
        // Note for searches: if !menOnly && !womenOnly, then this Shelter takes Anyone
        String bedKey = "";
        bedKey += isFamily ? "T" : "F";
        bedKey += menOnly ? "T" : "F";
        bedKey += womenOnly ? "T" : "F";
        bedKey += minAge.getAgeKeyVal();
        bedKey += maxAge.getAgeKeyVal();
        bedKey += veteranOnly ? "T" : "F";
        int lastId = lastBedAdded == null ? 0 : Integer.valueOf(lastBedAdded.getId().substring(4));
                                                // trimming off the "bed_" part of Id ^


        LinkedHashMap<String, Bed> bedType;
        if (getBeds().containsKey(bedKey)) { // if this type of bed already exists, add it to the existing bed list
            bedType = getBeds().get(bedKey);
        } else {    // if this is a new bed type, create the new bed type and add it to the beds hashmap
            bedType = new LinkedHashMap<>();
            getBeds().put(bedKey, bedType);
        }
        for (int i = lastId + 1; i < lastId + numberOfBeds + 1; i++) {
            Bed newBed = new Bed("bed_"+i, isFamily, menOnly, womenOnly, minAge, maxAge, veteranOnly, bedKey);
            bedType.put(String.valueOf(newBed.getId()), newBed);
            this.vacancies++;
            if (isFamily)
                familyCapacity++;
            else
                singleCapacity++;
            this.lastBedAdded = newBed;
        }
    }

    public boolean hasOpenBed(String userKey) {     // Ex key: 'FM25F'  <-- not family acct, male, 25 yrs old, not veteran
        return findValidBedType(userKey) != null;
    }

    public String findValidBedType(String userKey) {   //moved everything in hasOpenBed to more convenient and flexible private method
        if (userKey == null) {
            throw new IllegalArgumentException("User Key cannot be null");
        } else {
            char isFamilyChar = userKey.charAt(0);
            char genderChar = userKey.charAt(1);
            String ageString = userKey.substring(2, userKey.length() - 1);  // ageString = "25"
            char isVeteranChar = userKey.charAt(userKey.length() - 1);
//            HashMap<String, LinkedHashMap<String, Bed>> bedlist = this.beds;
            Shelter curShelter = Model.getShelterListPointer().get(this.getShelterName());
//            if (this.beds == null) {

//            for (Shelter s : Model.getShelterListPointer()) {
//                if (this.equals(s)) {
//                    this.beds = s.getBeds();
//                    curShelter = s;
//                }
//            }
//            }
            for (String bedKey : curShelter.getBeds().keySet()) {
                if (bedKey.length() > 1) {
                    if (this.restrictions.toLowerCase().equals("anyone"))
                        return bedKey;

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
                    if (genderChar == 'M') {
                        isMaleUser = true;
                    } else if (genderChar == 'F') {
                        isFemaleUser = true;
                    } else if (genderChar == 'O') {
                        isNonBinaryUser = true;
                    }
                    boolean isVeteranUser = isVeteranChar == 'T';
                    int minAge = Integer.parseInt(bedKey.substring(3, 6));
                    int maxAge = Integer.parseInt(bedKey.substring(7, 10));
                    int userAge = Integer.parseInt(ageString/*.substring(0, 3)*/);
                    if (isFamilyBed ^ isFamilyUser) { //make sure family type matches between user and bed
                        thisBedOpen = false;
                    }
                    if (menOnlyBed && (isFemaleUser /*&& (userAge > 15)*/)) { //make sure women don't get into men only shelters
                        thisBedOpen = false;
                    }
                    if (womenOnlyBed && (isMaleUser /*&& (userAge > 15)*/)) { //make sure men don't get into women only shelters
                        thisBedOpen = false;
                    }
                    if ((menOnlyBed ^ womenOnlyBed) && isNonBinaryUser) { //makes sure non-binary users can access shelters that exclude nobody OR BOTH genders
                        thisBedOpen = false;
                    }
                    if (veteranOnlyBed && !(isVeteranUser)) { //exclude veteran beds from non-veterans
                        thisBedOpen = false;
                    }
                    if (userAge > maxAge || userAge < minAge) { //make sure user is within the appropriate age range
                        thisBedOpen = false;
                    }
                    if (((HashMap<String, Bed>)(curShelter.beds.get(bedKey))).size() == 0) { //cannot have 0 vacancies of this bed type to be valid for use
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

    public HashMap<String, Collection<Bed>> reserveBed() {
        return reserveBed(1);
    }

    // Equivalent for checking in w/ a StayReport
    public HashMap<String, Collection<Bed>> reserveBed(int numBeds) { //function takes in User and returns ID of bed(s) being reserved
        User user = ((User)(Model.getInstance().getCurrUser()));
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        } else if (user.isOccupyingBed()) {
            throw new IllegalArgumentException("User must not have already reserved a bed.");
        }
        String userKey = user.generateKey();
        String bedTypeFoundKey = findValidBedType(userKey);

        Shelter curShelter = Model.getInstance().verifyShelterParcel(this);


        // ValidBedsFound is our structure containing all beds that must be updated in the database
        HashMap<String, Collection<Bed>> validBedsFound = new HashMap<>();
        // values will hold pointers to our newly reserved bed objects
        Collection<Bed> resValues = new ArrayList<>();
        HashMap<String,Bed> validBeds = (HashMap<String, Bed>) curShelter.getBeds().get(bedTypeFoundKey);
        Bed[] bedArr = new Bed[validBeds.values().size()];
        bedArr = (Bed[])((validBeds.values().toArray(bedArr)));

        LinkedHashMap<String, Bed> occupied = curShelter.getBeds().get("O");
        if (occupied == null) {
            occupied = new LinkedHashMap<>();
            curShelter.beds.put("O", occupied);
        }
        Collection<Bed> occValues = new ArrayList<>();

        for (int i = 0; i < numBeds; i++) {
            bedArr[i].setOccupant(user);
            // remove the valid bed from this shelter's beds list & place in the occupied list
            occupied.put(bedArr[i].getId(), bedArr[i]);
            occValues.add(bedArr[i]);
            resValues.add(((HashMap<String, Bed>)(curShelter.getBeds().get(bedTypeFoundKey)))
                    .remove(bedArr[i].getId()));
        }
        validBedsFound.put(bedTypeFoundKey, resValues);
        user.addStayReport(new StayReport(curShelter, user, (ArrayList<Bed>) resValues));
        validBedsFound.put("O", occValues);
//        values = curShelter.getBeds().get("O").values();
        int newVac = curShelter.getVacancies() - numBeds;
        curShelter.setVacancies(newVac);

//
////                curShelter.getBeds().get(bedTypeFoundKey); //collection of beds of appropriate type
//
////        HashMap<String, ArrayList<String>> bedsToReserve = new HashMap<>();
//
//
////        String[] bedIds = new String[numBeds];
//        for (int i = 0; i < numBeds; i++) {
//            String foundBedKey = (String) bedTypeFound.keySet().toArray()[0];
//
//            Bed foundBed = bedTypeFound.remove(foundBedKey); //remove first bed in the collection
//            foundBed.setOccupant(user);
////            user.setOccupyingBed();
//            bedArr[i] = foundBed;
//
//            String bedId = String.valueOf(foundBed.getId());
////            bedTypeFound.remove(bedId);   <- Isn't this redundant?
//            bedsToReserve.put(bedTypeFoundKey, bedId); // or should I use foundBedKey?
////            bedIds[i] = bedId;
//
//
////            curShelter.getBeds().get("O").put(bedId, foundBed);
//            occupied.put(bedId, foundBed);
//            curShelter.beds.put("O", occupied); // necessary?
//            curShelter.vacancies--;
//        }
//        user.addStayReport(new StayReport(this, user, bedArr));
//
////        this.vacancies--;
////        this.vacancies -= numBeds;
//
////        DBUtil dbUtil = DBUtil.getInstance();
////        try {
////            dbUtil.updateShelterVacanciesAndBeds(curShelter);
////            dbUtil.updateUserOccupancyAndStayReports(user);
////        } catch (DatabaseException de) {
////            Log.e("RESERVE_BEDS", de.getMessage());
////        }
////        return bedIds;
//
        return validBedsFound;
    }

    // TODO: Fix for Firebase compatibility
    // Clears all occupied beds for this shelter
    public void clearOccupiedBeds() {
        Shelter curShelter = Model.getInstance().verifyShelterParcel(this);
//        DBUtil dbUtil = DBUtil.getInstance();
        for (String bedId : curShelter.beds.get("O").keySet()) {
            Bed bed = curShelter.beds.get("O").remove(bedId);
            User occupant = bed.getOccupant();
            bed.removeOccupant();
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

//        dbUtil.updateShelterVacanciesAndBeds(curShelter);

    }

    // TODO: Fix for Firebase compatibility
    // Equivalent for checking out w/ a StayReport
    public void undoReservation() {
        User user = ((User)(Model.getInstance().getCurrUser()));
        Shelter curShelter = Model.getInstance().verifyShelterParcel(this);
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        } else if (!user.isOccupyingBed()) {
            throw new IllegalArgumentException("User must have bed reserved.");
        }
//        Bed bed = this.getBedOccupiedBy(user);
        StayReport currentStay = user.getCurrentStayReport();
        if (currentStay != null) {
            currentStay.checkOut();
            user.clearOccupiedBed();
            for (Bed bed : currentStay.getReservedBeds()) {
                if (bed != null) {
                    bed.removeOccupant();
                    String bedId = bed.getId();
                    curShelter.beds.get("O").remove(bedId);
                    curShelter.beds.get(bed.getSavedBedKey()).put(bedId, bed);
                    curShelter.vacancies++;
                }
            }
        } //else {
//            Model.getInstance().displayErrorMessage("No current shelter reservations found!", Shelter.this);
//        }
//        DBUtil dbUtil = DBUtil.getInstance();
//        dbUtil.updateShelterVacanciesAndBeds(curShelter);
//        dbUtil.updateUserOccupancyAndStayReports(user);
    }

    public Bed getBedOccupiedBy(User user) {
        for (Bed b : this.beds.get("O").values()) {
            User occupant = b.getOccupant();
            if (occupant != null && occupant.equals(user)) {
                return b;
            }
        }
        return null;
    }
    // ^ can also do user.getCurrentStayReport().getReservedBeds();


//    public boolean getTakesFamilies() {
//        return this.isTakesFamilies();
//    }

    public String getShelterName() {
        return this.shelterName;
    }

    public String getShelterKey() {
        return this.shelterKey;
    }

//    public int getCapacity() {
//        return this.familyCapacity + this.singleCapacity;
//    }
    public String getCapacityStr() {
        return this.capacityStr;
    }

    public int getFamilyCapacity() {
        return this.familyCapacity;
    }

    public int getSingleCapacity() {
        return this.singleCapacity;
    }

//    public void setCapacity(int capacity) {
//        this.totalCapacity = capacity;
//    }

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

//    public ContactsContract.CommonDataKinds.Phone getPhoneNumber() {
//        return phoneNumber;
//    }
//
//    public void setPhoneNumber(ContactsContract.CommonDataKinds.Phone phoneNumber) {
//        this.phoneNumber = phoneNumber;
//    }

    public String getPhone() { return this.phone; }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // For Parcelable capabilities
    @Override
    public int describeContents() {
        return 0;
    }

    // TODO: will likely need to update to use totalCapacity, vacancies, beds, etc.
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.shelterKey);
        dest.writeString(this.shelterName);
        dest.writeString(this.capacityStr);
        dest.writeString(this.restrictions);
        dest.writeDouble(this.longitude);
        dest.writeDouble(this.latitude);
        dest.writeString(this.address);
        dest.writeString(this.phone);
        dest.writeString(this.notes);
        dest.writeInt(this.vacancies);
//        dest.writeMap(this.beds);
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


//    public boolean isTakesFamilies() {
//        return takesFamilies;
//    }

    public HashMap<String, LinkedHashMap<String, Bed>> getBeds() {
        return beds;
    }

    public void setBeds(HashMap<String, LinkedHashMap<String, Bed>> beds) {
        this.beds = beds;
    }

    public int getVacancies() {
        return this.vacancies;
    }

    public void setVacancies(int vacancies) {
        this.vacancies = vacancies;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Shelter)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        Shelter s = (Shelter) o;
        return this.shelterName.equalsIgnoreCase(s.shelterName)
                && this.address.equals(s.address)
                && this.phone.equals(s.phone);
    }
}
