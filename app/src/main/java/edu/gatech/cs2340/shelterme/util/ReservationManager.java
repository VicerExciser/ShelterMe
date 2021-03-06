package edu.gatech.cs2340.shelterme.util;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.gatech.cs2340.shelterme.model.Bed;
import edu.gatech.cs2340.shelterme.model.Model;
import edu.gatech.cs2340.shelterme.model.Shelter;
import edu.gatech.cs2340.shelterme.model.StayReport;
import edu.gatech.cs2340.shelterme.model.User;

// Following a Command Pattern

/**
 * The type Reservation manager; executes User requests to check in or check out of a Shelter and
 * pushes them to the specific User's stack of StayReports.
 */
public class ReservationManager {

    private final Shelter currentShelter;
    private BedManager bedManager;
    private final Model model;
    private User user;

    /**
     * Instantiates a new Reservation manager.
     *
     * @param shelter the shelter
     * @param user    the user
     */
    public ReservationManager(Shelter shelter, User user) {
        currentShelter = shelter;
//        bedManager = new BedManager(shelter.getShelterName());
        model = Model.getInstance();
//        user = ((User) (model.getCurrUser()));
        this.user = user;
    }

    /**
     * Equivalent for checking in a User to a Shelter with a StayReport
     *
     * @param type    The type of bed to be reserved (Single or Family)
     * @param numBeds An integer from 1 to 5 for the quantity of beds to reserve at this shelter
     * @return A HashMap that maps a bedKey (representative of the User type that can legitimately
     * sleep in this bed) to a List of bedIDs (i.e. bed_50)
     */
    public Map<String, List<Bed>> reserveBed(String type, int numBeds) {
        //function takes in User and returns ID of bed(s) being reserved
        if (type == null) {
            throw new IllegalArgumentException("Bed type cannot be null.");
        }
        if (numBeds < 0) {
            throw new IllegalArgumentException("Number of beds must be a positive integer.");
        } else if (numBeds == 0) {
            return null;
        }

        if (user == null) {
            if (model.getCurrUser() == null) {
                throw new IllegalArgumentException("User cannot be null.");
            } else {
                user = (User)(model.getCurrUser());
            }
        }
        StayReport curStay = user.getCurrentStayReport();
        if (user.isOccupyingBed()) {
            throw new IllegalArgumentException("User must not have already reserved a bed.");
        } else if ((curStay != null)
                && curStay.isActive()) {
            throw new IllegalArgumentException("User is currently checked in already at " +
                    "this or another shelter.");
        }

        Shelter curShelter = model.verifyShelterParcel(currentShelter);
        if (curShelter == null) {
            curShelter = currentShelter;
        }
        String userKey = user.generateKey();
        if (bedManager == null) {
            bedManager = curShelter.getShelterBedManager();
        }
//        if (bedManager == null) bedManager = new BedManager(curShelter);
        String bedTypeFoundKey = bedManager.findValidBedType(userKey);
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
        Map<String, List<Bed>> validBedsFound = new HashMap<>();
        // reservedBeds will hold pointers to our newly reserved bed objects
        List<Bed> reservedBeds = new ArrayList<>();
        Map<String, Map<String, Bed>> shelterBeds = curShelter.getBeds();
        Map<String, Bed> bedOptions = shelterBeds.get(bedTypeFoundKey);
        List<Bed> v = new ArrayList<>(bedOptions.values());
        Bed[] bedArr = new Bed[v.size()];
        bedArr = (v.toArray(bedArr));

        Map<String, Bed> occupied = shelterBeds.get("O");
        if (occupied == null) {
            occupied = new HashMap<>();
            shelterBeds.put("O", occupied);
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
        int newVac = curVacancies - numBeds;
        curShelter.setVacancies(newVac);
        return validBedsFound;
    }

    /**
     * Undo reservation map.
     *
     * @param curStay the cur stay
     * @return the map
     */
// Equivalent for checking out w/ a StayReport
    public Map<String, List<Bed>> undoReservation(StayReport curStay) {
//        Model model = Model.getInstance();
//        User user = ((User)(model.getCurrUser()));
        Shelter curShelter = model.verifyShelterParcel(currentShelter);
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        } else if (!user.isOccupyingBed()) {
            throw new IllegalArgumentException("User must have bed reserved.");
        }

        List<String> bedIds = new ArrayList<>(curStay.getReservedBeds());
        List<Bed> beds = new ArrayList<>();
        Map<String, List<Bed>> reserved = new HashMap<>();
        Set<String> bedKeys = new HashSet<>();

        Map<String, Map<String, Bed>> shelterBeds = curShelter.getBeds();
        Map<String, Bed> nonVacantBeds = shelterBeds.get("O");
        List<Bed> occupied = new ArrayList<>(nonVacantBeds.values());

        for (String id : bedIds) {
            for (Bed b : occupied) {
                String curId = b.getId();
                String curKey = b.getSavedBedKey();
                if (curId.equals(id)) {
                    beds.add(b);
                    bedKeys.add(curKey);
                    Map<String, Bed> rightfulPlace = shelterBeds.get(curKey);
                    rightfulPlace.put(id, b);
                    String userEmail = user.getEmail();
                    b.removeOccupant(userEmail);
                }
            }
        }
        occupied.removeAll(beds);
        for (String key : bedKeys) {
            int numBedKeys = bedKeys.size();
            if (numBedKeys > 1) {
                List<Bed> beds1 = new ArrayList<>();
                for (Bed b : beds) {
                    String savedKey = b.getSavedBedKey();
                    if (savedKey.equals(key)) {
                        beds1.add(b);
                    }
                }
                int incrementVacancy = beds1.size();
                if (key.charAt(0) == 'T') {
                    int famCap = curShelter.getFamilyCapacity();
                    curShelter.setFamilyCapacity(famCap + incrementVacancy);
                } else if (key.charAt(0) == 'F') {
                    int singCap = curShelter.getSingleCapacity();
                    curShelter.setSingleCapacity(singCap + incrementVacancy);
                }
                reserved.put(key, beds1);
            } else {
                if (key.charAt(0) == 'T') {
                    int famCap = curShelter.getFamilyCapacity();
                    curShelter.setFamilyCapacity(famCap + numBedKeys);
                } else if (key.charAt(0) == 'F') {
                    int singCap = curShelter.getSingleCapacity();
                    curShelter.setSingleCapacity(singCap + numBedKeys);
                }
                reserved.put(key, beds);
            }
        }

        curStay.checkOut();
        user.clearOccupiedBed();
        int curVacancies = curShelter.getVacancies();
        int incrementVacancy = beds.size();
        int newVac = curVacancies + incrementVacancy;
        curShelter.setVacancies(newVac);

        return reserved;
    }

//    public BedManager getBedManager() {
//        return bedManager;
//    }

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
}
