
class LocalSupplier extends Supplier {

    public LocalSupplier() {
        loadSuppliersFromFile("src\\localsupplier.txt");
    }
    
     public LocalSupplier(String ID, String name, String contact, String address, String email) {
        super(ID, name, contact, address, email);
    }


    @Override
    public void displaySupplierActionMenu() {
        System.out.println("Local Supplier Menu:");
        System.out.println("1. Add Local Supplier");
        System.out.println("2. Modify Local Supplier");
        System.out.println("3. Delete Local Supplier");
        System.out.println("4. Search Local Supplier");
        System.out.println("5. Display All Local Suppliers");
        System.out.println("0. Back to Supplier Menu");
        System.out.print("Enter selection: ");
        int localSelection = getValidIntInput("Enter selection: ");
        handleLocalSupplierSelection(localSelection);
    }

    private void handleLocalSupplierSelection(int selection) {
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
