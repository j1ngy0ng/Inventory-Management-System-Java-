
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class InventoryReport {

    private ArrayList<InventoryItem> inventory;
    private Scanner scanner = new Scanner(System.in);

    // Constructor for InventoryReport
    public InventoryReport(ArrayList<InventoryItem> inventory) {
        this.inventory = inventory;
    }
    
    public void playSound(String soundFilePath) {
    try {
        File soundFile = new File(soundFilePath);
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
        System.out.println("Error playing sound: " + e.getMessage());
    }
}

    // Display Batch Track Menu
    public void reportDisplayMenu() {
        int choice = -1;

        do {
            try {
                System.out.println("\n Welcome to Report");
                System.out.println("1. Inventory Summary Report");
                System.out.println("2. Stock Level Report");
                System.out.println("3. Expired Items Report");
                System.out.println("0. Back to Previous Page");
                System.out.print("Please select your choice (0 to 3): ");

                // Get user input
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                switch (choice) {
                    case 0:
                        break;
                    case 1:
                        generateInventorySummaryReport();
                        break;
                    case 2:
                        StockReportMenu();
                        break;
                    case 3:
                        generateExpiredItemsReport();
                        break;
                    default:
                        System.out.println("Invalid choice, please select again.");

                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }

        } while (choice != 0);
    }

    public void generateInventorySummaryReport() {
        int totalItems = inventory.size();
        double totalQuantity = inventory.stream().mapToDouble(InventoryItem::getQuantity).sum();
        double totalValue = inventory.stream().mapToDouble(InventoryItem::getTotalPrice).sum();

        System.out.println("\n--- Inventory Summary Report ---");
        System.out.println("Total Items: " + totalItems);
        System.out.println("Total Quantity: " + totalQuantity);
        System.out.println("Total Value: " + totalValue);
    }

    public void StockReportMenu() {
        int choice = -1;
        do {
            try {
                System.out.println("\nCheck Stock Report");
                System.out.println("1. Low Stock ");
                System.out.println("2. Too Much Stock ");
                System.out.println("3. Stock in Moderate ");
                System.out.println("0. Back to previous page ");
                System.out.print("Please select your choice (0 to 3): ");
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 0:
                        break;
                    case 1:
                        generateLowStockReport();
                        break;
                    case 2:
                        generateMuchStockReport();
                        break;
                    case 3:
                        generateModerateStockReport();
                        break;
                    default:
                        System.out.println("Invalid choice, please select again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        } while (choice != 0);
    }

    public void generateLowStockReport() {
        boolean hasLowStockItems = false;
        System.out.println("\n--- Low Stock Report ---");
        for (InventoryItem item : inventory) {
            if (item.getQuantity() < item.getSafetyStock()) {
                item.displayItem(); 
                hasLowStockItems = true;
            }
        }
        if (!hasLowStockItems) {
            System.out.println("No items are below safety stock levels.");
        }else{
            playSound("src\\alarm.wav");
        }
    }

    public void generateMuchStockReport() {
        boolean hasLowStockItems = false;
        System.out.println("\n--- Too Much Stock Report ---");
        for (InventoryItem item : inventory) {
            if (item.getQuantity() > item.getSafetyStock() * 20) {
                item.displayItem(); 
                hasLowStockItems = true;
            }
        }
        if (!hasLowStockItems) {
            System.out.println("Inventory don't have items too much quantity. ");
        }else{
            playSound("src\\ring.wav");
        }
    }

    public void generateModerateStockReport() {
        System.out.println("\n--- Moderate Stock Report ---");
        boolean hasLowStockItems = false;
        for (InventoryItem item : inventory) {
            if (item.getQuantity() < item.getSafetyStock() * 20 && item.getQuantity() > item.getSafetyStock()) {
                item.displayItem(); 
                hasLowStockItems = true;
            }
        }
        if (!hasLowStockItems) {
            System.out.println("Inventory don't have items that quantity in moderate. ");
        }
    }

    public void generateExpiredItemsReport() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("\n--- Expired Items Report ---");
        boolean hasExpiredItem = false;
        String currentDate = dateFormat.format(new java.util.Date());

        for (InventoryItem item : inventory) {
            if (item.getExpiredDate().compareTo(currentDate) < 0) {
                item.displayItem(); 
                hasExpiredItem = true;
            }
        }
        
        if(!hasExpiredItem){
            System.out.println("There are not expired items in inventory. ");
        }
    }

}
