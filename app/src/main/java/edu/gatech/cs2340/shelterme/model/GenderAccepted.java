package edu.gatech.cs2340.shelterme.model;

/**
 * The enum Gender accepted.
 */
public enum GenderAccepted {
    /**
     * The Any.
     */
    ANY("Any gender"),
    /**
     * The Men.
     */
    MEN("Men only"),
    /**
     * The Women.
     */
    WOMEN("Women only");

    private final String _msg;
    GenderAccepted(String msg) { _msg = msg; }
    public String toString() { return _msg; }
}
