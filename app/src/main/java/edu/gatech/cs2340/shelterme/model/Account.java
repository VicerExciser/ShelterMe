package edu.gatech.cs2340.shelterme.model;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by austincondict on 2/12/18.
 */

public abstract class Account {
    String name;
    String username;        // login name (in recognition of current popular trends, this can be the email address)
    int password;       // hashed user password
    boolean accountLocked;  // account state (locked or unlocked)
    String email;           // contact info (email address)
//    int age;
//    Sex sex;
    Question secQuest;
    String secAns;

    public Account(String name, String username, String email, int password,
                   Question secQuest, String secAns) {
        this.username = username;
        this.password = password;
        this.email = email;
//        this.sex = sex;
//        this.age = age;
        this.secQuest = secQuest;
        this.secAns = secAns;
        accountLocked = false;
    }

    public String getUsername() {
        return this.username;
    }

    public boolean validatePassword(int pw) {
        return pw == this.password;
    }

    public enum Sex {
        MALE ("M"),
        FEMALE ("F");

        private final String _code;

        Sex(String code) {
            _code = code;
        }

        public String toString() { return _code; }
    }

    public enum Question {
        // Could make Question a key-value pair (Q & A)
        MOM ("What was your mother's maiden name?"),
        CITY ("What city were you born in?"),
        PET ("What was the name of your first pet?"),
        SCHOOL ("What was the name of your first school?");

        private final String _code;

        Question(String code) {
            _code = code;
        }

        public String toString() { return _code; }
    }

    public enum Type {
        USER ("User"),
        EMP ("Shelter Employee"),
        ADMIN ("Admin");

        private final String _code;

        Type(String code) {
            _code = code;
        }

        public String toString() { return _code; }
    }

}
