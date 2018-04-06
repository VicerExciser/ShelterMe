/*
 * Information holder for a User's stay at a Shelter
 */
package edu.gatech.cs2340.shelterme.model;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Stack;

public class StayReport {

//    startDate, endDate, daysStayed, sender, timeSent, message, accountEmail
    private boolean active;
    private int numReserved;
//    private Shelter shelter;
    private String shelterName;
    private List<String> reservedBeds;
    //Date startDate, endDate;
//    private Date timestamp;
//    private Long startDate, endDate;
    private String checkInDate;
    private String checkOutDate;
    private String accountEmail;

    public StayReport() {}

    public StayReport(Shelter shelter, User account, ArrayList<Bed> bedlist) {
        this.setActive(true);
        this.setShelterName(shelter.getShelterName());
//        account.setStayingAt(shelter);
        account.setIsOccupyingBed(true);
        setAccountEmail(account.getEmail());
        Date timestamp = (Calendar.getInstance().getTime());
        Long startDate = timestamp.getTime();
        this.setCheckInDate(new Date(startDate).toString());
//        Long endDate = null;
        this.setCheckOutDate(null);
        this.reservedBeds = new Stack<String>();
        for (Bed b : bedlist) {
//            ((Stack<String>)(this.reservedBeds)).push(b.getId());
            reservedBeds.add(b.getId());
        }
        this.setNumReserved(reservedBeds.size());
//        this.numReserved = 1; // default val for testing
    }


    public void checkOut() {
        setActive(false);
//        endDate = Calendar.getInstance().getTime().getTime();
        this.setCheckOutDate(Calendar.getInstance().getTime().toString());
    }


    public boolean isActive() {
        return this.active;
    }

    public int getNumReserved() {
        return numReserved;
    }

    public List<String> getReservedBeds() {
        return this.reservedBeds;
    }

// MAY BE ISSUES USING DATE TYPE WITH FIREBASE, IF SO THEN USE LONG TO REPRESENT MILLISECONDS SINCE EPOCH

//    public Long getStartDate() {
//        return this.startDate;
//    }
    public String getCheckInDate() { return this.checkInDate; }

//    public Date getCheckInTime() {
//        return new Date(this.startDate);
//    }

//    public Long getEndDate() {
//        return this.endDate;
//    }
    public String getCheckOutDate() { return this.checkOutDate; }

//    public Date getCheckOutTime() {
//        return this.endDate == null ? null : new Date(this.endDate);
//    }



//    public Date getTimestamp() {
//        return timestamp;
//    }
//
//    public void setTimestamp(Date timestamp) {
//        this.timestamp = timestamp;
//    }

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

//    public void setEndDate(Date leaving)

}


/*
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;

public class TimeToLongTest {
    Date timestamp;
    long tsLong;
    String tsString;
    long dateLong;

    public TimeToLongTest() {
        timestamp = Calendar.getInstance().getTime();
        System.out.println("timestamp = " + timestamp);
        tsString = timestamp.toString();
        System.out.println("instantiated at " + tsString);
        tsLong = Calendar.getInstance().getTimeInMillis();
    }

    public void printLong() {
        dateLong = timestamp.getTime();
        System.out.println("timestamp long = " + tsLong);
        System.out.println("date long = " + dateLong);
    }

    public void printTime() {

        // System.out.println("time from long 1: " + timestamp.from(ta));
        System.out.println("time from long 1: " + new Date(tsLong).toString());
        System.out.println("time from long 2: " + new Date(dateLong).toString());
        try {
        System.out.println("time from string: "
            + DateFormat.getInstance().parse(tsString));
        } catch (ParseException pe) {
            System.out.println(pe.getMessage());
        }
    }

    public static void main(String[] args) {
        TimeToLongTest time = new TimeToLongTest();
        System.out.println();
        time.printLong();
        System.out.println();
        time.printTime();
    }
 */