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

public class StayReport {

    private boolean active;
    private int numReserved;
    private String shelterName;
    private List<String> reservedBeds;
    private String checkInDate;
    private String checkOutDate;
    private String accountEmail;

    public StayReport() {
        this(new Shelter(), new User(), new ArrayList<Bed>());
    }

    public StayReport(Shelter shelter, User account, ArrayList<Bed> bedlist) {
        this.setActive(true);
        this.setShelterName(shelter.getShelterName());
        account.setIsOccupyingBed(true);
        setAccountEmail(account.getEmail());
        Date timestamp = (Calendar.getInstance().getTime());
        Long startDate = timestamp.getTime();
        this.setCheckInDate(new Date(startDate).toString());
        this.setCheckOutDate(null);
        this.reservedBeds = new Stack<>();
        for (Bed b : bedlist) {
            reservedBeds.add(b.getId());
        }
        this.setNumReserved(reservedBeds.size());
//        this.numReserved = 1; // default val for testing
    }

    // Called from Shelter.undoReservation()
    public void checkOut() {
        setActive(false);
        this.setCheckOutDate(Calendar.getInstance().getTime().toString());
    }

    public boolean isActive() {
        return this.active;
    }

    public int getNumReserved() {
        return numReserved;
    }

    // Returns a List of the reserved Bed IDs for this stay (i.e. "bed_50")
    public List<String> getReservedBeds() {
        return this.reservedBeds;
    }


    // EXAMPLE: If needing to compare check-in/check-out timestamps for stay reports, use
    //          -  Date time1 = new Date(stayreport1.getCheckInDate());
    //          -  if (time1.compareTo(new Date(stayreport2.getCheckInDate())) <= 0)
    //                 stayreport1 should be listed before stayreport2

    public String getCheckInDate() { return this.checkInDate; }

    public String getCheckOutDate() { return this.checkOutDate; }

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public String getShelterName() {
        return shelterName;
    }

    public void setShelterName(String shelterName) {
        this.shelterName = shelterName;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setNumReserved(int numReserved) {
        this.numReserved = numReserved;
    }


    @Override
    public boolean equals(Object other) {
//        if (other == this) return true;
        return (other == this) || ((other instanceof StayReport)
                && ((StayReport) other).checkInDate.equalsIgnoreCase(this.checkInDate)
                && ((StayReport) other).shelterName.equalsIgnoreCase(this.shelterName)
                && ((StayReport) other).accountEmail.equalsIgnoreCase(this.accountEmail));
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.checkInDate.toLowerCase().hashCode();
        result = 31 * result + this.shelterName.toLowerCase().hashCode();
        result = 31 * result + this.accountEmail.toLowerCase().hashCode();
        return result;
    }
}