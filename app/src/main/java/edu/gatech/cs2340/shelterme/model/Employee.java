package edu.gatech.cs2340.shelterme.model;


public class Employee extends Account {

    Shelter placeOfWork;

    public Employee(String name, String uname, String email, int pass, Shelter work,
                    Question secQ, String secA) {
        super(name, uname, email, pass, secQ, secA);
        placeOfWork = work;
    }

    public Employee() {
        super("Working Employee", "shelterboi420", "employeeboi@shelters.com",
                "employee".hashCode(), Question.PET, "Spot");
        placeOfWork = Model.findShelterByName("My Sister's House");
    }

//    public String getID() {
//        return super.getID();
//    }
}
