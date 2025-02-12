import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Payroll {
    private List<Employee> employees;
    private static final String FILE_PATH = "MotorPH Payroll System/Employees.csv";

    public Payroll() {
        employees = new ArrayList<>();
        loadEmployees();
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
        saveEmployees();
    }

    public void processPayroll(LocalDate startDate, LocalDate endDate) {
        for (Employee employee : employees) {
            double grossPay = employee.calculatePay(startDate, endDate);
            double totalDeductions = employee.calculateTotalDeductions(grossPay);
            double netPay = grossPay - totalDeductions;
            
            System.out.println("=======================================");
            System.out.println("Employee ID: " + employee.getId());
            System.out.println("Employee Name: " + employee.getName());
            System.out.println("Gross Pay: PHP " + grossPay);
            System.out.println("SSS Deduction: PHP " + employee.calculateSSSDeduction(grossPay));
            System.out.println("PhilHealth Deduction: PHP " + employee.calculatePhilHealthDeduction(grossPay));
            System.out.println("Pag-IBIG Deduction: PHP " + employee.calculatePagIBIGDeduction(grossPay));
            System.out.println("Tax Deduction: PHP " + employee.calculateTaxDeduction(grossPay));
            System.out.println("Total Deductions: PHP " + totalDeductions);
            System.out.println("Net Pay: PHP " + netPay);
            System.out.println("=======================================");
        }
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
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                employees.add(Employee.fromCSV(line));
            }
        } catch (IOException e) {
            System.out.println("No existing employee data found.");
        }
    }

    public void saveEmployees() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Employee employee : employees) {
                bw.write(employee.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}