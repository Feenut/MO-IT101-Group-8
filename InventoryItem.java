import java.util.Date;

/**
 * InventoryItem class represents an item in the MotorPH inventory system.
 * 
 * This class implements InventoryManageable interface to handle:
 * - Vehicle inventory tracking
 * - Item details including brand and engine number
 * - Purchase status tracking
 * - Data serialization for storage
 * 
 * @author MotorPH Development Team
 * @version 1.0
 */
public class InventoryItem implements InventoryManageable {
    /** Name of the inventory item */
    private String name;
    /** Brand of the vehicle */
    private String brand;
    /** Unique engine number */
    private String engineNumber;
    /** Current purchase status (Sold/On hand) */
    private String purchaseStatus;
    /** Date when the item was added to inventory */
    private Date dateAdded;

    /**
     * Constructs a new InventoryItem with the specified details
     * 
     * @param name Name of the item
     * @param brand Brand of the vehicle
     * @param engineNumber Unique engine number
     * @param purchaseStatus Current purchase status
     * @param dateAdded Date when the item was added
     */
    public InventoryItem(String name, String brand, String engineNumber, String purchaseStatus, Date dateAdded) {
        this.name = name;
        this.brand = brand;
        this.engineNumber = engineNumber;
        this.purchaseStatus = purchaseStatus;
        this.dateAdded = dateAdded;
    }

    /**
     * Gets the name of the inventory item
     * 
     * @return The item name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Gets a description of the inventory item
     * Combines brand and engine number
     * 
     * @return The item description
     */
    @Override
    public String getDescription() {
        return brand + " - Engine #" + engineNumber;
    }

    /**
     * Gets the brand of the vehicle
     * 
     * @return The brand name
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Gets the engine number of the vehicle
     * 
     * @return The engine number
     */
    public String getEngineNumber() {
        return engineNumber;
    }

    /**
     * Gets the current status of the inventory item
     * 
     * @return The item status
     */
    @Override
    public String getStatus() {
        return purchaseStatus;
    }
    
    /**
     * Gets the purchase status of the item
     * 
     * @return The purchase status
     */
    public String getPurchaseStatus() {
        return purchaseStatus;
    }

    /**
     * Gets the date when the item was added to inventory
     * 
     * @return The date added
     */
    @Override
    public Date getDateAdded() {
        return dateAdded;
    }

    /**
     * Converts the inventory item to CSV format for storage
     * 
     * @return CSV string representation of the item
     */
    @Override
    public String toCSVFormat() {
        return name + ", " + brand + ", " + engineNumber + ", " + purchaseStatus + ", " + dateAdded.getTime();
    }

    /**
     * Returns a string representation of the inventory item
     * 
     * @return String with item details
     */
    @Override
    public String toString() {
        return name + ", " + brand + ", " + engineNumber + ", " + purchaseStatus + ", " + dateAdded;
    }
    
    /**
     * Creates an InventoryItem object from CSV data
     * 
     * @param csv CSV string containing inventory item data
     * @return New InventoryItem object
     * @throws IllegalArgumentException if CSV data is invalid
     */
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