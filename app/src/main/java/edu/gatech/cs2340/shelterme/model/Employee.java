package edu.gatech.cs2340.shelterme.model;


/**
 * The type Employee.
 */
public class Employee extends Account {

    private Shelter placeOfWork;

    /**
     * Instantiates a new Employee.
     *
     * @param name  the name
     * @param uname the uname
     * @param email the email
     * @param pass  the pass
     * @param work  the work
     * @param secQ  the sec q
     * @param secA  the sec a
     */
    public Employee(String name, String uname, String email, int pass, Shelter work,
                    Question secQ, String secA) {
        super(name, uname, email, pass, secQ, secA);
        setPlaceOfWork(work);
    }

    /**
     * Instantiates a new Employee.
     */
    public Employee() {
        super("Working Employee", "shelterboi420", "employeeboi@shelters.com",
                "employee".hashCode(), Question.PET, "Spot");
        setPlaceOfWork(Model.findShelterByName("My Sister's House"));
        super.setAccountType(Type.EMP);
    }

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    public Shelter getPlaceOfWork() {
//        return placeOfWork;
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)

    private void setPlaceOfWork(Shelter placeOfWork) {
        this.placeOfWork = placeOfWork;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Employee) {
            return (((Employee) other).placeOfWork.equals(this.placeOfWork)
                    && super.equals(other));
        }
        return (other instanceof Account) && super.equals(other);
    }

    @Override
    public int hashCode() {
        return super.hashCode() + this.placeOfWork./*getShelterName().toLowerCase().*/hashCode();
    }
}