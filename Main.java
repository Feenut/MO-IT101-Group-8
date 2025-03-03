import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    private static final int MAX_EMPLOYEES = 100;

    public static void main(String[] args) {
        Payroll payroll = new Payroll();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=======================================");
        System.out.println("          Welcome to the Payroll System");
        System.out.println("=======================================");
        boolean exit = false;

        while (!exit) {
            System.out.println("\n=======================================");
            System.out.println("          Select Role to Log In");
            System.out.println("=======================================");
            System.out.println("1. Admin");
            System.out.println("2. Employee");
            System.out.println("3. Exit");
            System.out.println("=======================================");
            System.out.print("Choose an option (1-3): ");

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

    private static void adminMenu(Payroll payroll, Scanner scanner) {
        boolean backToRoleSelection = false;

        while (!backToRoleSelection) {
            System.out.println("\n=======================================");
            System.out.println("                Admin Menu");
            System.out.println("=======================================");
            System.out.println("1. Add Employees");
            System.out.println("2. Enter Hours Worked");
            System.out.println("3. Process Payroll");
            System.out.println("4. Inventory Management");
            System.out.println("5. Back to Role Selection");
            System.out.println("=======================================");
            System.out.print("Choose an option (1-5): ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        addEmployees(payroll, scanner);
                        break;
                    case 2:
                        enterHoursWorked(payroll, scanner);
                        break;
                    case 3:
                        processPayrollMenu(payroll, scanner);
                        break;
                    case 4:
                        ShelfWatch.displayMenu();
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

    private static void addEmployees(Payroll payroll, Scanner scanner) {
        System.out.println("Enter the number of employees to add (maximum " + MAX_EMPLOYEES + "):");

        int numEmployees;
        while (true) {
            try {
                numEmployees = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                if (numEmployees > 0 && numEmployees <= MAX_EMPLOYEES) {
                    break;
                } else {
                    System.out.println("Please enter a number between 1 and " + MAX_EMPLOYEES);
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a valid number.");
                scanner.nextLine(); // Clear invalid input
            }
        }

        for (int i = 0; i < numEmployees; i++) {
            System.out.println("Enter details for employee " + (i + 1) + ":");
            System.out.print("Name: ");
            String name = scanner.nextLine();

            int id;
            while (true) {
                System.out.print("ID: ");
                try {
                    id = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input! Please enter a valid number.");
                    scanner.nextLine(); // Clear invalid input
                }
            }

            System.out.print("Position: ");
            String position = scanner.nextLine();
            System.out.print("Department: ");
            String department = scanner.nextLine();
            System.out.print("SSS Number: ");
            String sssNumber = scanner.nextLine();
            System.out.print("PhilHealth Number: ");
            String philHealthNumber = scanner.nextLine();
            System.out.print("Pag-IBIG Number: ");
            String pagIbigNumber = scanner.nextLine();
            System.out.print("TIN: ");
            String tin = scanner.nextLine();

            double hourlyRate;
            while (true) {
                System.out.print("Hourly Rate (PHP): ");
                try {
                    hourlyRate = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input! Please enter a valid hourly rate.");
                    scanner.nextLine(); // Clear invalid input
                }
            }

            payroll.addEmployee(new Employee(name, id, position, department, sssNumber, philHealthNumber, pagIbigNumber, tin, hourlyRate));
        }
    }

    private static void enterHoursWorked(Payroll payroll, Scanner scanner) {
        if (payroll.getEmployees().isEmpty()) {
            System.out.println("No employees in the system. Please add employees first.");
            return;
        }
        
        System.out.println("\n=======================================");
        System.out.println("          Enter Hours Worked");
        System.out.println("=======================================");
        System.out.println("1. Enter for a single date");
        System.out.println("2. Enter for a date range");
        System.out.println("3. Back to Admin Menu");
        System.out.println("=======================================");
        System.out.print("Choose an option (1-3): ");
        
        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    enterHoursForSingleDate(payroll, scanner);
                    break;
                case 2:
                    enterHoursForDateRange(payroll, scanner);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice! Please choose a valid option.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input! Please enter a number.");
            scanner.nextLine(); // Clear invalid input
        }
    }

    private static void enterHoursForSingleDate(Payroll payroll, Scanner scanner) {
        System.out.println("Enter hours worked for employees for a single date:");

        LocalDate date = null;
        while (date == null) {
            System.out.print("Enter date (YYYY-MM-DD): ");
            String dateStr = scanner.next();
            scanner.nextLine(); // Consume newline
            try {
                date = LocalDate.parse(dateStr);
            } catch (Exception e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD format.");
            }
        }
        
        for (Employee employee : payroll.getEmployees()) {
            double hours = 0;
            boolean validHours = false;
            
            while (!validHours) {
                System.out.print("Employee ID " + employee.getId() + " (" + employee.getName() + ") hours: ");
                try {
                    hours = scanner.nextDouble();
                    if (hours < 0) {
                        System.out.println("Hours cannot be negative. Please enter a valid number.");
                    } else {
                        validHours = true;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input! Please enter a valid number of hours.");
                    scanner.nextLine(); // Clear invalid input
                }
            }
            
            employee.addHoursWorked(date, hours);
            System.out.println("Hours recorded for " + employee.getName() + ".");
        }
        
        payroll.saveEmployees();
        System.out.println("All hours have been saved.");
    }

    private static void enterHoursForDateRange(Payroll payroll, Scanner scanner) {
        System.out.println("Enter hours worked for employees for a date range:");

        LocalDate startDate = null;
        while (startDate == null) {
            System.out.print("Enter start date (YYYY-MM-DD): ");
            String dateStr = scanner.next();
            scanner.nextLine(); // Consume newline
            try {
                startDate = LocalDate.parse(dateStr);
            } catch (Exception e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD format.");
            }
        }
        
        LocalDate endDate = null;
        while (endDate == null) {
            System.out.print("Enter end date (YYYY-MM-DD): ");
            String dateStr = scanner.next();
            scanner.nextLine(); // Consume newline
            try {
                endDate = LocalDate.parse(dateStr);
                if (endDate.isBefore(startDate)) {
                    System.out.println("End date cannot be before start date.");
                    endDate = null;
                }
            } catch (Exception e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD format.");
            }
        }
        
        System.out.println("Enter daily hours for each employee in the date range " + 
                          startDate + " to " + endDate + ":");
        
        for (Employee employee : payroll.getEmployees()) {
            System.out.println("\nEntering hours for: " + employee.getName() + " (ID: " + employee.getId() + ")");
            
            System.out.print("Do you want to enter the same hours for all days? (Y/N): ");
            String sameHoursChoice = scanner.nextLine().trim().toUpperCase();
            
            if (sameHoursChoice.equals("Y")) {
                double hours = 0;
                boolean validHours = false;
                
                while (!validHours) {
                    System.out.print("Enter hours for each day: ");
                    try {
                        hours = scanner.nextDouble();
                        scanner.nextLine(); // Consume newline
                        if (hours < 0) {
                            System.out.println("Hours cannot be negative. Please enter a valid number.");
                        } else {
                            validHours = true;
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input! Please enter a valid number of hours.");
                        scanner.nextLine(); // Clear invalid input
                    }
                }
                
                // Add the same hours for each day in the range
                LocalDate currentDate = startDate;
                while (!currentDate.isAfter(endDate)) {
                    employee.addHoursWorked(currentDate, hours);
                    currentDate = currentDate.plusDays(1);
                }
                
                System.out.println("Added " + hours + " hours for each day from " + 
                                  startDate + " to " + endDate + " for " + employee.getName());
            } else {
                // Add different hours for each day
                LocalDate currentDate = startDate;
                while (!currentDate.isAfter(endDate)) {
                    double hours = 0;
                    boolean validHours = false;
                    
                    while (!validHours) {
                        System.out.print("Hours for " + currentDate + ": ");
                        try {
                            hours = scanner.nextDouble();
                            scanner.nextLine(); // Consume newline
                            if (hours < 0) {
                                System.out.println("Hours cannot be negative. Please enter a valid number.");
                            } else {
                                validHours = true;
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input! Please enter a valid number of hours.");
                            scanner.nextLine(); // Clear invalid input
                        }
                    }
                    
                    employee.addHoursWorked(currentDate, hours);
                    currentDate = currentDate.plusDays(1);
                }
                
                System.out.println("Hours recorded for " + employee.getName() + " for each day in the range.");
            }
        }
        
        payroll.saveEmployees();
        System.out.println("All hours have been saved successfully.");
    }

    private static void processPayrollMenu(Payroll payroll, Scanner scanner) {
        System.out.println("\n=======================================");
        System.out.println("            Payroll Processing");
        System.out.println("=======================================");
        System.out.println("1. Process Regular Payroll");
        System.out.println("2. Process Weekly Payroll");
        System.out.println("3. Back to Admin Menu");
        System.out.println("=======================================");
        System.out.print("Choose an option (1-3): ");
        
        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    processRegularPayroll(payroll, scanner);
                    break;
                case 2:
                    processWeeklyPayroll(payroll, scanner);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice! Please choose a valid option.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input! Please enter a number.");
            scanner.nextLine(); // Clear invalid input
        }
    }

    private static void processRegularPayroll(Payroll payroll, Scanner scanner) {
        LocalDate startDate = null;
        LocalDate endDate = null;
        
        while (startDate == null) {
            System.out.print("Enter start date (YYYY-MM-DD): ");
            String startDateStr = scanner.next();
            try {
                startDate = LocalDate.parse(startDateStr);
            } catch (Exception e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD format.");
            }
        }
        
        while (endDate == null) {
            System.out.print("Enter end date (YYYY-MM-DD): ");
            String endDateStr = scanner.next();
            try {
                endDate = LocalDate.parse(endDateStr);
                if (endDate.isBefore(startDate)) {
                    System.out.println("End date cannot be before start date.");
                    endDate = null;
                }
            } catch (Exception e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD format.");
            }
        }
        
        payroll.processPayroll(startDate, endDate);
    }

    private static void processWeeklyPayroll(Payroll payroll, Scanner scanner) {
        LocalDate startDate = null;
        LocalDate endDate = null;
        
        while (startDate == null) {
            System.out.print("Enter start date (YYYY-MM-DD): ");
            String startDateStr = scanner.next();
            try {
                startDate = LocalDate.parse(startDateStr);
            } catch (Exception e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD format.");
            }
        }
        
        while (endDate == null) {
            System.out.print("Enter end date (YYYY-MM-DD): ");
            String endDateStr = scanner.next();
            try {
                endDate = LocalDate.parse(endDateStr);
                if (endDate.isBefore(startDate)) {
                    System.out.println("End date cannot be before start date.");
                    endDate = null;
                }
            } catch (Exception e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD format.");
            }
        }
        
        payroll.processWeeklyPayroll(startDate, endDate);
    }

    private static void viewPayroll(Employee employee, Scanner scanner) {
        LocalDate startDate = null;
        LocalDate endDate = null;
        
        while (startDate == null) {
            System.out.print("Enter start date (YYYY-MM-DD): ");
            String startDateStr = scanner.next();
            try {
                startDate = LocalDate.parse(startDateStr);
            } catch (Exception e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD format.");
            }
        }
        
        while (endDate == null) {
            System.out.print("Enter end date (YYYY-MM-DD): ");
            String endDateStr = scanner.next();
            try {
                endDate = LocalDate.parse(endDateStr);
                if (endDate.isBefore(startDate)) {
                    System.out.println("End date cannot be before start date.");
                    endDate = null;
                }
            } catch (Exception e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD format.");
            }
        }
        
        System.out.println("Displaying payroll details for: " + employee.getName());
        double pay = employee.calculatePay(startDate, endDate);
        double weeklySalary = employee.calculateWeeklySalary(startDate, endDate);
        double basicDeduction = employee.calculateBasicDeduction(pay);
        double taxDeduction = employee.calculateTaxDeduction(pay);
        double totalDeductions = basicDeduction + taxDeduction;
        double netPay = pay - totalDeductions;

        System.out.println("=======================================");
        System.out.println("Employee ID: " + employee.getId());
        System.out.println("Employee Name: " + employee.getName());
        System.out.println("Position: " + employee.getPosition());
        System.out.println("Department: " + employee.getDepartment());
        System.out.println("=======================================");
        System.out.println("Gross Pay: PHP " + String.format("%.2f", pay));
        System.out.println("Weekly Salary: PHP " + String.format("%.2f", weeklySalary));
        System.out.println("=======================================");
        System.out.println("Deductions:");
        System.out.println("  Basic Deductions:");
        System.out.println("    SSS: PHP " + String.format("%.2f", employee.calculateSSSDeduction(pay)));
        System.out.println("    PhilHealth: PHP " + String.format("%.2f", employee.calculatePhilHealthDeduction(pay)));
        System.out.println("    Pag-IBIG: PHP " + String.format("%.2f", employee.calculatePagIBIGDeduction(pay)));
        System.out.println("  Total Basic Deductions: PHP " + String.format("%.2f", basicDeduction));
        System.out.println("  Tax: PHP " + String.format("%.2f", taxDeduction));
        System.out.println("  Total Deductions: PHP " + String.format("%.2f", totalDeductions));
        System.out.println("=======================================");
        System.out.println("Net Pay: PHP " + String.format("%.2f", netPay));
        System.out.println("=======================================");
    }

    private static void employeeLoginMenu(Payroll payroll, Scanner scanner) {
        System.out.println("\n=======================================");
        System.out.println("            Employee Login");
        System.out.println("=======================================");
        
        System.out.print("Enter Employee ID: ");
        int id;
        try {
            id = scanner.nextInt();
            scanner.nextLine(); // Consume newline
        } catch (InputMismatchException e) {
            System.out.println("Invalid ID format. Please enter a number.");
            scanner.nextLine(); // Clear the invalid input
            return;
        }
        
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        
        Employee employee = payroll.findEmployeeByIdAndName(id, name);
        
        if (employee != null) {
            employeeMenu(employee, scanner);
        } else {
            System.out.println("Employee not found. Please check your ID and name.");
        }
    }

    private static void employeeMenu(Employee employee, Scanner scanner) {
        boolean backToRoleSelection = false;
        
        while (!backToRoleSelection) {
            System.out.println("\n=======================================");
            System.out.println("            Employee Menu");
            System.out.println("=======================================");
            System.out.println("Welcome, " + employee.getName() + "!");
            System.out.println("1. View My Payroll");
            System.out.println("2. Back to Role Selection");
            System.out.println("=======================================");
            System.out.print("Choose an option (1-2): ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                
                switch (choice) {
                    case 1:
                        viewPayroll(employee, scanner);
                        break;
                    case 2:
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
}
