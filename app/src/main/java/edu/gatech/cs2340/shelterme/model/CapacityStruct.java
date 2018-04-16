package edu.gatech.cs2340.shelterme.model;

/**
 * This class serves as an information holder for Shelter & Bed attribute initializations
 */
@SuppressWarnings("PublicField")
public class CapacityStruct {
    public String shelterName;
    public String cap;
    public boolean family;
    public boolean menOnly;
    public boolean womenOnly;
    public boolean vetsOnly;
    public int minimumAge;
    public int maximumAge;
    public int singleBeds;
    public int familyBeds;
    public Age fromAge;
    public Age toAge;

    /**
     * Instantiates a new Capacity struct.
     */
    public CapacityStruct() {
        shelterName = "Sample Shelter";
        fromAge = Age.MIN_AGE;
        fromAge = Age.MAX_AGE;
    }
}
