import java.util.*;
import java.io.*;

public class ShelfWatch {
    private static ArrayList<InventoryItem> inventory = new ArrayList<>();
    private static final String FILE_NAME = "MotorPH Payroll System/inventory.csv";
    private static Scanner scanner = new Scanner(System.in);

    public static void displayMenu() {
        loadInventory();
        while (true) {
            System.out.println("\nInventory Management System");
            System.out.println("1. Add Item");
            System.out.println("2. Remove Item");
            System.out.println("3. View Inventory");
            System.out.println("4. Sort Inventory");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    addItem();
                    saveInventory();
                    break;
                case 2:
                    removeItem();
                    saveInventory();
                    break;
                case 3:
                    viewInventory();
                    break;
                case 4:
                    sortInventory();
                    break;
                case 5:
                    saveInventory();
                    System.out.println("Exiting Inventory Management. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void loadInventory() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(", ");
                if (data.length == 5) {
                    inventory.add(new InventoryItem(data[0], data[1], data[2], data[3], new Date(Long.parseLong(data[4]))));
                } else {
                    System.out.println("Skipping invalid inventory entry: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("No existing inventory file found. Starting with an empty inventory.");
        }
    }

    private static void saveInventory() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (InventoryItem item : inventory) {
                writer.write(item.getName() + ", " + item.getBrand() + ", " + item.getEngineNumber() + ", " + item.getPurchaseStatus() + ", " + item.getDateAdded().getTime());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving inventory: " + e.getMessage());
        }
    }

    private static void addItem() {
        System.out.print("Enter item name: ");
        String name = scanner.nextLine();
        System.out.print("Enter brand: ");
        String brand = scanner.nextLine();
        System.out.print("Enter engine number: ");
        String engineNumber = scanner.nextLine();
        System.out.print("Enter purchase status (Sold/On hand): ");
        String purchaseStatus = scanner.nextLine();
        
        inventory.add(new InventoryItem(name, brand, engineNumber, purchaseStatus, new Date()));
        System.out.println(name + " has been added to the inventory.");
    }

    private static void removeItem() {
        System.out.print("Enter item name to remove: ");
        String name = scanner.nextLine();
        
        boolean removed = inventory.removeIf(item -> item.getName().equalsIgnoreCase(name));
        
        if (removed) {
            System.out.println(name + " has been removed from the inventory.");
        } else {
            System.out.println("Item not found in inventory.");
        }
    }

    private static void viewInventory() {
        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty.");
        } else {
            System.out.println("Current Inventory:");
            for (InventoryItem item : inventory) {
                System.out.println("- " + item);
            }
        }
    }

    private static void sortInventory() {
        inventory.sort(Comparator.comparing(InventoryItem::getDateAdded));
        System.out.println("Inventory has been sorted by date added.");
    }
}