/*
 * Information holder for a User's stay at a Shelter
 * Instances created from Shelter.reserveBed()
 */
package edu.gatech.cs2340.shelterme.model;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Stack;

/**
 * Information holder for a User's stay at a Shelter
 * Instances created from Shelter.reserveBed()
 */
public class StayReport {

    private boolean active;
    private int numReserved;
    private String shelterName;
    private final Collection<String> reservedBeds;
    private String checkInDate;
    private String checkOutDate;
    private String accountEmail;

    /**
     * A no-arg constructor for StayReport class
     */
    public StayReport() {
        this(new Shelter(), new User(), new ArrayList<Bed>());
    }

    /**
     * A cosntructor for StayReport class
     *
     * @param shelter the name of the shelter
     * @param account the account type
     * @param bedList the list of beds
     */
    public StayReport(Shelter shelter, User account, Iterable<Bed> bedList) {
        this.setActive(true);
        this.setShelterName(shelter.getShelterName());
        if (!account.isOccupyingBed()) {
            account.setIsOccupyingBed(true);
        }
        setAccountEmail(account.getEmail());
        Calendar calendar = Calendar.getInstance();
        Date timestamp = calendar.getTime();
        Long startDate = timestamp.getTime();
        this.setCheckInDate(timestamp.toString());
//        this.setCheckInDate(new Date(startDate).toString());
        this.setCheckOutDate(null);
        this.reservedBeds = new Stack<>();
        for (Bed b : bedList) {
            reservedBeds.add(b.getId());
        }
        this.setNumReserved(reservedBeds.size());
//        this.numReserved = 1; // default val for testing
    }

    // Called from Shelter.undoReservation

    /**
     * Sets the stay/reservation checkout time
     */
    public void checkOut() {
        setActive(false);
        Calendar calendar = Calendar.getInstance();
        Date time = calendar.getTime();
        this.setCheckOutDate(time.toString());
    }

    /**
     * A getter for the active status of the stay/reservation
     *
     * @return boolean
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * A getter for numReserved
     *
     * @return the number of beds reserved
     */
    public int getNumReserved() {
        return numReserved;
    }

    /**
     * A getter for the reserved beds
     *
     * @return a List of the reserved Bed IDs for this stay (i.e. "bed_50")
     */
    public Collection<String> getReservedBeds() {
        return this.reservedBeds;
    }


    // EXAMPLE: If needing to compare check-in/check-out timestamps for stay reports, use
    //          -  Date time1 = new Date(stayReport1.getCheckInDate());
    //          -  if (time1.compareTo(new Date(stayReport2.getCheckInDate())) <= 0)
    //                 stayReport1 should be listed before stayReport2

    /**
     * A getter for checkInDate
     *
     * @return the check in date for the bed's occupant
     */
    public String getCheckInDate() { return this.checkInDate; }

    /**
     * Gets check out date.
     *
     * @return the check out date
     */
    public String getCheckOutDate() { return this.checkOutDate; }

    /**
     * Gets account email.
     *
     * @return the account email
     */
    public String getAccountEmail() {
        return accountEmail;
    }

    private void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    /**
     * Gets shelter name.
     *
     * @return the shelter name
     */
    public String getShelterName() {
        return shelterName;
    }

    private void setShelterName(String shelterName) {
        this.shelterName = shelterName;
    }

    private void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    private void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    private void setActive(boolean active) {
        this.active = active;
    }

    private void setNumReserved(int numReserved) {
        this.numReserved = numReserved;
    }


    @Override
    public boolean equals(Object other) {
        return (other == this) || ((other instanceof StayReport)
                && ((StayReport) other).checkInDate.equalsIgnoreCase(this.checkInDate)
                && ((StayReport) other).shelterName.equalsIgnoreCase(this.shelterName)
                && ((StayReport) other).accountEmail.equalsIgnoreCase(this.accountEmail));
    }

    @Override
    public int hashCode() {
        int result = 17;
        String checkInDateLower = this.checkInDate.toLowerCase();
        result = (31 * result) + checkInDateLower.hashCode();
        String shelterNameLower = this.shelterName.toLowerCase();
        result = (31 * result) + shelterNameLower.hashCode();
        String accountEmailLower = this.accountEmail.toLowerCase();
        result = (31 * result) + accountEmailLower.hashCode();
        return result;
    }
}