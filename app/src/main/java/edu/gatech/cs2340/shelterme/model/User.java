package edu.gatech.cs2340.shelterme.model;
import android.util.Log;
import java.util.List;
import java.util.Stack;

/**
 * Created by austincondict on 2/12/18.
 */

public class User extends Account {

    public boolean isFamily;
    private int age;
    private Sex sex;
    public boolean isVeteran;
    public boolean isOccupyingBed;
    private List<StayReport> stayReports;


    /**
     * A constructor for User class
     * @param name the name of the user
     * @param uname the username of the user
     * @param email the email of the user
     * @param pass the password of the user
     * @param sex the sex of the user
     * @param age the age of the user
     * @param isFamily true or false, depending on whether this user is a family account
     * @param secQ the security question of the user
     * @param secA the security answer of the user
     */
    // TODO: Collect isVeteran information from registration/edit profile, pass into constructor (set to False for default until then)
    public User(String name, String uname, String email, int pass, Sex sex, int age, boolean isFamily,
                Question secQ, String secA) {
        super(name, uname, email, pass, secQ, secA);
        this.isFamily = isFamily;
        this.age = age;
        this.sex = sex;
        this.isVeteran = false;
        this.setIsOccupyingBed(false);
        this.stayReports = new Stack<>();
//        Log.e("USER_KEY", this.generateKey());
        super.setAccountType(Type.USER);
    }


    /**
     * A no-arg constructor for User class
     */
    public User() {
        this("user steve", "user", "user@gmail.com", "pass".hashCode(), Sex.MALE, 25, false,
                Question.PET, "Spot");
    }


    /**
     * A method that generates a key containing information about the account holder
     * @return a string represetning a key
     */
    public String generateKey() {   // Ex. key:  'FM25F'  <-- not family account, male, 25 yrs old, not a veteran
        String userKey = "";
        userKey += this.isFamily ? 'T' : 'F';
        userKey += this.sex.toString();
        userKey += Integer.toString(this.age);
        userKey += this.isVeteran ? 'T' : 'F';
        return userKey;
    }

    /**
     * A getter for stayreports
     * @return a list of stay reports
     */
    public List<StayReport> getStayReports() {
        return this.stayReports;
    }

    /**
     * A method that adds stay reports
     * @param stay an object of type StayReport
     */
    public void addStayReport(StayReport stay) {
//        ((Stack<StayReport>)this.stayReports).push(stay);
        ((List<StayReport>) this.stayReports).add(stay);
    }

    /**
     * A getter of current stay reports
     * @return an object of type StayReport
     */
    public StayReport getCurrentStayReport () {
        StayReport cur = null;
        if (!this.stayReports.isEmpty()) {
//            cur = ((Stack<StayReport>)this.stayReports).peek();
            cur = (this.stayReports).get(stayReports.size() - 1);
            if (!cur.isActive()) {
                for (StayReport s : stayReports) {
                    if (s.isActive()) {
                        cur = s;
                        break;
                    }
                }
            }
        }
        return cur;
    }

    /**
     * A getter for name
     * @return the name of the user
     */
    public String getName() {
        return super.getName();
    }

    /**
     * A getter for email
     * @return the email of the user
     */
    public String getEmail() {
        return super.getEmail();
    }

    /**
     * A getter for username
     * @return the username of the user
     */
    public String getUsername() {
        return super.getUsername();
    }

    /**
     * A getter for isFamily
     * @return the family status of the user
     */
    public boolean getIsFamily() {
        return this.isFamily;
    }

    /**
     * A getter for age
     * @return the age of the user
     */
    public int getAge() {
        return this.age;
    }

    /**
     * A getter for sex
     * @return the sex of the user
     */
    public Sex getSex() {
        return this.sex;
    }

    /**
     * A getter for isVeteran
     * @return the veteran status of the user
     */
    public boolean getIsVeteran() {
        return this.isVeteran;
    }

    /**
     * A getter for bed's occupied
     * @return true if bed is occupied, false otherwise
     */
    public boolean isOccupyingBed() {
        return this.isOccupyingBed;
    }

    /**
     * A setter for a bed's occupation status
     * @param tf true or false, depending on whether the bed is occupied or not
     */
    public void setIsOccupyingBed(boolean tf) {
        this.isOccupyingBed = tf;
    }

    /**
     * A method that clears all beds that were previously occupied
     */
    public void clearOccupiedBed() {
        setIsOccupyingBed(false);
    }

    /**
     * A method that clears the stay report history
     */
    public void clearStayReportHistory() {
        this.stayReports.clear();
        if (stayReports == null)
            this.stayReports = new Stack<>();
    }

    /**
     *  Checks if two accounts equal eachother based on age, sex, email, name, username, or password
     * @param other a user object
     * @return true if two accounts equal eachother, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof User)
            return (((User) other).age == this.age
                    && ((User) other).sex == this.sex
                    && super.equals(other));
        return (other instanceof Account) && super.equals(other);
    }

    /**
     * A method that created a hashcode
     * @return the hashcode as an integer
     */
    @Override
    public int hashCode() {
        int result = 17;
        result += super.hashCode();
        result = 31 * result + this.age;
        result = 31 * result + this.sex.toString().toLowerCase().hashCode();
        result += 31 * (this.isOccupyingBed ? 1 : 0);
        result += 31 * (this.isVeteran ? 1 : 0);
        result += 31 * (this.isFamily ? 1 : 0);
        return result;
    }

}

