package edu.gatech.cs2340.shelterme.model;

import edu.gatech.cs2340.shelterme.model.Account;

/**
 * Created by austincondict on 2/12/18.
 */

public class User extends Account {

//    Question secQuest;
//    String secAns;
    private boolean isFamily;
    private int age;
    private Sex sex;

    public User(String name, String uname, String email, int pass, Sex sex, int age, boolean isFamily,
                Question secQ, String secA) {
        super(name, uname, email, pass, secQ, secA);
//        secQuest = secQ;
//        secAns = secA;
        this.isFamily = isFamily;
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
        this("user steve", "user", "user@gmail.com", "pass".hashCode(), Sex.MALE, 25, false,
                Question.PET, "Spot");
    }

//    public String getID() {
//        return super.getID();
//    }

    public String generateKey() {
        String userKey = "";
        if (isFamily) {
            userKey += 'T';
        } else {
            userKey += 'F';
        }
        userKey += this.sex.toString();
        userKey += Integer.toString(this.age);
        return userKey;
    }

    //TODO getStayReports and addStayReport




    public String getName() {
        return super.getName();
    }

    public String getEmail() {
        return super.getEmail();
    }

    public String getUsername() {
        return super.getUsername();
    }

    public boolean getIsFamily() {
        return this.isFamily;
    }

    public int getAge() {
        return this.age;
    }

    public Sex getSex() {
        return this.sex;
    }
}

