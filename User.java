/**
 * User class represents a system user in the MotorPH Payroll System.
 * 
 * This class handles user authentication and role-based access control:
 * - Stores username and role information
 * - Supports different user roles (admin or employee)
 * - Used for access control throughout the application
 * 
 * @author MotorPH Development Team
 * @version 1.0
 */
public class User {
    /** Username for authentication */
    private String username;
    /** User role (admin or employee) */
    private String role; // "admin" or "employee"

    /**
     * Constructs a new User with the specified username and role
     * 
     * @param username The user's username
     * @param role The user's role (admin or employee)
     */
    public User(String username, String role) {
        this.username = username;
        this.role = role;
    }

    /**
     * Gets the user's username
     * 
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the user's role
     * 
     * @return The role (admin or employee)
     */
    public String getRole() {
        return role;
    }
}