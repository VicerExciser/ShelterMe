package edu.gatech.cs2340.shelterme.model;


public abstract class Account {
    private final String name;
    private final String username;          // login name (in recognition of current popular trends,
                                            // this can be the email address)
    private final int password;             // hashed user password
    private final boolean accountLocked;          // account state (locked or unlocked)
    private final String email;             // contact info (email address)
    private Type accountType;
    private Question secQuest;
    private String secAns;

    Account(String name, String username, String email, int password,
            Question secQuest, String secAns) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.secQuest = secQuest;
        this.secAns = secAns;
        accountLocked = false;
    }

    String getName() {
        return this.name;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() { return this.email; }

    public boolean validatePassword(int pw) {
        return pw == this.password;
    }

    public boolean isAccountLocked() { return this.accountLocked; }

    // --Commented out by Inspection (4/13/2018 6:17 PM):
    // public void setAccountLocked(boolean locked) { this.accountLocked = locked; }

    @Override
    public boolean equals(Object other) {
//        if (other == this) return true;
        return (other == this) || ((other instanceof Account)
                && ((Account) other).email.equalsIgnoreCase(this.email)
                && ((Account) other).name.equalsIgnoreCase(this.name)
                && ((Account) other).username.equalsIgnoreCase(this.username)
                && (((Account) other).password == this.password));
    }

    @SuppressWarnings("ChainedMethodCall")
    @Override
    public int hashCode() {
        int result = 17;
        result = (31 * result) + this.username.toLowerCase().hashCode();
        result = (31 * result) + this.email.toLowerCase().hashCode();
        result = (31 * result) + this.name.toLowerCase().hashCode();
        result = (31 * result) + this.password;
        result = (31 * result) + this.accountType.toString().toLowerCase().hashCode();
        return result;
    }

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public Type getAccountType() {
//        return accountType;
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)

    void setAccountType(Type accountType) {
        this.accountType = accountType;
    }
    public Type getAccountType() { return this.accountType; }

    public Question getSecQuest() {
        return secQuest;
    }

    public void setSecQuest(Question secQuest) {
        this.secQuest = secQuest;
    }

    public String getSecAns() {
        return secAns;
    }

    public void setSecAns(String secAns) {
        this.secAns = secAns;
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
