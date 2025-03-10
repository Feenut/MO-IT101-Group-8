import java.time.LocalDate;

public interface PaymentCalculator {
    double calculatePay(LocalDate startDate, LocalDate endDate);
    double calculateDeduction(double grossPay);
    double calculateNetPay(double grossPay);
} 