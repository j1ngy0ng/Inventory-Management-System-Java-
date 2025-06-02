
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.time.LocalDateTime;

public class Account {

    private String userID;
    private String password;
    private String email;
    private String role;
    private String name;
    private String phoneNumber;
    private int selection;
    private boolean continueInput;
    private boolean loginStatus;
    Scanner input = new Scanner(System.in);
    private AccountManagement accountManagement;

    public Account() {
        loginStatus = true;
        accountManagement = new AccountManagement();
    }

    public Account(String userID, String password, String email, String role, String name, String phoneNumber) {
        this.userID = userID;
        this.password = password;
        this.email = email;
        this.role = role;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void loginSection() {
        do {
            System.out.print("User ID: ");
            userID = input.next();
            System.out.print("Password: ");
            password = input.next();

            if (authenticate(userID, password)) {
                logActivity("Login");
                System.out.println("===============================");
                System.out.println("TAR INVENTORY MANAGEMENT SYSTEM");
                System.out.println("===============================");
                displayMenu();
                loginStatus = false;
            } else {
                System.out.println("Invalid user ID or password. Please try again.");
            }
        } while (loginStatus);
    }

    private boolean authenticate(String userID, String password) {
        Account[] account = accountManagement.getUsers(); // Get accounts from UserManagement
        if (account == null) {
            System.out.println("No account found. The user data file might be empty or corrupted.");
            return false;
        }

        for (Account acc : account) {
            if (acc == null) {
                continue;
            }

            if (acc.getUserID().equals(userID) && acc.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    private void displayMenu() {
        continueInput = true;
        while (continueInput) {
            System.out.println("\nFeatures:");
            System.out.println("1. Account Management");
            System.out.println("2. Inventory Management Control");
            System.out.println("3. Customer Order Management");
            System.out.println("4. Supplier Order Management");
            System.out.println("5. Product Management");
            System.out.println("6. Supplier Management");
            System.out.println("0. Exit");

            try {
                System.out.print("Enter your selection: ");
                selection = input.nextInt();
                handleMenuSelection(selection);
                if (selection == 0) {
                    continueInput = false;
                }
            } catch (Exception ex) {
                System.out.println("Invalid Selection. Try Again: ");
                input.nextLine();
            }
        }
    }

    private void handleMenuSelection(int selection) {
        switch (selection) {
            case 1:
                System.out.println("Account Management selected.");
                accountManagement.accountManagementMenuList();
                break;
            case 2:
                System.out.println("Inventory Management Control selected.");
                InventoryControl control = new InventoryControl();
                control.inventoryMenu();
                break;
            case 3:
                System.out.println("Customer Order Management selected.");
                CustomerOrderManagement customerOrderManagement = new CustomerOrderManagement();
                customerOrderManagement.displayMenu();
                break;

            case 4:
                System.out.println("Supplier Order Management selected");
                SupplierOrderManagement.orderManagementMenu();
                break;
            case 5:
                System.out.println("Product Management selected.");
                ProductManagement productManagement = new ProductManagement();
                productManagement.displayProductMenuList();
                break;
            case 6:
                System.out.println("Supplier Management selected.");
                SupplierManagement supplierManagement = new SupplierManagement();
                supplierManagement.supplierMenuList();
                break;
            case 0:
                System.out.println("Exiting...");
                break;
            default:
                System.out.println("Invalid Selection. Please try again.");
        }
    }

    public void logActivity(String activityType) {
        String filename = "src\\userActivity.txt";
        try (FileWriter fw = new FileWriter(filename, true); PrintWriter pw = new PrintWriter(fw)) {

            LocalDateTime time = LocalDateTime.now();
            pw.println(userID + "|" + activityType + "|" + time);

        } catch (IOException e) {
            System.out.println("An error occurred while logging the activity.");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Account account = (Account) obj;
        return userID.equals(account.userID);
    }

}
