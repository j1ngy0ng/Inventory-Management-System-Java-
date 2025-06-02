import java.util.Scanner;

public class SupplierManagement {

    private Scanner scanner = new Scanner(System.in);
    private int selection;

    // Supplier selection method
    public void supplierMenuList() {
        System.out.println("1. Local Supplier");
        System.out.println("2. Foreign Supplier");
        System.out.println("0. Back to main menu");
        System.out.print("Enter selection: ");
        selection = scanner.nextInt();

        handleSupplierSelection(selection);
    }

    // Handle user selection
    public void handleSupplierSelection(int selection) {
        Supplier supplier;

        switch (selection) {
            case 1:
                supplier = new LocalSupplier(); 
                supplier.setFilePath("src\\localsupplier.txt");
                supplier.displaySupplierActionMenu(); 
                break;
            case 2:
                supplier = new ForeignSupplier(); 
                supplier.setFilePath("src\\foreignsupplier.txt");
                supplier.displaySupplierActionMenu(); 
                break;
            case 0:
                System.out.println("Returning to the main menu...");
                break;
            default:
                System.out.println("Invalid choice, please try again.");
                supplierMenuList();
        }
    }
}
