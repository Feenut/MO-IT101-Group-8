import java.util.Date;

public class InventoryItem {
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

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getEngineNumber() {
        return engineNumber;
    }

    public String getPurchaseStatus() {
        return purchaseStatus;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    @Override
    public String toString() {
        return name + ", " + brand + ", " + engineNumber + ", " + purchaseStatus + ", " + dateAdded;
    }
}