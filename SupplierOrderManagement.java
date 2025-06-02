
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;

public class SupplierOrderManagement {

    private List<SupplierOrder> orders;
    private List<Product> products;
    private static final String orderFromFile = "src\\orders.txt";
    private static final double discountFromOrderManagement = 0.10; // 10% discount

    public SupplierOrderManagement(ArrayList<Product> products) {
        orders = new ArrayList<>();
        this.products = products;
        loadOrdersFromFile();
    }

    public void updateOrderStatus(String orderID, String newStatus) {
        boolean orderFound = false;
        for (SupplierOrder order : orders) {
            if (order.getOrderId().equals(orderID)) {
                order.setStatus(newStatus);
                orderFound = true;
                break;
            }
        }
        if (orderFound) {
            saveOrdersToFile(null);
            System.out.println("Order status updated successfully.");
        } else {
            System.out.println("Order ID not found.");
        }
    }

    private void saveOrdersToFile(SupplierOrder order) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(orderFromFile, order != null))) {
            if (order != null) {
                writer.write(order.toString());
                writer.newLine();
            } else {
                for (SupplierOrder o : orders) {
                    writer.write(o.toString());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public void displayAllOrders() {
        System.out.println("\nRegistered Orders:");
        System.out.println("\nOrder request has been send successfully: \nFormat:");
        System.out.println("====================================================================================================================================================================================================");
        System.out.println("OrderID|Order Date|Expiry Date|Product ID|Product Name|Unit|Quantity|Price(per unit)|Total Price(include 10% discount)|Category|Discount|Status|Supplier ID|");
        System.out.println("====================================================================================================================================================================================================\n\n");
        System.out.println("====================================================================================================================================================================================================");
        for (SupplierOrder order : orders) {
            System.out.println(order.toString());
            System.out.println("====================================================================================================================================================================================================");
        }
    }

    private void loadOrdersFromFile() {
        orders.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(orderFromFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 13) {
                    String orderID = parts[0];
                    String date = parts[1];
                    String expiryDate = parts[2];
                    String productID = parts[3];
                    String productName = parts[4];
                    String unit = parts[5];
                    int quantity = Integer.parseInt(parts[6]);
                    double pricePerUnit = Double.parseDouble(parts[7]);
                    double totalPrice = Double.parseDouble(parts[8]);
                    String category = parts[9];
                    double discount = Double.parseDouble(parts[10]);
                    String status = parts[11];
                    String supplierID = parts[12];

                    SupplierOrder order = new SupplierOrder(orderID, date, expiryDate, productID, productName, unit, quantity, pricePerUnit, totalPrice, category, discount, status, supplierID);
                    orders.add(order);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
    }

    private String generateOrderID() {
        int maxID = 0;

        for (SupplierOrder order : orders) {
            String idStr = order.getOrderId().substring(1); // Remove the 'O' prefix
            int id = Integer.parseInt(idStr);
            if (id > maxID) {
                maxID = id;
            }
        }
        return String.format("O%04d", maxID + 1); // Generates next ID with leading zeros (e.g., O0002)
    }

    public boolean isOrderIdUnique(String orderID) {
        for (SupplierOrder order : orders) {
            if (order.getOrderId().equals(orderID)) {
                return false;
            }
        }
        return true;
    }

    private static boolean orderExists(String orderID, SupplierOrderManagement management) {
        management.loadOrdersFromFile();
        for (SupplierOrder order : management.orders) {
            if (order.getOrderId().equals(orderID)) {
                return true;
            }
        }
        return false;
    }

    public static int inputChoice(Scanner scanner) {
        int choice = -1;
        boolean validChoice = false;
        do {
            System.out.println("\n1. Register Order");
            System.out.println("2. Display All Orders");
            System.out.println("3. Update Order");
            System.out.println("4. Search Order");
            System.out.println("5. Report");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            try {
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine();
                    validChoice = true;
                } else {
                    System.out.println("Error: Please enter a numeric value.");
                    scanner.next();
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a numeric value.");
                scanner.next();
            }
        } while (!validChoice);
        return choice;
    }
//Order Registration

    private void orderRegistration(SupplierOrderManagement management, Scanner scanner) {
        Product product = null;
        String orderID;
        boolean productFoundedOrNot = false;
        do {
            System.out.print("\nEnter Product ID: ");
            String productID = scanner.nextLine().trim();
            product = findProductByID(management, productID);
            if (product != null) {
                productFoundedOrNot = true;
            } else {
                System.out.println("Product ID not found.");
            }
        } while (!productFoundedOrNot);
        SupplierOrderPricing calculationForDisplay = new SupplierOrderPricing(discountFromOrderManagement);
        System.out.println("\nSelected Product:" + product.getName() + "\n    Category    :" + product.getCategory()+ "\n      Unit      :" + product.getUnit() + "\n      Price     :" + String.format("%.2f", product.getPurchasePrice()) + "\n    Discount    :" + String.format("%.0f%%", calculationForDisplay.discountInPercentage()) + "\n  From Supplier :" + product.getSupplierID());
        int quantity = getValidAmount(scanner);
        SupplierOrderPricing calculationForSelected = new SupplierOrderPricing(quantity, product.getPurchasePrice(), discountFromOrderManagement);
        if (calculationForSelected.amountOfProduct() > 1000) {
            Toolkit.getDefaultToolkit().beep();
        }
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String expiryDate = getValidExpiryDate(scanner, date);
        do {
            orderID = management.generateOrderID();
        } while (!management.isOrderIdUnique(orderID));

        SupplierOrder order = new SupplierOrder(
                orderID,
                date,
                expiryDate,
                product.getId(),
                product.getName(),
                product.getUnit(),
                calculationForSelected.amountOfProduct(),
                product.getPurchasePrice(),
                calculationForSelected.totalPriceWithDiscount(),
                product.getCategory(),
                calculationForSelected.discount(),
                "Pending",
                product.getSupplierID()
        );

        management.registerOrder(order);
    }

    private String getValidExpiryDate(Scanner scanner, String orderDateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        boolean validDate = false;
        String expiryDate = "";

        do {
            System.out.print("Enter expiry date (yyyy-mm-dd): ");
            expiryDate = scanner.nextLine().trim();

            if (expiryDate.length() == 10 && isValidDateFormat(expiryDate)) {

                try {
                    Date parsedExpiryDate = dateFormat.parse(expiryDate);
                    Date orderDate = dateFormat.parse(orderDateStr);

                    if (parsedExpiryDate.after(orderDate)) {
                        validDate = true;
                    } else {
                        System.out.println("Expiry date must be after the order date.");
                    }
                } catch (ParseException e) {
                    System.out.println("Invalid date format or value. Please enter the date in yyyy-mm-dd format.");
                }
            } else {
                System.out.println("Invalid date format or value. Please enter the date in yyyy-MM-dd format.");
            }
        } while (!validDate);

        return expiryDate;
    }

    private boolean isValidDateFormat(String date) {
        return date.charAt(4) == '-' && date.charAt(7) == '-'
                && isDigitOnly(date.substring(0, 4))
                && isDigitOnly(date.substring(5, 7))
                && isDigitOnly(date.substring(8, 10));
    }

    private boolean isDigitOnly(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    private static int getStatusChoice(Scanner scanner) {
        int whichStatus = -1;
        boolean validStatus = false;
        do {
            System.out.print("Enter the number corresponding to the status: ");
            try {
                if (scanner.hasNextInt()) {
                    whichStatus = scanner.nextInt();
                    if (whichStatus == 1 || whichStatus == 2 || whichStatus == 3 || whichStatus == 4) {
                        validStatus = true;
                    } else {
                        System.out.println("Error: Please enter 1 , 2 ,3 or 4.");
                        scanner.next();
                    }
                } else {
                    System.out.println("Error: Please enter a numeric value.");
                    scanner.next();
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a numeric value.");
                scanner.next();
            }
        } while (!validStatus);
        return whichStatus;
    }

    private static int getStatusChoiceAccordingToCurrentStatus(String status, Scanner scanner) {
        int whichStatus = -1;
        boolean validStatus = false;
        do {
            try {
                System.out.println("Which do you want to enter:");
                whichStatus = scanner.nextInt();
                if ("Pending".equals(status)) {
                    if (whichStatus == 1 || whichStatus == 2) {
                        validStatus = true;
                    } else {
                        System.out.println("Error: Please enter 1 or 2");
                    }
                } else if ("Approved and coming".equals(status)) {
                    if (whichStatus == 1 || whichStatus == 2) {
                        validStatus = true;
                    } else {
                        System.out.println("Error: Please enter 1 (Cancelled) or 2 (Arrived).");
                    }
                    if (whichStatus == 1) {
                        whichStatus = 3;
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a numeric value.");
                scanner.nextLine();
            }
        } while (!validStatus);
        return whichStatus;
    }

    private void statusUpdate(SupplierOrderManagement management, Scanner scanner) {
        String orderIsValid = getValidOrderID(scanner, management);
        boolean detectErrorChoice = false;
        do {
            for (SupplierOrder order : orders) {
                if (order.getOrderId().equals(orderIsValid)) {
                    if ("Arrived".equals(order.getStatus()) || "Cancelled".equals(order.getStatus())) {
                        System.out.printf("The order status is %s.", order.getStatus());
                        detectErrorChoice = true;
                    } else if ("Pending".equals(order.getStatus())) {
                        System.out.println("Status:");
                        System.out.println("1. Approved and coming\n2. Cancelled\n");
                        int whichStatus = getStatusChoiceAccordingToCurrentStatus("Pending", scanner);
                        String newStatus = changesOfStatus(whichStatus);

                        if (newStatus != null) {
                            management.updateOrderStatus(orderIsValid, newStatus);
                            detectErrorChoice = true;
                        }
                    } else if ("Approved and coming".equals(order.getStatus())) {
                        System.out.println("Status:");
                        System.out.println("1. Arrived\n2. Cancelled\n");
                        int whichStatus = getStatusChoiceAccordingToCurrentStatus("Approved and coming", scanner);
                        String newStatus = changesOfStatus(whichStatus);

                        if (newStatus != null) {
                            management.updateOrderStatus(orderIsValid, newStatus);
                            detectErrorChoice = true;
                        }
                    }
                }
            }
        } while (!detectErrorChoice);
    }

    public void updateOrderExpiryDate(SupplierOrderManagement management, Scanner scanner) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        boolean orderFound = false;
        boolean differentDate = false;

        String validedOrderId = getValidOrderID(scanner, management);
        for (SupplierOrder order : orders) {
            if (order.getOrderId().equals(validedOrderId)) {
                orderFound = true;
                if ("Arrived".equals(order.getStatus()) || "Approved and coming".equals(order.getStatus()) || "Cancelled".equals(order.getStatus())) {
                    System.out.printf("The order status is %s.", order.getStatus());
                } else {
                    do {
                        String newExpiryDate = getValidExpiryDate(scanner, order.getOrderDate());
                        if (newExpiryDate.equals(order.getExpiryDate())) {
                            System.out.println("The new expiry date cannot be the same as the current expiry date. Please enter a different date.");
                        } else {
                            differentDate = true;
                            order.setExpiryDate(newExpiryDate);
                            System.out.println("Expiry date updated successfully.");
                        }
                    } while (!differentDate);

                    if (differentDate) {
                        saveOrdersToFile(null);
                    }

                }
                break;
            }
        }

        if (!orderFound) {
            System.out.println("Order ID not found.");
        }
    }

    private static Product findProductByID(SupplierOrderManagement management, String productID) {
        for (Product p : management.products) {
            if (p.getId().equals(productID)) {
                return p;
            }
        }
        return null;
    }

    private static int getValidAmount(Scanner scanner) {
        int set = -1;
        boolean validAmount = false;
        do {
            System.out.print("\nEnter how many do you want to restock: ");
            try {
                if (scanner.hasNextInt()) {
                    set = scanner.nextInt();
                    validAmount = true;
                } else {
                    System.out.println("Error: Please enter a numeric value.");
                    scanner.next();
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a numeric value.");
                scanner.next();
            }
        } while (!validAmount);
        scanner.nextLine();
        return set;
    }

    public void registerOrder(SupplierOrder order) {
        orders.add(order);
        System.out.println("\n\n^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^" + "\nOrder request has been send successfully: " + "\nDate           :" + order.getOrderDate() + "\nExpiry Date    :" + order.getExpiryDate() + "\n[Order ID]     :" + order.getOrderId() + "\n========================================================================\nProduct ID     :" + order.getProductId() + "\nProduct Name   :" + order.getProductName() + "\nCategory       :" + order.getCategory() + "\nUnit           :" + order.getUnit() + "\nPrice          :" + String.format("%.2f", order.getPricePerUnit()) + "\n========================================================================\nQuantity                           :" + order.getQuantity() + "\nDiscount(10%)                      :" + String.format("%.2f", order.getDiscount()) + "\nTotal Price (include 10% discount) :" + String.format("%.2f", order.getTotalPrice()) + "\n========================================================================\nStatus                             :" + order.getStatus() + "\nFrom Supplier ID                   :" + order.getSupplierID() + "\n^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n"
        );
        saveOrdersToFile(order);
    }

    private static String getValidOrderID(Scanner scanner, SupplierOrderManagement management) {
        String orderIDToUpdate;
        boolean validId = false;
        do {
            System.out.print("Enter Order ID : ");
            orderIDToUpdate = scanner.nextLine().trim();

            if (orderIDToUpdate.length() > 0 && Character.isLetter(orderIDToUpdate.charAt(0))) {
                char firstChar = Character.toUpperCase(orderIDToUpdate.charAt(0));
                orderIDToUpdate = firstChar + orderIDToUpdate.substring(1);
            }
            if (orderExists(orderIDToUpdate, management)) {
                validId = true;
            } else {
                System.out.println("Order ID not found. Please enter a valid Order ID.");
            }
        } while (!validId);
        return orderIDToUpdate;
    }

    private static String changesOfStatus(int whichStatus) {
        switch (whichStatus) {
            case 1:
                return "Approved and coming";
            case 2:
                return "Cancelled";
            case 3:
                return "Arrived";
            default:
                System.out.println("Invalid input!");
                return null;
        }
    }

    public void generateReports() {
        SupplierOrderReport report = new SupplierOrderReport(orders);
        report.generateReport();
    }

    public void searchOrderByStatus(SupplierOrderManagement management, Scanner scanner) {

        System.out.println("Search Orders by Status:");
        System.out.println("1. Pending");
        System.out.println("2. Approved and coming");
        System.out.println("3. Cancelled");
        System.out.println("4. Arrived");

        int statusChoice = getStatusChoice(scanner);
        String status = getStatusString(statusChoice);

        if (status != null) {
            System.out.println("\nOrders with status: " + status);
            System.out.println("==================================================================================================================================================================================================================================");
            System.out.println("OrderID|Order Date|Product ID|Product Name|Category|Unit|Price(per unit)|Quantity|Discount|Total Price(include 10% discount)|Status|Supplier ID|Expiry Date|");
            System.out.println("==================================================================================================================================================================================================================================");
            boolean found = false;
            for (SupplierOrder order : management.orders) {
                if (status.equalsIgnoreCase(order.getStatus())) {
                    System.out.format("%-10s %-15s %-10s %-17s %-17s %-10s %-10.2f %-10d %-20.2f %-34.2f %-10s %-15s %-15s\n",
                            order.getOrderId(), order.getOrderDate(), order.getProductId(), order.getProductName(), order.getCategory(),
                            order.getUnit(), order.getPricePerUnit(), order.getQuantity(), order.getDiscount(),
                            order.getTotalPrice(), order.getStatus(), order.getSupplierID(), order.getExpiryDate());
                    System.out.println("==================================================================================================================================================================================================================================");
                    found = true;
                }
            }
            if (!found) {
                System.out.println("No orders found with the status: " + status);
            }
        } else {
            System.out.println("Invalid status choice.");
        }
    }

    private static void searchOrder(SupplierOrderManagement management, Scanner scanner) {
        boolean whichTypeOfSearchYouWant = false;

        do {
            try {
                System.out.println("Search Order Menu:");
                System.out.println("1. Search by Order Status");
                System.out.println("2. Search by Order ID");
                System.out.print("Enter your choice: ");

                int searchType = scanner.nextInt();

                switch (searchType) {
                    case 1:
                        management.searchOrderByStatus(management, scanner);
                        whichTypeOfSearchYouWant = true;
                        break;
                    case 2:
                        scanner.nextLine();
                        management.searchOrderId(management, scanner);
                        whichTypeOfSearchYouWant = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        searchOrder(management, scanner);
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number.");
                scanner.next();
            }
        } while (!whichTypeOfSearchYouWant);
    }

    public void searchOrderId(SupplierOrderManagement management, Scanner scanner) {
        String searchIdIsValidOrNot = getValidOrderID(scanner, management);
        for (SupplierOrder order : management.orders) {
            if (order.getOrderId().equals(searchIdIsValidOrNot)) {
                System.out.println("\nOrder Found:");
                System.out.println("[Order ID]    : " + order.getOrderId());
                System.out.println("Date          : " + order.getOrderDate());
                System.out.println("Expiry Date   : " + order.getExpiryDate());
                System.out.println("Product ID    : " + order.getProductId());
                System.out.println("Product Name  : " + order.getProductName());
                System.out.println("Category      : " + order.getCategory());
                System.out.println("Unit          : " + order.getUnit());
                System.out.println("Quantity        : " + order.getQuantity());
                System.out.println("Price Per Unit: " + String.format("%.2f", order.getPricePerUnit()));
                System.out.println("Total Price   : " + String.format("%.2f", order.getTotalPrice()));
                System.out.println("Discount      : " + String.format("%.2f", order.getDiscount()));
                System.out.println("Status        : " + order.getStatus());
                System.out.println("Supplier ID   : " + order.getSupplierID());
                break;
            }
        }
    }

    private static String getStatusString(int statusChoice) {
        switch (statusChoice) {
            case 1:
                return "Pending";
            case 2:
                return "Approved and coming";
            case 3:
                return "Cancelled";
            case 4:
                return "Arrived";
            default:
                return null;
        }
    }

    private static void updateOrder(SupplierOrderManagement management, Scanner scanner) {
        boolean whichTypeOfUpdateYouWant = false;
        do {
            System.out.println("Update Order Menu:");
            System.out.println("1. Update Order Expiry Date");
            System.out.println("2. Update Order Status");
            System.out.print("Enter your choice: ");
            try {

                int updateType = scanner.nextInt();
                switch (updateType) {
                    case 1:
                        scanner.nextLine();
                        management.updateOrderExpiryDate(management, scanner);
                        whichTypeOfUpdateYouWant = true;
                        break;
                    case 2:
                        scanner.nextLine();
                        management.statusUpdate(management, scanner);
                        whichTypeOfUpdateYouWant = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a numeric value.\n");
                scanner.next();
            }
        } while (!whichTypeOfUpdateYouWant);
    }

    private static void filterOrRegisterOrder(ProductManagement productManagement, SupplierOrderManagement management, Scanner scanner) {
        int filterOrContinueRegisterOrder = 0;
        do {
            System.out.println("1. Filter Products");
            System.out.println("2. Register Order");
            System.out.print("Enter your choice: ");

            try {
                filterOrContinueRegisterOrder = scanner.nextInt();

                switch (filterOrContinueRegisterOrder) {
                    case 1:
                        productManagement.filterProducts();
                        break;
                    case 2:
                        scanner.nextLine();
                        management.orderRegistration(management, scanner);
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number.");
                scanner.next();
            }
        } while (filterOrContinueRegisterOrder != 2);
    }

    public static void orderManagementMenu() {
        ProductManagement productManagement = new ProductManagement();
        ArrayList<Product> productList = productManagement.getProducts();
        SupplierOrderManagement management = new SupplierOrderManagement(productList);
        Scanner scanner = new Scanner(System.in);
        boolean isFunctionExist = false;

        do {
            int choice = inputChoice(scanner);
            switch (choice) {
                case 1:
                    filterOrRegisterOrder(productManagement, management, scanner);
                    break;
                case 2:
                    management.displayAllOrders();
                    break;
                case 3:
                    updateOrder(management, scanner);
                    break;
                case 4:
                    searchOrder(management, scanner);
                    break;
                case 5:
                    management.generateReports();
                    break;
                case 6:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (!isFunctionExist);
    }
}
