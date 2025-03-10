import java.util.*;
import java.io.*;

public class ShelfWatch implements FileStorage<InventoryItem> {
    private static List<InventoryItem> inventory = new ArrayList<>();
    private static final String FILE_NAME = "C:\\Users\\Johanzen\\Documents\\PROJECTS IT\\MotorPH Payroll System\\inventory.csv";
    private static Scanner scanner = new Scanner(System.in);

    public static void displayMenu() {
        ShelfWatch shelfWatch = new ShelfWatch();
        inventory = new ArrayList<>(shelfWatch.load());
        
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
                    shelfWatch.save(inventory);
                    break;
                case 2:
                    removeItem();
                    shelfWatch.save(inventory);
                    break;
                case 3:
                    viewInventory();
                    break;
                case 4:
                    sortInventory();
                    break;
                case 5:
                    shelfWatch.save(inventory);
                    System.out.println("Exiting Inventory Management. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    @Override
    public List<InventoryItem> load() {
        ArrayList<InventoryItem> loadedInventory = new ArrayList<>();
        File file = new File(FILE_NAME);
        
        if (!file.exists()) {
            System.out.println("No existing inventory file found. Starting with an empty inventory.");
            return loadedInventory;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
                try {
                    loadedInventory.add(InventoryItem.fromCSV(line));
                } catch (IllegalArgumentException e) {
                    System.out.println("Skipping invalid inventory entry: " + line);
                    System.out.println("Error: " + e.getMessage());
                }
            }
            System.out.println("Loaded " + loadedInventory.size() + " items from inventory.");
        } catch (IOException e) {
            System.out.println("Error reading inventory file: " + e.getMessage());
        }
        
        return loadedInventory;
    }

    @Override
    public void save(List<InventoryItem> items) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (InventoryItem item : items) {
                writer.write(item.toCSVFormat());
                writer.newLine();
            }
            System.out.println("Inventory saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving inventory: " + e.getMessage());
        }
    }
    
    @Override
    public String getFilePath() {
        return FILE_NAME;
    }

    private static void addItem() {
        try {
            System.out.print("Enter item name: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Item name cannot be empty.");
                return;
            }
            
            System.out.print("Enter brand: ");
            String brand = scanner.nextLine().trim();
            if (brand.isEmpty()) {
                System.out.println("Brand cannot be empty.");
                return;
            }
            
            System.out.print("Enter engine number: ");
            String engineNumber = scanner.nextLine().trim();
            if (engineNumber.isEmpty()) {
                System.out.println("Engine number cannot be empty.");
                return;
            }
            
            System.out.print("Enter purchase status (Sold/On hand): ");
            String purchaseStatus = scanner.nextLine().trim();
            if (!purchaseStatus.equalsIgnoreCase("Sold") && !purchaseStatus.equalsIgnoreCase("On hand")) {
                System.out.println("Purchase status must be either 'Sold' or 'On hand'.");
                return;
            }
            
            inventory.add(new InventoryItem(name, brand, engineNumber, purchaseStatus, new Date()));
            System.out.println(name + " has been added to the inventory.");
        } catch (Exception e) {
            System.out.println("Error adding item: " + e.getMessage());
        }
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
            for (InventoryManageable item : inventory) {
                System.out.println("- " + item);
            }
        }
    }

    private static void sortInventory() {
        System.out.println("Sort by:");
        System.out.println("1. Date Added");
        System.out.println("2. Name");
        System.out.println("3. Brand");
        System.out.print("Choose an option: ");
        
        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    inventory.sort(Comparator.comparing(InventoryItem::getDateAdded));
                    System.out.println("Inventory has been sorted by date added.");
                    break;
                case 2:
                    inventory.sort(Comparator.comparing(InventoryItem::getName));
                    System.out.println("Inventory has been sorted by name.");
                    break;
                case 3:
                    inventory.sort(Comparator.comparing(InventoryItem::getBrand));
                    System.out.println("Inventory has been sorted by brand.");
                    break;
                default:
                    System.out.println("Invalid choice. Inventory remains unsorted.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.nextLine(); // Clear invalid input
        }
    }
}