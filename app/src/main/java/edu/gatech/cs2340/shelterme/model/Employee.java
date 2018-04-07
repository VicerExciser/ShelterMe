package edu.gatech.cs2340.shelterme.model;


public class Employee extends Account {

    private Shelter placeOfWork;

    public Employee(String name, String uname, String email, int pass, Shelter work,
                    Question secQ, String secA) {
        super(name, uname, email, pass, secQ, secA);
        setPlaceOfWork(work);
    }

    public Employee() {
        super("Working Employee", "shelterboi420", "employeeboi@shelters.com",
                "employee".hashCode(), Question.PET, "Spot");
        setPlaceOfWork(Model.findShelterByName("My Sister's House"));
        super.setAccountType(Type.EMP);
    }

    public Shelter getPlaceOfWork() {
        return placeOfWork;
    }

    public void setPlaceOfWork(Shelter placeOfWork) {
        this.placeOfWork = placeOfWork;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Employee)
            return (((Employee) other).placeOfWork.equals(this.placeOfWork)
                    && super.equals(other));
        return (other instanceof Account) && super.equals(other);
    }

    @Override
    public int hashCode() {
        return super.hashCode() + this.placeOfWork./*getShelterName().toLowerCase().*/hashCode();
    }
}