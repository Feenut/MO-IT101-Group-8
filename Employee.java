import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ArrayList;

/**
 * Employee class represents an employee in the MotorPH Payroll System.
 * 
 * This class extends Person and implements Payable interface to handle:
 * - Employee personal and professional information
 * - Payroll calculations including salary and deductions
 * - Time tracking and attendance records
 * - Data serialization for storage
 * 
 * @author MotorPH Development Team
 * @version 1.0
 */
public class Employee extends Person implements Payable {
    // Personal Information
    /** Unique employee identifier */
    private final String employeeId;
    /** Employee's job position */
    private final String position;
    /** Employee's immediate supervisor */
    private final String supervisor;
    /** Employee's department */
    private String department;
    /** Employee's birthday */
    private String birthday;
    /** Employee's address */
    private String address;
    /** Employee's phone number */
    private String phoneNumber;
    /** Employee's employment status */
    private String status;

    // Government IDs
    /** Social Security System number */
    private final String sssNumber;
    /** PhilHealth insurance number */
    private final String philHealthNumber;
    /** Pag-IBIG Fund number */
    private final String pagIbigNumber;
    /** Tax Identification Number */
    private final String tin;

    // Payroll Information
    /** Hourly rate for salary computation */
    private final double hourlyRate;
    /** Basic monthly salary */
    private double basicSalary;
    /** Rice subsidy amount */
    private double riceSubsidy;
    /** Phone allowance amount */
    private double phoneAllowance;
    /** Clothing allowance amount */
    private double clothingAllowance;
    /** Gross semi-monthly rate */
    private double grossSemiMonthlyRate;
    /** Record of hours worked by date */
    private final Map<LocalDate, Double> hoursWorked;
    
    // Attendance Records
    /** Stores daily attendance records with login and logout times */
    private final Map<LocalDate, AttendanceRecord> attendanceRecords;

    /**
     * Constructs a new Employee with the specified details.
     * 
     * @param id Employee ID
     * @param firstName First name
     * @param lastName Last name
     * @param position Job position
     * @param supervisor Immediate supervisor
     * @param sssNumber SSS number
     * @param philHealthNumber PhilHealth number
     * @param pagIbigNumber Pag-IBIG number
     * @param tin Tax Identification Number
     * @param hourlyRate Hourly rate for salary computation
     */
    public Employee(String id, String firstName, String lastName, String position, String supervisor,
                   String sssNumber, String philHealthNumber, String pagIbigNumber, String tin, double hourlyRate) {
        super(firstName, lastName);
        this.employeeId = id;
        this.position = position;
        this.supervisor = supervisor;
        this.sssNumber = sssNumber;
        this.philHealthNumber = philHealthNumber;
        this.pagIbigNumber = pagIbigNumber;
        this.tin = tin;
        this.hourlyRate = hourlyRate;
        this.hoursWorked = new HashMap<>();
        this.attendanceRecords = new HashMap<>();
        
        // Initialize compensation components with default values
        this.basicSalary = hourlyRate * 160; // Assuming 160 hours per month
        this.riceSubsidy = 0.0;
        this.phoneAllowance = 0.0;
        this.clothingAllowance = 0.0;
        this.grossSemiMonthlyRate = this.basicSalary / 2;
    }

    /**
     * Gets the employee's ID
     * 
     * @return The employee ID
     */
    @Override
    public String getId() {
        return employeeId;
    }

    /**
     * Gets the employee's position
     * 
     * @return The job position
     */
    public String getPosition() {
        return position;
    }

    /**
     * Gets the employee's supervisor
     * 
     * @return The supervisor's name
     */
    public String getSupervisor() {
        return supervisor;
    }

    /**
     * Gets the employee's department
     * 
     * @return The department name
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Gets the employee's hourly rate
     * 
     * @return The hourly rate
     */
    public double getHourlyRate() {
        return hourlyRate;
    }

    /**
     * Gets a copy of the employee's hours worked records
     * 
     * @return Map of dates to hours worked
     */
    public Map<LocalDate, Double> getHoursWorked() {
        return new HashMap<>(hoursWorked);
    }

    /**
     * Sets the employee's department
     * 
     * @param department The department name
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * Records worked hours for a specific date
     * Adds to existing hours if the date already has an entry
     * 
     * @param date The date of work
     * @param hours Number of hours worked
     */
    public void addHoursWorked(LocalDate date, double hours) {
        hoursWorked.put(date, hoursWorked.getOrDefault(date, 0.0) + hours);
    }

    /**
     * Calculates total pay based on hours worked and hourly rate
     * Uses actual login and logout times from attendance records to calculate hours worked
     * 
     * @param startDate Start date of the period
     * @param endDate End date of the period
     * @return Total pay for the period
     */
    public double calculatePay(LocalDate startDate, LocalDate endDate) {
        System.out.println("Calculating pay for " + this.getName() + " (ID: " + this.getId() + ")");
        System.out.println("Period: " + startDate + " to " + endDate);
        System.out.println("Hourly rate: PHP " + this.hourlyRate);
        
        // Check if we have attendance records
        if (attendanceRecords.isEmpty()) {
            System.out.println("WARNING: No attendance records found for this employee!");
            System.out.println("Using fallback calculation method...");
            return calculatePayWithoutAttendance(startDate, endDate);
        }
        
        // Filter attendance records for the specified period and sort by date
        List<Map.Entry<LocalDate, AttendanceRecord>> periodRecords = attendanceRecords.entrySet().stream()
            .filter(entry -> !entry.getKey().isBefore(startDate) && !entry.getKey().isAfter(endDate))
            .sorted(Map.Entry.comparingByKey())
            .collect(java.util.stream.Collectors.toList());
        
        if (periodRecords.isEmpty()) {
            System.out.println("WARNING: No attendance records found for the specified period!");
            System.out.println("Using fallback calculation method...");
            return calculatePayWithoutAttendance(startDate, endDate);
        }
        
        System.out.println("Found " + periodRecords.size() + " attendance records in the specified period");
        
        // Calculate total hours worked and pay for each day
        double totalHoursWorked = 0.0;
        double totalBasePay = 0.0;
        
        for (Map.Entry<LocalDate, AttendanceRecord> entry : periodRecords) {
            LocalDate date = entry.getKey();
            AttendanceRecord record = entry.getValue();
            double hoursForDay = record.getHoursWorked();
            
            if (hoursForDay > 0) {
                double payForDay = hoursForDay * hourlyRate;
                totalHoursWorked += hoursForDay;
                totalBasePay += payForDay;
                
                System.out.printf("  %s: %.2f hours × PHP %.2f = PHP %.2f%n", 
                    date, hoursForDay, hourlyRate, payForDay);
            } else {
                System.out.println("  " + date + ": No valid hours recorded (login/logout may be missing)");
            }
        }
        
        System.out.println("Total hours worked: " + totalHoursWorked);
        System.out.println("Total base pay: PHP " + totalBasePay);
        
        // Add allowances if employee was present at least one day
        double totalPay = totalBasePay;
        if (totalHoursWorked > 0) {
            // Add allowances (these will be prorated in the Payroll class)
            System.out.println("Employee was present during the period - eligible for allowances");
        }
        
        return totalBasePay;
    }
    
    /**
     * Fallback method to calculate pay without attendance records
     * Used when no attendance records are available
     * 
     * @param startDate Start date of the period
     * @param endDate End date of the period
     * @return Estimated pay for the period
     */
    private double calculatePayWithoutAttendance(LocalDate startDate, LocalDate endDate) {
        // Calculate number of workdays in the period (excluding weekends)
        long days = 0;
        LocalDate current = startDate;
        while (!current.isAfter(endDate)) {
            // Skip weekends (Saturday and Sunday)
            if (current.getDayOfWeek().getValue() < 6) {
                days++;
            }
            current = current.plusDays(1);
        }
        
        // Assume 8 hours per workday
        double estimatedHours = days * 8.0;
        double estimatedPay = estimatedHours * hourlyRate;
        
        System.out.println("WARNING: Using estimated pay calculation!");
        System.out.println("Workdays in period: " + days);
        System.out.println("Estimated hours (8 per workday): " + estimatedHours);
        System.out.println("Estimated pay: PHP " + estimatedPay);
        
        return estimatedPay;
    }

    /**
     * Calculates SSS deduction based on gross pay
     * Uses the latest SSS contribution table (2023)
     * 
     * @param grossPay The gross pay amount
     * @return SSS deduction amount
     */
    public double calculateSSSDeduction(double grossPay) {
        // Latest SSS contribution table (2023)
        if (grossPay <= 3250) return 135.00;
        if (grossPay <= 4250) return 157.50;
        if (grossPay <= 5250) return 180.00;
        if (grossPay <= 6250) return 202.50;
        if (grossPay <= 7250) return 225.00;
        if (grossPay <= 8250) return 247.50;
        if (grossPay <= 9250) return 270.00;
        if (grossPay <= 10250) return 292.50;
        if (grossPay <= 11250) return 315.00;
        if (grossPay <= 12250) return 337.50;
        if (grossPay <= 13250) return 360.00;
        if (grossPay <= 14250) return 382.50;
        if (grossPay <= 15250) return 405.00;
        if (grossPay <= 16250) return 427.50;
        if (grossPay <= 17250) return 450.00;
        if (grossPay <= 18250) return 472.50;
        if (grossPay <= 19250) return 495.00;
        if (grossPay <= 20250) return 517.50;
        if (grossPay <= 21250) return 540.00;
        if (grossPay <= 22250) return 562.50;
        if (grossPay <= 23250) return 585.00;
        if (grossPay <= 24250) return 607.50;
        return 630.00; // Maximum contribution
    }

    /**
     * Calculates PhilHealth deduction based on gross pay
     * Uses the latest PhilHealth contribution rate (2023)
     * 
     * @param grossPay The gross pay amount
     * @return PhilHealth deduction amount
     */
    public double calculatePhilHealthDeduction(double grossPay) {
        // Latest PhilHealth contribution rate (2023) - 4%
        return grossPay * 0.04;
    }

    /**
     * Calculates Pag-IBIG deduction based on gross pay
     * Uses the latest Pag-IBIG contribution rate (2023)
     * 
     * @param grossPay The gross pay amount
     * @return Pag-IBIG deduction amount
     */
    public double calculatePagIBIGDeduction(double grossPay) {
        // Latest Pag-IBIG contribution rate (2023) - 2% with 100 peso cap
        return Math.min(grossPay * 0.02, 100);
    }

    /**
     * Calculates tax deduction based on gross pay
     * Uses simplified tax calculation (2023), assumes no exemptions
     * 
     * @param grossPay The gross pay amount
     * @return Tax deduction amount
     */
    public double calculateTaxDeduction(double grossPay) {
        // Simplified tax calculation (2023), assumes no exemptions
        if (grossPay <= 20833) return 0; // No tax for income up to 20,833
        if (grossPay <= 33333) return (grossPay - 20833) * 0.20; // 20% of excess over 20,833
        if (grossPay <= 66667) return 2500 + (grossPay - 33333) * 0.25; // 2,500 + 25% of excess over 33,333
        if (grossPay <= 166667) return 10833.33 + (grossPay - 66667) * 0.30; // 10,833.33 + 30% of excess over 66,667
        if (grossPay <= 666667) return 40833.33 + (grossPay - 166667) * 0.32; // 40,833.33 + 32% of excess over 166,667
        return 200833.33 + (grossPay - 666667) * 0.35; // 200,833.33 + 35% of excess over 666,667
    }

    /**
     * Calculates total basic deductions (SSS, PhilHealth, Pag-IBIG)
     * 
     * @param grossPay The gross pay amount
     * @return Total basic deductions
     */
    public double calculateBasicDeduction(double grossPay) {
        return calculateSSSDeduction(grossPay) +
               calculatePhilHealthDeduction(grossPay) +
               calculatePagIBIGDeduction(grossPay);
    }

    /**
     * Calculates weekly salary based on hours worked
     * Uses actual login and logout times from attendance records
     * 
     * @param startDate Start date of the period
     * @param endDate End date of the period
     * @return Weekly salary amount
     */
    public double calculateWeeklySalary(LocalDate startDate, LocalDate endDate) {
        double totalPay = calculatePay(startDate, endDate);
        long days = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
        return totalPay / Math.ceil(days / 7.0);
    }

    /**
     * Returns a string representation of the employee
     * 
     * @return String with employee ID, name, and position
     */
    @Override
    public String toString() {
        return String.format("%s - %s (%s)", employeeId, getName(), position);
    }

    /**
     * Converts employee data to CSV format for storage
     * 
     * @return CSV string representation of the employee
     */
    public String toCSV() {
        StringBuilder csv = new StringBuilder();
        csv.append(employeeId).append(",")
           .append(getFirstName()).append(",")
           .append(getLastName()).append(",")
           .append(position).append(",")
           .append(supervisor).append(",")
           .append(sssNumber).append(",")
           .append(philHealthNumber).append(",")
           .append(pagIbigNumber).append(",")
           .append(tin).append(",")
           .append(hourlyRate);
        
        // Add hours worked records
        for (Map.Entry<LocalDate, Double> entry : hoursWorked.entrySet()) {
            csv.append(",").append(entry.getKey()).append(",").append(entry.getValue());
        }
        return csv.toString();
    }

    /**
     * Creates an Employee object from CSV data
     * 
     * @param csv CSV string containing employee data
     * @return New Employee object
     * @throws IllegalArgumentException if CSV data is invalid
     */
    public static Employee fromCSV(String csv) {
        String[] parts = csv.split(",");
        if (parts.length < 9) {
            throw new IllegalArgumentException("Invalid CSV data: Expected at least 9 fields, got " + parts.length);
        }
        
        try {
            // Parse basic employee information
            String id = parts[0];
            String firstName = parts[1];
            String lastName = parts[2];
            String position = parts[3];
            String supervisor = parts[4];
            String sssNumber = parts[5];
            String philHealthNumber = parts[6];
            String pagIbigNumber = parts[7];
            String tin = parts[8];
            double hourlyRate = Double.parseDouble(parts[9]);
            
            // Create employee
            Employee employee = new Employee(id, firstName, lastName, position, supervisor, 
                                          sssNumber, philHealthNumber, pagIbigNumber, tin, hourlyRate);
            
            // Process hours worked entries (if any)
            for (int i = 10; i < parts.length - 1; i += 2) {
                try {
                    LocalDate date = LocalDate.parse(parts[i]);
                    double hours = Double.parseDouble(parts[i + 1]);
                    employee.addHoursWorked(date, hours);
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Invalid date format at position " + i + ": " + parts[i]);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid hours format at position " + (i+1) + ": " + parts[i+1]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new IllegalArgumentException("Missing hours value for date at position " + i);
                }
            }
            
            return employee;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format in CSV: " + e.getMessage());
        }
    }

    /**
     * Gets the employee's SSS number
     * 
     * @return The SSS number
     */
    public String getSssNumber() {
        return sssNumber;
    }

    /**
     * Gets the employee's PhilHealth number
     * 
     * @return The PhilHealth number
     */
    public String getPhilHealthNumber() {
        return philHealthNumber;
    }

    /**
     * Gets the employee's Pag-IBIG number
     * 
     * @return The Pag-IBIG number
     */
    public String getPagIbigNumber() {
        return pagIbigNumber;
    }

    /**
     * Gets the employee's TIN
     * 
     * @return The TIN
     */
    public String getTin() {
        return tin;
    }

    /**
     * Sets the employee's birthday
     * 
     * @param birthday The birthday in MM/DD/YYYY format
     */
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    
    /**
     * Sets the employee's address
     * 
     * @param address The employee's address
     */
    public void setAddress(String address) {
        this.address = address;
    }
    
    /**
     * Sets the employee's phone number
     * 
     * @param phoneNumber The employee's phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    /**
     * Sets the employee's employment status
     * 
     * @param status The employment status (e.g., Regular, Probationary)
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    /**
     * Gets the employee's birthday
     * 
     * @return The birthday in MM/DD/YYYY format
     */
    public String getBirthday() {
        return birthday;
    }
    
    /**
     * Gets the employee's address
     * 
     * @return The employee's address
     */
    public String getAddress() {
        return address;
    }
    
    /**
     * Gets the employee's phone number
     * 
     * @return The employee's phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    /**
     * Gets the employee's employment status
     * 
     * @return The employment status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Records a login time for the employee on a specific date
     * 
     * @param date The date of the login
     * @param loginTime The time of login
     */
    public void recordLogin(LocalDate date, LocalTime loginTime) {
        AttendanceRecord record = attendanceRecords.getOrDefault(date, new AttendanceRecord());
        record.setLoginTime(loginTime);
        attendanceRecords.put(date, record);
        
        // If logout is already recorded, calculate hours worked
        if (record.getLogoutTime() != null) {
            calculateAndUpdateHoursWorked(date, record);
        }
        
        // For debugging
        System.out.println("Recorded login for employee " + getId() + " on " + date + " at " + loginTime);
    }
    
    /**
     * Records a logout time for the employee on a specific date
     * 
     * @param date The date of the logout
     * @param logoutTime The time of logout
     */
    public void recordLogout(LocalDate date, LocalTime logoutTime) {
        AttendanceRecord record = attendanceRecords.getOrDefault(date, new AttendanceRecord());
        record.setLogoutTime(logoutTime);
        attendanceRecords.put(date, record);
        
        // If login is already recorded, calculate hours worked
        if (record.getLoginTime() != null) {
            calculateAndUpdateHoursWorked(date, record);
        }
        
        // For debugging
        System.out.println("Recorded logout for employee " + getId() + " on " + date + " at " + logoutTime);
    }
    
    /**
     * Calculates and updates hours worked based on login and logout times
     * 
     * @param date The date of the attendance record
     * @param record The attendance record with login and logout times
     */
    private void calculateAndUpdateHoursWorked(LocalDate date, AttendanceRecord record) {
        if (record.getLoginTime() != null && record.getLogoutTime() != null) {
            // Ensure logout time is after login time
            if (record.getLogoutTime().isAfter(record.getLoginTime())) {
                Duration duration = Duration.between(record.getLoginTime(), record.getLogoutTime());
                double hours = duration.toMinutes() / 60.0;
                
                // Round to 2 decimal places for clarity
                hours = Math.round(hours * 100.0) / 100.0;
                
                // Update hours worked for this date
                hoursWorked.put(date, hours);
                
                // For debugging
                System.out.println("Calculated " + hours + " hours worked for employee " + 
                                  getId() + " on " + date + " (Login: " + record.getLoginTime() + 
                                  ", Logout: " + record.getLogoutTime() + ")");
            } else {
                System.out.println("Warning: Invalid time record for employee " + getId() + 
                                  " on " + date + " - logout time (" + record.getLogoutTime() + 
                                  ") is before login time (" + record.getLoginTime() + ")");
            }
        }
    }
    
    /**
     * Gets the attendance records for this employee
     * 
     * @return Map of dates to attendance records
     */
    public Map<LocalDate, AttendanceRecord> getAttendanceRecords() {
        return new HashMap<>(attendanceRecords);
    }
    
    /**
     * Inner class to represent a daily attendance record with login and logout times
     */
    public static class AttendanceRecord {
        private LocalTime loginTime;
        private LocalTime logoutTime;
        
        /**
         * Gets the login time
         * 
         * @return The login time or null if not set
         */
        public LocalTime getLoginTime() {
            return loginTime;
        }
        
        /**
         * Sets the login time
         * 
         * @param loginTime The login time
         */
        public void setLoginTime(LocalTime loginTime) {
            this.loginTime = loginTime;
        }
        
        /**
         * Gets the logout time
         * 
         * @return The logout time or null if not set
         */
        public LocalTime getLogoutTime() {
            return logoutTime;
        }
        
        /**
         * Sets the logout time
         * 
         * @param logoutTime The logout time
         */
        public void setLogoutTime(LocalTime logoutTime) {
            this.logoutTime = logoutTime;
        }
        
        /**
         * Calculates the hours worked in this attendance record
         * 
         * @return Hours worked or 0 if login or logout is missing
         */
        public double getHoursWorked() {
            if (loginTime == null || logoutTime == null) {
                return 0;
            }
            
            Duration duration = Duration.between(loginTime, logoutTime);
            return duration.toMinutes() / 60.0;
        }
        
        /**
         * Returns a string representation of the attendance record
         * 
         * @return String with login and logout times
         */
        @Override
        public String toString() {
            return "Login: " + (loginTime != null ? loginTime : "Not recorded") + 
                   ", Logout: " + (logoutTime != null ? logoutTime : "Not recorded");
        }
    }

    /**
     * Gets the employee's basic monthly salary
     * 
     * @return The basic monthly salary
     */
    public double getBasicSalary() {
        return basicSalary;
    }
    
    /**
     * Sets the employee's basic monthly salary
     * 
     * @param basicSalary The basic monthly salary
     */
    public void setBasicSalary(double basicSalary) {
        this.basicSalary = basicSalary;
    }
    
    /**
     * Gets the employee's rice subsidy
     * 
     * @return The rice subsidy amount
     */
    public double getRiceSubsidy() {
        return riceSubsidy;
    }
    
    /**
     * Sets the employee's rice subsidy
     * 
     * @param riceSubsidy The rice subsidy amount
     */
    public void setRiceSubsidy(double riceSubsidy) {
        this.riceSubsidy = riceSubsidy;
    }
    
    /**
     * Gets the employee's phone allowance
     * 
     * @return The phone allowance amount
     */
    public double getPhoneAllowance() {
        return phoneAllowance;
    }
    
    /**
     * Sets the employee's phone allowance
     * 
     * @param phoneAllowance The phone allowance amount
     */
    public void setPhoneAllowance(double phoneAllowance) {
        this.phoneAllowance = phoneAllowance;
    }
    
    /**
     * Gets the employee's clothing allowance
     * 
     * @return The clothing allowance amount
     */
    public double getClothingAllowance() {
        return clothingAllowance;
    }
    
    /**
     * Sets the employee's clothing allowance
     * 
     * @param clothingAllowance The clothing allowance amount
     */
    public void setClothingAllowance(double clothingAllowance) {
        this.clothingAllowance = clothingAllowance;
    }
    
    /**
     * Gets the employee's gross semi-monthly rate
     * 
     * @return The gross semi-monthly rate
     */
    public double getGrossSemiMonthlyRate() {
        return grossSemiMonthlyRate;
    }
    
    /**
     * Sets the employee's gross semi-monthly rate
     * 
     * @param grossSemiMonthlyRate The gross semi-monthly rate
     */
    public void setGrossSemiMonthlyRate(double grossSemiMonthlyRate) {
        this.grossSemiMonthlyRate = grossSemiMonthlyRate;
    }

    /**
     * Calculates hours worked between login and logout times
     * 
     * @param loginTime The login time
     * @param logoutTime The logout time
     * @return Hours worked or 0 if times are invalid
     */
    public double calculateHoursWorked(LocalTime loginTime, LocalTime logoutTime) {
        if (loginTime == null || logoutTime == null) {
            return 0;
        }
        
        // Ensure logout time is after login time
        if (logoutTime.isAfter(loginTime)) {
            Duration duration = Duration.between(loginTime, logoutTime);
            double hours = duration.toMinutes() / 60.0;
            
            // Round to 2 decimal places for clarity
            hours = Math.round(hours * 100.0) / 100.0;
            
            return hours;
        } else {
            System.out.println("Warning: Invalid time record - logout time (" + 
                              logoutTime + ") is before login time (" + loginTime + ")");
            return 0;
        }
    }
}