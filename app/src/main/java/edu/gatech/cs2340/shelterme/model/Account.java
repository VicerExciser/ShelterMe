package edu.gatech.cs2340.shelterme.model;

/**
 * Represents an Account
 */
public abstract class Account {
    private String name;
    private String username;                // login name (in recognition of current popular trends,
                                            // this can be the email address)
    private int password;                   // hashed user password (for security)
    private boolean accountLocked;          // account state (locked or unlocked)
    private final String email;             // contact info (email address)
    private /*Type*/ String accountType;
    private Question secQuest;    // add eligibility for more than 1 security question in the future
    private String secAns;

    /**
     * A constructor for Account class
     *
     * @param name     the name of the account holder
     * @param username the username of the account holder
     * @param email    the email of the account holder
     * @param password the password of the account holder
     * @param secQuest the security question of the account holder
     * @param secAns   the security answer of the account holder
     */
    public Account(String name, String username, String email, int password,
            Question secQuest, String secAns) {
        this.setName(name);
        this.setUsername(username);
        this.password = password;
        this.email = email;
        this.secQuest = secQuest;
        this.secAns = secAns;
        accountLocked = false;
    }

    /**
     * Default constructor for Account.
     */
    public Account() {
        this("account test", "test", "account@test.com",
            "password".hashCode(), Question.CITY, "Atlanta");
    }

    /**
     * A getter for name
     *
     * @return the name of the account holder
     */
    @SuppressWarnings("unused")
    public String getName() {
        return this.name;
    }

    /**
     * A getter for username
     *
     * @return the username of the account holder
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * A getter for email
     *
     * @return the email of the account holder
     */
    public String getEmail() { return this.email; }

    /**
     * Gets password.
     *
     * @return the password
     */
    public int getPassword() { return this.password; }

    /**
     * Sets password.
     *
     * @param pass the pass
     */
    public void setPassword(int pass) { this.password = pass; }

    /**
     * A method to validate the password
     *
     * @param pw the password of the account
     * @return true if password matches, false otherwise
     */
    public boolean validatePassword(int pw) {
        return pw == this.password;
    }

    /**
     * A method to check if the account is locked
     *
     * @return true if account is locked, false otherwise
     */
    @SuppressWarnings("unused")
    public boolean isAccountLocked() { return this.accountLocked; }

    /**
     * Sets account as locked or unlocked; necessary setter for Firebase.
     *
     * @param locked the locked condition
     */
    public void setAccountLocked(boolean locked) {
        this.accountLocked = locked;
//        Model.updateUserAccountStatus(this, locked);
    }

    /**
     * Ban account.
     */
    public void banAccount() {
        setAccountLocked(true);
        Model.updateUserAccountStatus(this, true);
    }

    /**
     * Unlock account.
     */
    public void unlockAccount() {
        setAccountLocked(false);
        Model.updateUserAccountStatus(this, false);
    }

    @Override
    public boolean equals(Object other) {
//        if (other == this) return true;
        return (other == this) || ((other instanceof Account)
                && ((Account) other).email.equalsIgnoreCase(this.email)
                && ((Account) other).getName().equalsIgnoreCase(this.getName())
                && ((Account) other).getUsername().equalsIgnoreCase(this.getUsername())
                && (((Account) other).password == this.password));
    }

    @SuppressWarnings("ChainedMethodCall")
    @Override
    public int hashCode() {
        int result = 17;
        result = (31 * result) + this.getUsername().toLowerCase().hashCode();
        result = (31 * result) + this.email.toLowerCase().hashCode();
        result = (31 * result) + this.getName().toLowerCase().hashCode();
        result = (31 * result) + this.password;
//        result = (31 * result) + this.accountType.toString().toLowerCase().hashCode();
        return result;
    }


    // Work to eliminate any sort of subclass type checking

    /**
     * A setter for account types
     *
     * @param accountType the account type (admin, employee, user)
     */
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    /**
     * A getter for the account type
     *
     * @return the type of the account (admin, employee, user)
     */
    public /*Type*/ String getAccountType() { return this.accountType; }

    /**
     * Gets sec quest.
     *
     * @return the sec quest
     */
    @SuppressWarnings("unused")
    public Question getSecQuest() {
        return secQuest;
    }

    /**
     * Sets sec quest.
     *
     * @param secQuest the sec quest
     */
    @SuppressWarnings("unused")
    public void setSecQuest(Question secQuest) {
        this.secQuest = secQuest;
    }

    /**
     * Gets sec ans.
     *
     * @return the sec ans
     */
    @SuppressWarnings("unused")
    public String getSecAns() {
        return secAns;
    }

    /**
     * Sets sec ans.
     *
     * @param secAns the sec ans
     */
    @SuppressWarnings("unused")
    public void setSecAns(String secAns) {
        this.secAns = secAns;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }


    /**
     * The enum Sex.
     */
    public enum Sex {
        /**
         * Male sex.
         */
        MALE ("M"),
        /**
         * Female sex.
         */
        FEMALE ("F"),
        /**
         * Other sex.
         */
        OTHER ("O");

        private final String _code;

        Sex(String code) {
            _code = code;
        }

        public String toString() { return _code; }
    }

    /**
     * The enum Question.
     */
    public enum Question {
        /**
         * The Mom.
         */
// Could make Question a key-value pair (Q & A)
        MOM ("What was your mother's maiden name?"),
        /**
         * The City.
         */
        CITY ("What city were you born in?"),
        /**
         * The Pet.
         */
        PET ("What was the name of your first pet?"),
        /**
         * The School.
         */
        SCHOOL ("What was the name of your first school?");

        private final String _code;

        Question(String code) {
            _code = code;
        }

        public String toString() { return _code; }
    }

    /**
     * The enum Type.
     */
    public enum Type {
        /**
         * User type.
         */
        USER ("User"),
        /**
         * The Emp.
         */
        EMP ("Shelter Employee"),
        /**
         * Admin type.
         */
        ADMIN ("Admin");

        private final String _code;

        Type(String code) {
            _code = code;
        }

        public String toString() { return _code; }
    }

}
