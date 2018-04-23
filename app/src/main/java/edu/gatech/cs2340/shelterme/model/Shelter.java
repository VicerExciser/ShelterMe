
package edu.gatech.cs2340.shelterme.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import edu.gatech.cs2340.shelterme.util.BedManager;
import edu.gatech.cs2340.shelterme.util.ShelterBuilder;

/**
 * The type Shelter.
 */
public class Shelter implements Parcelable {
    // Had to change shelterKey to String in the form: "s_1" for the sake of serialization
    // (throws DatabaseException otherwise)
//    private static final int DEFAULT_BED_COUNT = 50;
    private final String shelterName;
    private String shelterKey;
    private int familyCapacity;     // # of family accommodations
    private int singleCapacity;     // # of single-person beds
    private final String capacityStr;
    private String restrictions;
//    private boolean takesFamilies;

    //    private Location location;
    private double latitude;
    private double longitude;
    private String notes;
    private final String phone;
    private String address;

    //    private HashMap<String, LinkedHashMap<String, Bed>> beds;
    private Map<String, Map<String, Bed>> beds;
    @Nullable
    private Bed lastBedAdded;
    private int vacancies;

    private BedManager bedManager;

//    @interface IgnoreExtraProperties {}
//    @JsonIgnore
//    private DBUtil dbUtil = DBUtil.getInstance();

    /**
     * Instantiates a new Shelter.
     */
    public Shelter() {
        this("s_1001", "<Shelter Name Here>", "500", "Anyone",
                0, 0,
                "123 Sesame St", "", "(800) 800-8008");
    }

    /**
     * Instantiates a new Shelter.
     *
     * @param key          the key
     * @param name         the name
     * @param capacity     the capacity
     * @param restrictions the restrictions
     * @param longitude    the longitude
     * @param latitude     the latitude
     * @param address      the address
     * @param specNotes    the spec notes
     * @param num          the num
     */
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
        this.lastBedAdded = null;
        finishBuildingShelter();
    }

    private void finishBuildingShelter() {
        ShelterBuilder shelterBuilder = new ShelterBuilder(this);
        if (this.restrictions != null) {
            shelterBuilder.processRestrictions(this.restrictions);
        } else {
            Log.v("Shelter", "restrictions for " + shelterName + " were not found\n"
                    + this.toString());
        }
    }

    /**
     * Instantiates a new Shelter.
     *
     * @param parcel the parcel
     */
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

    /**
     * Detail string.
     *
     * @return the string
     */
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

//    public Bed getBedOccupiedBy(User user) {
//        for (Bed b : this.beds.get("O").values()) {
//            User occupant = (User)Model.getAccountByEmail(b.getOccupantEmail());
//            if ((occupant != null) && occupant.equals(user)) {
//                return b;
//            }
//        }
//        return null;
//    }
    // ^ can also do user.getCurrentStayReport().getReservedBeds();

    /**
     * Has open bed boolean.
     *
     * @param userKey the user key
     * @return the boolean
     */
    public boolean hasOpenBed(String userKey) {
        // Ex key: 'FM25F'  <-- not family acct, male, 25 yrs old, not veteran
        BedManager bedManager = getShelterBedManager();
        return (bedManager != null) && (bedManager.findValidBedType(userKey) != null);
    }

    /**
     * Gets shelter bed manager.
     *
     * @return the shelter bed manager
     */
    public BedManager getShelterBedManager() {
//        if (shelterBuilder != null) {
//            bedManager = shelterBuilder.getBedManager();
//        }
        if (this.bedManager == null) {
            this.bedManager = new BedManager(this);
        }
        return this.bedManager;  // != null ? bedManager : new BedManager(this);
    }

    /**
     * Gets shelter name.
     *
     * @return the shelter name
     */
    public String getShelterName() {
        return this.shelterName;
    }

    /**
     * Gets shelter key.
     *
     * @return the shelter key
     */
    public String getShelterKey() {
        return this.shelterKey;
    }

    /**
     * Gets capacity str.
     *
     * @return the capacity str
     */
    public String getCapacityStr() {
        return this.capacityStr;
    }

    /**
     * Gets family capacity.
     *
     * @return the family capacity
     */
    public int getFamilyCapacity() {
        return this.familyCapacity;
    }

    /**
     * Gets single capacity.
     *
     * @return the single capacity
     */
    public int getSingleCapacity() {
        return this.singleCapacity;
    }

    /**
     * Gets restrictions.
     *
     * @return the restrictions
     */
    public String getRestrictions() {
        return restrictions;
    }

    /**
     * Sets restrictions.
     *
     * @param restrictions the restrictions
     */
    public void setRestrictions(String restrictions) {
        this.restrictions = restrictions;
        ShelterBuilder sb = new ShelterBuilder(this);
        sb.processRestrictions(restrictions);
    }

    /**
     * Gets latitude.
     *
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets latitude.
     *
     * @param latitude the latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }


    /**
     * Gets longitude.
     *
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets longitude.
     *
     * @param longitude the longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Gets notes.
     *
     * @return the notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets notes.
     *
     * @param notes the notes
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Gets phone.
     *
     * @return the phone
     */
    public String getPhone() { return this.phone; }

    /**
     * Gets address.
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets address.
     *
     * @param address the address
     */
    public void setAddress(String address) {
        this.address = address;
    }

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

    /**
     * The constant CREATOR.
     */
    @SuppressWarnings("unused")
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

    /**
     * Gets beds.
     *
     * @return the beds
     */
    public Map<String, Map<String, Bed>> getBeds() {
        return beds;
    }

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public void setBeds(HashMap<String, HashMap<String, Bed>> beds) {
//        this.beds = beds;
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)

    /**
     * Gets vacancies.
     *
     * @return the vacancies
     */
    public int getVacancies() {
        return this.vacancies;
    }

    /**
     * Sets vacancies.
     *
     * @param vacancies the vacancies
     */
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
        String shelterNameLower = this.shelterName.toLowerCase();
        result = (31 * result) + shelterNameLower.hashCode();
//        result = 31 * result + this.restrictions.toLowerCase().hashCode();
        String addressLower = this.address.toLowerCase();
        result = (31 * result) + addressLower.hashCode();
        result = (31 * result) + this.phone.hashCode();
        return result;
    }

    /**
     * Sets family capacity.
     *
     * @param familyCapacity the family capacity
     */
    public final void setFamilyCapacity(int familyCapacity) {
        this.familyCapacity = familyCapacity;
    }

    /**
     * Sets single capacity.
     *
     * @param singleCapacity the single capacity
     */
    public final void setSingleCapacity(int singleCapacity) {
        this.singleCapacity = singleCapacity;
    }

    /**
     * Gets last bed added.
     *
     * @return the last bed added
     */
    @Nullable
    public Bed getLastBedAdded() {
        return lastBedAdded;
    }

    /**
     * Sets last bed added.
     *
     * @param lastBedAdded the last bed added
     */
    public void setLastBedAdded(@Nullable Bed lastBedAdded) {
        this.lastBedAdded = lastBedAdded;
    }
}