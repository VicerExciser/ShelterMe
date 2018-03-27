/*
 * Information holder for a User's stay at a Shelter
 */
package edu.gatech.cs2340.shelterme.model;


import java.util.Calendar;
import java.util.Date;

public class StayReport {

//    startDate, endDate, daysStayed, sender, timeSent, message, accountEmail
    private boolean active;
    private int numReserved;
    private Shelter shelter;
    private Bed[] reservedBeds;
    //Date startDate, endDate;
    private Date timestamp;
    private Long startDate, endDate;
    private String accountEmail;

    public StayReport() {}

    public StayReport(Shelter shelter, User account, Bed[] reservedBeds) {
        this.active = true;
        this.shelter = shelter;
        account.setStayingAt(shelter);
        account.setIsOccupyingBed(true);
        accountEmail = account.getEmail();
        timestamp = Calendar.getInstance().getTime();
        startDate = timestamp.getTime();
        endDate = null;
        this.reservedBeds = reservedBeds;
        this.numReserved = reservedBeds.length;
    }


    public void checkOut() {
        active = false;
        endDate = Calendar.getInstance().getTime().getTime();
    }


    public boolean isActive() {
        return this.active;
    }

    public int getNumReserved() {
        return numReserved;
    }

    public Bed[] getReservedBeds() {
        return this.reservedBeds;
    }

// MAY BE ISSUES USING DATE TYPE WITH FIREBASE, IF SO THEN USE LONG TO REPRESENT MILLISECONDS SINCE EPOCH

    public Long getStartDate() {
        return this.startDate;
    }

    public Date getCheckInTime() {
        return new Date(this.startDate);
    }

    public Long getEndDate() {
        return this.endDate;
    }

    public Date getCheckOutTime() {
        return this.endDate == null ? null : new Date(this.endDate);
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