
/**
 *
 * @author User
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class StockTrack {

    private static final String HEADER_FORMAT = "%-20s %-15s %-15s %-15s %-15s %-20s %-15s %-15s\n";
    private static final String[] HEADERS = {"Order ID", "Order Date", "Expired Date", "Product Id", "Item Name", "Category", "Quantity", "Total Price"};
    private Scanner scanner = new Scanner(System.in);
    private String ordersFilePath;
    private String batchFilePath;
    private OrderProcessor orderProcessor;

    // Constructor to initialize file paths and OrderProcessor
    public StockTrack(String ordersFilePath, String batchFilePath, OrderProcessor orderProcessor) {
        this.ordersFilePath = ordersFilePath;
        this.batchFilePath = batchFilePath;
        this.orderProcessor = orderProcessor;
    }

    // Display Batch Track Menu
    public void batchTrackDisplayMenu() {
        int choice = -1;
        orderProcessor.filterOrders(ordersFilePath, batchFilePath);

        do {
            try{
            System.out.println("\nWelcome to Check Stock In/Out");
            System.out.println("1. Stock In");
            System.out.println("2. Stock Out");
            System.out.println("0. Back to Previous Page");
            System.out.print("Please select your choice (0 to 2): ");

            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 0:
                    break;
                case 1:
                    processBatchFile(batchFilePath);
                    break;
                case 2:
                    CustomerOrderManagement orderManager = new CustomerOrderManagement();
                    orderManager.viewCustomerOrders();
                    break;
                default:
                    System.out.println("Invalid choice, please select again.");
            }
            }catch(InputMismatchException e){
                 System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }

        } while (choice != 0);
        
    }

    // Read the file and print it out
    private void processBatchFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            displayHeaders();
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split("\\|");
                
                if (fields.length >= 8) { // Adjust to check for at least 8 fields
                    displayBatchData(fields);
                } else {
                    System.err.println("Incomplete data in file: " + filePath);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + filePath);
            e.printStackTrace();
        }
    }

    private void displayHeaders() {
        System.out.printf(HEADER_FORMAT, (Object[]) HEADERS);
    }

    private void displayBatchData(String[] fields) {
        System.out.printf(HEADER_FORMAT, fields[0], fields[1], fields[2], fields[3], fields[4], fields[9], fields[6], fields[8]); // Adjust index
    }
}

