package edu.gatech.cs2340.shelterme.model;

/**
 * Created by Russell on 3/6/2018.
 */

public enum Age {
    ZERO("000/"),
    FIVE("005/"),
    FIFTEEN("015/"),
    TWENTYFIVE("025/"),
    FIFTY("050/"),
    TWOHUNDRED("200/");

    String ageString;

    Age(String ageString) {
        this.ageString = ageString;
    }

    public String getAgeString() {
        return ageString;
    }
}
