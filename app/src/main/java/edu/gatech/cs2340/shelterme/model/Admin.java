package edu.gatech.cs2340.shelterme.model;


import java.util.ArrayList;

public class Admin extends Account {

    public Admin() {
        this("admin", "admin", "admin@shelters.atl", "admin".hashCode(),
                Question.CITY, "Atlanta");
    }

    public Admin(String name, String username, String email, int password,
                 Question secQ, String secA) {
        super(name, username, email, password, secQ, secA);
        super.setAccountType(Type.ADMIN);
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof Admin) && super.equals(other);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}