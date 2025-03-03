import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Payroll {
    private List<Employee> employees;
    private static final String FILE_PATH = "C:\\Users\\Johanzen\\Documents\\PROJECTS IT\\MotorPH Payroll System\\Employees.csv";

    public Payroll() {
        employees = new ArrayList<>();
        loadEmployees();
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
        saveEmployees();
    }

    public void processPayroll(LocalDate startDate, LocalDate endDate) {
        System.out.println("=================================================================================================================================");
        System.out.println("Employee No | Employee Name        | Position    | Department  | Gross Income | Basic Deduction | Tax | Total Deductions | Net Pay");
        System.out.println("=================================================================================================================================");

        for (Employee employee : employees) {
            double grossPay = employee.calculatePay(startDate, endDate);
            double basicDeduction = employee.calculateBasicDeduction(grossPay);
            double taxDeduction = employee.calculateTaxDeduction(grossPay);
            double totalDeductions = basicDeduction + taxDeduction;
            double netPay = grossPay - totalDeductions;

            System.out.printf("%-12d | %-20s | %-10s | %-11s | %-12.2f | %-15.2f | %-5.2f | %-16.2f | %-7.2f\n",
                employee.getId(), employee.getName(), employee.getPosition(), employee.getDepartment(), 
                grossPay, basicDeduction, taxDeduction, totalDeductions, netPay);
        }

        System.out.println("=================================================================================================================================");
    }

    public void processWeeklyPayroll(LocalDate startDate, LocalDate endDate) {
        System.out.println("=================================================================================================================================");
        System.out.println("Employee No | Employee Name        | Position    | Department  | Weekly Salary | Basic Deduction | Tax | Total Deductions | Net Pay");
        System.out.println("=================================================================================================================================");

        for (Employee employee : employees) {
            double weeklySalary = employee.calculateWeeklySalary(startDate, endDate);
            double basicDeduction = employee.calculateBasicDeduction(weeklySalary);
            double taxDeduction = employee.calculateTaxDeduction(weeklySalary);
            double totalDeductions = basicDeduction + taxDeduction;
            double netPay = weeklySalary - totalDeductions;

            System.out.printf("%-12d | %-20s | %-10s | %-11s | %-13.2f | %-15.2f | %-5.2f | %-16.2f | %-7.2f\n",
                employee.getId(), employee.getName(), employee.getPosition(), employee.getDepartment(), 
                weeklySalary, basicDeduction, taxDeduction, totalDeductions, netPay);
        }

        System.out.println("=================================================================================================================================");
    }

    public Employee findEmployeeByIdAndName(int id, String name) {
        for (Employee employee : employees) {
            if (employee.getId() == id && employee.getName().equalsIgnoreCase(name)) {
                return employee;
            }
        }
        return null;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    private void loadEmployees() {
        File file = new File(FILE_PATH);
        
        if (!file.exists()) {
            System.out.println("No existing employee data found. Starting with an empty employee list.");
            return;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int lineCount = 0;
            while ((line = br.readLine()) != null && !line.trim().isEmpty()) {
                lineCount++;
                try {
                    employees.add(Employee.fromCSV(line));
                } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
                    System.out.println("Error on line " + lineCount + ": " + e.getMessage());
                    System.out.println("Skipping invalid employee entry: " + line);
                }
            }
            System.out.println("Loaded " + employees.size() + " employees.");
        } catch (IOException e) {
            System.out.println("Error reading employee data: " + e.getMessage());
        }
    }

    public void saveEmployees() {
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
}