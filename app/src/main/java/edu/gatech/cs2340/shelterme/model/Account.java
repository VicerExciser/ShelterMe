package edu.gatech.cs2340.shelterme.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;


public abstract class Account {
    String name;
    String username;        // login name (in recognition of current popular trends, this can be the email address)
    int password;       // hashed user password
    boolean accountLocked;  // account state (locked or unlocked)
    String email;           // contact info (email address)
    Question secQuest;
    String secAns;
    private Type accountType;

    public Account(String name, String username, String email, int password,
                   Question secQuest, String secAns) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.secQuest = secQuest;
        this.secAns = secAns;
        accountLocked = false;
    }

    public String getName() {
        return this.name;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() { return this.email; }

    public boolean validatePassword(int pw) {
        return pw == this.password;
    }



    @Override
    public boolean equals(Object other) {
//        if (other == this) return true;
        return (other == this) || ((other instanceof Account)
                && ((Account) other).email.equalsIgnoreCase(this.email)
                && ((Account) other).name.equalsIgnoreCase(this.name)
                && ((Account) other).username.equalsIgnoreCase(this.username)
                && ((Account) other).password == this.password);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.username.toLowerCase().hashCode();
        result = 31 * result + this.email.toLowerCase().hashCode();
        result = 31 * result + this.name.toLowerCase().hashCode();
        result = 31 * result + this.password;
        result = 31 * result + this.accountType.toString().toLowerCase().hashCode();
        return result;
    }

    public Type getAccountType() {
        return accountType;
    }

    public void setAccountType(Type accountType) {
        this.accountType = accountType;
    }


    public enum Sex {
        MALE ("M"),
        FEMALE ("F"),
        OTHER ("O");

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
