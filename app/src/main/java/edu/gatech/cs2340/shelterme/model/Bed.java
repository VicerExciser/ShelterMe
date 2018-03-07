package edu.gatech.cs2340.shelterme.model;

/**
 * Created by austincondict on 2/20/18.
 */

public class Bed {

    private int id;
    private User occupant;
    private boolean isOccupied;
    private boolean isFamily;       //designates if the bed is of "family type" or for a single person
    private boolean noAdultMen;     //designates if the bed is a "women and children only" bed
    private Age minAge;             //designates minimum age that applies to an individual or the children of a family
    private Age maxAge;             //designates maximum age that applies to an individual or the children of a family
    private boolean veteranOnly;    //designates if the bed must have at least one veteran occupying it

    public Bed(int id, boolean isFamily, boolean noAdultMen, Age minAge, Age maxAge, boolean veteranOnly) {
        this.id = id;
        this.isOccupied = false;
        this.isFamily = isFamily;
        this.noAdultMen = noAdultMen;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.veteranOnly = veteranOnly;
    }

    public int getId() {
        return id;
    }

    public User getOccupant() {
        return occupant;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public boolean isFamily() {
        return isFamily;
    }

    public boolean isNoAdultMen() {
        return noAdultMen;
    }

    public Age getMinAge() {
        return minAge;
    }

    public Age getMaxAge() {
        return maxAge;
    }

    public boolean isVeteranOnly() {
        return veteranOnly;
    }

    public void setOccupant(User occupant) {
        this.occupant = occupant;
        this.isOccupied = true;
    }

    public void removeOccupant() {
        this.occupant = null;
        this.isOccupied = false;
    }
}
