import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Payroll class handles employee data management and payroll processing
 * 
 * This class is responsible for:
 * - Loading and saving employee data from/to CSV files
 * - Processing payroll calculations for all employees
 * - Managing employee records
 * - Generating payroll reports
 * 
 * Implements FileStorage interface for data persistence
 * 
 * @author MotorPH Development Team
 * @version 1.0
 */
public class Payroll implements FileStorage<Employee> {
    /** List of employees in the system */
    private final List<Employee> employees;
    
    /** Path to the employee data CSV file */
    private static final String FILE_PATH = "C:\\Users\\Johanzen\\Documents\\PROJECTS IT\\MotorPH Payroll System\\MotorPH Employee Data - Employee Details.csv";
    
    /** Path to the attendance records CSV file */
    private static final String ATTENDANCE_FILE_PATH = "C:\\Users\\Johanzen\\Documents\\PROJECTS IT\\MotorPH Payroll System\\MotorPH Employee Data - Attendance Record.csv";
    
    /** Formatting constant for payroll report headers */
    private static final String PAYROLL_HEADER = "=================================================================================================================================";
    
    /** Format string for payroll report rows */
    private static final String PAYROLL_FORMAT = "%-12s | %-20s | %-10s | %-11s | %-12.2f | %-15.2f | %-5.2f | %-16.2f | %-7.2f\n";

    /**
     * Constructs a new Payroll system and loads employee data from file
     */
    public Payroll() {
        System.out.println("Initializing Payroll system...");
        this.employees = new ArrayList<>();
        
        try {
            System.out.println("Loading employee data from: " + FILE_PATH);
            loadEmployeesFromFile();
            System.out.println("Successfully loaded " + employees.size() + " employees.");
            
            System.out.println("Loading attendance records from: " + ATTENDANCE_FILE_PATH);
            loadAttendanceRecords();
            
            // Count total attendance records
            int totalRecords = 0;
            for (Employee emp : employees) {
                totalRecords += emp.getAttendanceRecords().size();
            }
            System.out.println("Successfully loaded " + totalRecords + " attendance records across all employees.");
            
        } catch (Exception e) {
            System.err.println("ERROR during Payroll initialization: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Processes payroll for all employees within a date range
     */
    public void processPayroll(LocalDate startDate, LocalDate endDate) {
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                     PAYROLL PROCESSING INFORMATION                                              ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ Period: %s to %s%n", startDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")), 
                                                 endDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        System.out.printf("║ Total Employees: %d%n", employees.size());
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝");

        if (employees.isEmpty()) {
            System.out.println("\n║ No employees found in the system. Please check if employee data was loaded correctly.                         ║");
            return;
        }
        
        printPayrollHeader();
        
        int employeesProcessed = 0;
        int employeesWithHours = 0;
        double totalGrossPay = 0.0;
        double totalNetPay = 0.0;
        double totalHoursWorked = 0.0;
        double totalSssDeduction = 0.0;
        double totalPhilHealthDeduction = 0.0;
        double totalPagIbigDeduction = 0.0;
        double totalTaxDeduction = 0.0;

        for (Employee employee : employees) {
            try {
                // Process each employee's payroll
                Map<String, Double> payrollData = processEmployeePayroll(employee, startDate, endDate);
                employeesProcessed++;
                
                double employeeHours = payrollData.get("totalHoursWorked");
                double grossIncome = payrollData.get("grossIncome");
                double sssDeduction = payrollData.get("sssDeduction");
                double philHealthDeduction = payrollData.get("philHealthDeduction");
                double pagIbigDeduction = payrollData.get("pagIbigDeduction");
                double taxDeduction = payrollData.get("taxDeduction");
                double netPay = payrollData.get("netPay");
                
                // Print the main payroll row with exactly 2 decimal places and all deductions
                System.out.printf("║ %-8s ║ %-15s ║ %-12s ║ %-13s ║ %,12.2f ║ %,11.2f ║ %,11.2f ║ %,11.2f ║ %,11.2f ║ %,11.2f ║ %,10.2f ║%n",
                    employee.getId(),
                    employee.getName(),
                    employee.getPosition(),
                    employee.getDepartment(),
                    employeeHours,
                    grossIncome,
                    sssDeduction,
                    philHealthDeduction,
                    pagIbigDeduction,
                    taxDeduction,
                    netPay
                );
                
                // Update totals
                if (employeeHours > 0) {
                    employeesWithHours++;
                    totalHoursWorked += employeeHours;
                    totalGrossPay += grossIncome;
                    totalSssDeduction += sssDeduction;
                    totalPhilHealthDeduction += philHealthDeduction;
                    totalPagIbigDeduction += pagIbigDeduction;
                    totalTaxDeduction += taxDeduction;
                    totalNetPay += netPay;
                }
                
            } catch (Exception e) {
                System.out.println("║ Error processing payroll for employee " + employee.getId() + ": " + e.getMessage() + " ║");
                e.printStackTrace();
            }
        }
        
        // Print summary
        System.out.println("╠══════════╩═══════════════════╩════════════════╩═══════════════╩════════════════╩═══════════════╩═══════════════╩═══════════════╩═══════════════╩═══════════════╩══════════════╣");
        System.out.println("║                                                                    PAYROLL SUMMARY                                                                                             ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ Total Employees Processed: %-125d ║%n", employeesProcessed);
        System.out.printf("║ Employees With Hours: %-128d ║%n", employeesWithHours);
        System.out.printf("║ Total Hours Worked: %-129.2f ║%n", totalHoursWorked);
        System.out.printf("║ Average Hours Per Employee: %-123.2f ║%n", 
            employeesWithHours > 0 ? totalHoursWorked / employeesWithHours : 0);
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ Total Gross Pay:          PHP %-123.2f ║%n", totalGrossPay);
        System.out.printf("║ Total SSS Deductions:     PHP %-123.2f ║%n", totalSssDeduction);
        System.out.printf("║ Total PhilHealth:         PHP %-123.2f ║%n", totalPhilHealthDeduction);
        System.out.printf("║ Total Pag-IBIG:           PHP %-123.2f ║%n", totalPagIbigDeduction);
        System.out.printf("║ Total Tax Deductions:     PHP %-123.2f ║%n", totalTaxDeduction);
        System.out.printf("║ Total Deductions:         PHP %-123.2f ║%n", 
            totalSssDeduction + totalPhilHealthDeduction + totalPagIbigDeduction + totalTaxDeduction);
        System.out.printf("║ Total Net Pay:            PHP %-123.2f ║%n", totalNetPay);
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝");
    }

    /**
     * Processes payroll for a single employee
     * Calculates gross pay, deductions, and net pay
     * 
     * @param employee Employee to process
     * @param startDate Start date of the period
     * @param endDate End date of the period
     * @return Map containing the calculated payroll values
     */
    private Map<String, Double> processEmployeePayroll(Employee employee, LocalDate startDate, LocalDate endDate) {
        Map<String, Double> payrollResults = new HashMap<>();
        
        // Calculate total hours worked in the period
        double totalHoursWorked = 0.0;
        int daysPresent = 0;
        
        for (Map.Entry<LocalDate, Employee.AttendanceRecord> entry : employee.getAttendanceRecords().entrySet()) {
            LocalDate date = entry.getKey();
            if (!date.isBefore(startDate) && !date.isAfter(endDate)) {
                Employee.AttendanceRecord record = entry.getValue();
                double hoursForDay = record.getHoursWorked();
                if (hoursForDay > 0) {
                    totalHoursWorked += hoursForDay;
                    daysPresent++;
                }
            }
        }

        // Calculate pay components
        double hourlyRate = employee.getHourlyRate();
        double basePay = totalHoursWorked * hourlyRate;
        
        // Calculate prorated allowances
        int standardWorkDays = 22;
        double prorationFactor = Math.min(1.0, (double) daysPresent / standardWorkDays);
        
        double riceSubsidy = employee.getRiceSubsidy() * prorationFactor;
        double phoneAllowance = employee.getPhoneAllowance() * prorationFactor;
        double clothingAllowance = employee.getClothingAllowance() * prorationFactor;
        double totalAllowances = riceSubsidy + phoneAllowance + clothingAllowance;
        
        // Calculate gross income and deductions
        double grossIncome = basePay + totalAllowances;
        double sssDeduction = employee.calculateSSSDeduction(grossIncome);
        double philHealthDeduction = employee.calculatePhilHealthDeduction(grossIncome);
        double pagIbigDeduction = employee.calculatePagIBIGDeduction(grossIncome);
        double basicDeduction = sssDeduction + philHealthDeduction + pagIbigDeduction;
        double taxDeduction = employee.calculateTaxDeduction(grossIncome);
            double totalDeductions = basicDeduction + taxDeduction;
        double netPay = grossIncome - totalDeductions;

        // Store all calculated values
        payrollResults.put("totalHoursWorked", totalHoursWorked);
        payrollResults.put("hourlyRate", hourlyRate);
        payrollResults.put("basePay", basePay);
        payrollResults.put("riceSubsidy", riceSubsidy);
        payrollResults.put("phoneAllowance", phoneAllowance);
        payrollResults.put("clothingAllowance", clothingAllowance);
        payrollResults.put("totalAllowances", totalAllowances);
        payrollResults.put("grossIncome", grossIncome);
        payrollResults.put("sssDeduction", sssDeduction);
        payrollResults.put("philHealthDeduction", philHealthDeduction);
        payrollResults.put("pagIbigDeduction", pagIbigDeduction);
        payrollResults.put("taxDeduction", taxDeduction);
        payrollResults.put("totalDeductions", totalDeductions);
        payrollResults.put("netPay", netPay);
        payrollResults.put("daysPresent", (double) daysPresent);

        return payrollResults;
    }

    /**
     * Displays payroll information in a formatted grid
     * 
     * @param employee Employee whose payroll to display
     * @param payrollData Map containing the calculated payroll values
     * @param startDate Start date of the period
     * @param endDate End date of the period
     */
    private void displayPayrollInformation(Employee employee, Map<String, Double> payrollData, LocalDate startDate, LocalDate endDate) {
        DateTimeFormatter displayFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        
        // Print header with company information
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                              MOTORPH CORPORATION                                                 ║");
        System.out.println("║                                               PAYROLL STATEMENT                                                 ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════════════════════════════════════╣");

        // Employee Information Section with enhanced grid
        System.out.println("║                                             EMPLOYEE INFORMATION                                               ║");
        System.out.println("╠══════════════════════════════════╦═════════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ Employee ID: %-20s ║ Name: %-63s ║%n", employee.getId(), employee.getName());
        System.out.printf("║ Position: %-23s ║ Department: %-59s ║%n", employee.getPosition(), employee.getDepartment());
        System.out.printf("║ Pay Period: %s - %s %-37s ║%n", startDate.format(displayFormat), endDate.format(displayFormat), "");
        System.out.println("╠══════════════════════════════════╩═════════════════════════════════════════════════════════════════════════════╣");

        // Attendance Summary Section with enhanced grid
        System.out.println("║                                             ATTENDANCE SUMMARY                                                 ║");
        System.out.println("╠════════════════════════════════════════════╦═══════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ Total Days Present: %-25d ║ Total Hours Worked: %-41.2f ║%n", 
            (int)payrollData.get("daysPresent").doubleValue(), payrollData.get("totalHoursWorked"));
        System.out.println("╠════════════════════════════════════════════╩═══════════════════════════════════════════════════════════════════╣");

        // Earnings Section with enhanced grid
        System.out.println("║                                                  EARNINGS                                                     ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╦═══════════════════════════════════════════════════╣");
        System.out.printf("║ Base Pay (%,.2f hours × PHP %,.2f)                          ║ PHP %,14.2f                              ║%n",
            payrollData.get("totalHoursWorked"), payrollData.get("hourlyRate"), payrollData.get("basePay"));
        System.out.println("║                                                              ║                                               ║");
        System.out.println("║ Allowances:                                                  ║                                               ║");
        System.out.printf("║   ├─ Rice Subsidy                                            ║ PHP %,14.2f                              ║%n",
            payrollData.get("riceSubsidy"));
        System.out.printf("║   ├─ Phone Allowance                                         ║ PHP %,14.2f                              ║%n",
            payrollData.get("phoneAllowance"));
        System.out.printf("║   └─ Clothing Allowance                                      ║ PHP %,14.2f                              ║%n",
            payrollData.get("clothingAllowance"));
        System.out.println("║                                                              ║                                               ║");
        System.out.printf("║ Total Allowances                                             ║ PHP %,14.2f                              ║%n",
            payrollData.get("totalAllowances"));
        System.out.println("╠══════════════════════════════════════════════════════════════╬═══════════════════════════════════════════════════╣");
        System.out.printf("║ GROSS INCOME                                                 ║ PHP %,14.2f                              ║%n",
            payrollData.get("grossIncome"));
        System.out.println("╠══════════════════════════════════════════════════════════════╩═══════════════════════════════════════════════════╣");

        // Deductions Section with enhanced grid
        System.out.println("║                                                 DEDUCTIONS                                                    ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╦═══════════════════════════════════════════════════╣");
        System.out.printf("║ Mandatory Deductions:                                         ║                                               ║%n");
        System.out.printf("║   ├─ SSS Contribution                                        ║ PHP %,14.2f                              ║%n",
            payrollData.get("sssDeduction"));
        System.out.printf("║   ├─ PhilHealth Contribution                                 ║ PHP %,14.2f                              ║%n",
            payrollData.get("philHealthDeduction"));
        System.out.printf("║   ├─ Pag-IBIG Contribution                                   ║ PHP %,14.2f                              ║%n",
            payrollData.get("pagIbigDeduction"));
        System.out.printf("║   └─ Withholding Tax                                        ║ PHP %,14.2f                              ║%n",
            payrollData.get("taxDeduction"));
        System.out.println("║                                                              ║                                               ║");
        System.out.printf("║ TOTAL DEDUCTIONS                                             ║ PHP %,14.2f                              ║%n",
            payrollData.get("totalDeductions"));
        System.out.println("╠══════════════════════════════════════════════════════════════╩═══════════════════════════════════════════════════╣");

        // Net Pay Section with enhanced grid
        System.out.println("║                                                  NET PAY                                                     ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╦═══════════════════════════════════════════════════╣");
        System.out.printf("║ NET PAY                                                       ║ PHP %,14.2f                              ║%n",
            payrollData.get("netPay"));
        System.out.println("╚══════════════════════════════════════════════════════════════╩═══════════════════════════════════════════════════╝");
        
        // Add a note about the pay period
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║ Note: This payroll statement covers the period from " + 
            startDate.format(displayFormat) + " to " + endDate.format(displayFormat) + "                              ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝");
    }

    /**
     * Calculates and displays payroll for a specific employee
     */
    public void calculatePayroll(String employeeId, LocalDate startDate, LocalDate endDate) {
        Employee employee = findEmployeeById(employeeId);
        if (employee == null) {
            System.out.println("\n╔════════════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                              ERROR                                         ║");
            System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");
            System.out.println("║ Employee not found with ID: " + employeeId + "                              ║");
            System.out.println("╚════════════════════════════════════════════════════════════════════════════╝");
            return;
        }

        if (startDate == null || endDate == null) {
            LocalDate now = LocalDate.now();
            startDate = LocalDate.of(now.getYear(), now.getMonth(), 1);
            endDate = startDate.plusMonths(1).minusDays(1);
        }

        Map<String, Double> payrollData = processEmployeePayroll(employee, startDate, endDate);
        displayPayrollInformation(employee, payrollData, startDate, endDate);
    }

    /**
     * Overloaded method for calculating payroll without dates
     * 
     * @param employeeId ID of the employee to calculate payroll for
     */
    public void calculatePayroll(String employeeId) {
        calculatePayroll(employeeId, null, null);
    }

    /**
     * Views payroll information for a specific employee with date selection
     * This method is used by both admin and employee views
     * 
     * @param employeeId ID of the employee to view payroll for
     * @param startDateStr Start date in MM/DD/YYYY format for employee view, yyyy-MM-dd for admin
     * @param endDateStr End date in MM/DD/YYYY format for employee view, yyyy-MM-dd for admin
     * @param isEmployeeView true if being accessed by employee, false if by admin
     */
    public void viewPayrollWithDates(String employeeId, String startDateStr, String endDateStr, boolean isEmployeeView) {
        try {
            // Use different date formats for employee and admin views
            DateTimeFormatter dateFormatter = isEmployeeView ? 
                DateTimeFormatter.ofPattern("MM/dd/yyyy") : 
                DateTimeFormatter.ofPattern("yyyy-MM-dd");

            DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            
            LocalDate startDate = LocalDate.parse(startDateStr, dateFormatter);
            LocalDate endDate = LocalDate.parse(endDateStr, dateFormatter);
            
            // Validate date range
            if (endDate.isBefore(startDate)) {
                System.out.println("\n╔════════════════════════════════════════════════════════════════════════════╗");
                System.out.println("║                              ERROR                                         ║");
                System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");
                System.out.println("║ End date cannot be before start date                                      ║");
                System.out.println("╚════════════════════════════════════════════════════════════════════════════╝");
                return;
            }

            Employee employee = findEmployeeById(employeeId);
            if (employee == null) {
                System.out.println("\n╔════════════════════════════════════════════════════════════════════════════╗");
                System.out.println("║                              ERROR                                         ║");
                System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");
                System.out.println("║ Employee not found with ID: " + employeeId + "                              ║");
                System.out.println("╚════════════════════════════════════════════════════════════════════════════╝");
                return;
            }
            
            Map<String, Double> payrollData = processEmployeePayroll(employee, startDate, endDate);
            displayPayrollInformation(employee, payrollData, startDate, endDate);
            
        } catch (Exception e) {
            System.out.println("\n╔════════════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                              ERROR                                         ║");
            System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");
            if (isEmployeeView) {
                System.out.println("║ Invalid date format. Please use MM/DD/YYYY format (e.g., 03/15/2024)      ║");
            } else {
                System.out.println("║ Invalid date format. Please use yyyy-MM-dd format (e.g., 2024-03-15)      ║");
            }
            System.out.println("╚════════════════════════════════════════════════════════════════════════════╝");
        }
    }

    /**
     * Processes weekly payroll for all employees
     */
    public void processWeeklyPayroll(LocalDate startDate, LocalDate endDate) {
        printPayrollHeader();  // Changed from printWeeklyPayrollHeader
        for (Employee employee : employees) {
            Map<String, Double> payrollData = processEmployeePayroll(employee, startDate, endDate);
            // Display weekly payroll data
            System.out.printf(PAYROLL_FORMAT,
                employee.getId(), 
                employee.getName(), 
                employee.getPosition(), 
                employee.getDepartment(),
                payrollData.get("totalHoursWorked"),
                payrollData.get("grossIncome"),
                payrollData.get("netPay")
            );
        }
        System.out.println(PAYROLL_HEADER);
    }

    /**
     * Prints the header for regular payroll reports
     */
    private void printPayrollHeader() {
        // Print the main header
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                       MOTORPH PAYROLL SYSTEM                                                                                    ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ Note: Gross Income = (Hours Worked × Hourly Rate) + Prorated Allowances                                                                                                        ║");
        System.out.println("╠══════════╦═══════════════════╦════════════════╦═══════════════╦════════════════╦═══════════════╦═══════════════╦═══════════════╦═══════════════╦═══════════════╦══════════════╣");
        System.out.println("║   ID     ║    Employee Name  ║    Position    ║   Department  ║  Hours Worked  ║ Gross Income  ║     SSS       ║   PhilHealth  ║    Pag-IBIG   ║      Tax      ║    Net Pay   ║");
        System.out.println("╠══════════╬═══════════════════╬════════════════╬═══════════════╬════════════════╬═══════════════╬═══════════════╬═══════════════╬═══════════════╬═══════════════╬══════════════╣");
    }

    /**
     * Adds a new employee to the system and saves to file
     * 
     * @param employee The employee to add
     */
    public void addEmployee(Employee employee) {
        employees.add(employee);
        save(employees);
    }

    /**
     * Finds an employee by ID and optionally by name
     * 
     * @param id Employee ID to search for
     * @param name Employee name to search for (can be empty to search by ID only)
     * @return The found employee or null if not found
     */
    public Employee findEmployeeByIdAndName(int id, String name) {
        // Convert to string without leading zeros
        String idStr = String.valueOf(id);
        String idStrFormatted = String.format("%05d", id);
        
        for (Employee employee : employees) {
            // Check if the ID matches (either exact match or with/without leading zeros)
            boolean idMatches = employee.getId().equals(idStr) || 
                               employee.getId().equals(idStrFormatted) ||
                               String.valueOf(Integer.parseInt(employee.getId())).equals(idStr);
                               
            // Check if name matches (if provided)
            boolean nameMatches = name.isEmpty() || 
                                 employee.getName().toLowerCase().contains(name.toLowerCase()) ||
                                 (employee.getFirstName() + " " + employee.getLastName()).toLowerCase().contains(name.toLowerCase());
                                 
            if (idMatches && nameMatches) {
                return employee;
            }
        }
        return null;
    }

    /**
     * Finds an employee by ID string
     * 
     * @param idStr Employee ID string to search for
     * @return The found employee or null if not found
     */
    public Employee findEmployeeById(String idStr) {
        // Try exact match first
        for (Employee employee : employees) {
            if (employee.getId().equals(idStr)) {
                return employee;
            }
        }
        
        // Try with/without leading zeros
        try {
            int idNum = Integer.parseInt(idStr);
            String formattedId = String.format("%05d", idNum);
            
            for (Employee employee : employees) {
                if (employee.getId().equals(formattedId) || 
                    String.valueOf(Integer.parseInt(employee.getId())).equals(idStr)) {
                    return employee;
                }
            }
        } catch (NumberFormatException e) {
            // Not a valid number, skip this attempt
        }
        
        return null;
    }

    /**
     * Gets the list of all employees in the system
     * 
     * @return List of employees
     */
    public List<Employee> getEmployees() {
        return employees;
    }

    /**
     * Loads employee data from the CSV file
     * Implementation of FileStorage interface
     * 
     * @return List of loaded employees
     */
    @Override
    public List<Employee> load() {
        List<Employee> loadedEmployees = new ArrayList<>();
        File file = new File(FILE_PATH);
        
        if (!file.exists()) {
            System.out.println("No existing employee data found. Starting with an empty employee list.");
            return loadedEmployees;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int lineCount = 0;
            while ((line = br.readLine()) != null && !line.trim().isEmpty()) {
                lineCount++;
                try {
                    loadedEmployees.add(Employee.fromCSV(line));
                } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
                    System.out.println("Error on line " + lineCount + ": " + e.getMessage());
                    System.out.println("Skipping invalid employee entry: " + line);
                }
            }
            System.out.println("Loaded " + loadedEmployees.size() + " employees.");
        } catch (IOException e) {
            System.out.println("Error reading employee data: " + e.getMessage());
        }
        
        return loadedEmployees;
    }

    /**
     * Saves employee data to the CSV file
     * Implementation of FileStorage interface
     * 
     * @param employees List of employees to save
     */
    @Override
    public void save(List<Employee> employees) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Employee employee : employees) {
                bw.write(employee.toCSV());
                bw.newLine();
            }
            System.out.println("Employee data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving employee data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Gets the file path for employee data storage
     * Implementation of FileStorage interface
     * 
     * @return File path string
     */
    @Override
    public String getFilePath() {
        return FILE_PATH;
    }
    
    /**
     * Saves the current list of employees to file
     */
    public void saveEmployees() {
        save(employees);
    }

    /**
     * Loads employees from the CSV file into the system
     * Parses the CSV format and creates Employee objects
     */
    private void loadEmployeesFromFile() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                System.err.println("ERROR: Employee data file not found at: " + FILE_PATH);
                System.err.println("Current working directory: " + System.getProperty("user.dir"));
                return;
            }
            
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                boolean firstLine = true;
                while ((line = br.readLine()) != null) {
                    if (firstLine) {
                        firstLine = false;
                        continue; // Skip header row
                    }
                    
                    try {
                        // Handle quoted fields in CSV properly
                        List<String> fields = parseCSVLine(line);
                        if (fields.size() < 19) {
                            System.out.println("Warning: Skipping line with insufficient fields: " + line);
                            continue;
                        }
                        
                        // Extract employee data
                        String id = fields.get(0).trim();
                        String lastName = fields.get(1).trim();
                        String firstName = fields.get(2).trim();
                        String birthday = fields.get(3).trim();
                        String address = fields.get(4).trim();
                        String phoneNumber = fields.get(5).trim();
                        String sssNumber = fields.get(6).trim();
                        String philHealthNumber = fields.get(7).trim();
                        String tinNumber = fields.get(8).trim();
                        String pagIbigNumber = fields.get(9).trim();
                        String status = fields.get(10).trim();
                        String position = fields.get(11).trim();
                        String supervisor = fields.get(12).trim();
                        
                        // Parse compensation information
                        double basicSalary = parseNumericValue(fields.get(13));
                        double riceSubsidy = parseNumericValue(fields.get(14));
                        double phoneAllowance = parseNumericValue(fields.get(15));
                        double clothingAllowance = parseNumericValue(fields.get(16));
                        double grossSemiMonthlyRate = parseNumericValue(fields.get(17));
                        double hourlyRate = parseNumericValue(fields.get(18));
                        
                        // Create employee object
                        Employee employee = new Employee(
                            id, firstName, lastName,
                            position, supervisor,
                            sssNumber,
                            philHealthNumber,
                            pagIbigNumber,
                            tinNumber,
                            hourlyRate
                        );
                        
                        // Set additional information
                        employee.setDepartment(position.split(" ")[0]); // Set department from position
                        employee.setStatus(status);
                        employee.setPhoneNumber(phoneNumber);
                        employee.setAddress(address);
                        employee.setBirthday(birthday);
                        
                        // Set compensation components
                        employee.setBasicSalary(basicSalary);
                        employee.setRiceSubsidy(riceSubsidy);
                        employee.setPhoneAllowance(phoneAllowance);
                        employee.setClothingAllowance(clothingAllowance);
                        employee.setGrossSemiMonthlyRate(grossSemiMonthlyRate);
                        
                        employees.add(employee);
                        System.out.println("Loaded employee: " + employee.getId() + " - " + employee.getName());
                    } catch (Exception e) {
                        System.out.println("Error processing employee line: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
                System.out.println("Successfully loaded " + employees.size() + " employees from " + FILE_PATH);
            }
        } catch (IOException e) {
            System.err.println("Error reading employee data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Parses a CSV line, handling quoted fields correctly
     * 
     * @param line The CSV line to parse
     * @return List of fields from the CSV line
     */
    private List<String> parseCSVLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                // Toggle the inQuotes flag
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                // End of field, add to list
                fields.add(sb.toString());
                sb.setLength(0); // Clear the StringBuilder
            } else {
                // Add character to the current field
                sb.append(c);
            }
        }
        
        // Add the last field
        fields.add(sb.toString());
        
        return fields;
    }

    /**
     * Parses a numeric value from a string, handling currency formatting
     * 
     * @param value The string value to parse
     * @return The parsed numeric value
     */
    private double parseNumericValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0.0;
        }
        
        // Remove currency symbols, commas, and other non-numeric characters except decimal point
        String cleanValue = value.replaceAll("[^\\d.]", "");
        
        try {
            return Double.parseDouble(cleanValue);
        } catch (NumberFormatException e) {
            System.out.println("Warning: Could not parse numeric value: " + value);
            return 0.0;
        }
    }

    /**
     * Views time records for a specific employee
     * 
     * @param employeeId ID of the employee to view time records for
     */
    public void viewTimeRecords(String employeeId) {
        Employee employee = findEmployeeById(employeeId);
        if (employee == null) {
            System.out.println("\n╔════════════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                              ERROR                                         ║");
            System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");
            System.out.println("║ Employee not found with ID: " + String.format("%-41s", employeeId) + " ║");
            System.out.println("╚════════════════════════════════════════════════════════════════════════════╝");
            return;
        }

        // Print header
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                     EMPLOYEE TIME RECORDS                                           ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ Employee ID: %-82s ║\n", employee.getId());
        System.out.printf("║ Name: %-88s ║\n", employee.getName());
        System.out.printf("║ Position: %-85s ║\n", employee.getPosition());
        System.out.printf("║ Department: %-83s ║\n", employee.getDepartment());
        System.out.println("╠════════════════╦═══════════╦════════════╦════════════╦═══════════════╦═════════════════════════════╣");
        System.out.println("║     Date      ║   Login   ║  Logout    ║   Hours    ║    Status     ║           Notes             ║");
        System.out.println("╠════════════════╬═══════════╬════════════╬════════════╬═══════════════╬═════════════════════════════╣");

        // Display time records
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        
        Map<LocalDate, Employee.AttendanceRecord> records = employee.getAttendanceRecords();
        if (records.isEmpty()) {
            System.out.println("║ No time records found for this employee.                                                              ║");
        } else {
            double totalHours = 0.0;
            int totalDays = 0;
            
            for (Map.Entry<LocalDate, Employee.AttendanceRecord> entry : records.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .collect(java.util.stream.Collectors.toList())) {
                
                LocalDate date = entry.getKey();
                Employee.AttendanceRecord record = entry.getValue();
                
                String loginTime = record.getLoginTime() != null ? record.getLoginTime().format(timeFormatter) : "---";
                String logoutTime = record.getLogoutTime() != null ? record.getLogoutTime().format(timeFormatter) : "---";
                double hours = record.getHoursWorked();
                
                String status = hours >= 8.0 ? "COMPLETE" : hours > 0 ? "PARTIAL" : "ABSENT";
                String notes = hours < 8.0 && hours > 0 ? "Incomplete hours" : 
                              hours >= 8.0 ? "Regular working day" : "No time record";
                
                System.out.printf("║ %-14s ║ %-9s ║ %-10s ║ %8.2f   ║ %-13s ║ %-27s ║\n",
                    date.format(dateFormatter), loginTime, logoutTime, hours, status, notes);
                
                totalHours += hours;
                totalDays++;
            }
            
            // Print summary
            System.out.println("╠════════════════╩═══════════╩════════════╩════════════╩═══════════════╩═════════════════════════════╣");
            System.out.printf("║ Total Days: %-87d ║\n", totalDays);
            System.out.printf("║ Total Hours: %-86.2f ║\n", totalHours);
            System.out.printf("║ Average Hours per Day: %-77.2f ║\n", totalDays > 0 ? totalHours / totalDays : 0.0);
        }
        
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════════════════════════╝");
    }

    /**
     * Displays employee details in a grid format
     * 
     * @param employee The employee to display details for
     */
    public void displayEmployeeDetails(Employee employee) {
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                     EMPLOYEE INFORMATION                                           ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════════════════════════╣");
        
        // Personal Information
        System.out.println("║                                    PERSONAL INFORMATION                                           ║");
        System.out.println("╠══════════════════════╦═══════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ Employee ID          ║ %-65s ║\n", employee.getId());
        System.out.printf("║ Name                 ║ %-65s ║\n", employee.getName());
        System.out.printf("║ Birthday             ║ %-65s ║\n", employee.getBirthday());
        System.out.printf("║ Address              ║ %-65s ║\n", employee.getAddress());
        System.out.printf("║ Phone Number         ║ %-65s ║\n", employee.getPhoneNumber());
        
        // Employment Information
        System.out.println("╠══════════════════════╬═══════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║                                   EMPLOYMENT INFORMATION                                          ║");
        System.out.println("╠══════════════════════╬═══════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ Status               ║ %-65s ║\n", employee.getStatus());
        System.out.printf("║ Position             ║ %-65s ║\n", employee.getPosition());
        System.out.printf("║ Department           ║ %-65s ║\n", employee.getDepartment());
        System.out.printf("║ Supervisor           ║ %-65s ║\n", employee.getSupervisor());
        
        // Government IDs
        System.out.println("╠══════════════════════╬═══════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║                                    GOVERNMENT NUMBERS                                             ║");
        System.out.println("╠══════════════════════╬═══════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ SSS Number           ║ %-65s ║\n", employee.getSssNumber());
        System.out.printf("║ PhilHealth Number    ║ %-65s ║\n", employee.getPhilHealthNumber());
        System.out.printf("║ Pag-IBIG Number      ║ %-65s ║\n", employee.getPagIbigNumber());
        System.out.printf("║ TIN Number           ║ %-65s ║\n", employee.getTinNumber());
        
        // Compensation Information
        System.out.println("╠══════════════════════╬═══════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║                                   COMPENSATION DETAILS                                            ║");
        System.out.println("╠══════════════════════╬═══════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ Basic Salary         ║ PHP %,63.2f ║\n", employee.getBasicSalary());
        System.out.printf("║ Rice Subsidy         ║ PHP %,63.2f ║\n", employee.getRiceSubsidy());
        System.out.printf("║ Phone Allowance      ║ PHP %,63.2f ║\n", employee.getPhoneAllowance());
        System.out.printf("║ Clothing Allowance   ║ PHP %,63.2f ║\n", employee.getClothingAllowance());
        System.out.printf("║ Gross Semi-Monthly   ║ PHP %,63.2f ║\n", employee.getGrossSemiMonthlyRate());
        System.out.printf("║ Hourly Rate          ║ PHP %,63.2f ║\n", employee.getHourlyRate());
        
        System.out.println("╚══════════════════════╩═══════════════════════════════════════════════════════════════════════════╝");
    }

    /**
     * Loads attendance records from the CSV file
     * Parses login and logout times and calculates hours worked
     */
    private void loadAttendanceRecords() {
        File file = new File(ATTENDANCE_FILE_PATH);
        if (!file.exists()) {
            System.err.println("ERROR: Attendance records file not found at: " + ATTENDANCE_FILE_PATH);
            System.err.println("Current working directory: " + System.getProperty("user.dir"));
            System.err.println("No attendance records will be loaded. Payroll calculations will be inaccurate!");
            return;
        }
        
        System.out.println("Loading attendance records from: " + ATTENDANCE_FILE_PATH);
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
            
            int recordsLoaded = 0;
            int recordsSkipped = 0;
            int linesProcessed = 0;
            
            // Create a map to track records per employee for verification
            Map<String, Integer> recordsPerEmployee = new HashMap<>();
            
            while ((line = br.readLine()) != null) {
                linesProcessed++;
                
                if (firstLine) {
                    firstLine = false;
                    System.out.println("Header row: " + line);
                    continue; // Skip header row
                }
                
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                try {
                    System.out.println("Processing attendance line: " + line);
                    
                    // Parse the CSV line
                    String[] fields = line.split(",");
                    if (fields.length < 6) {
                        System.out.println("Warning: Skipping attendance record with insufficient fields: " + line);
                        recordsSkipped++;
                        continue;
                    }
                    
                    // Extract attendance data based on the specific format in the CSV
                    String empId = fields[0].trim();
                    String lastName = fields[1].trim();
                    String firstName = fields[2].trim();
                    String dateStr = fields[3].trim();
                    String loginTimeStr = fields[4].trim();
                    String logoutTimeStr = fields[5].trim();
                    
                    System.out.println("  Employee ID: " + empId);
                    System.out.println("  Name: " + firstName + " " + lastName);
                    System.out.println("  Date: " + dateStr);
                    System.out.println("  Login: " + loginTimeStr);
                    System.out.println("  Logout: " + logoutTimeStr);
                    
                    // Find the employee
                    Employee employee = findEmployeeById(empId);
                    
                    if (employee == null) {
                        System.out.println("  Employee not found with ID: " + empId + ". Trying alternative search methods...");
                        
                        // Try with formatted ID (adding leading zeros)
                        try {
                            int idNum = Integer.parseInt(empId);
                            String formattedId = String.format("%05d", idNum);
                            employee = findEmployeeById(formattedId);
                            if (employee != null) {
                                System.out.println("  Found employee with formatted ID: " + formattedId);
                            }
                        } catch (NumberFormatException e) {
                            // Not a valid number, skip this attempt
                        }
                        
                        // If still not found, try searching by name
                        if (employee == null) {
                            for (Employee emp : employees) {
                                if ((emp.getFirstName().equalsIgnoreCase(firstName) && emp.getLastName().equalsIgnoreCase(lastName)) ||
                                    emp.getName().equalsIgnoreCase(firstName + " " + lastName)) {
                                    employee = emp;
                                    System.out.println("  Found employee by name match: " + emp.getId() + " - " + emp.getName());
                                    break;
                                }
                            }
                        }
                    } else {
                        System.out.println("  Found employee: " + employee.getId() + " - " + employee.getName());
                    }
                    
                    if (employee == null) {
                        System.out.println("  WARNING: Employee not found for attendance record: " + empId + " (" + firstName + " " + lastName + ")");
                        recordsSkipped++;
                        continue;
                    }
                    
                    // Parse date and times
                    try {
                        // Parse date with flexible format handling
                        LocalDate date;
                        try {
                            // Try M/d/yyyy format (matches the sample data)
                            date = LocalDate.parse(dateStr, dateFormatter);
                        } catch (Exception e) {
                            System.out.println("  Failed to parse date with M/d/yyyy format, trying alternatives...");
                            try {
                                // Try MM/dd/yyyy format
                                date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                            } catch (Exception e2) {
                                try {
                                    // Try MM-dd-yyyy format
                                    date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("MM-dd-yyyy"));
                                } catch (Exception e3) {
                                    // Try yyyy-MM-dd format as last resort
                                    date = LocalDate.parse(dateStr);
                                }
                            }
                        }
                        System.out.println("  Parsed date: " + date);
                        
                        // Parse login time
                        LocalTime loginTime = null;
                        if (loginTimeStr != null && !loginTimeStr.isEmpty()) {
                            try {
                                // Try H:mm format (matches the sample data)
                                loginTime = LocalTime.parse(loginTimeStr, timeFormatter);
                            } catch (Exception e) {
                                System.out.println("  Failed to parse login time with H:mm format, trying alternatives...");
                                try {
                                    // Try HH:mm format
                                    loginTime = LocalTime.parse(loginTimeStr, DateTimeFormatter.ofPattern("HH:mm"));
                                } catch (Exception e2) {
                                    // Try HH:mm:ss format
                                    loginTime = LocalTime.parse(loginTimeStr, DateTimeFormatter.ofPattern("HH:mm:ss"));
                                }
                            }
                            System.out.println("  Parsed login time: " + loginTime);
                            employee.recordLogin(date, loginTime);
                        }
                        
                        // Parse logout time
                        LocalTime logoutTime = null;
                        if (logoutTimeStr != null && !logoutTimeStr.isEmpty()) {
                            try {
                                // Try H:mm format (matches the sample data)
                                logoutTime = LocalTime.parse(logoutTimeStr, timeFormatter);
                            } catch (Exception e) {
                                System.out.println("  Failed to parse logout time with H:mm format, trying alternatives...");
                                try {
                                    // Try HH:mm format
                                    logoutTime = LocalTime.parse(logoutTimeStr, DateTimeFormatter.ofPattern("HH:mm"));
                                } catch (Exception e2) {
                                    // Try HH:mm:ss format
                                    logoutTime = LocalTime.parse(logoutTimeStr, DateTimeFormatter.ofPattern("HH:mm:ss"));
                                }
                            }
                            System.out.println("  Parsed logout time: " + logoutTime);
                            employee.recordLogout(date, logoutTime);
                        }
                        
                        // Calculate hours worked for this record
                        if (loginTime != null && logoutTime != null) {
                            double hours = employee.calculateHoursWorked(loginTime, logoutTime);
                            System.out.println("  Hours worked: " + hours);
                        }
                        
                        // Track records per employee
                        recordsPerEmployee.put(employee.getId(), recordsPerEmployee.getOrDefault(employee.getId(), 0) + 1);
                        
                        recordsLoaded++;
                        System.out.println("  Successfully loaded attendance record #" + recordsLoaded);
                    } catch (Exception e) {
                        System.out.println("  Error parsing date/time for attendance record: " + e.getMessage());
                        e.printStackTrace();
                        recordsSkipped++;
                    }
                } catch (Exception e) {
                    System.out.println("Error processing attendance record: " + e.getMessage());
                    e.printStackTrace();
                    recordsSkipped++;
                }
            }
            
            System.out.println("\n=== ATTENDANCE RECORDS LOADING SUMMARY ===");
            System.out.println("Total lines processed: " + linesProcessed);
            System.out.println("Records successfully loaded: " + recordsLoaded);
            System.out.println("Records skipped: " + recordsSkipped);
            
            // Print summary of records per employee
            System.out.println("\nAttendance records per employee:");
            int employeesWithRecords = 0;
            for (Employee emp : employees) {
                int recordCount = recordsPerEmployee.getOrDefault(emp.getId(), 0);
                if (recordCount > 0) {
                    employeesWithRecords++;
                    System.out.println("  " + emp.getId() + " - " + emp.getName() + ": " + recordCount + " records");
                    
                    // Verify hours worked calculation
                    double totalHours = 0.0;
                    for (Map.Entry<LocalDate, Employee.AttendanceRecord> entry : emp.getAttendanceRecords().entrySet()) {
                        totalHours += entry.getValue().getHoursWorked();
                    }
                    System.out.println("    Total hours worked: " + String.format("%.2f", totalHours));
                }
            }
            
            System.out.println("\nEmployees with attendance records: " + employeesWithRecords + " out of " + employees.size());
            System.out.println("=== ATTENDANCE RECORDS LOADING COMPLETE ===\n");
            
        } catch (IOException e) {
            System.err.println("Error reading attendance records: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Method specifically for employee self-service payroll view with date selection
     * Uses MM/DD/YYYY format for dates
     * 
     * @param employeeId ID of the employee
     * @param startDateStr Start date in MM/DD/YYYY format
     * @param endDateStr End date in MM/DD/YYYY format
     */
    public void viewMyPayrollWithDates(String employeeId, String startDateStr, String endDateStr) {
        // Validate date format before proceeding
        if (!isValidEmployeeDateFormat(startDateStr) || !isValidEmployeeDateFormat(endDateStr)) {
            System.out.println("\n╔════════════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                              ERROR                                         ║");
            System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");
            System.out.println("║ Please enter dates in MM/DD/YYYY format (e.g., 03/15/2024)                ║");
            System.out.println("║ Make sure the month is between 01-12 and day is valid for the month       ║");
            System.out.println("╚════════════════════════════════════════════════════════════════════════════╝");
            return;
        }
        viewPayrollWithDates(employeeId, startDateStr, endDateStr, true);
    }

    /**
     * Method specifically for admin payroll view with date selection
     * 
     * @param employeeId ID of the employee to view
     * @param startDateStr Start date in yyyy-MM-dd format
     * @param endDateStr End date in yyyy-MM-dd format
     */
    public void viewEmployeePayrollWithDates(String employeeId, String startDateStr, String endDateStr) {
        viewPayrollWithDates(employeeId, startDateStr, endDateStr, false);
    }

    /**
     * Gets the current pay period dates in employee format (MM/DD/YYYY)
     * 
     * @return String array containing [startDate, endDate] in MM/DD/YYYY format
     */
    public String[] getCurrentPayPeriodEmployee() {
        LocalDate now = LocalDate.now();
        LocalDate startDate = LocalDate.of(now.getYear(), now.getMonth(), 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return new String[] {
            startDate.format(formatter),
            endDate.format(formatter)
        };
    }

    /**
     * Validates a date string in employee format (MM/DD/YYYY)
     * 
     * @param dateStr Date string to validate
     * @return true if valid, false otherwise
     */
    public boolean isValidEmployeeDateFormat(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return false;
        }
        
        try {
            // Check basic format using regex
            if (!dateStr.matches("\\d{2}/\\d{2}/\\d{4}")) {
                return false;
            }
            
            // Parse the date parts
            String[] parts = dateStr.split("/");
            int month = Integer.parseInt(parts[0]);
            int day = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);
            
            // Basic validation
            if (month < 1 || month > 12) return false;
            if (day < 1 || day > 31) return false;
            if (year < 2000 || year > 2100) return false;
            
            // Parse with DateTimeFormatter for full validation
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate.parse(dateStr, formatter);
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}