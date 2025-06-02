
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class InventoryControl {

    private Scanner scanner = new Scanner(System.in);
    private ArrayList<InventoryItem> inventory;
    private final String fileName = "src\\inventory.txt";

    // Constructor
    public InventoryControl() {
        inventory = new ArrayList<>();
        scanner = new Scanner(System.in);
        loadInventoryFromFile();  // Load inventory when the system starts
    }

    public void playSound(String soundFilePath) {
        try {
            File soundFile = new File(soundFilePath);
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(soundFile));
            clip.start();
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            System.out.println("Error playing sound: " + e.getMessage());
        }
    }

    // Method to check stock levels and play sounds accordingly
    private void checkStockLevels() {
        for (InventoryItem item : inventory) {
            if (item.getQuantity() < item.getSafetyStock()) {
                playSound("src\\alarm.wav"); // Sound for low stock
                break;  // Exit after playing sound for low stock
            }
            if (item.getQuantity() > item.getSafetyStock() * 200) {
                playSound("src\\ring.wav"); // Sound for too much stock
                break;  // Exit after playing sound for too much stock
            }
        }
    }

    public void inventoryMenu() {
        int choice = -1;
        checkStockLevels();
        do {
            try {
                // Display menu options
                System.out.println("\nInventory Management System");
                System.out.println("1. Add Item");
                System.out.println("2. Remove Item");
                System.out.println("3. Update Item");
                System.out.println("4. Display Inventory");
                System.out.println("5. Search Item");
                System.out.println("6. Check Stock In / Out");
                System.out.println("7. Report");
                System.out.println("0. Back to previous page");
                System.out.print("Enter your choice: ");

                // Get user input
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                switch (choice) {
                    case 0:
                        System.out.println("Exiting to the previous page. ");
                        break;
                    case 1:
                        addItem();
                        break;
                    case 2:
                        removeItem();
                        break;
                    case 3:
                        updateItem();
                        break;
                    case 4:
                        displayInventory();
                        break;
                    case 5:
                        searchItem();
                        break;
                    case 6:
                        OrderProcessor orderProcessor = new OrderProcessor();
                        StockTrack batch = new StockTrack("src\\orders.txt", "src\\batchIn.txt", orderProcessor);
                        batch.batchTrackDisplayMenu();
                        break;
                    case 7:
                        InventoryReport report = new InventoryReport(inventory);
                        report.reportDisplayMenu();
                        break;
                    case 8:
                        System.out.println("Exiting the system.");
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 8.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        } while (choice != 0);
    }

    // Method to add an item to the inventory
    public void addItem() {
        String name = getValidStringInput("Enter Item name: ").toUpperCase();

        for (InventoryItem item : inventory) {
            if (item.getName().equalsIgnoreCase(name)) {
                System.out.println("Item with this name already exists. Please update it instead.");
                return;
            }
        }

        double quantity = getValidDoubleInput("Enter item quantity: ");
        double totalPrice = getValidDoubleInput("Enter item Total Price: ");
        double safetyStock = getValidDoubleInput("Enter safety stock: ");

        String expiredDate = getValidDateInput("Enter expired date: ");
        String category = getValidStringInput("Enter category: ").toUpperCase();
        String orderDate = getValidDateInput("Enter order date: ");

        // Create the new InventoryItem and add it to the inventory
        InventoryItem newItem = new InventoryItem(name, quantity, totalPrice, safetyStock, expiredDate, category, orderDate);
        inventory.add(newItem);
        System.out.println("Item added to inventory.");

        saveInventoryToFile();  // Save the inventory after adding a new item
    }

    private double getValidDoubleInput(String prompt) {
        double value = 0;
        boolean validInput = false;
        do {
            try {
                System.out.print(prompt);
                value = scanner.nextDouble();
                if (value < 0) {
                    System.out.println("Value cannot be negative. Please try again.");
                } else {
                    validInput = true; // Valid input received
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
            }
        } while (!validInput);
        scanner.nextLine(); // Consume leftover newline
        return value;
    }

    private String getValidStringInput(String prompt) {
        String value;
        do {
            System.out.print(prompt);
            value = scanner.nextLine().trim();
            if (value.isEmpty()) {
                System.out.println("Input cannot be empty. Please try again.");
            }
        } while (value.isEmpty());
        return value;
    }

    private String getValidDateInput(String prompt) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        String dateInput;
        boolean validDate = false;
        do {
            System.out.print(prompt);
            dateInput = scanner.nextLine().trim();
            try {
                dateFormat.parse(dateInput); // Try parsing the input
                validDate = true;
            } catch (ParseException e) {
                System.out.println("Invalid date format. Please enter in 'yyyy-MM-dd' format.");
            }
        } while (!validDate);
        return dateInput;
    }

    // Remove an item from the inventory
    public void removeItem() {
        System.out.println("\nEnter the name of the item to remove: ");
        String name = scanner.nextLine();

        InventoryItem itemToRemove = null;
        for (InventoryItem item : inventory) {
            if (item.getName().equalsIgnoreCase(name)) {
                itemToRemove = item;
                break;
            }
        }

        if (itemToRemove != null) {
            inventory.remove(itemToRemove);
            System.out.println("Item removed successfully!");
            saveInventoryToFile();  // Save changes to the file
        } else {
            System.out.println("Item not found in the inventory.");
        }

        saveInventoryToFile();
    }

    // Update an existing item in the inventory
    public void updateItem() {
        String name = getValidStringInput("Enter the name of the item to update: ").toUpperCase();

        InventoryItem itemToUpdate = null;
        for (InventoryItem item : inventory) {
            if (item.getName().equalsIgnoreCase(name)) {
                itemToUpdate = item;
                break;
            }
        }

        if (itemToUpdate != null) {

            double newQuantity = getValidDoubleInput("Enter new quantity: ");
            double newPrice = getValidDoubleInput("Enter new Total Price: ");
            String newExpiredDate = getValidDateInput("Enter new expired date: ");
            String newOrderDate = getValidDateInput("Enter new order date: ");

            itemToUpdate.setQuantity(newQuantity);
            itemToUpdate.setTotalPrice(newPrice);
            itemToUpdate.setExpiredDate(newExpiredDate);
            itemToUpdate.setOrderDate(newOrderDate);
            System.out.println("Item updated successfully!");

            saveInventoryToFile();
        } else {
            System.out.println("Item not found in the inventory.");
        }
    }

    // Method to load inventory from a file
    public void loadInventoryFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                InventoryItem item = InventoryItem.fromString(line);
                inventory.add(item);
            }
            System.out.println("Welcome to Inventory Contol!");
        } catch (FileNotFoundException e) {
            System.out.println("No previous inventory found. Starting with an empty inventory.");
        } catch (IOException e) {
            System.out.println("Error loading inventory: " + e.getMessage());
        }
    }

    // Method to display all items in the inventory
    public void displayInventory() {
        System.out.println("\nInventory Items:");
        if (inventory.isEmpty()) {
            System.out.println("No items in inventory.");
        } else {
            for (InventoryItem item : inventory) {
                item.displayItem();
            }
        }
    }

// Method to search for an item in the inventory by name
    public void searchItem() {
        System.out.println("\nEnter the name of the item to search: ");
        String searchName = scanner.nextLine();

        // Use a stream to find and display the item
        boolean itemFound = inventory.stream()
                .filter(item -> item.getName().equalsIgnoreCase(searchName))
                .peek(InventoryItem::displayItem) // Display the item if found
                .findFirst()
                .isPresent(); // Check if any item was found

        if (!itemFound) {
            System.out.println("Item not found in the inventory.");
        }
    }

    // Method to save inventory to a file
    public void saveInventoryToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (InventoryItem item : inventory) {
                writer.write(item.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving inventory: " + e.getMessage());
        }
    }

}
