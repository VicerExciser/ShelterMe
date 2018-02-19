package edu.gatech.cs2340.shelterme.model;

/**
 * Created by austincondict on 2/18/18.
 */

public class Admin extends Account {

    public Admin() {
        super("admin", "admin", "admin@shelters.atl", "admin".hashCode(),
                Question.CITY, "Atlanta");
    }
}
