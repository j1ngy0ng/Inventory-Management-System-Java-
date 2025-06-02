
public class SupplierOrder extends Order {

    private String unit;
    private double pricePerUnit;
    private double totalPrice;
    private String category;
    private double discount;
    private String status;
    private String supplierID;
    private String expiryDate;

     public SupplierOrder() {
         this("","","","","","",0,0.00,0.00,"",0.00,"","");
    }
    
    public SupplierOrder(String orderId, String orderDate, String expiryDate, String productId, String productName, String unit, int quantity, double pricePerUnit, double totalPrice, String category, double discount, String status, String supplierID) {
        super(orderId, productId, productName, quantity, totalPrice, orderDate);
        this.expiryDate = expiryDate;
        this.category = category;
        this.unit = unit;
        this.totalPrice = totalPrice;
        this.pricePerUnit = pricePerUnit;
        this.discount = discount;
        this.status = status;
        this.supplierID = supplierID;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSupplierID() {
        return supplierID;
    }

    @Override
    public String toString() {
        return getOrderId() + "|" + getOrderDate() + "|" + expiryDate + "|" + getProductId() + "|" + getProductName() + "|" + unit + "|" + getQuantity() + "|" + pricePerUnit + "|" + totalPrice + "|" + category + "|" + discount + "|" + status + "|" + supplierID;
    }

}
