package edu.gatech.cs2340.shelterme.model;

import android.util.Log;

import java.util.List;
import java.util.Stack;

/**
 * Created by austincondict on 2/12/18.
 */

public class User extends Account {

//    Question secQuest;
//    String secAns;
    private boolean isFamily;
    private int age;
    private Sex sex;
    private boolean isVeteran;
    private boolean isOccupyingBed;
    private Shelter stayingAt;
//    private HashMap<Long, StayReport> stayReports;
//    private LongSparseArray<StayReport> stayReports;
    private List<StayReport> stayReports;


    // TODO: Collect isVeteran information from registration/edit profile, pass into contructor (set to False for default until then)
    public User(String name, String uname, String email, int pass, Sex sex, int age, boolean isFamily,
                Question secQ, String secA) {
        super(name, uname, email, pass, secQ, secA);
//        secQuest = secQ;
//        secAns = secA;
        this.isFamily = isFamily;
        this.age = age;
        this.sex = sex;
        this.isVeteran = false;
        this.setIsOccupyingBed(false);
//        this.setOccupyingBed(false);
        this.stayingAt = null;
//        this.stayReports = new HashMap<>();
        this.stayReports = new Stack<StayReport>();
        Log.e("USER_KEY", this.generateKey());
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
        StayReport cur;
        if (!this.stayReports.isEmpty()) {
//            cur = ((Stack<StayReport>)this.stayReports).peek();
            cur = (this.stayReports).get(stayReports.size() - 1);
            if (!cur.isActive()) {
                for (StayReport s : stayReports) {
                    if (s.isActive()) {
                        cur = s;
                    }
                }
            }
            return cur;
        }
        return null;
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
//        setOccupyingBed(tf);
    }

//    public void setOccupyingBed(boolean tf) {
//        this.isOccupyingBed = tf;
////        setIsOccupyingBed(tf);
//    }

    public void clearOccupiedBed() {
        setIsOccupyingBed(false);
//        setOccupyingBed(false);
        this.setStayingAt(null);
    }

    public Shelter getStayingAt() {
        return this.stayingAt;
    }

    public void setStayingAt(Shelter currentShelter) {
        this.stayingAt = currentShelter;
    }

    @Override
    public boolean equals(Object o) {
        User u;
        if (o instanceof User)
            u = (User)o;
        else return false;
        return this.getEmail().equals(u.getEmail())
                && this.getName().equals(u.getName())
                && this.getUsername().equals(u.getUsername())
                && this.password == u.password;

    }

//    public void setOccupyingBed(boolean occupyingBed) {
//        isOccupyingBed = occupyingBed;
//    }

//    public Bed getOccupiedBed() {
////        return occupiedBed;
//        for (Shelter s : Model.getShelterListPointer()) {
//            for (s.getBeds().keySet())
//        }
//    }

//    @Override
//    public int hashCode() {
//        hash = 31
//    }
}

