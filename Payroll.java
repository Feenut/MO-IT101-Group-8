import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Payroll {
    private List<Employee> employees;
    private static final String FILE_PATH = "Employees.csv";

    public Payroll() {
        employees = new ArrayList<>();
        loadEmployees();
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
        saveEmployees();
    }

    public void processPayroll() {
        for (Employee employee : employees) {
            double pay = employee.calculatePay(LocalDate.MIN, LocalDate.MAX);
            System.out.println("Employee ID: " + employee.getId());
            System.out.println("Employee Name: " + employee.getName());
            System.out.println("Hourly Rate: PHP " + employee.getHourlyRate());
            System.out.println("Total Pay: PHP " + pay);
            System.out.println("---------------");
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