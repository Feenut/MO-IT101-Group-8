import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;


/**
 * Main class for the MotorPH Payroll System
 * 
 * This class serves as the entry point for the payroll system and handles:
 * - User interface and menu navigation
 * - Role-based access (Admin/Employee)
 * - System operations coordination
 * - Input validation and error handling
 * 
 * @author MotorPH Development Team
 * @version 1.0
 */
public class Main {
    private static Payroll payroll;
    private static Scanner scanner;

    /**
     * Main method - entry point of the application
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Check if the CSV file exists before starting
        checkDataFileExists();

        scanner = new Scanner(System.in);
        payroll = new Payroll();

        // Display welcome message
        System.out.println("=======================================");
        System.out.println("          Welcome to the Payroll System");
        System.out.println("=======================================");
        boolean exit = false;

        // Main application loop
        while (!exit) {
            displayRoleSelectionMenu();

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        adminMenu(payroll, scanner);
                        break;
                    case 2:
                        employeeLoginMenu(payroll, scanner);
                        break;
                    case 3:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice! Please choose a valid option.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
            }
        }

        scanner.close();
    }

    /**
     * Displays the role selection menu
     */
    private static void displayRoleSelectionMenu() {
        System.out.println("\n=======================================");
        System.out.println("          Select Role to Log In");
        System.out.println("=======================================");
        System.out.println("1. Admin");
        System.out.println("2. Employee");
        System.out.println("3. Exit");
        System.out.println("=======================================");
        System.out.print("Choose an option (1-3): ");
    }
    
    /**
     * Checks if the employee data file exists and prints its location
     * Provides helpful error messages if the file is not found
     */
    private static void checkDataFileExists() {
        String csvPath = "C:\\Users\\Johanzen\\Documents\\PROJECTS IT\\MotorPH Payroll System\\MotorPH Employee Data - Employee Details.csv";
        File csvFile = new File(csvPath);
        
        if (csvFile.exists()) {
            System.out.println("Employee data file found at: " + csvFile.getAbsolutePath());
        } else {
            System.err.println("WARNING: Employee data file not found at: " + csvFile.getAbsolutePath());
            System.err.println("Please ensure the CSV file is in the correct location.");
            System.err.println("Current working directory: " + System.getProperty("user.dir"));
        }
    }

    /**
     * Displays and handles the admin menu options
     * 
     * @param payroll The payroll system instance
     * @param scanner Scanner for user input
     */
    private static void adminMenu(Payroll payroll, Scanner scanner) {
        boolean backToRoleSelection = false;

        while (!backToRoleSelection) {
            displayAdminMenu();

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        viewEmployeeList();
                        break;
                    case 2:
                        calculatePayroll();
                        break;
                    case 3:
                        viewAttendanceRecords(scanner);
                        break;
                    case 4:
                        searchEmployeeInformation(scanner);
                        break;
                    case 5:
                        backToRoleSelection = true;
                        break;
                    default:
                        System.out.println("Invalid choice! Please choose a valid option.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    /**
     * Displays the admin menu options
     */
    private static void displayAdminMenu() {
        System.out.println("\n=======================================");
        System.out.println("                Admin Menu");
        System.out.println("=======================================");
        System.out.println("1. View Employee List");
        System.out.println("2. Process Payroll");
        System.out.println("3. View Attendance Records");
        System.out.println("4. Search Employee Information");
        System.out.println("5. Back to Role Selection");
        System.out.println("=======================================");
        System.out.print("Choose an option (1-5): ");
    }

    /**
     * Displays the employee list with their details
     */
    private static void viewEmployeeList() {
        System.out.println("\n=======================================");
        System.out.println("           Employee List");
        System.out.println("=======================================");
        
        List<Employee> employees = payroll.getEmployees();
        if (employees.isEmpty()) {
            System.out.println("No employees found in the system.");
                } else {
            for (Employee emp : employees) {
                System.out.printf("ID: %s | Name: %-20s | Position: %-15s | Supervisor: %s%n",
                    emp.getId(), emp.getName(), emp.getPosition(), emp.getSupervisor());
            }
        }
        
        System.out.println("=======================================");
    }

    /**
     * Calculates payroll for all employees
     * Prompts for date range and processes payroll
     */
    private static void calculatePayroll() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                           PAYROLL CALCULATION                              ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ Payroll is calculated based on attendance records plus fixed allowances    ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════╝");
        
        // Get payroll period from user
        LocalDate startDate = null;
        LocalDate endDate = null;
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        
        try {
            System.out.print("Enter start date (MM/DD/YYYY): ");
            String startDateStr = scanner.nextLine();
            startDate = LocalDate.parse(startDateStr, dateFormatter);
            
            System.out.print("Enter end date (MM/DD/YYYY): ");
            String endDateStr = scanner.nextLine();
            endDate = LocalDate.parse(endDateStr, dateFormatter);
            
            // Validate that end date is after start date
            if (endDate.isBefore(startDate)) {
                System.out.println("\n╔════════════════════════════════════════════════════════════════════════════╗");
                System.out.println("║                                 ERROR                                       ║");
                System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");
                System.out.println("║ End date cannot be before start date                                       ║");
                System.out.println("╚════════════════════════════════════════════════════════════════════════════╝");
                    return;
            }
        } catch (Exception e) {
            System.out.println("\n╔════════════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                                 ERROR                                       ║");
            System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");
            System.out.println("║ Invalid date format. Please use MM/DD/YYYY format (e.g., 03/15/2024)       ║");
            System.out.println("╚════════════════════════════════════════════════════════════════════════════╝");
            return;
        }
        
        // Display total days in the period
        long days = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
        
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                           PROCESSING PAYROLL                               ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ Period: %s to %s (%d days)                               ║%n", 
            startDate.format(dateFormatter), endDate.format(dateFormatter), days);
        
        // Check if payroll is null
        if (payroll == null) {
            System.out.println("║ ERROR: Payroll object is null. Creating a new instance.                    ║");
            payroll = new Payroll();
        }
        
        // Check if there are employees loaded
        List<Employee> employees = payroll.getEmployees();
        System.out.printf("║ Number of employees loaded: %-45d ║%n", employees.size());
        
        // Check if any employees have attendance records
        int employeesWithAttendance = 0;
        for (Employee emp : employees) {
            if (!emp.getAttendanceRecords().isEmpty()) {
                employeesWithAttendance++;
            }
        }
        System.out.printf("║ Employees with attendance records: %-40d ║%n", employeesWithAttendance);
        System.out.println("╚════════════════════════════════════════════════════════════════════════════╝");
        
        if (employees.isEmpty()) {
            System.out.println("\n╔════════════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                                 ERROR                                       ║");
            System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");
            System.out.println("║ No employees found in the system. Please check if data was loaded correctly.║");
            System.out.println("╚════════════════════════════════════════════════════════════════════════════╝");
            return;
        }
        
        if (employeesWithAttendance == 0) {
            System.out.println("\n╔════════════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                                WARNING                                      ║");
            System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");
            System.out.println("║ No attendance records found for any employee in the system.                ║");
            System.out.println("║ Payroll will be processed but may show zero hours worked.                  ║");
            System.out.println("╚════════════════════════════════════════════════════════════════════════════╝");
        }
        
        // Process payroll with detailed error handling
        try {
            // Display processing message
            System.out.println("\nProcessing payroll for " + employees.size() + " employees...");
            
            // Process payroll and display results
            payroll.processPayroll(startDate, endDate);
            
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine(); // Wait for user to press Enter
            } catch (Exception e) {
            System.out.println("\n╔════════════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                                 ERROR                                       ║");
            System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");
            System.out.printf("║ Error processing payroll: %-47s ║%n", e.getMessage());
            System.out.println("╚════════════════════════════════════════════════════════════════════════════╝");
            e.printStackTrace();
        }
    }

    /**
     * Views attendance records for a specific employee
     * 
     * @param scanner Scanner for user input
     */
    private static void viewAttendanceRecords(Scanner scanner) {
        System.out.print("\nEnter Employee ID: ");
        String empId = scanner.nextLine().trim();
        
        Employee emp = findEmployeeByAnyId(payroll, empId);
        
        if (emp != null) {
            System.out.println("\n=======================================");
            System.out.println("Attendance Records for " + emp.getName() + " (ID: " + emp.getId() + ")");
            System.out.println("=======================================");
            
            Map<LocalDate, Employee.AttendanceRecord> attendanceRecords = emp.getAttendanceRecords();
            
            if (attendanceRecords.isEmpty()) {
                System.out.println("No attendance records found for this employee.");
                System.out.println("Please check if the attendance record CSV file is properly loaded.");
                System.out.println("File path: " + payroll.getFilePath());
            } else {
                // Sort attendance records by date (most recent first)
                List<Map.Entry<LocalDate, Employee.AttendanceRecord>> sortedRecords = 
                    new ArrayList<>(attendanceRecords.entrySet());
                sortedRecords.sort(Map.Entry.<LocalDate, Employee.AttendanceRecord>comparingByKey().reversed());
                
                // Format for displaying time and date
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy (EEE)");
                
                System.out.println("Date           | Login Time | Logout Time | Hours Worked");
                System.out.println("-------------------------------------------------------");
                
                double totalHours = 0.0;
                int daysPresent = 0;
                int daysLate = 0;
                int daysOvertime = 0;
                LocalTime regularStartTime = LocalTime.of(8, 0); // 8:00 AM
                LocalTime regularEndTime = LocalTime.of(17, 0);  // 5:00 PM
                
                for (Map.Entry<LocalDate, Employee.AttendanceRecord> entry : sortedRecords) {
                    LocalDate date = entry.getKey();
                    Employee.AttendanceRecord record = entry.getValue();
                    
                    String loginTime = record.getLoginTime() != null ? 
                        record.getLoginTime().format(timeFormatter) : "Not recorded";
                    
                    String logoutTime = record.getLogoutTime() != null ? 
                        record.getLogoutTime().format(timeFormatter) : "Not recorded";
                    
                    double hoursWorked = record.getHoursWorked();
                    
                    // Check if employee was late
                    boolean isLate = record.getLoginTime() != null && 
                                    record.getLoginTime().isAfter(regularStartTime);
                    
                    // Check if employee did overtime
                    boolean didOvertime = record.getLogoutTime() != null && 
                                         record.getLogoutTime().isAfter(regularEndTime);
                    
                    if (hoursWorked > 0) {
                        totalHours += hoursWorked;
                        daysPresent++;
                        
                        if (isLate) daysLate++;
                        if (didOvertime) daysOvertime++;
                    }
                    
                    // Add indicators for late arrival or overtime
                    String statusIndicator = "";
                    if (isLate) statusIndicator += "L";
                    if (didOvertime) statusIndicator += "O";
                    if (!statusIndicator.isEmpty()) statusIndicator = " [" + statusIndicator + "]";
                    
                    System.out.printf("%-14s | %-10s | %-11s | %6.2f hours%s%n", 
                        date.format(dateFormatter), loginTime, logoutTime, hoursWorked, statusIndicator);
                }
                
                System.out.println("-------------------------------------------------------");
                System.out.printf("Total Days Present: %d days%n", daysPresent);
                System.out.printf("Days Late: %d days%n", daysLate);
                System.out.printf("Days with Overtime: %d days%n", daysOvertime);
                System.out.printf("Total Hours Worked: %.2f hours%n", totalHours);
                
                // Calculate average hours per day
                if (daysPresent > 0) {
                    double averageHours = totalHours / daysPresent;
                    System.out.printf("Average Hours per Day: %.2f hours%n", averageHours);
                }
                
                System.out.println("-------------------------------------------------------");
                System.out.println("Legend: [L] = Late, [O] = Overtime");
            }
            
            System.out.println("=======================================");
        } else {
            System.out.println("Employee not found! Please check the ID.");
        }
    }

    /**
     * Displays and handles the employee login menu
     * 
     * @param payroll The payroll system instance
     * @param scanner Scanner for user input
     */
    private static void employeeLoginMenu(Payroll payroll, Scanner scanner) {
        System.out.println("\n=======================================");
        System.out.println("            Employee Login");
        System.out.println("=======================================");
        
        // Get employee ID
        System.out.print("Enter Employee ID: ");
        String idInput = scanner.nextLine().trim();
        
        // Find employee using the helper method
        Employee employee = findEmployeeByAnyId(payroll, idInput);
        
        if (employee != null) {
            // Employee found, verify with name
            System.out.print("Enter your name: ");
            String name = scanner.nextLine().trim();
            
            // Check if name matches (more flexible matching)
            if (name.isEmpty() || employee.getName().toLowerCase().contains(name.toLowerCase()) || 
                (employee.getFirstName() + " " + employee.getLastName()).toLowerCase().contains(name.toLowerCase())) {
                employeeMenu(employee, scanner);
            } else {
                System.out.println("Name does not match the employee record.");
            }
        } else {
            System.out.println("Employee not found. Please check your ID.");
        }
    }

    /**
     * Displays and handles the employee menu options
     * 
     * @param employee The authenticated employee
     * @param scanner Scanner for user input
     */
    private static void employeeMenu(Employee employee, Scanner scanner) {
        boolean backToRoleSelection = false;
        
        while (!backToRoleSelection) {
            displayEmployeeMenu(employee);
        
        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                        displayEmployeeInformation(employee);
                        scanner.nextLine(); // Wait for Enter key
                    break;
                case 2:
                        viewPayroll(employee, scanner);
                    break;
                case 3:
                        backToRoleSelection = true;
                        break;
                default:
                    System.out.println("Invalid choice! Please choose a valid option.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input! Please enter a number.");
            scanner.nextLine(); // Clear invalid input
        }
    }
    }

    /**
     * Displays the employee menu with grid-style formatting
     * 
     * @param employee The employee to display menu for
     */
    private static void displayEmployeeMenu(Employee employee) {
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                              EMPLOYEE MENU                                  ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ Welcome, %-61s ║%n", employee.getName() + "!");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║  [1] View My Information                                                   ║");
        System.out.println("║  [2] View My Payroll                                                      ║");
        System.out.println("║  [3] Back to Role Selection                                               ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║  Please select an option (1-3):                                           ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════╝");
    }

    /**
     * Displays employee information with grid-style formatting
     * 
     * @param employee The employee to display information for
     */
    private static void displayEmployeeInformation(Employee employee) {
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                         EMPLOYEE INFORMATION                               ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║  Employee ID: %-58s ║%n", employee.getId());
        System.out.printf("║  Name: %-64s ║%n", employee.getName());
        System.out.printf("║  Birthday: %-60s ║%n", employee.getBirthday());
        System.out.printf("║  Address: %-61s ║%n", employee.getAddress());
        System.out.printf("║  Phone Number: %-57s ║%n", employee.getPhoneNumber());
        System.out.printf("║  Position: %-60s ║%n", employee.getPosition());
        System.out.printf("║  Department: %-58s ║%n", employee.getDepartment());
        System.out.printf("║  Supervisor: %-58s ║%n", employee.getSupervisor());
        System.out.printf("║  Status: %-62s ║%n", employee.getStatus());
        System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║                       GOVERNMENT INFORMATION                              ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║  SSS Number: %-58s ║%n", employee.getSssNumber());
        System.out.printf("║  PhilHealth Number: %-53s ║%n", employee.getPhilHealthNumber());
        System.out.printf("║  Pag-IBIG Number: %-54s ║%n", employee.getPagIbigNumber());
        System.out.printf("║  TIN: %-66s ║%n", employee.getTin());
        System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║                       COMPENSATION INFORMATION                            ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║  Hourly Rate: PHP %-54.2f ║%n", employee.getHourlyRate());
        System.out.println("╚════════════════════════════════════════════════════════════════════════════╝");
        
        System.out.println("\nPress Enter to continue...");
    }

    /**
     * Displays payroll details for a specific employee
     * 
     * @param employee The employee to view payroll for
     * @param scanner Scanner for user input
     */
    private static void viewPayroll(Employee employee, Scanner scanner) {
        // Get date range for payroll calculation
        LocalDate startDate = null;
        LocalDate endDate = null;
        
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                              PAYROLL VIEWER                                 ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ Please enter the date range for payroll calculation:                       ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════╝");
        
        while (startDate == null) {
            System.out.print("Enter start date (MM/DD/YYYY): ");
            String startDateStr = scanner.next();
            try {
                startDate = parseDate(startDateStr.replace("/", "-"));
            } catch (Exception e) {
                System.out.println("Invalid date format. Please use MM/DD/YYYY format.");
            }
        }
        
        while (endDate == null) {
            System.out.print("Enter end date (MM/DD/YYYY): ");
            String endDateStr = scanner.next();
            try {
                endDate = parseDate(endDateStr.replace("/", "-"));
                if (endDate.isBefore(startDate)) {
                    System.out.println("End date cannot be before start date.");
                    endDate = null;
                }
            } catch (Exception e) {
                System.out.println("Invalid date format. Please use MM/DD/YYYY format.");
            }
        }
        
        // Format dates for display
        DateTimeFormatter displayFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String startDateFormatted = startDate.format(displayFormat);
        String endDateFormatted = endDate.format(displayFormat);
        
        // Display employee information header
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                              PAYROLL STATEMENT                              ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ Employee ID: %-58s ║%n", employee.getId());
        System.out.printf("║ Name: %-64s ║%n", employee.getName());
        System.out.printf("║ Position: %-60s ║%n", employee.getPosition());
        System.out.printf("║ Department: %-58s ║%n", employee.getDepartment());
        System.out.printf("║ Pay Period: %s - %-47s ║%n", startDateFormatted, endDateFormatted);
        System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");
        
        // Get attendance records and sort by date
        Map<LocalDate, Employee.AttendanceRecord> attendanceRecords = employee.getAttendanceRecords();
        List<Map.Entry<LocalDate, Employee.AttendanceRecord>> sortedRecords = 
            new ArrayList<>(attendanceRecords.entrySet());
        sortedRecords.sort(Map.Entry.comparingByKey());
        
        // Format for displaying time
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        
        // Track total hours worked in the period
        double totalHoursWorked = 0.0;
        int daysPresent = 0;
        
        // Display attendance details section
        System.out.println("║                            ATTENDANCE DETAILS                               ║");
        System.out.println("╠════════════════════╦═══════════════╦════════════════╦═════════════════════╣");
        System.out.println("║       Date         ║   Login Time  ║   Logout Time  ║    Hours Worked     ║");
        System.out.println("╠════════════════════╬═══════════════╬════════════════╬═════════════════════╣");
        
        // Display each day's attendance within the period
        for (Map.Entry<LocalDate, Employee.AttendanceRecord> entry : sortedRecords) {
            LocalDate date = entry.getKey();
            
            // Only show dates within the specified range
            if (!date.isBefore(startDate) && !date.isAfter(endDate)) {
                Employee.AttendanceRecord record = entry.getValue();
                
                String loginTime = record.getLoginTime() != null ? 
                    record.getLoginTime().format(timeFormatter) : "Not recorded";
                
                String logoutTime = record.getLogoutTime() != null ? 
                    record.getLogoutTime().format(timeFormatter) : "Not recorded";
                
                double hoursWorked = record.getHoursWorked();
                if (hoursWorked > 0) {
                    totalHoursWorked += hoursWorked;
                    daysPresent++;
                }
                
                System.out.printf("║ %-18s ║ %-13s ║ %-14s ║ %17.2f ║%n", 
                    date.format(displayFormat), loginTime, logoutTime, hoursWorked);
            }
        }
        
        // Display attendance summary
        System.out.println("╠════════════════════╩═══════════════╩════════════════╩═════════════════════╣");
        System.out.printf("║ Total Days Present: %-52d ║%n", daysPresent);
        System.out.printf("║ Total Hours Worked: %-52.2f ║%n", totalHoursWorked);
        System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");
        
        // Display payroll calculation section
        System.out.println("║                           PAYROLL CALCULATION                              ║");
        System.out.println("╠════════════════════════════════════════════╦═══════════════════════════════╣");
        
        // Calculate hourly pay
        double hourlyRate = employee.getHourlyRate();
        double hourlyPay = totalHoursWorked * hourlyRate;
        
        System.out.printf("║ Hourly Rate:                               ║ PHP %,19.2f ║%n", hourlyRate);
        System.out.printf("║ Hourly Pay (%,.2f hours × PHP %,.2f):      ║ PHP %,19.2f ║%n", 
            totalHoursWorked, hourlyRate, hourlyPay);
        System.out.println("║                                            ║                           ║");
        
        // Add full allowances if employee was present at least one day
        double riceSubsidy = daysPresent > 0 ? employee.getRiceSubsidy() : 0;
        double phoneAllowance = daysPresent > 0 ? employee.getPhoneAllowance() : 0;
        double clothingAllowance = daysPresent > 0 ? employee.getClothingAllowance() : 0;
        
        System.out.println("║ Fixed Allowances:                          ║                           ║");
        System.out.printf("║   ├─ Rice Subsidy:                         ║ PHP %,19.2f ║%n", riceSubsidy);
        System.out.printf("║   ├─ Phone Allowance:                      ║ PHP %,19.2f ║%n", phoneAllowance);
        System.out.printf("║   └─ Clothing Allowance:                   ║ PHP %,19.2f ║%n", clothingAllowance);
        System.out.println("║                                            ║                           ║");
        
        // Add allowances to gross pay
        double totalAllowances = riceSubsidy + phoneAllowance + clothingAllowance;
        double grossIncome = hourlyPay + totalAllowances;
        
        System.out.printf("║ Total Allowances:                          ║ PHP %,19.2f ║%n", totalAllowances);
        System.out.println("╠════════════════════════════════════════════╬═══════════════════════════════╣");
        System.out.printf("║ GROSS INCOME:                              ║ PHP %,19.2f ║%n", grossIncome);
        System.out.println("╠════════════════════════════════════════════╩═══════════════════════════════╣");
        
        // Display deductions section
        System.out.println("║                              DEDUCTIONS                                    ║");
        System.out.println("╠════════════════════════════════════════════╦═══════════════════════════════╣");
        
        // Calculate deductions based on gross income
        double sssDeduction = employee.calculateSSSDeduction(grossIncome);
        double philHealthDeduction = employee.calculatePhilHealthDeduction(grossIncome);
        double pagIbigDeduction = employee.calculatePagIBIGDeduction(grossIncome);
        double basicDeduction = sssDeduction + philHealthDeduction + pagIbigDeduction;
        double taxDeduction = employee.calculateTaxDeduction(grossIncome);
        double totalDeductions = basicDeduction + taxDeduction;
        
        System.out.println("║ Mandatory Deductions:                      ║                           ║");
        System.out.printf("║   ├─ SSS:                                  ║ PHP %,19.2f ║%n", sssDeduction);
        System.out.printf("║   ├─ PhilHealth:                           ║ PHP %,19.2f ║%n", philHealthDeduction);
        System.out.printf("║   ├─ Pag-IBIG:                             ║ PHP %,19.2f ║%n", pagIbigDeduction);
        System.out.printf("║   └─ Withholding Tax:                      ║ PHP %,19.2f ║%n", taxDeduction);
        System.out.println("║                                            ║                           ║");
        System.out.printf("║ TOTAL DEDUCTIONS:                          ║ PHP %,19.2f ║%n", totalDeductions);
        System.out.println("╠════════════════════════════════════════════╩═══════════════════════════════╣");
        
        // Calculate and display net pay
        double netPay = grossIncome - totalDeductions;
        
        System.out.println("║                               NET PAY                                      ║");
        System.out.println("╠════════════════════════════════════════════╦═══════════════════════════════╣");
        System.out.printf("║ NET PAY:                                   ║ PHP %,19.2f ║%n", netPay);
        System.out.println("╚════════════════════════════════════════════╩═══════════════════════════════╝");
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine(); // Consume the remaining newline
        scanner.nextLine(); // Wait for user to press Enter
    }

    /**
     * Parses a date string in MM-DD-YYYY format to a LocalDate object
     * 
     * @param dateStr Date string in MM-DD-YYYY format
     * @return LocalDate object representing the parsed date
     * @throws Exception if the date format is invalid
     */
    private static LocalDate parseDate(String dateStr) throws Exception {
        // Check if the string matches MM-DD-YYYY pattern
        if (!dateStr.matches("\\d{1,2}-\\d{1,2}-\\d{4}")) {
            throw new Exception("Invalid date format");
        }
        
        String[] parts = dateStr.split("-");
        int month = Integer.parseInt(parts[0]);
        int day = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);
        
        // Validate month and day
        if (month < 1 || month > 12) {
            throw new Exception("Invalid month: " + month);
        }
        
        // Simple validation for days in month (not accounting for leap years)
        int[] daysInMonth = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        // Adjust February for leap years
        if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) {
            daysInMonth[2] = 29;
        }
        
        if (day < 1 || day > daysInMonth[month]) {
            throw new Exception("Invalid day for month: " + day);
        }
        
        return LocalDate.of(year, month, day);
    }

    /**
     * Displays the main menu options
     */
    public static void showMainMenu() {
        System.out.println("\n=== MotorPH Payroll System ===");
        System.out.println("[1] View Employee List");
        System.out.println("[2] Calculate Payroll");
        System.out.println("[3] View Attendance Records");
        System.out.println("[4] Exit");
        System.out.print("Enter choice: ");
    }

    /**
     * Processes the main menu selection
     * 
     * @param scanner Scanner for user input
     */
    public static void processMainMenu(Scanner scanner) {
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        switch (choice) {
            case 1:
                viewEmployeeList();
                break;
            case 2:
                calculatePayroll();
                break;
            case 3:
                viewAttendanceRecords(scanner);
                break;
            case 4:
                System.out.println("Thank you for using MotorPH Payroll System!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    /**
     * Displays employee information from the CSV file
     * 
     * @param employee The employee to display information for
     */
    private static void displayEmployeeInfo(Employee employee) {
        System.out.println("\n=======================================");
        System.out.println("         Employee Information");
        System.out.println("=======================================");
        System.out.println("Employee ID: " + employee.getId());
        System.out.println("Name: " + employee.getName());
        
        // Personal Information
        if (employee.getBirthday() != null) {
            System.out.println("Birthday: " + employee.getBirthday());
        }
        if (employee.getAddress() != null) {
            System.out.println("Address: " + employee.getAddress());
        }
        if (employee.getPhoneNumber() != null) {
            System.out.println("Phone Number: " + employee.getPhoneNumber());
        }
        
        // Employment Information
        System.out.println("Position: " + employee.getPosition());
        System.out.println("Department: " + employee.getDepartment());
        System.out.println("Supervisor: " + employee.getSupervisor());
        if (employee.getStatus() != null) {
            System.out.println("Status: " + employee.getStatus());
        }
        
        // Government IDs
        System.out.println("SSS Number: " + employee.getSssNumber());
        System.out.println("PhilHealth Number: " + employee.getPhilHealthNumber());
        System.out.println("Pag-IBIG Number: " + employee.getPagIbigNumber());
        System.out.println("TIN: " + employee.getTin());
        
        // Salary Information
        System.out.println("Hourly Rate: PHP " + employee.getHourlyRate());
        System.out.println("=======================================");
    }

    /**
     * Searches for and displays detailed employee information
     * 
     * @param scanner Scanner for user input
     */
    private static void searchEmployeeInformation(Scanner scanner) {
        System.out.println("\n=======================================");
        System.out.println("        Search Employee Information");
        System.out.println("=======================================");
        System.out.println("Search by:");
        System.out.println("1. Employee ID");
        System.out.println("2. Employee Name");
        System.out.print("Choose an option (1-2): ");
        
        try {
            int searchOption = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            Employee employee = null;
            
            switch (searchOption) {
                case 1:
                    System.out.print("Enter Employee ID: ");
                    String empId = scanner.nextLine().trim();
                    
                    // Use the helper method to find employee
                    employee = findEmployeeByAnyId(payroll, empId);
                    break;
                    
                case 2:
                    System.out.print("Enter Employee Name: ");
                    String empName = scanner.nextLine().trim();
                    
                    // Search through all employees for name match (more flexible)
                    List<Employee> employees = payroll.getEmployees();
                    for (Employee emp : employees) {
                        if (emp.getName().toLowerCase().contains(empName.toLowerCase()) || 
                            (emp.getFirstName() + " " + emp.getLastName()).toLowerCase().contains(empName.toLowerCase())) {
                            employee = emp;
                            break;
                        }
                    }
                    break;
                    
                default:
                    System.out.println("Invalid option!");
                    return;
            }
            
            if (employee != null) {
                displayDetailedEmployeeInfo(employee);
            } else {
                System.out.println("Employee not found! Please check your search criteria.");
            }
            
        } catch (InputMismatchException e) {
            System.out.println("Invalid input! Please enter a number.");
            scanner.nextLine(); // Clear invalid input
        }
    }
    
    /**
     * Displays detailed employee information including government IDs and salary details
     * 
     * @param employee The employee to display information for
     */
    private static void displayDetailedEmployeeInfo(Employee employee) {
        System.out.println("\n=======================================");
        System.out.println("       Detailed Employee Information");
        System.out.println("=======================================");
        System.out.println("Employee ID: " + employee.getId());
        System.out.println("Name: " + employee.getName());
        
        // Personal Information
        System.out.println("=======================================");
        System.out.println("Personal Information:");
        if (employee.getBirthday() != null) {
            System.out.println("Birthday: " + employee.getBirthday());
        }
        if (employee.getAddress() != null) {
            System.out.println("Address: " + employee.getAddress());
        }
        if (employee.getPhoneNumber() != null) {
            System.out.println("Phone Number: " + employee.getPhoneNumber());
        }
        
        // Employment Information
        System.out.println("=======================================");
        System.out.println("Employment Information:");
        System.out.println("Position: " + employee.getPosition());
        System.out.println("Department: " + employee.getDepartment());
        System.out.println("Supervisor: " + employee.getSupervisor());
        if (employee.getStatus() != null) {
            System.out.println("Status: " + employee.getStatus());
        }
        
        // Government IDs
        System.out.println("=======================================");
        System.out.println("Government IDs:");
        System.out.println("SSS Number: " + employee.getSssNumber());
        System.out.println("PhilHealth Number: " + employee.getPhilHealthNumber());
        System.out.println("Pag-IBIG Number: " + employee.getPagIbigNumber());
        System.out.println("TIN: " + employee.getTin());
        
        // Salary Information
            System.out.println("=======================================");
        System.out.println("Compensation Information:");
        System.out.println("Basic Salary: PHP " + String.format("%,.2f", employee.getBasicSalary()));
        System.out.println("Rice Subsidy: PHP " + String.format("%,.2f", employee.getRiceSubsidy()));
        System.out.println("Phone Allowance: PHP " + String.format("%,.2f", employee.getPhoneAllowance()));
        System.out.println("Clothing Allowance: PHP " + String.format("%,.2f", employee.getClothingAllowance()));
        System.out.println("Gross Semi-monthly Rate: PHP " + String.format("%,.2f", employee.getGrossSemiMonthlyRate()));
        System.out.println("Hourly Rate: PHP " + String.format("%,.2f", employee.getHourlyRate()));
        
        // Display attendance records if available
        Map<LocalDate, Employee.AttendanceRecord> attendanceRecords = employee.getAttendanceRecords();
        if (!attendanceRecords.isEmpty()) {
            System.out.println("=======================================");
            System.out.println("Recent Attendance Records:");
            
            // Sort attendance records by date (most recent first)
            List<Map.Entry<LocalDate, Employee.AttendanceRecord>> sortedRecords = 
                new ArrayList<>(attendanceRecords.entrySet());
            sortedRecords.sort(Map.Entry.<LocalDate, Employee.AttendanceRecord>comparingByKey().reversed());
            
            // Format for displaying time
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
            
            // Display the 5 most recent records
            for (int i = 0; i < Math.min(5, sortedRecords.size()); i++) {
                Map.Entry<LocalDate, Employee.AttendanceRecord> entry = sortedRecords.get(i);
                LocalDate date = entry.getKey();
                Employee.AttendanceRecord record = entry.getValue();
                
                String loginTime = record.getLoginTime() != null ? 
                    record.getLoginTime().format(timeFormatter) : "Not recorded";
                
                String logoutTime = record.getLogoutTime() != null ? 
                    record.getLogoutTime().format(timeFormatter) : "Not recorded";
                
                System.out.printf("%s: Login: %s, Logout: %s, Hours: %.2f%n", 
                    date, loginTime, logoutTime, record.getHoursWorked());
            }
        }
        
        System.out.println("=======================================");
    }

    /**
     * Helper method to find an employee by ID using multiple search methods
     * Tries different ID formats to increase chances of finding the employee
     * 
     * @param payroll The payroll system instance
     * @param idInput The ID input from user
     * @return The found employee or null if not found
     */
    private static Employee findEmployeeByAnyId(Payroll payroll, String idInput) {
        // First try direct string match
        Employee employee = payroll.findEmployeeById(idInput);
        if (employee != null) {
            return employee;
        }
        
        // Try with leading zeros (format to 5 digits)
        try {
            int idNum = Integer.parseInt(idInput);
            String formattedId = String.format("%05d", idNum);
            employee = payroll.findEmployeeById(formattedId);
            if (employee != null) {
                return employee;
            }
        } catch (NumberFormatException e) {
            // Not a number, skip this attempt
        }
        
        // Try without leading zeros
        try {
            int idNum = Integer.parseInt(idInput);
            employee = payroll.findEmployeeByIdAndName(idNum, "");
            if (employee != null) {
                return employee;
            }
        } catch (NumberFormatException e) {
            // Not a number, skip this attempt
        }
        
        // Try searching through all employees manually
        List<Employee> employees = payroll.getEmployees();
        for (Employee emp : employees) {
            // Try matching with or without leading zeros
            if (emp.getId().equals(idInput) || 
                emp.getId().equals(String.format("%05d", Integer.parseInt(idInput))) || 
                Integer.toString(Integer.parseInt(emp.getId())).equals(idInput)) {
                return emp;
            }
        }
        
        return null; // Not found with any method
    }
}
