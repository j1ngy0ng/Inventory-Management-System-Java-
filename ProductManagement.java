
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductManagement {

    private ArrayList<Product> products = new ArrayList<>();
    private Scanner scanner;
    private static final String FILE_NAME = "src\\products.txt";
    private Supplier localsupplier;
    private Supplier foreignsupplier;

    public ArrayList<Product> getProducts() {
        return products;
    }

    public ProductManagement() {
        products = new ArrayList<>();
        scanner = new Scanner(System.in);
        loadProductsFromFile();
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

    public double getValidDoubleInput(String prompt) {
        double input = -1;
        while (true) {
            try {
                System.out.print(prompt);
                input = scanner.nextDouble();
                scanner.nextLine();  // Consume the newline character
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a decimal number.");
                scanner.next();  // Clear invalid input
            }
        }
        return input;
    }

    public String enterCheckSupplierID() {
        localsupplier = new LocalSupplier();
        foreignsupplier = new ForeignSupplier();

        System.out.println("\nSupplier Type (Local/Foreign)");
        System.out.println("1. Local Supplier");
        System.out.println("2. Foreign Supplier");
        int selection = getValidIntInput("Enter Selection: ");

        String supplierID;

        switch (selection) {
            case 1:
                System.out.println(localsupplier.toString());
                System.out.print("Enter Local Supplier ID: ");
                supplierID = scanner.nextLine().toUpperCase();
                for (int i = 0; i < localsupplier.getSupplierCount(); i++) {
                    if (supplierID.equals(localsupplier.getSuppliers()[i].getID())) {
                        return supplierID; // Return valid Local Supplier ID
                    }
                }
                System.out.println("\nLocal Supplier with ID " + supplierID + " does not exist. Please try again.");
                return enterCheckSupplierID(); // Recur until valid ID is entered

            case 2:
                System.out.println(foreignsupplier.toString());
                System.out.print("Enter Foreign Supplier ID: ");
                supplierID = scanner.nextLine().toUpperCase();
                for (int i = 0; i < foreignsupplier.getSupplierCount(); i++) {
                    if (supplierID.equals(foreignsupplier.getSuppliers()[i].getID())) {
                        return supplierID; // Return valid Foreign Supplier ID
                    }
                }
                System.out.println("\nForeign Supplier with ID " + supplierID + " does not exist. Please try again.");
                return enterCheckSupplierID(); // Recur until valid ID is entered

            default:
                System.out.println("Invalid choice, please try again.");
                return enterCheckSupplierID(); // Recur if an invalid selection is made
        }
    }

    public void addProduct() {
        System.out.print("\nEnter Product ID: ");
        String id = scanner.nextLine().toUpperCase();

        // Check if the product ID already exists
        for (Product existingProduct : products) {
            if (existingProduct.getId().equals(id)) {
                System.out.println("Product with ID " + id + " already exists. Cannot add duplicate ID.");
                return;
            }
        }

        System.out.print("Enter Product Name: ");
        String name = scanner.nextLine().toUpperCase();
        double salesPrice = getValidDoubleInput("Enter Sales Price: ");
        double purchasePrice = getValidDoubleInput("Enter Purchase Price: ");
        System.out.print("Enter Unit: ");
        String unit = scanner.nextLine().toUpperCase();
        System.out.print("Enter Product Category: ");
        String category = scanner.nextLine().toUpperCase();

        //call and get the supplier ID
        String supplierID = enterCheckSupplierID(); //call the enter and validate method

        Product newProduct = new Product(id, name, salesPrice, purchasePrice, unit, category, supplierID); // Create the new product

        // Print out the product details before adding
        System.out.println("\nNew Product Details: ");
        System.out.println("ID: " + newProduct.getId() + " | "
                + "Name: " + newProduct.getName() + " | "
                + "Sales Price: " + newProduct.getSalesPrice() + " | "
                + "Purchase Price: " + newProduct.getPurchasePrice() + " | "
                + "Unit: " + newProduct.getUnit() + " | "
                + "Category: " + newProduct.getCategory() + " | "
                + "Supplier ID: " + newProduct.getSupplierID());

        // Add the product with a valid supplier ID
        products.add(newProduct);
        System.out.println("Product added successfully!");

        // Save the products to the file
        saveProductsToFile();
    }

    public void updateProduct() {
        viewProducts();
        System.out.print("\nEnter Product ID to update: ");
        String id = scanner.nextLine().toUpperCase();

        for (Product product : products) {
            if (product.getId().equals(id)) {
                boolean keepUpdating = true;
                while (keepUpdating) {
                    System.out.println("Select the field to update:");
                    System.out.println("1. Name\n2. Sales Price\n3. Purchase Price\n4. Unit\n5. Category\n6. Supplier ID\n0. Finish updating");
                    int choice = getValidIntInput("Enter your choice: ");
                    switch (choice) {
                        case 1:
                            System.out.print("\nEnter new Product Name: ");
                            String newName = scanner.nextLine().toUpperCase();
                            product.setName(newName);
                            System.out.println("Product name updated successfully!");
                            break;
                        case 2:
                            double newSalesPrice = getValidDoubleInput("\nEnter new Sales Price: ");
                            product.setSalesPrice(newSalesPrice);
                            System.out.println("Sales Price updated successfully!");
                            break;
                        case 3:
                            double newPurchasePrice = getValidDoubleInput("\nEnter new Purchase Price: ");
                            product.setPurchasePrice(newPurchasePrice);
                            System.out.println("Purchase Price updated successfully!");
                            break;
                        case 4:
                            System.out.print("\nEnter new Unit: ");
                            String newUnit = scanner.nextLine().toUpperCase();
                            product.setUnit(newUnit);
                            System.out.println("Unit updated successfully!");
                            break;
                        case 5:
                            System.out.print("\nEnter new Product Category: ");
                            String newCategory = scanner.nextLine().toUpperCase();
                            product.setCategory(newCategory);
                            System.out.println("Category updated successfully!");
                            break;
                        case 6:
                            System.out.println("\n<Enter new Supplier ID> ");
                            String newSupplierID = enterCheckSupplierID();
                            product.setSupplierID(newSupplierID);
                            System.out.println("Supplier ID updated successfully!");
                            break;
                        case 0:
                            keepUpdating = false;
                            System.out.println("Finished updating product.");
                            break;
                        default:
                            System.out.println("Invalid choice! Please try again.");
                    }
                }
                saveProductsToFile();
                return;
            }
        }
        System.out.println("Product not found!");
    }

    public void deleteProduct() {
        viewProducts();
        System.out.print("\nEnter Product ID to delete: ");
        String id = scanner.nextLine().toUpperCase();

        boolean removed = products.removeIf(product -> product.getId().equals(id));
        if (removed) {
            System.out.println("Product deleted successfully!");
            saveProductsToFile();
        } else {
            System.out.println("Product not found!");
        }
    }

    public void viewProducts() {
        // Check if the product list is empty and load products if necessary
        if (products.isEmpty()) {
            loadProductsFromFile(); // Ensure products are loaded from file
            if (products.isEmpty()) { // Check again after loading
                System.out.println("No products available.");
                return;
            }
        }

        // Display the product header
        Product.displayHeader();

        // Use the overridden toString method for each product
        for (Product product : products) {
            System.out.println(product);
        }

        displayCategories();
    }

    public void displayCategories() {
        System.out.println("\nCurrent Categories:");
        Set<String> categories = new HashSet<>();
        for (Product product : products) {
            categories.add(product.getCategory());
        }

        // Display categories
        categories.forEach(category -> System.out.println("- " + category));
    }

    public void filterProducts() {
        while (true) {
            System.out.println("\nFilter by:");
            System.out.println("1. Product ID\n2. Product Name\n3. Sales Price (range)\n4. Purchase Price (range)\n5. Category\n6. Supplier ID\n0. Exit");
            int choice = getValidIntInput("Enter your choice: ");
            List<Product> filteredProducts;

            switch (choice) {
                case 1:
                    System.out.print("Enter Product ID: ");
                    String id = scanner.nextLine().toUpperCase();
                    filteredProducts = products.stream().filter(product -> product.getId().equals(id)).collect(Collectors.toList());
                    break;
                case 2:
                    System.out.print("Enter Product Name: ");
                    String name = scanner.nextLine().toUpperCase();
                    filteredProducts = products.stream().filter(product -> product.getName().equals(name)).collect(Collectors.toList());
                    break;
                case 3:
                    double salesPriceMin = getValidDoubleInput("Enter minimum Sales Price: ");
                    double salesPriceMax = getValidDoubleInput("Enter maximum Sales Price: ");
                    filteredProducts = products.stream()
                            .filter(product -> product.getSalesPrice() >= salesPriceMin && product.getSalesPrice() <= salesPriceMax)
                            .collect(Collectors.toList());
                    break;
                case 4:
                    double purchasePriceMin = getValidDoubleInput("Enter minimum Purchase Price: ");
                    double purchasePriceMax = getValidDoubleInput("Enter maximum Purchase Price: ");
                    filteredProducts = products.stream()
                            .filter(product -> product.getPurchasePrice() >= purchasePriceMin && product.getPurchasePrice() <= purchasePriceMax)
                            .collect(Collectors.toList());
                    break;
                case 5:
                    System.out.print("Enter Category: ");
                    String category = scanner.nextLine().toUpperCase();
                    filteredProducts = products.stream().filter(product -> product.getCategory().equals(category)).collect(Collectors.toList());
                    break;
                case 6:
                    System.out.print("Enter Supplier ID: ");
                    String supplierId = scanner.nextLine().toUpperCase();
                    filteredProducts = products.stream().filter(product -> product.getSupplierID().equals(supplierId)).collect(Collectors.toList());
                    break;
                case 0:
                    System.out.println("Exiting filter.");
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
                    continue;
            }

            // Display filtered products
            if (filteredProducts.isEmpty()) {
                System.out.println("No products found matching the criteria.");
            } else {
                System.out.println("\nFiltered Products:");
                Product.displayHeader();
                filteredProducts.forEach(System.out::println);
            }
        }
    }

    public void reportProducts() {
        System.out.println("\n-- Category Report --");

        // Group products by category
        Map<String, List<Product>> categoryProducts = products.stream()
                .collect(Collectors.groupingBy(Product::getCategory));

        // Loop through each category and its products
        for (Map.Entry<String, List<Product>> entry : categoryProducts.entrySet()) {
            String category = entry.getKey();
            List<Product> productList = entry.getValue();

            // Display category and count
            System.out.printf("Product Category: %-20s (Products Count: %-10d)\n", category, productList.size());

            // Print header for product details
            System.out.printf("%-10s| %-20s| %-15s| %-15s\n", "ID", "Name", "Sales Price", "Purchase Price");
            System.out.println("-----------------------------------------------------------------------------");

            // Display each product in the category
            for (Product product : productList) {
                System.out.printf("%-10s| %-20s| %-15.2f| %-15.2f\n",
                        product.getId(),
                        product.getName(),
                        product.getSalesPrice(),
                        product.getPurchasePrice());
            }
            System.out.println();  // Print an empty line for better readability
        }
    }

    public void loadProductsFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length >= 6) {
                    String id = fields[0].trim();
                    String name = fields[1].trim();
                    double salesPrice = Double.parseDouble(fields[2].trim());
                    double purchasePrice = Double.parseDouble(fields[3].trim());
                    String unit = fields[4].trim();
                    String category = fields[5].trim();
                    String supplierID = fields[6].trim();
                    Product product = new Product(id, name, salesPrice, purchasePrice, unit, category, supplierID);
                    products.add(product);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    public void saveProductsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Product product : products) {
                writer.println(product.getId() + ","
                        + product.getName() + ","
                        + product.getSalesPrice() + ","
                        + product.getPurchasePrice() + ","
                        + product.getUnit() + ","
                        + product.getCategory() + ","
                        + product.getSupplierID());
            }
        } catch (IOException e) {
            System.out.println("Error saving products to file: " + e.getMessage());
        }
    }

    public void handleProductSelection(int choice) {
        switch (choice) {
            case 1:
                addProduct();
                displayProductMenuList();
                break;
            case 2:
                updateProduct();
                displayProductMenuList();
                break;
            case 3:
                deleteProduct();
                displayProductMenuList();
                break;
            case 4:
                viewProducts();
                displayProductMenuList();
                break;
            case 5:
                filterProducts();
                displayProductMenuList();
                break;
            case 6:
                reportProducts();
                displayProductMenuList();
                break;
            case 0:
                System.out.println("Exiting...\n");
                break;
            default:
                System.out.println("Invalid choice! Please try again.");
                displayProductMenuList();
        }
    }

    public void displayProductMenuList() {
        System.out.println("\nPRODUCT MANAGEMENT SYSTEM");
        System.out.println("1. Add Product\n2. Update Product\n3. Delete Product\n4. View Products"
                + "\n5. Filter Products\n6. Generate Product Report\n0. Exit");
        int choice = getValidIntInput("Enter your choice: ");
        handleProductSelection(choice);

    }
}