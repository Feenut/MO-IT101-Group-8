import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Payroll {
    private List<Employee> employees;

    public Payroll() {
        employees = new ArrayList<>();
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
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
}