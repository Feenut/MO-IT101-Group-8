import java.time.LocalDate;

/**
 * Abstract base class for representing a person with basic information
 * 
 * This class provides common functionality for all person types in the system:
 * - Basic personal information storage
 * - Name handling
 * - Abstract methods that all person types must implement
 * 
 * @author MotorPH Development Team
 * @version 1.0
 */
public abstract class Person {
    /** Person's first name */
    protected String firstName;
    
    /** Person's last name */
    protected String lastName;
    
    /** Person's ID number */
    protected int id;
    
    /** Social Security System number */
    protected String sssNumber;
    
    /** PhilHealth insurance number */
    protected String philHealthNumber;
    
    /** Pag-IBIG Fund number */
    protected String pagIbigNumber;
    
    /** Tax Identification Number */
    protected String tin;

    /**
     * Constructs a new Person with the specified names
     * 
     * @param firstName The person's first name
     * @param lastName The person's last name
     */
    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Gets the person's first name
     * 
     * @return The first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets the person's last name
     * 
     * @return The last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the person's full name
     * 
     * @return The full name (first name + last name)
     */
    public String getName() {
        return firstName + " " + lastName;
    }

    /**
     * Gets the person's ID
     * Must be implemented by subclasses
     * 
     * @return The ID
     */
    public abstract String getId();

    /**
     * Gets the person's SSS number
     * 
     * @return The SSS number
     */
    public String getSssNumber() {
        return sssNumber;
    }

    /**
     * Gets the person's PhilHealth number
     * 
     * @return The PhilHealth number
     */
    public String getPhilHealthNumber() {
        return philHealthNumber;
    }

    /**
     * Gets the person's Pag-IBIG number
     * 
     * @return The Pag-IBIG number
     */
    public String getPagIbigNumber() {
        return pagIbigNumber;
    }

    /**
     * Gets the person's TIN
     * 
     * @return The TIN
     */
    public String getTin() {
        return tin;
    }
    
    /**
     * Calculates pay for a given period
     * Must be implemented by subclasses
     * 
     * @param startDate Start date of the period
     * @param endDate End date of the period
     * @return Calculated payment amount
     */
    public abstract double calculatePay(LocalDate startDate, LocalDate endDate);
} 