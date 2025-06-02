
public class InventoryItem {

    private String name;
    private double quantity;
    private double totalPrice;
    private double safetyStock;
    private String expiredDate;
    private String category;
    private String orderDate;

    // Constructor
    public InventoryItem(String name, double quantity, double totalPrice, double safetyStock, String expiredDate, String category, String orderDate) {
        this.name = name;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.safetyStock = safetyStock;
        this.expiredDate = expiredDate;
        this.category = category;
        this.orderDate = orderDate;
    }

    public void displayItem() {
        System.out.printf("Item Name: %-20s | Quantity: %-15.1f | Total Price: RM%-15.2f | Safety Stock: %-15.1f | Expired Date: %-15s | Category: %-20s | Order Date: %-15s%n",
                name, quantity, totalPrice, safetyStock, expiredDate, category, orderDate);
    }

    // Return item details in a string format to write to file
    @Override
    public String toString() {
        return name + "," + quantity + "," + totalPrice + "," + safetyStock + "," + expiredDate + "," + category + "," + orderDate;
    }

    // Create an item from a string (for loading from file)
    public static InventoryItem fromString(String itemString) {
        String[] parts = itemString.split(",");
        String name = parts[0];
        double quantity = Double.parseDouble(parts[1]);
        double totalPrice = Double.parseDouble(parts[2]);
        double safetyStock = Double.parseDouble(parts[3]);
        String expiredDate = parts[4];
        String category = parts[5];
        String orderDate = parts[6];
        return new InventoryItem(name, quantity, totalPrice, safetyStock, expiredDate, category, orderDate);
    }

    // Getters and setters for all fields
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getSafetyStock() {
        return safetyStock;
    }

    public void setSafetyStock(double safetyStock) {
        this.safetyStock = safetyStock;
    }

    public String getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

}
