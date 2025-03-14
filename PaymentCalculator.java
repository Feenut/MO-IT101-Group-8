import java.time.LocalDate;

/**
 * Interface for objects that perform payment calculations
 * 
 * This interface defines the contract for classes that need to:
 * - Calculate payment amounts for a given period
 * - Calculate deductions from gross pay
 * - Calculate net pay after deductions
 * 
 * @author MotorPH Development Team
 * @version 1.0
 */
public interface PaymentCalculator {
    /**
     * Calculates payment for a given period
     * 
     * @param startDate Start date of the period
     * @param endDate End date of the period
     * @return Calculated payment amount
     */
    double calculatePay(LocalDate startDate, LocalDate endDate);
    
    /**
     * Calculates total deductions from gross pay
     * 
     * @param grossPay The gross pay amount
     * @return Total deduction amount
     */
    double calculateDeduction(double grossPay);
    
    /**
     * Calculates net pay after deductions
     * 
     * @param grossPay The gross pay amount
     * @return Net pay amount
     */
    double calculateNetPay(double grossPay);
} 