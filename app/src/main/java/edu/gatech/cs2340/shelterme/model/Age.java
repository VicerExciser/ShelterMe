package edu.gatech.cs2340.shelterme.model;

/**
 * Created by Russell on 3/6/2018.
 */

// Had to change the key values to use '_' because '/' is not allowed in a HashMap key
public enum Age {
    MINAGE("000_"),
    BABIES("005_"),      // Newborns:      0-5    yrs
    CHILDREN("015_"),    // Children:      6-15   yrs
    YOUNGADULTS("025_"), // Young Adults:  16-25  yrs
    ADULTS("065_"),      // Adult:         26-65  yrs
    MAXAGE("200_");      // Ancient:       61-200 yrs

    private String ageKeyVal;

    Age(String ageKeyVal) {
        this.ageKeyVal = ageKeyVal;
    }

    public String getAgeKeyVal() {
        return ageKeyVal;
    }
}
