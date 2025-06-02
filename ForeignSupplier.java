
class ForeignSupplier extends Supplier {

    public ForeignSupplier() {
        loadSuppliersFromFile("src\\foreignsupplier.txt");
    }

     public ForeignSupplier(String ID, String name, String contact, String address, String email) {
        super(ID, name, contact, address, email);
    }

     
    @Override
    public void displaySupplierActionMenu() {
        System.out.println("Foreign Supplier Menu:");
        System.out.println("1. Add Foreign Supplier");
        System.out.println("2. Modify Foreign Supplier");
        System.out.println("3. Delete Foreign Supplier");
        System.out.println("4. Search Foreign Supplier");
        System.out.println("5. Display All Foreign Suppliers");
        System.out.println("0. Back to Supplier Menu");
        System.out.print("Enter selection: ");
        int foreignSelection = getValidIntInput("Enter selection: ");
        handleForeignSupplierSelection(foreignSelection);
    }

    private void handleForeignSupplierSelection(int selection) {
        SupplierManagement supplierManagement = new SupplierManagement();
        switch (selection) {
            case 1:
                try {
                    addSupplier();
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
                break;
            case 2:
                try {
                    System.out.println(toString());
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
                updateSupplier();
                break;
            case 3:
                try {
                    System.out.println(toString());
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
                deleteSupplier();
                break;
            case 4:
                try {
                    searchSupplier();
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
                break;
            case 5:
                try {
                    System.out.println(toString());
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
                break;
            case 0:
                supplierManagement.supplierMenuList(); // Return to supplier menu list
                return; // Prevent further execution
            default:
                System.out.println("Invalid selection. Please try again.");
                break;
        }

        displaySupplierActionMenu();
    }
}
