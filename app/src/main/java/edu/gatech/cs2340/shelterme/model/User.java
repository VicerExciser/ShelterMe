package edu.gatech.cs2340.shelterme.model;

import edu.gatech.cs2340.shelterme.model.Account;

/**
 * Created by austincondict on 2/12/18.
 */

// TODO: Make other user type classes
public class User extends Account {

//    Question secQuest;
//    String secAns;
    private int age;
    private Sex sex;

    public User(String name, String uname, String email, int pass, Sex sex, int age,
                Question secQ, String secA) {
        super(name, uname, email, pass, secQ, secA);
//        secQuest = secQ;
//        secAns = secA;
        this.age = age;
        this.sex = sex;
    }

//    public User(String name, String pass) {
//        String emailAddress = "";
//        if (isValidEmailAddress(name))
//            emailAddress = name;
//        else
//            // error: email needed for account registration
//
//        this(name, pass, emailAddress);
//    }

    public User() {
        this("user steve", "user", "user@gmail.com", "pass".hashCode(), Sex.MALE, 25,
                Question.PET, "Spot");
    }

//    public String getID() {
//        return super.getID();
//    }


}

