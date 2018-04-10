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


    // TODO: Collect isVeteran information from registration/edit profile, pass into constructor (set to False for default until then)
    public User(String name, String uname, String email, int pass, Sex sex, int age, boolean isFamily,
                Question secQ, String secA) {
        super(name, uname, email, pass, secQ, secA);
        this.isFamily = isFamily;
        this.age = age;
        this.sex = sex;
        this.isVeteran = false;
        this.setIsOccupyingBed(false);
        this.stayReports = new Stack<StayReport>();
//        Log.e("USER_KEY", this.generateKey());
        super.setAccountType(Type.USER);
    }


    public User() {
        this("user steve", "user", "user@gmail.com", "pass".hashCode(), Sex.MALE, 25, false,
                Question.PET, "Spot");
    }


    public String generateKey() {   // Ex. key:  'FM25F'  <-- not family account, male, 25 yrs old, not a veteran
        String userKey = "";
        userKey += this.isFamily ? 'T' : 'F';
        userKey += this.sex.toString();
        userKey += Integer.toString(this.age);
        userKey += this.isVeteran ? 'T' : 'F';
        return userKey;
    }

    public List<StayReport> getStayReports() {
        return this.stayReports;
    }

    public void addStayReport(StayReport stay) {
//        ((Stack<StayReport>)this.stayReports).push(stay);
        ((List<StayReport>) this.stayReports).add(stay);
    }

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

    public boolean getIsVeteran() {
        return this.isVeteran;
    }

    public boolean isOccupyingBed() {
        return this.isOccupyingBed;
    }

    public void setIsOccupyingBed(boolean tf) {
        this.isOccupyingBed = tf;
    }

    public void clearOccupiedBed() {
        setIsOccupyingBed(false);
    }

    public void clearStayReportHistory() {
        this.stayReports.clear();
        if (stayReports == null)
            this.stayReports = new Stack<StayReport>();
    }


    @Override
    public boolean equals(Object other) {
        if (other instanceof User)
            return (((User) other).age == this.age
                    && ((User) other).sex == this.sex
                    && super.equals(other));
        return (other instanceof Account) && super.equals(other);
    }

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

