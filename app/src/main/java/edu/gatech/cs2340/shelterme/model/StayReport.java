/*
 * Information holder for a User's stay at a Shelter
 * Instances created from Shelter.reserveBed()
 */
package edu.gatech.cs2340.shelterme.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Stack;
/**
 * Represents a stay request by a user.
 */
public class StayReport {

    private boolean active;
    private int numReserved;
    private String shelterName;
    private List<String> reservedBeds;
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
     * @param shelter the name of the shelter
     * @param account the account type
     * @param bedList the list of beds
     */
    public StayReport(Shelter shelter, User account, ArrayList<Bed> bedList) {
        this.setActive(true);
        this.setShelterName(shelter.getShelterName());
        account.setIsOccupyingBed(true);
        setAccountEmail(account.getEmail());
        Date timestamp = (Calendar.getInstance().getTime());
        Long startDate = timestamp.getTime();
        this.setCheckInDate(new Date(startDate).toString());
        this.setCheckOutDate(null);
        this.reservedBeds = new Stack<>();
        for (Bed b : bedList) {
            reservedBeds.add(b.getId());
        }
        this.setNumReserved(reservedBeds.size());
//        this.numReserved = 1; // default val for testing
    }

    /**
     * Sets the bed checkout time
     */
    // Called from Shelter.undoReservation()
    public void checkOut() {
        setActive(false);
        this.setCheckOutDate(Calendar.getInstance().getTime().toString());
    }

    /**
     * A getter for the active status of the bed
     * @return
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * A getter for numReserved
     * @return the number of beds reserved
     */
    public int getNumReserved() {
        return numReserved;
    }

    /**
     * A getter for the reserved beds
     * @return a List of the reserved Bed IDs for this stay (i.e. "bed_50")
     */
    public List<String> getReservedBeds() {
        return this.reservedBeds;
    }


    // EXAMPLE: If needing to compare check-in/check-out timestamps for stay reports, use
    //          -  Date time1 = new Date(stayReport1.getCheckInDate());
    //          -  if (time1.compareTo(new Date(stayReport2.getCheckInDate())) <= 0)
    //                 stayReport1 should be listed before stayReport2

    /**
     * A getter for checkInDate
     * @return the check in date for the bed's occupant
     */
    public String getCheckInDate() { return this.checkInDate; }

    /**
     * A getter for checkOutDate
     * @return the check out date for the bed's occupant
     */
    public String getCheckOutDate() { return this.checkOutDate; }

    /**
     * A getter for accountEmail
     * @return the email of the account
     */
    public String getAccountEmail() {
        return accountEmail;
    }

    /**
     * A setter for the enmail of the account
     * @param accountEmail the email of the account
     */
    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    /**
     * A getter for shelterName
     * @return the shelter's name
     */
    public String getShelterName() {
        return shelterName;
    }

    /**
     * A setter for the shelter's name
     * @param shelterName the shelter's name
     */
    public void setShelterName(String shelterName) {
        this.shelterName = shelterName;
    }

    /**
     * A setter for the check in date
     * @param checkInDate the check in date
     */
    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    /**
     * A setter for the check out date
     * @param checkOutDate the checkout date
     */
    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    /**
     * A setter for the active status of the bed
     * @param active the active status of the bed
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * A setter of the number of beds reserved
     * @param numReserved the number of beds a user want to reserve
     */
    public void setNumReserved(int numReserved) {
        this.numReserved = numReserved;
    }


    /**
     * Checks if two stay reports equal eachother based on the checkin date, checkout date, and
     * the email of the user.
     * @param other
     * @return
     */
    @Override
    public boolean equals(Object other) {
//        if (other == this) return true;
        return (other == this) || ((other instanceof StayReport)
                && ((StayReport) other).checkInDate.equalsIgnoreCase(this.checkInDate)
                && ((StayReport) other).shelterName.equalsIgnoreCase(this.shelterName)
                && ((StayReport) other).accountEmail.equalsIgnoreCase(this.accountEmail));
    }

    /**
     * A method that created a hashcode
     * @return the hashcode as an integer
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.checkInDate.toLowerCase().hashCode();
        result = 31 * result + this.shelterName.toLowerCase().hashCode();
        result = 31 * result + this.accountEmail.toLowerCase().hashCode();
        return result;
    }
}