package edu.gatech.cs2340.shelterme.model;

/**
 * Represents Age for a shelter's restrictions.
 */

// Had to change the key values to use '_' because '/' is not allowed in a HashMap key
public enum Age {
    MIN_AGE("000_"),
    BABIES("005_"),            // Newborns:      0-5    yrs
    CHILDREN_BASE("006_"),
    CHILDREN_CAP("015_"),      // Children:      6-15   yrs
    YOUNG_ADULTS_BASE("016_"),
    YOUNG_ADULTS_CAP("025_"),   // Young Adults:  16-25  yrs
    ADULTS_BASE("026_"),
    ADULTS_CAP("065_"),        // Adult:         26-65  yrs
    MAX_AGE("200_");            // Ancient:       61-200 yrs

    private final String ageKeyVal;

    Age(String ageKeyVal) {
        this.ageKeyVal = ageKeyVal;
    }

    /**
     * A getter for ageKeyValue
     * @return the key associated with age
     */
    public String getAgeKeyVal() {
        return ageKeyVal;
    }

    /**
     * @return An integer representation of the age value
     */
    public int toInt() {
        int cutoff = this.ageKeyVal.indexOf('_');
        return Integer.valueOf(this.ageKeyVal.substring(0, cutoff));
    }
}
