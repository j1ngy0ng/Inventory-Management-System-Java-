class Order {
    private String orderId;
    private String productId;
    private String productName;  
    private int quantity;
    private double totalPrice;
    private String orderDate;

    public Order(String orderId, String productId, String productName, int quantity, double totalPrice, String orderDate) {
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
    }

    // Getters
    public String getOrderId() {
        return orderId;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getOrderDate() {
        return orderDate;
    }

    // Override toString method for easy display
    @Override
    public String toString() {
        return String.format("%-10s%-13s%-25s%-10d%-15.2f%-15s", 
                orderId, productId, productName, quantity, totalPrice, orderDate);
    }
}
