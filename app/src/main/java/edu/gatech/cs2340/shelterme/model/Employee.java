package edu.gatech.cs2340.shelterme.model;


/**
 * The type Employee.
 */
public class Employee extends Account {

    private String placeOfWork;

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
    public Employee(String name, String uname, String email, int pass, String work,
                    Question secQ, String secA) {
        super(name, uname, email, pass, secQ, secA);
        setPlaceOfWork(work);
        super.setAccountType(Type.EMP.toString());
    }

    /**
     * Instantiates a new Employee.
     */
    public Employee() {
        super("Working Employee", "shelterboi420", "employeeboi@shelters.com",
                "employee".hashCode(), Question.PET, "Spot");
        setPlaceOfWork("My Sister's House");
        super.setAccountType(Type.EMP.toString());
    }

    /**
     * Gets place of work.
     *
     * @return the place of work
     */
    public String getPlaceOfWork() {
        return placeOfWork;
    }

    private void setPlaceOfWork(String placeOfWork) {
        this.placeOfWork = placeOfWork;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Employee) {
            return (super.equals(other) && ((Employee) other).placeOfWork.equals(this.placeOfWork));
        }
        return (other instanceof Account) && super.equals(other);
    }

    @Override
    public int hashCode() {
        return super.hashCode() + this.placeOfWork.hashCode();
    }
}