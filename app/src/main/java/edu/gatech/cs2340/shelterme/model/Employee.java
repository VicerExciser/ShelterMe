package edu.gatech.cs2340.shelterme.model;

/**
 * Created by austincondict on 2/18/18.
 */

public class Employee extends Account {

    Shelter placeOfWork;

    public Employee(String name, String uname, String email, int pass, Shelter work,
                    Question secQ, String secA) {
        super(name, uname, email, pass, secQ, secA);
        placeOfWork = work;
    }
}
