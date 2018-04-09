package edu.gatech.cs2340.shelterme.model;

/**
 * Created by austincondict on 4/6/18.
 */

public enum GenderAccepted {
    ANY("Any gender"),
    MEN("Men only"),
    WOMEN("Women only");

    private final String _msg;
    GenderAccepted(String msg) { _msg = msg; }
    public String toString() { return _msg; }
}
