package edu.gatech.cs2340.shelterme.model;

/**
 * Created by austincondict on 2/20/18.
 */

public class Bed {

    User occupant;
    boolean isOccupied;
    boolean isFamily;       //designates if the bed is of "family type" or for a single person
    boolean noAdultMen;     //designates if the bed is a "women and children only" bed
    int minAge;             //designates minimum age that applies to an individual or the children of a family
    int maxAge;             //designates maximum age that applies to an individual or the children of a family
    boolean veteranOnly;    //designates if the bed must have at least one veteran occupying it

}
