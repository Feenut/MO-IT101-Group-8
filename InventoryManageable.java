import java.util.Date;

public interface InventoryManageable {
    String getName();
    String getDescription();
    Date getDateAdded();
    String getStatus();
    String toCSVFormat();
} 