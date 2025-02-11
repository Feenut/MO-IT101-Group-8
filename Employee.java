import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Employee {
    private String name;
    private int id;
    private double hourlyRate;
    private Map<LocalDate, Double> hoursWorked;

    public Employee(String name, int id, double hourlyRate) {
        this.name = name;
        this.id = id;
        this.hourlyRate = hourlyRate;
        this.hoursWorked = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public Map<LocalDate, Double> getHoursWorked() {
        return hoursWorked;
    }

    public void addHoursWorked(LocalDate date, double hours) {
        hoursWorked.put(date, hoursWorked.getOrDefault(date, 0.0) + hours);
    }

    public double calculatePay(LocalDate startDate, LocalDate endDate) {
        return hoursWorked.entrySet().stream()
            .filter(entry -> !entry.getKey().isBefore(startDate) && !entry.getKey().isAfter(endDate))
            .mapToDouble(entry -> entry.getValue() * hourlyRate)
            .sum();
    }
}