package edu.gatech.cs2340.shelterme.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Shelter implements Parcelable {
    // Had to change shelterKey to String in the form: "s_1" for the sake of serialization
    // (throws DatabaseException otherwise)
//    private static final int DEFAULT_BED_COUNT = 50;
    private final String shelterName;
    private String shelterKey;
    private int familyCapacity;     // # of family accommodations
    private int singleCapacity;     // # of single-person beds
    private final String capacityStr;
    private final String restrictions;
//    private boolean takesFamilies;

    //    private Location location;
    private final double latitude;
    private final double longitude;
    private final String notes;
    private final String phone;
    private final String address;

//    private HashMap<String, LinkedHashMap<String, Bed>> beds;
    private Map<String, Map<String, Bed>> beds;
    private Bed lastBedAdded;
    private int vacancies;

//    @interface IgnoreExtraProperties {}
//    @JsonIgnore
//    private DBUtil dbUtil = DBUtil.getInstance();

    public Shelter() {
        this("<Shelter Name Here>");
    }

    public Shelter(String name) {
        this("s_1001", name, "500", "Anyone", 0, 0,
                "123 Sesame St", "", "(800) 800-8008");
    }

    public Shelter(String key, String name, String capacity, String restrictions, double longitude,
                   double latitude, String address, String specNotes, String num) {
        shelterKey = key;
        if ((key != null) && !shelterKey.contains("s_")) {
            shelterKey = "s_" + key;
        }
        shelterName = name;
        this.capacityStr = capacity;
        this.restrictions = restrictions;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.notes = specNotes;
        this.phone = num;
        setFamilyCapacity(0);
        setSingleCapacity(0);
        this.beds = new HashMap<>();
        //noinspection MismatchedQueryAndUpdateOfCollection
        Map<String, Bed> occupiedBeds = new HashMap<>();
        // Occupied beds is used and maintained purely by Firebase
        this.beds.put("O", occupiedBeds);
        if (restrictions != null) {
            processRestrictions(this.restrictions);
        } else {
            Log.v("Shelter", "restrictions for " + name + " were not found\n"
                    + this.toString());
        }
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
        setSingleCapacity(parcel.readInt());
        setFamilyCapacity(parcel.readInt());
    }

    @Override
    public final String toString() {
        return String.format(Locale.US, "%s | %s | %s | %s | %2.6f | %2.6f | %s | %s | %s\n",
                shelterKey, shelterName,
                capacityStr, restrictions, longitude, latitude, address, notes, phone);
    }

    String detail() {
        String buff = "====================================================================" +
                "========================\n";
        String description = String.format(Locale.US, " Shelter no. %s\n %s: %s\n Located "+
                        "at %s (%2.6f, %2.6f)\n Phone: %s\n " +
                        "Currently accepting: %s", this.shelterKey, this.shelterName, this.notes,
                this.address, this.longitude, this.latitude, this.phone, this.restrictions);
        if (!"N/A".equals(this.capacityStr)) {
            description += " with a capacity of " + this.capacityStr;
        }
        return buff + description + "\n" + buff;
    }

/*
    private void processRestrictions(String rs) {
//        if (rs != null && !rs.isEmpty())
//            rs = "Anyone";
        String cp = this.capacityStr;
        boolean fam;
        boolean anyone;
        boolean exMen;
        boolean exWomen;
        boolean exVets;
        boolean youngAdults;
        int ageFloor = Age.MIN_AGE.toInt();
        int ageCeiling = Age.MAX_AGE.toInt();
        String rs1 = rs.toLowerCase();
        anyone = rs1.contains("anyone");
        if (anyone) {
/*
fam = true;
exMen = false;
exWomen = false;
exVets = false;
*/ /*
            ageFloor = Age.MIN_AGE.toInt();
            ageCeiling = Age.MAX_AGE.toInt();
            parseCapacity(cp, true, false, false, ageFloor, ageCeiling, false);
            return;
        } else {
            fam = rs1.contains("families");
            exWomen = !fam && rs1.contains("women");
            exMen = !fam && !exWomen && rs1.contains("men");
        }
        if (exMen) {
            ageFloor = Age.ADULTS_BASE.toInt();
        }
        exVets = rs1.contains("veterans");
        if (exVets) {
            ageFloor = Age.ADULTS_BASE.toInt();
            ageCeiling = Age.MAX_AGE.toInt();
        }
        youngAdults = rs1.contains("young adults");
        if (rs1.contains("children") || rs1.contains("newborns")) {
            if (rs1.contains("newborns") || rs1.contains("under 5") || exWomen || exMen) {
                ageFloor = Age.MIN_AGE.toInt();
                ageCeiling = Age.BABIES.toInt();
            } else {
                ageFloor = Age.CHILDREN_BASE.toInt();
                ageCeiling = Age.CHILDREN_CAP.toInt();
            }
            if (exWomen || exMen) {
                ageCeiling = Age.MAX_AGE.toInt();
                fam = true;
                if (cp.split(" ").length < 2) {
                    cp = (getInt(cp) / 4) + " apartment";
                }
            } else if (youngAdults) {
                ageCeiling = Age.YOUNG_ADULTS_CAP.toInt();
            }
        } else if (youngAdults) {
            ageFloor = Age.YOUNG_ADULTS_BASE.toInt();
            ageCeiling = Age.YOUNG_ADULTS_CAP.toInt();
        }
        parseCapacity(cp, fam, exMen, exWomen, ageFloor, ageCeiling, exVets);
    }

    // parse Capacity strings to extrapolate integers & bed types
    // make this set totalCapacity & call:
    //   addNewBeds(int numberOfSingleBeds, boolean isFamily, boolean menOnly, boolean womenOnly,
    //       Age minAge, Age maxAge, boolean veteranOnly)
    private void parseCapacity(String cp, boolean fm, boolean mo, boolean wo, int mna, int mxa,
                               boolean vo) {
        int singleBeds = 0;
        int familyBeds = 0;
        if ((cp != null) && !cp.isEmpty() && !"N/A".equals(cp)) {
            // split up Capacity strings by spaces
            cp = cp.toLowerCase();
            String[] tokens = cp.split(" ");
            int length = tokens.length;
            for (int i = 0; i < length; i++) {
                int val = getInt(tokens[i]);
                if (val > 0) {
                    if (length > 1) {
                        for (int j = 1; j <= 3; j++) {
                            if ((i + j) < length) {
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
                familyBeds = DEFAULT_BED_COUNT;
            } else {
                singleBeds = DEFAULT_BED_COUNT;
            }
        }
        Age minAge;
        Age maxAge;

        if (mna == Age.MIN_AGE.toInt()) {
            minAge = Age.MIN_AGE;
        } else if (mna == Age.CHILDREN_BASE.toInt()) {
            minAge = Age.CHILDREN_BASE;
        } else if (mna == Age.YOUNG_ADULTS_BASE.toInt()) {
            minAge = Age.YOUNG_ADULTS_BASE;
        } else if (mna == Age.ADULTS_BASE.toInt()) {
            minAge = Age.ADULTS_BASE;
        } else {
            minAge = Age.MIN_AGE;
        }

        if (mxa == Age.BABIES.toInt()) {
            maxAge = Age.BABIES;
        } else if (mxa == Age.CHILDREN_CAP.toInt()) {
            maxAge = Age.CHILDREN_CAP;
        } else if (mxa == Age.YOUNG_ADULTS_CAP.toInt()) {
            maxAge = Age.YOUNG_ADULTS_CAP;
        } else if (mxa == Age.ADULTS_CAP.toInt()) {
            maxAge = Age.ADULTS_CAP;
        } else {
            maxAge = Age.MAX_AGE;
        }

        //this.setVacancies(singleBeds + familyBeds);
        if (singleBeds > 0) {
            addNewBeds(singleBeds, false, mo, wo, minAge, maxAge, vo);
        }
        if (familyBeds > 0) {
            addNewBeds(familyBeds, true, mo, wo, minAge, maxAge, vo);
        }
    }


    private int getInt(String text) {
        int val = -1;
        try {
            val = Integer.valueOf(text.trim());
        } catch (NumberFormatException ignored) {
        }
        return val;
    }
*/
//--------------------------------------------------------------------------------------------

/*
//Bed Handling
// Ex. bedKey: 'FFF026_200_T'  <-- single bed, not men only, not women only, minAge = 26 (ADULT),
//                                   maxAge = 200 (MAX_AGE), is veterans only
    private void addNewBeds(int numberOfBeds, boolean isFamily, boolean menOnly, boolean womenOnly,
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
        int lastId = 0;
        if (lastBedAdded != null) {
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
                    int userAge = Integer.parseInt(ageString);
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
//                    if (veteranOnlyBed && !(isVeteranUser)) {
                        exclude veteran beds from non-veterans
//                        thisBedOpen = false;
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
    */
//--------------------------------------------------------------------------------------------
// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public HashMap<String, Collection<Bed>> reserveBed() {
//        return reserveBed("Single", 1);
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)
/*
    /**
     * Equivalent for checking in a User to a Shelter with a StayReport
     * @param type The type of bed to be reserved (Single or Family)
     * @param numBeds An integer from 1 to 5 for the quantity of beds to reserve at this shelter
     * @return A HashMap that maps a bedKey (representative of the User type that can legitimately
     *          sleep in this bed) to a collection of bedIDs (i.e. bed_50)
     *//*
    public HashMap<String, Collection<Bed>> reserveBed(String type, int numBeds) {
        //function takes in User and returns ID of bed(s) being reserved
        if (type == null) {
            throw new IllegalArgumentException("Bed type cannot be null.");
        }
        if (numBeds < 0) {
            throw new IllegalArgumentException("Number of beds must be a positive integer.");
        } else if (numBeds == 0) {
            return null;
        }
        Model model = Model.getInstance();
        User user = ((User) (model.getCurrUser()));
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }
        StayReport curStay = user.getCurrentStayReport();
        if (user.isOccupyingBed()) {
            throw new IllegalArgumentException("User must not have already reserved a bed.");
        } else if ((curStay != null)
                && curStay.isActive()) {
            throw new IllegalArgumentException("User is currently checked in already at " +
                    "this or another shelter.");
        }

        Shelter curShelter = model.verifyShelterParcel(this);
        String userKey = user.generateKey();
        String bedTypeFoundKey = findValidBedType(userKey);
        int curVacancies = curShelter.getVacancies();

        switch (type) {
            case "Family":
                if (bedTypeFoundKey.charAt(0) != 'T') {
                    bedTypeFoundKey = "T" + bedTypeFoundKey.substring(1);
                }
                int famCap = curShelter.getFamilyCapacity();
                if ((famCap - numBeds) >= 0) {
                    curShelter.setFamilyCapacity(famCap - numBeds);
                } else {
                    throw new IllegalArgumentException("Too many beds requested!");
                }
                break;
            case "Single":
                if (bedTypeFoundKey.charAt(0) != 'F') {
                    bedTypeFoundKey = "F" + bedTypeFoundKey.substring(1);
                }
                int singCap = curShelter.getSingleCapacity();
                if ((singCap - numBeds) >= 0) {
                    curShelter.setSingleCapacity(singCap - numBeds);
                } else {
                    throw new IllegalArgumentException("Too many beds requested!");
                }
                break;
            default:
                throw new IllegalArgumentException("Bed type must either be 'Single' or 'Family'");
        }

        // ValidBedsFound is our structure containing all beds that must be updated in the database
        HashMap<String, Collection<Bed>> validBedsFound = new HashMap<>();
        // reservedBeds will hold pointers to our newly reserved bed objects
        Collection<Bed> reservedBeds = new ArrayList<>();
        HashMap<String, HashMap<String, Bed>> shelterBeds = curShelter.getBeds();
        HashMap<String, Bed> bedOptions = shelterBeds.get(bedTypeFoundKey);
        Collection<Bed> v = bedOptions.values();
        Bed[] bedArr = new Bed[v.size()];
        bedArr = (v.toArray(bedArr));

        HashMap<String, Bed> occupied = shelterBeds.get("O");
        if (occupied == null) {
            occupied = new HashMap<>();
            curShelter.beds.put("O", occupied);
        }
        String curEmail = user.getEmail();
        for (int i = 0; i < numBeds; i++) {
            bedArr[i].setOccupantEmail(curEmail);
            // remove the valid bed from this shelter's beds list & place in the occupied list
            String reserveID = bedArr[i].getId();
            occupied.put(reserveID, bedArr[i]);
            reservedBeds.add(bedOptions.remove(reserveID));
        }
        validBedsFound.put(bedTypeFoundKey, reservedBeds);
        user.addStayReport(new StayReport(curShelter, user, reservedBeds));
        int newVac = curShelter.getVacancies() - numBeds;
        curShelter.setVacancies(newVac);
        return validBedsFound;
    }
*/
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

/*
    // Equivalent for checking out w/ a StayReport
    public HashMap<String, Collection<Bed>> undoReservation(StayReport curStay) {
        Model model = Model.getInstance();
        User user = ((User)(model.getCurrUser()));
        Shelter curShelter = model.verifyShelterParcel(this);
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        } else if (!user.isOccupyingBed()) {
            throw new IllegalArgumentException("User must have bed reserved.");
        }

        List<String> bedIds = curStay.getReservedBeds();
        Collection<Bed> beds = new ArrayList<>();
        HashMap<String, Collection<Bed>> reserved = new HashMap<>();
        Collection<String> bedKeys = new HashSet<>();

        Collection<Bed> occupied = curShelter.getBeds().get("O").values();

        for (String id : bedIds) {
            for (Bed b : occupied) {
                if (b.getId().equals(id)) {
                    beds.add(b);
                    bedKeys.add(b.getSavedBedKey());
                    curShelter.getBeds().get(b.getSavedBedKey()).put(id, b);
                    b.removeOccupant(user.getEmail());
                }
            }
        }
        occupied.removeAll(beds);
        for (String key : bedKeys) {
            if (bedKeys.size() > 1) {
                Collection<Bed> beds1 = new ArrayList<>();
                for (Bed b : beds) {
                    if (b.getSavedBedKey().equals(key)) {
                        beds1.add(b);
                    }
                }
                if (key.charAt(0) == 'T') {
                    curShelter.setFamilyCapacity(curShelter.getFamilyCapacity() + beds1.size());
                } else if (key.charAt(0) == 'F') {
                    curShelter.setSingleCapacity(curShelter.getSingleCapacity() + beds1.size());
                }
                reserved.put(key, beds1);
            } else {
                if (key.charAt(0) == 'T') {
                    curShelter.setFamilyCapacity(curShelter.getFamilyCapacity() + beds.size());
                } else if (key.charAt(0) == 'F') {
                    curShelter.setSingleCapacity(curShelter.getSingleCapacity() + beds.size());
                }
                reserved.put(key, beds);
            }
        }

        curStay.checkOut();
        user.clearOccupiedBed();
        int newVac = curShelter.getVacancies() + beds.size();
        curShelter.setVacancies(newVac);

        return reserved;
    }
    */

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public Bed getBedOccupiedBy(User user) {
//        for (Bed b : this.beds.get("O").values()) {
//            User occupant = (User)Model.getAccountByEmail(b.getOccupantEmail());
//            if ((occupant != null) && occupant.equals(user)) {
//                return b;
//            }
//        }
//        return null;
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)
    // ^ can also do user.getCurrentStayReport().getReservedBeds();


    public String getShelterName() {
        return this.shelterName;
    }

    public String getShelterKey() {
        return this.shelterKey;
    }

    public String getCapacityStr() {
        return this.capacityStr;
    }

    public int getFamilyCapacity() {
        return this.familyCapacity;
    }

    public int getSingleCapacity() {
        return this.singleCapacity;
    }

    public String getRestrictions() {
        return restrictions;
    }

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public void setRestrictions(String restrictions) {
//        this.restrictions = restrictions;
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)

    public double getLatitude() {
        return latitude;
    }

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public void setLatitude(double latitude) {
//        this.latitude = latitude;
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)

    public double getLongitude() {
        return longitude;
    }

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public void setLongitude(double longitude) {
//        this.longitude = longitude;
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)

    public String getNotes() {
        return notes;
    }

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public void setNotes(String notes) {
//        this.notes = notes;
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)

    public String getPhone() { return this.phone; }

    public String getAddress() {
        return address;
    }

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public void setAddress(String address) {
//        this.address = address;
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)

    // For Parcelable capabilities
    @Override
    public int describeContents() {
        return 0;
    }

    // will likely need to update to use totalCapacity, vacancies, beds, etc.
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
        dest.writeInt(this.getSingleCapacity());
        dest.writeInt(this.getFamilyCapacity());
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



    public Map<String, Map<String, Bed>> getBeds() {
        return beds;
    }

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public void setBeds(HashMap<String, HashMap<String, Bed>> beds) {
//        this.beds = beds;
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)

    public int getVacancies() {
        return this.vacancies;
    }

    public void setVacancies(int vacancies) {
        this.vacancies = vacancies;
    }

    @Override
    public boolean equals(Object other) {
        return (other == this) || ((other instanceof Shelter)
                && this.shelterKey.equals(((Shelter)other).shelterKey)
                && this.shelterName.equalsIgnoreCase(((Shelter)other).shelterName)
//                && this.restrictions.equalsIgnoreCase(((Shelter)other).restrictions);
                && this.address.equalsIgnoreCase(((Shelter)other).address)
                && this.phone.equals(((Shelter)other).phone));
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = (31 * result) + this.shelterKey.hashCode();
        result = (31 * result) + this.shelterName.toLowerCase().hashCode();
//        result = 31 * result + this.restrictions.toLowerCase().hashCode();
        result = (31 * result) + this.address.toLowerCase().hashCode();
        result = (31 * result) + this.phone.hashCode();
        return result;
    }

    public void setFamilyCapacity(int familyCapacity) {
        this.familyCapacity = familyCapacity;
    }

    public void setSingleCapacity(int singleCapacity) {
        this.singleCapacity = singleCapacity;
    }
}
