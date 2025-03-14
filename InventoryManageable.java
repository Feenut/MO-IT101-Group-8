import java.util.Date;

/**
 * Interface for objects that can be managed in the inventory system
 * 
 * This interface defines the contract for classes that need to:
 * - Be tracked in the inventory management system
 * - Provide descriptive information about inventory items
 * - Support serialization to CSV format for storage
 * 
 * @author MotorPH Development Team
 * @version 1.0
 */
public interface InventoryManageable {
    /**
     * Gets the name of the inventory item
     * 
     * @return The item name
     */
    String getName();
    
    /**
     * Gets a description of the inventory item
     * 
     * @return The item description
     */
    String getDescription();
    
    /**
     * Gets the date when the item was added to inventory
     * 
     * @return The date added
     */
    Date getDateAdded();
    
    /**
     * Gets the current status of the inventory item
     * 
     * @return The item status
     */
    String getStatus();
    
    /**
     * Converts the inventory item to CSV format for storage
     * 
     * @return CSV string representation of the item
     */
    String toCSVFormat();
} 