import java.time.LocalDate;

/**
 * Interface for objects that can receive payment calculations
 * 
 * This interface defines the contract for classes that need to:
 * - Calculate payment amounts for a given period
 * - Calculate various types of deductions
 * - Handle payroll-related calculations
 * 
 * @author MotorPH Development Team
 * @version 1.0
 */
public interface Payable {
    /**
     * Calculates payment for a given period
     * 
     * @param startDate Start date of the period
     * @param endDate End date of the period
     * @return Calculated payment amount
     */
    double calculatePay(LocalDate startDate, LocalDate endDate);
    
    /**
     * Calculates basic government deductions
     * This typically includes SSS, PhilHealth, and Pag-IBIG
     * 
     * @param grossPay The gross pay amount
     * @return Total basic deductions
     */
    double calculateBasicDeduction(double grossPay);
    
    /**
     * Calculates tax deductions
     * This is based on the current tax brackets and rates
     * 
     * @param grossPay The gross pay amount
     * @return Tax deduction amount
     */
    double calculateTaxDeduction(double grossPay);
} 