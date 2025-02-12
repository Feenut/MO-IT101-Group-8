import java.time.LocalDate;
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
            System.out.println("4. Back to Role Selection");
            System.out.println("=======================================");
            System.out.print("Choose an option (1-4): ");
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
                    payroll.processPayroll();
                    break;
                case 4:
                    backToRoleSelection = true;
                    break;
                default:
                    System.out.println("Invalid choice! Please choose a valid option.");
            }
        }
    }

    private static void employeeLoginMenu(Payroll payroll, Scanner scanner) {
        boolean backToRoleSelection = false;

        while (!backToRoleSelection) {
            System.out.println("\n=======================================");
            System.out.println("            Employee Login");
            System.out.println("=======================================");
            System.out.println("1. Log in");
            System.out.println("2. Back to Role Selection");
            System.out.println("=======================================");
            System.out.print("Choose an option (1-2): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    boolean authenticated = false;
                    Employee loggedInEmployee = null;

                    while (!authenticated) {
                        System.out.print("Enter your name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter your employee ID: ");
                        int id = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        loggedInEmployee = payroll.findEmployeeByIdAndName(id, name);

                        if (loggedInEmployee != null) {
                            authenticated = true;
                            employeeMenu(loggedInEmployee, scanner);
                        } else {
                            System.out.println("Invalid name or employee ID. Please try again.");
                        }
                    }
                    break;
                case 2:
                    backToRoleSelection = true;
                    break;
                default:
                    System.out.println("Invalid choice! Please choose a valid option.");
            }
        }
    }

    private static void employeeMenu(Employee loggedInEmployee, Scanner scanner) {
        boolean backToRoleSelection = false;

        while (!backToRoleSelection) {
            System.out.println("\n=======================================");
            System.out.println("              Employee Menu");
            System.out.println("=======================================");
            System.out.println("1. View Payroll");
            System.out.println("2. Back to Role Selection");
            System.out.println("=======================================");
            System.out.print("Choose an option (1-2): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    viewPayroll(loggedInEmployee, scanner);
                    break;
                case 2:
                    backToRoleSelection = true;
                    break;
                default:
                    System.out.println("Invalid choice! Please choose a valid option.");
            }
        }
    }

    private static void addEmployees(Payroll payroll, Scanner scanner) {
        System.out.println("Enter the number of employees to add (maximum " + MAX_EMPLOYEES + "):");
        int numEmployees = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (numEmployees > MAX_EMPLOYEES) {
            System.out.println("The number of employees exceeds the maximum limit of " + MAX_EMPLOYEES);
            return;
        }

        for (int i = 0; i < numEmployees; i++) {
            System.out.println("Enter details for employee " + (i + 1) + ":");
            System.out.print("Name: ");
            String name = scanner.nextLine();
            System.out.print("ID: ");
            int id = scanner.nextInt();
            System.out.print("Hourly Rate (PHP): ");
            double hourlyRate = scanner.nextDouble();
            scanner.nextLine(); // Consume newline

            payroll.addEmployee(new Employee(name, id, hourlyRate));
        }
    }

    private static void enterHoursWorked(Payroll payroll, Scanner scanner) {
        System.out.println("Enter hours worked for employees:");
        for (Employee employee : payroll.getEmployees()) {
            System.out.print("Employee ID " + employee.getId() + " (" + employee.getName() + "): ");
            double hours = scanner.nextDouble();
            System.out.print("Enter date (YYYY-MM-DD): ");
            String dateStr = scanner.next();
            LocalDate date = LocalDate.parse(dateStr);
            employee.addHoursWorked(date, hours);
        }
        payroll.saveEmployees();
    }

    private static void viewPayroll(Employee employee, Scanner scanner) {
        System.out.print("Enter start date (YYYY-MM-DD): ");
        String startDateStr = scanner.next();
        LocalDate startDate = LocalDate.parse(startDateStr);
        System.out.print("Enter end date (YYYY-MM-DD): ");
        String endDateStr = scanner.next();
        LocalDate endDate = LocalDate.parse(endDateStr);

        System.out.println("Displaying payroll details for: " + employee.getName());
        double pay = employee.calculatePay(startDate, endDate);
        System.out.println("=======================================");
        System.out.println("Employee ID: " + employee.getId());
        System.out.println("Employee Name: " + employee.getName());
        System.out.println("Hourly Rate: PHP " + employee.getHourlyRate());
        System.out.println("Total Pay from " + startDate + " to " + endDate + ": PHP " + pay);
        System.out.println("=======================================");
    }
}