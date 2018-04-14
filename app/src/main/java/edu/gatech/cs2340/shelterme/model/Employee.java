package edu.gatech.cs2340.shelterme.model;


public class Employee extends Account {

    private Shelter placeOfWork;

    /**
     * A constructor for Employee class
     * @param name the name of the employee
     * @param uname the username of the employee
     * @param email the email of the employee
     * @param pass the password of the employee
     * @param work the work of the employee
     * @param secQ the security question of the employee
     * @param secA the security answer of the employee
     */
    public Employee(String name, String uname, String email, int pass, Shelter work,
                    Question secQ, String secA) {
        super(name, uname, email, pass, secQ, secA);
        setPlaceOfWork(work);
    }

    /**
     * A no-arg constructor of the Employee class
     */
    public Employee() {
        super("Working Employee", "shelterboi420", "employeeboi@shelters.com",
                "employee".hashCode(), Question.PET, "Spot");
        setPlaceOfWork(Model.findShelterByName("My Sister's House"));
        super.setAccountType(Type.EMP);
    }

    /**
     * A getter for placeOfWork
     * @return the place of work for the employee
     */
    public Shelter getPlaceOfWork() {
        return placeOfWork;
    }

    /**
     * A setter for place of work of the employee
     * @param placeOfWork the place of work for the employee
     */
    public void setPlaceOfWork(Shelter placeOfWork) {
        this.placeOfWork = placeOfWork;
    }

    /**
     * Checks if two employee accounts equal eachother based on place of work, email, name,
     * username, or password.
     * @param other an account object
     * @return true if the two employee accounts equal eachother, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Employee)
            return (((Employee) other).placeOfWork.equals(this.placeOfWork)
                    && super.equals(other));
        return (other instanceof Account) && super.equals(other);
    }

    /**
     * A method that created a hashcode
     * @return the hashcode as an integer
     */
    @Override
    public int hashCode() {
        return super.hashCode() + this.placeOfWork./*getShelterName().toLowerCase().*/hashCode();
    }
}