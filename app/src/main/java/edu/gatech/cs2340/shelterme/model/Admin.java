package edu.gatech.cs2340.shelterme.model;


import java.util.ArrayList;

public class Admin extends Account {

    /**
     * A no-arg constructor for Admin
     */
    public Admin() {
        this("admin", "admin", "admin@shelters.atl", "admin".hashCode(),
                Question.CITY, "Atlanta");
    }

    /**
     * A constructor for Admin class
     * @param name the name of the admin
     * @param username the username of the admin
     * @param email the email of the admin
     * @param password the password of the admin
     * @param secQ the security question of the admin
     * @param secA the security answer of the admin
     */
    public Admin(String name, String username, String email, int password,
                 Question secQ, String secA) {
        super(name, username, email, password, secQ, secA);
        super.setAccountType(Type.ADMIN);
    }

    /**
     * Checks if two admin accounts equal eachother based on email, name, username, or password.
     * @param other an account object
     * @return true if the two admin accounts equal eachother, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof Admin) && super.equals(other);
    }

    /**
     * A method that created a hashcode
     * @return the hashcode as an integer
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}