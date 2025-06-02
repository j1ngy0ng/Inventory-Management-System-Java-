
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Scanner;

public abstract class Supplier {

    private String ID;
    private String name;
    private String contact;
    private String address;
    private String email;
    private Supplier[] suppliers;
    private String filepath;
    private int supplierCount = 0;
    private Scanner scanner = new Scanner(System.in);
    private int selection;

    public Supplier(String ID, String name, String contact, String address, String email) {
        this.ID = ID;
        this.name = name;
        this.contact = contact;
        this.address = address;
        this.email = email;
    }

    public Supplier() {
        suppliers = new Supplier[300];
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFilePath(String filePath) {
        this.filepath = filePath;
    }

    public Supplier[] getSuppliers() {
        return suppliers;
    }

    public int getSupplierCount() {
        return supplierCount;
    }

    
    //Method to manage supplier
    public void addSupplier() {
        if (supplierCount >= suppliers.length) {
            System.out.println("No more suppliers can be added (array is full).");
            return;
        }

        System.out.print("\nEnter Supplier ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Supplier Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Contact: ");
        String contact = scanner.nextLine();
        System.out.print("Enter Address: ");
        String address = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();

        Supplier newSupplier;
        switch (selection) {
            case 1:
                newSupplier = new LocalSupplier(id, name, contact, address, email);  // LocalSupplier instance
                break;
            case 2:
                newSupplier = new ForeignSupplier(id, name, contact, address, email);  // ForeignSupplier instance
                break;
            default:
                System.out.println("Invalid choice. Supplier not added.");
                return;
        }

        for (int i = 0; i < supplierCount; i++) {
            if (newSupplier.equals(suppliers[i])) {
                System.out.println("Supplier with ID " + id + " already exists. Cannot add duplicate ID.");
                return;
            }
        }

        suppliers[supplierCount++] = newSupplier;
        System.out.println("Supplier added successfully!");
        saveSuppliersToFile();
    }

    public void updateSupplier() {
        toString();
        System.out.print("\nEnter Supplier ID to update: ");
        String id = scanner.nextLine().toUpperCase();

        for (int i = 0; i < supplierCount; i++) {
            if (suppliers[i].getID().equals(id)) {
                System.out.println("Select the field to update:");
                System.out.println("1. Name\n2. Contact\n3. Address\n4. Email\n0. Finish updating");
                int choice = getValidIntInput("Enter your choice: ");
                switch (choice) {
                    case 1:
                        System.out.print("\nEnter new Supplier Name: ");
                        suppliers[i].setName(scanner.nextLine());
                        System.out.println("Supplier name updated successfully!");
                        break;
                    case 2:
                        System.out.print("\nEnter new Contact: ");
                        suppliers[i].setContact(scanner.nextLine());
                        System.out.println("Contact updated successfully!");
                        break;
                    case 3:
                        System.out.print("\nEnter new Address: ");
                        suppliers[i].setAddress(scanner.nextLine());
                        System.out.println("Address updated successfully!");
                        break;
                    case 4:
                        System.out.print("\nEnter new Email: ");
                        suppliers[i].setEmail(scanner.nextLine());
                        System.out.println("Email updated successfully!");
                        break;
                    case 0:
                        System.out.println("Finished updating supplier.");
                        break;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                }
                saveSuppliersToFile();
                return;
            }
        }
        System.out.println("Supplier not found!");
    }

    public void deleteSupplier() {
        toString();
        System.out.print("\nEnter Supplier ID to delete: ");
        String id = scanner.nextLine().toUpperCase();

        for (int i = 0; i < supplierCount; i++) {
            if (suppliers[i].getID().equals(id)) {
                for (int j = i; j < supplierCount - 1; j++) {
                    suppliers[j] = suppliers[j + 1];
                }
                suppliers[--supplierCount] = null;
                System.out.println("Supplier deleted successfully!");
                saveSuppliersToFile();
                return;
            }
        }
        System.out.println("Supplier not found!");
    }

    public void searchSupplier() {
        if (supplierCount == 0) {
            System.out.println("No suppliers available.");
            return;
        }

        System.out.println("Search by:\n1. ID\n2. Name\n3. Contact\n4. Email\n5. Address");
        int choice = getValidIntInput("Enter your choice: ");

        System.out.print("Enter search term: ");
        String searchTerm = scanner.nextLine();

        boolean found = false;

        System.out.printf("\u001B[43m" + "\n%-11s %-30s %-12s %-21s %-30s", "SupplierID", "SupplierName", "SupplierContact", "SupplierAddress", "SupplierEmail                    " + "\u001B[0m");
        System.out.println("\u001B[41m" + "\n==================================================================================================================" + "\u001B[0m");

        for (int i = 0; i < supplierCount; i++) {
            Supplier supplier = suppliers[i];
            switch (choice) {
                case 1:
                    if (supplier.getID().contains(searchTerm)) {
                        System.out.printf("\u001B[46m" + "%-11s %-30s %-15s %-21s %-30s\n", suppliers[i].getID(), suppliers[i].getName(), suppliers[i].getContact(), suppliers[i].getAddress(), suppliers[i].getEmail() + "\u001B[0m");
                        found = true;
                    }
                    break;
                case 2:
                    if (supplier.getName().contains(searchTerm)) {
                        System.out.printf("\u001B[46m" + "%-11s %-30s %-15s %-21s %-30s\n", suppliers[i].getID(), suppliers[i].getName(), suppliers[i].getContact(), suppliers[i].getAddress(), suppliers[i].getEmail() + "\u001B[0m");
                        found = true;
                    }
                    break;
                case 3:
                    if (supplier.getContact().contains(searchTerm)) {
                        System.out.printf("\u001B[46m" + "%-11s %-30s %-15s %-21s %-30s\n", suppliers[i].getID(), suppliers[i].getName(), suppliers[i].getContact(), suppliers[i].getAddress(), suppliers[i].getEmail() + "\u001B[0m");
                        found = true;
                    }
                    break;
                case 4:
                    if (supplier.getEmail().contains(searchTerm)) {
                        System.out.printf("\u001B[46m" + "%-11s %-30s %-15s %-21s %-30s\n", suppliers[i].getID(), suppliers[i].getName(), suppliers[i].getContact(), suppliers[i].getAddress(), suppliers[i].getEmail() + "\u001B[0m");
                        found = true;
                    }
                    break;
                case 5:
                    if (supplier.getAddress().contains(searchTerm)) {
                        System.out.printf("\u001B[46m" + "%-11s %-30s %-15s %-21s %-30s\n", suppliers[i].getID(), suppliers[i].getName(), suppliers[i].getContact(), suppliers[i].getAddress(), suppliers[i].getEmail() + "\u001B[0m");
                        found = true;
                    }
                    break;
                default:
                    System.out.println("Invalid choice!");
                    return;
            }
        }

        if (!found) {
            System.out.println("No matching suppliers found.");
        }
    }

    public int getValidIntInput(String prompt) {
        int input = -1;
        while (true) {
            try {
                System.out.print(prompt);
                input = scanner.nextInt();
                scanner.nextLine();  // Consume the newline character
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter an integer.");
                scanner.next();  // Clear invalid input
            }
        }
        return input;
    }

    private void saveSuppliersToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filepath))) {
            for (int i = 0; i < supplierCount; i++) {
                Supplier supplier = suppliers[i];
                writer.println(supplier.getID() + "|" + supplier.getName() + "|" + supplier.getContact() + "|"
                        + supplier.getAddress() + "|" + supplier.getEmail());
            }
        } catch (IOException e) {
            System.err.println("Error saving suppliers to file: " + e.getMessage());
        }
    }

    public void loadSuppliersFromFile(String filepath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = reader.readLine()) != null && supplierCount < suppliers.length) {
                String[] parts = line.split("\\|");
                if (parts.length < 5) {
                    System.err.println("Skipping invalid supplier entry: " + line);
                    continue;
                }
                if (selection == 1) {
                    suppliers[supplierCount++] = new LocalSupplier(parts[0], parts[1], parts[2], parts[3], parts[4]);
                }
                else{
                    suppliers[supplierCount++] = new ForeignSupplier(parts[0], parts[1], parts[2], parts[3], parts[4]);
                }

            }
        } catch (FileNotFoundException e) {
            System.out.println("No existing supplier file found. A new file will be created.");
        } catch (IOException e) {
            System.err.println("Error loading suppliers from file: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        if (supplierCount == 0) {
            return "No suppliers available.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("\u001B[43m").append("\n");
        sb.append(String.format("%-4s %-11s %-30s %-12s %-21s %-30s",
                "No", "SupplierID", "SupplierName",
                "SupplierContact", "SupplierAddress",
                "SupplierEmail")).append("\u001B[0m").append("\n");
        sb.append("\u001B[41m").append("====================================================================================================================").append("\u001B[0m").append("\n");

        for (int i = 0; i < supplierCount; i++) {
            sb.append("\u001B[46m").append(String.format("%-4d %-11s %-30s %-15s %-21s %-30s",
                    i + 1, suppliers[i].getID(),
                    suppliers[i].getName(),
                    suppliers[i].getContact(),
                    suppliers[i].getAddress(),
                    suppliers[i].getEmail())).append("\u001B[0m").append("\n");
            sb.append("\u001B[41m").append("--------------------------------------------------------------------------------------------------------------------").append("\u001B[0m").append("\n");
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Supplier other = (Supplier) obj;
        return this.ID.equals(other.getID());
    }

    public abstract void displaySupplierActionMenu();

}
