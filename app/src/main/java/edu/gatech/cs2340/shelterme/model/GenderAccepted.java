package edu.gatech.cs2340.shelterme.model;

/**
 * Represents the genders accepted/not accepted at a shelters.
 * Created by austincondict on 4/6/18.
 */

public enum GenderAccepted {
    ANY("Any gender"),
    MEN("Men only"),
    WOMEN("Women only");

    private final String _msg;
    GenderAccepted(String msg) { _msg = msg; }

    /**
     * @return a string representation of the object
     */
    public String toString() { return _msg; }
}
