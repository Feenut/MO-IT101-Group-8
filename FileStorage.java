import java.util.List;

/**
 * Interface for objects that need file storage capabilities
 * 
 * This interface defines the contract for classes that need to:
 * - Load data from files
 * - Save data to files
 * - Manage file paths
 * 
 * @param <T> The type of objects to be stored
 * 
 * @author MotorPH Development Team
 * @version 1.0
 */
public interface FileStorage<T> {
    /**
     * Loads objects from a file
     * 
     * @return List of loaded objects
     */
    List<T> load();
    
    /**
     * Saves objects to a file
     * 
     * @param items List of objects to save
     */
    void save(List<T> items);
    
    /**
     * Gets the file path used for storage
     * 
     * @return The file path
     */
    String getFilePath();
} 