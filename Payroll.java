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
        System.out.println("Employee No\tEmployee Full Name\tPosition\tDepartment\tGross Income\tSOCIAL SECURITY SYSTEM\t\tPHILHEALTH\t\tPAG-IBIG\t\tBIR\t\tNet Pay");
        System.out.println("\t\t\t\t\t\tSocial Security No.\tSocial Security Contribution\tPhilhealth No.\tPhilhealth Contribution\tPag-ibig No.\tPag-Ibig Contribution\tTIN\tWithholding Tax");

        for (Employee employee : employees) {
            double grossPay = employee.calculatePay(startDate, endDate);
            double sssDeduction = employee.calculateSSSDeduction(grossPay);
            double philHealthDeduction = employee.calculatePhilHealthDeduction(grossPay);
            double pagIbigDeduction = employee.calculatePagIBIGDeduction(grossPay);
            double taxDeduction = employee.calculateTaxDeduction(grossPay);
            double totalDeductions = sssDeduction + philHealthDeduction + pagIbigDeduction + taxDeduction;
            double netPay = grossPay - totalDeductions;

            System.out.printf("%d\t%s\t%s\t%s\t%.2f\t%s\t%.2f\t%s\t%.2f\t%s\t%.2f\t%s\t%.2f\t%.2f\n",
                employee.getId(), employee.getName(), employee.getPosition(), employee.getDepartment(), grossPay,
                employee.getSssNumber(), sssDeduction, employee.getPhilHealthNumber(), philHealthDeduction,
                employee.getPagIbigNumber(), pagIbigDeduction, employee.getTin(), taxDeduction, netPay);
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
                try {
                    employees.add(Employee.fromCSV(line));
                } catch (IllegalArgumentException e) {
                    System.out.println("Skipping invalid employee entry: " + line);
                }
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