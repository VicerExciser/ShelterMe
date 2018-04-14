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

    /**
     * A constructor for Account class
     * @param name the name of the account holder
     * @param username the username of the account holder
     * @param email the email of the account holder
     * @param password the password of the account holder
     * @param secQuest the security question of the account holder
     * @param secAns the security answer of the account holder
     */
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

    /**
     * A getter for name
     * @return the name of the account holder
     */
    public String getName() {
        return this.name;
    }

    /**
     * A getter for username
     * @return the username of the account holder
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * A getter for email
     * @return the email of the account holder
     */
    public String getEmail() { return this.email; }

    /**
     * A method to validate the password
     * @param pw the password of the account
     * @return true if password matches, false otherwise
     */
    public boolean validatePassword(int pw) {
        return pw == this.password;
    }

    /**
     * A method to check if the account is locked
     * @return true if account is locked, false otherwise
     */
    public boolean isAccountLocked() { return this.accountLocked; }

    /**
     * A setter for accountLocked
     * @param locked a boolean, true or false
     */
    public void setAccountLocked(boolean locked) { this.accountLocked = locked; }

    /**
     * A method that compares two accounts and checks whether
     * they match with email, name, username, or password.
     * @param other an account object
     * @return true if the accounts match, false otherwise
     */
    @Override
    public boolean equals(Object other) {
//        if (other == this) return true;
        return (other == this) || ((other instanceof Account)
                && ((Account) other).email.equalsIgnoreCase(this.email)
                && ((Account) other).name.equalsIgnoreCase(this.name)
                && ((Account) other).username.equalsIgnoreCase(this.username)
                && ((Account) other).password == this.password);
    }

    /**
     * Creates a hashcode a account holder's account's components
     * @return the hashcode as an integer
     */
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

    /**
     * A getter for the account type
     * @return the type of the account (admin, employee, user)
     */
    public Type getAccountType() {
        return accountType;
    }

    /**
     * A setter for account types
     * @param accountType the account type (admin, employee, user)
     */
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

        /**
         * @return Returns a string representation of the object
         */
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
