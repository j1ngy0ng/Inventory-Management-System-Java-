
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class CustomerOrderManagement extends Order {

    private String customerName;
    private List<CustomerOrderManagement> customerOrders = new ArrayList<>();
    private String filePath = "src\\customerOrders.txt"; // Add filePath as a private member

    // Constructor
    public CustomerOrderManagement(String orderId, String productId, String productName, int quantity, double totalPrice, String orderDate, String customerName) {
        super(orderId, productId, productName, quantity, totalPrice, orderDate);
        this.customerName = customerName;
    }

    public CustomerOrderManagement() {
        super("", "", "", 0, 0.0, "");
        loadOrdersFromFile();
    }

    public String getCustomerName() {
        return customerName;
    }

    @Override
    public String toString() {
        return String.format("%-10s%-13s%-25s%-10d%-15.2f%-20s%-15s",
                getOrderId(), getProductId(), getProductName(), getQuantity(), getTotalPrice(), customerName, getOrderDate());
    }

    // Load customer orders from the file
    private void loadOrdersFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) { // Use filePath from class variable
            String line;
            while ((line = br.readLine()) != null) {
                String[] orderData = line.split(",");
                if (orderData.length == 7) {
                    String orderId = orderData[0];
                    String productId = orderData[1];
                    String productName = orderData[2];
                    int quantity = Integer.parseInt(orderData[3]);
                    double totalPrice = Double.parseDouble(orderData[4]);
                    String customerName = orderData[5];
                    String orderDate = orderData[6];

                    customerOrders.add(new CustomerOrderManagement(orderId, productId, productName, quantity, totalPrice, orderDate, customerName));
                } else {
                    System.out.println("Invalid order data: " + line);
                }
            }
        } catch (NumberFormatException | IOException e) {
            System.out.println("Error loading orders: " + e.getMessage());
        }
    }

    // Display customer orders
    public void viewCustomerOrders() {
        System.out.println("\n[Customer Order List]");
        System.out.printf("%-10s%-13s%-25s%-10s%-15s%-20s%-15s%n", "OrderID", "ProductID", "ProductName", "Quantity", "TotalPrice", "CustomerName", "OrderDate");
        System.out.println("-------------------------------------------------------------------------------------------------------");
        for (CustomerOrderManagement order : customerOrders) {
            System.out.println(order);
        }
    }

    // Save and display orders
    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\nCUSTOMER ORDER MANAGEMENT");
            System.out.println("1. View Customer Orders");
            System.out.println("2. Generate Customer Order Report");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            while (true) {
                try {
                    choice = scanner.nextInt();
                    scanner.nextLine();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input! Please enter an integer.");
                    scanner.next();
                }
            }

            switch (choice) {
                case 1:
                    viewCustomerOrders();
                    break;
                case 2:
                    generateCustomerOrderReport();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 0);

    }

    // Generate report
    public void generateCustomerOrderReport() {
        double totalSales = customerOrders.stream().mapToDouble(CustomerOrderManagement::getTotalPrice).sum();
        int totalOrders = customerOrders.size();
        System.out.printf("\nTotal Sales: $%.2f from %d orders.%n", totalSales, totalOrders);

        // Product Sales Report
        Map<String, Integer> productSales = new HashMap<>();
        for (CustomerOrderManagement order : customerOrders) {
            productSales.put(order.getProductName(), productSales.getOrDefault(order.getProductName(), 0) + order.getQuantity());
        }
        System.out.println("\n-- Product Sales Report --");
        System.out.printf("%-25s%-10s%n", "ProductName", "Quantity");
        System.out.println("------------------------------");
        for (Map.Entry<String, Integer> entry : productSales.entrySet()) {
            System.out.printf("%-25s%-10d%n", entry.getKey(), entry.getValue());
        }

        // Customer Purchase Report
        Map<String, Double> customerPurchases = new HashMap<>();
        for (CustomerOrderManagement order : customerOrders) {
            customerPurchases.put(order.getCustomerName(), customerPurchases.getOrDefault(order.getCustomerName(), 0.0) + order.getTotalPrice());
        }
        System.out.println("\n-- Customer Purchase Report --");
        System.out.printf("%-20s%-15s%n", "CustomerName", "TotalSpent");
        System.out.println("-----------------------------");
        for (Map.Entry<String, Double> entry : customerPurchases.entrySet()) {
            System.out.printf("%-20s$%-14.2f%n", entry.getKey(), entry.getValue());
        }

        // Daily Sales Report
        Map<String, Double> dailySales = new TreeMap<>();
        for (CustomerOrderManagement order : customerOrders) {
            dailySales.put(order.getOrderDate(), dailySales.getOrDefault(order.getOrderDate(), 0.0) + order.getTotalPrice());
        }
        System.out.println("\n-- Daily Sales Report --");
        System.out.printf("%-15s%-15s%n", "OrderDate", "TotalSales");
        System.out.println("-----------------------------");
        for (Map.Entry<String, Double> entry : dailySales.entrySet()) {
            System.out.printf("%-15s$%-14.2f%n", entry.getKey(), entry.getValue());
        }
    }
}
