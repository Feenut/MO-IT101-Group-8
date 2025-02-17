import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Employee {
    private String name;
    private int id;
    private String position;
    private String department;
    private String sssNumber;
    private String philHealthNumber;
    private String pagIbigNumber;
    private String tin;
    private double hourlyRate;
    private Map<LocalDate, Double> hoursWorked;

    public Employee(String name, int id, String position, String department, String sssNumber, String philHealthNumber, String pagIbigNumber, String tin, double hourlyRate) {
        this.name = name;
        this.id = id;
        this.position = position;
        this.department = department;
        this.sssNumber = sssNumber;
        this.philHealthNumber = philHealthNumber;
        this.pagIbigNumber = pagIbigNumber;
        this.tin = tin;
        this.hourlyRate = hourlyRate;
        this.hoursWorked = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getPosition() {
        return position;
    }

    public String getDepartment() {
        return department;
    }

    public String getSssNumber() {
        return sssNumber;
    }

    public String getPhilHealthNumber() {
        return philHealthNumber;
    }

    public String getPagIbigNumber() {
        return pagIbigNumber;
    }

    public String getTin() {
        return tin;
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

    public double calculateSSSDeduction(double grossPay) {
        // Latest SSS contribution table (2023)
        if (grossPay <= 3250) return 135.00;
        if (grossPay <= 4250) return 157.50;
        if (grossPay <= 5250) return 180.00;
        if (grossPay <= 6250) return 202.50;
        if (grossPay <= 7250) return 225.00;
        if (grossPay <= 8250) return 247.50;
        if (grossPay <= 9250) return 270.00;
        if (grossPay <= 10250) return 292.50;
        if (grossPay <= 11250) return 315.00;
        if (grossPay <= 12250) return 337.50;
        if (grossPay <= 13250) return 360.00;
        if (grossPay <= 14250) return 382.50;
        if (grossPay <= 15250) return 405.00;
        if (grossPay <= 16250) return 427.50;
        if (grossPay <= 17250) return 450.00;
        if (grossPay <= 18250) return 472.50;
        if (grossPay <= 19250) return 495.00;
        if (grossPay <= 20250) return 517.50;
        if (grossPay <= 21250) return 540.00;
        if (grossPay <= 22250) return 562.50;
        if (grossPay <= 23250) return 585.00;
        if (grossPay <= 24250) return 607.50;
        return 630.00;
    }

    public double calculatePhilHealthDeduction(double grossPay) {
        // Latest PhilHealth contribution rate (2023)
        return grossPay * 0.04;
    }

    public double calculatePagIBIGDeduction(double grossPay) {
        // Latest Pag-IBIG contribution rate (2023)
        return Math.min(grossPay * 0.02, 100);
    }

    public double calculateTaxDeduction(double grossPay) {
        // Simplified tax calculation (2023), assumes no exemptions
        if (grossPay <= 20833) return 0;
        if (grossPay <= 33333) return (grossPay - 20833) * 0.20;
        if (grossPay <= 66667) return 2500 + (grossPay - 33333) * 0.25;
        if (grossPay <= 166667) return 10833.33 + (grossPay - 66667) * 0.30;
        if (grossPay <= 666667) return 40833.33 + (grossPay - 166667) * 0.32;
        return 200833.33 + (grossPay - 666667) * 0.35;
    }

    public double calculateTotalDeductions(double grossPay) {
        double sss = calculateSSSDeduction(grossPay);
        double philHealth = calculatePhilHealthDeduction(grossPay);
        double pagIBIG = calculatePagIBIGDeduction(grossPay);
        double tax = calculateTaxDeduction(grossPay);
        return sss + philHealth + pagIBIG + tax;
    }

    public String toCSV() {
        StringBuilder csv = new StringBuilder();
        csv.append(name).append(",").append(id).append(",").append(position).append(",").append(department).append(",")
           .append(sssNumber).append(",").append(philHealthNumber).append(",").append(pagIbigNumber).append(",").append(tin).append(",")
           .append(hourlyRate);
        for (Map.Entry<LocalDate, Double> entry : hoursWorked.entrySet()) {
            csv.append(",").append(entry.getKey()).append(",").append(entry.getValue());
        }
        return csv.toString();
    }

    public static Employee fromCSV(String csv) {
        String[] parts = csv.split(",");
        if (parts.length < 9) {
            throw new IllegalArgumentException("Invalid CSV data: " + csv);
        }
        String name = parts[0];
        int id = Integer.parseInt(parts[1]);
        String position = parts[2];
        String department = parts[3];
        String sssNumber = parts[4];
        String philHealthNumber = parts[5];
        String pagIbigNumber = parts[6];
        String tin = parts[7];
        double hourlyRate = Double.parseDouble(parts[8]);
        Employee employee = new Employee(name, id, position, department, sssNumber, philHealthNumber, pagIbigNumber, tin, hourlyRate);
        for (int i = 9; i < parts.length; i += 2) {
            LocalDate date = LocalDate.parse(parts[i]);
            double hours = Double.parseDouble(parts[i + 1]);
            employee.addHoursWorked(date, hours);
        }
        return employee;
    }
}