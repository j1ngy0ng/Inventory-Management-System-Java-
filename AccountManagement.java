import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;

public class AccountManagement {

    private int accountCount;
    private Account[] account;
    private String filePath = "src\\UserData.txt";
    Scanner scanner = new Scanner(System.in);
    int selection;

    public AccountManagement() {
        account = new Account[300];
        accountCount = 0;
        loadAccountsFromFile();
    }

    public void addAccount() {
        if (accountCount >= 300) {
            System.out.println("Cannot add more account. Maximum limit reached.");
            return;
        }

        System.out.print("Enter User ID: ");
        String userID = scanner.nextLine();

        Account tempAccount = new Account(userID, "", "", "", "", "");

        for (int i = 0; i < accountCount; i++) {
            if (tempAccount.equals(account[i])) {
                System.out.println("Account with ID " + userID + " already exists. Cannot add duplicate ID.");
                return;
            }
        }

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Role: ");
        String role = scanner.nextLine();
        System.out.print("Enter Phone Number: ");
        String phoneNumber = scanner.nextLine();

        account[accountCount] = new Account(userID, password, email, role, name, phoneNumber);
        accountCount++;
        System.out.println("Account added successfully!");
        saveAccountsToFile();
    }

    public void modifyAccount() {
        System.out.println(toString());
        System.out.print("\nEnter User ID to update: ");
        String userID = scanner.nextLine().trim().toUpperCase();
        Account acc = findAccountByID(userID);

        if (acc == null) {
            System.out.println("Account with ID " + userID + " not found!");
            return;
        }

        while (true) {
            System.out.println("\nSelect the field to update:");
            System.out.println("1. Password");
            System.out.println("2. Name");
            System.out.println("3. Email");
            System.out.println("4. Role");
            System.out.println("5. Phone Number");
            System.out.println("0. Finish updating");
            int choice = getValidIntInput("Enter your choice: ");
            if (choice == 0) {
                System.out.println("Finished updating account.");
                break;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter new Password: ");
                    String newPassword = scanner.nextLine();
                    acc.setPassword(newPassword);
                    System.out.println("Password updated successfully!");
                    break;
                case 2:
                    System.out.print("Enter new Name: ");
                    String newName = scanner.nextLine();
                    acc.setName(newName);
                    System.out.println("Name updated successfully!");
                    break;
                case 3:
                    System.out.print("Enter new Email: ");
                    String newEmail = scanner.nextLine();
                    acc.setEmail(newEmail);
                    System.out.println("Email updated successfully!");
                    break;
                case 4:
                    System.out.print("Enter new Role: ");
                    String newRole = scanner.nextLine();
                    acc.setRole(newRole);
                    System.out.println("Role updated successfully!");
                    break;
                case 5:
                    System.out.print("Enter new Phone Number: ");
                    String newPhone = scanner.nextLine();
                    acc.setPhoneNumber(newPhone);
                    System.out.println("Phone Number updated successfully!");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
        saveAccountsToFile();
    }

    public void deleteAccount() {
        System.out.println(toString());
        System.out.print("\nEnter User ID to delete: ");
        String userID = scanner.nextLine();
        Account acc = findAccountByID(userID);

        if (acc == null) {
            System.out.println("Account with ID " + userID + " not found!");
            return;
        }

        for (int i = 0; i < accountCount; i++) {
            if (account[i].getUserID().equalsIgnoreCase(userID)) {
                account[i] = account[accountCount - 1]; // Replace deleted user with the last user
                account[accountCount - 1] = null;
                accountCount--;
                System.out.println("Account deleted successfully!");
                saveAccountsToFile();
                return;
            }
        }
    }

    public void searchAccount() {
        if (accountCount == 0) {
            System.out.println("No account available to search.");
            return;
        }

        System.out.println("Search by:");
        System.out.println("1. User ID");
        System.out.println("2. Name");
        System.out.println("3. Email");
        System.out.println("4. Role");
        System.out.println("5. Phone Number");
        int choice = getValidIntInput("Enter your choice: ");
        System.out.print("Enter search term: ");
        String searchTerm = scanner.nextLine();

        boolean found = false;
        System.out.printf("\u001B[43m" + "\n%-15s %-20s %-25s %-15s %-15s", "UserID", "Name", "Email", "Role", "Phone Number  " + "\u001B[0m");
        System.out.println("\u001B[41m" + "\n---------------------------------------------------------------------------------------------" + "\u001B[0m");
        for (int i = 0; i < accountCount; i++) {
            Account acc = account[i];
            switch (choice) {
                case 1:
                    if (acc.getUserID().contains(searchTerm)) {
                        found = true;
                        System.out.printf("\u001B[46m" + "%-15s %-20s %-25s %-15s %-15s\n", account[i].getUserID(), account[i].getName(), account[i].getEmail(), account[i].getRole(), account[i].getPhoneNumber() + "\u001B[0m");
                    }
                    break;
                case 2:
                    if (acc.getName().contains(searchTerm)) {
                        found = true;
                        System.out.printf("\u001B[46m" + "%-15s %-20s %-25s %-15s %-15s\n", account[i].getUserID(), account[i].getName(), account[i].getEmail(), account[i].getRole(), account[i].getPhoneNumber() + "\u001B[0m");
                    }
                    break;
                case 3:
                    if (acc.getEmail().contains(searchTerm)) {
                        found = true;
                        System.out.printf("\u001B[46m" + "%-15s %-20s %-25s %-15s %-15s\n", account[i].getUserID(), account[i].getName(), account[i].getEmail(), account[i].getRole(), account[i].getPhoneNumber() + "\u001B[0m");
                    }
                    break;
                case 4:
                    if (acc.getRole().contains(searchTerm)) {
                        found = true;
                        System.out.printf("\u001B[46m" + "%-15s %-20s %-25s %-15s %-15s\n", account[i].getUserID(), account[i].getName(), account[i].getEmail(), account[i].getRole(), account[i].getPhoneNumber() + "\u001B[0m");
                    }
                    break;
                case 5:
                    if (acc.getPhoneNumber().contains(searchTerm)) {
                        found = true;
                        System.out.printf("\u001B[46m" + "%-15s %-20s %-25s %-15s %-15s\n", account[i].getUserID(), account[i].getName(), account[i].getEmail(), account[i].getRole(), account[i].getPhoneNumber() + "\u001B[0m");
                    }
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
                    return;
            }
        }

        if (!found) {
            System.out.println("No matching account found.");
        }
    }

    public void accountActivityReport() {
        int no = 1;
        File file = new File("src\\userActivity.txt");

        if (!file.exists()) {
            System.out.println("Account activity file not found.");
            return;
        }

        try (Scanner fileScanner = new Scanner(file)) {
            System.out.printf("\u001B[43m" + "\n%-4s %-7s %-11s %-30s", "No", "User ID", "Activity", "TimeStamp                    " + "\u001B[0m");
            System.out.println("");

            while (fileScanner.hasNextLine()) {
                String str = fileScanner.nextLine();
                String[] arrOfStr = str.split("\\|");

                if (arrOfStr.length == 3) {
                    System.out.printf("\u001B[46m" + "%-4d %-7s %-11s %-30s\n", no, arrOfStr[0], arrOfStr[1], arrOfStr[2] + "\u001B[0m");
                } else {
                    System.out.println("Invalid record format: " + str);
                }

                System.out.println("\u001B[41m" + "------------------------------------------------------" + "\u001B[0m");
                no++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error reading user activity file: " + e.getMessage());
        }
    }

    public void accountManagementMenuList() {
        while (true) {
            System.out.println("\n--- Account Management Menu ---");
            System.out.println("1. Add Account");
            System.out.println("2. Modify Account");
            System.out.println("3. Delete Account");
            System.out.println("4. Search Account");
            System.out.println("5. Display All Accounts");
            System.out.println("6. Account Activity Report");
            System.out.println("0. Exit to Main Menu");
            int selection = getValidIntInput("Enter your selection: ");
            if (selection == 0) {
                System.out.println("Returning to main menu...");
                break;
            }
            handleAccountSelection(selection);
        }
    }

    private void handleAccountSelection(int selection) {
        switch (selection) {
            case 1:
                addAccount();
                break;
            case 2:
                modifyAccount();
                break;
            case 3:
                deleteAccount();
                break;
            case 4:
                searchAccount();
                break;
            case 5:
                System.out.println(toString());
                break;
            case 6:
                accountActivityReport();
                break;
            default:
                System.out.println("Invalid selection! Please try again.");
        }
    }

    private Account findAccountByID(String userID) {
        for (int i = 0; i < accountCount; i++) {
            if (account[i].getUserID().equalsIgnoreCase(userID)) {
                return account[i];
            }
        }
        return null;
    }

    private void saveAccountsToFile() {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (int i = 0; i < accountCount; i++) {
                Account acc = account[i];
                writer.write(acc.getUserID() + "|" + acc.getPassword() + "|" + acc.getEmail() + "|" + acc.getRole() + "|" + acc.getName() + "|" + acc.getPhoneNumber() + "\n");
            }
            System.out.println("Account saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving account to file: " + e.getMessage());
        }
    }

    public void loadAccountsFromFile() {

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 6) {
                    account[accountCount++] = new Account(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading account from file: " + e.getMessage());
        }
    }

    private int getValidIntInput(String prompt) {
        int result = -1;
        while (true) {
            try {
                System.out.print(prompt);
                result = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
            }
        }
        return result;
    }

    public Account[] getUsers() {
        return account;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (accountCount == 0) {
            sb.append("No account available.\n");
            return sb.toString();
        }

        sb.append(String.format("\u001B[43m" + "%-5s %-15s %-20s %-25s %-20s %-15s\n", "No", "UserID", "Name", "Email", "Role", "Phone Number  " + "\u001B[0m"));
        sb.append("\u001B[41m" + "========================================================================================================\n" + "\u001B[0m");
        for (int i = 0; i < accountCount; i++) {
            Account acc = account[i];
            sb.append(String.format("\u001B[46m" + "%-5d %-15s %-20s %-25s %-20s %-13s\n" + "\u001B[0m", i + 1, acc.getUserID(), acc.getName(), acc.getEmail(), acc.getRole(), acc.getPhoneNumber()));
            sb.append("\u001B[41m").append("--------------------------------------------------------------------------------------------------------").append("\u001B[0m").append("\n");
        }

        return sb.toString();
    }

}
