package edu.gatech.cs2340.shelterme.model;

/**
 * Created by austincondict on 2/18/18.
 */

public class Admin extends Account {

    public Admin() {
        this("admin", "admin", "admin@shelters.atl", "admin".hashCode(),
                Question.CITY, "Atlanta");
    }

    public Admin(String name, String username, String email, int password,
                 Question secQ, String secA) {
        super(name, username, email, password, secQ, secA);
    }
}
