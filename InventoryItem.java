import java.util.Date;

public class InventoryItem implements InventoryManageable {
    private String name;
    private String brand;
    private String engineNumber;
    private String purchaseStatus;
    private Date dateAdded;

    public InventoryItem(String name, String brand, String engineNumber, String purchaseStatus, Date dateAdded) {
        this.name = name;
        this.brand = brand;
        this.engineNumber = engineNumber;
        this.purchaseStatus = purchaseStatus;
        this.dateAdded = dateAdded;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return brand + " - Engine #" + engineNumber;
    }

    public String getBrand() {
        return brand;
    }

    public String getEngineNumber() {
        return engineNumber;
    }

    @Override
    public String getStatus() {
        return purchaseStatus;
    }
    
    public String getPurchaseStatus() {
        return purchaseStatus;
    }

    @Override
    public Date getDateAdded() {
        return dateAdded;
    }

    @Override
    public String toCSVFormat() {
        return name + ", " + brand + ", " + engineNumber + ", " + purchaseStatus + ", " + dateAdded.getTime();
    }

    @Override
    public String toString() {
        return name + ", " + brand + ", " + engineNumber + ", " + purchaseStatus + ", " + dateAdded;
    }
    
    public static InventoryItem fromCSV(String csv) {
        try {
            String[] parts = csv.split(", ");
            if (parts.length < 5) {
                throw new IllegalArgumentException("Invalid CSV data: " + csv);
            }
            String name = parts[0];
            String brand = parts[1];
            String engineNumber = parts[2];
            String purchaseStatus = parts[3];
            Date dateAdded = new Date(Long.parseLong(parts[4]));
            
            return new InventoryItem(name, brand, engineNumber, purchaseStatus, dateAdded);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid date format in CSV: " + csv);
        }
    }
}