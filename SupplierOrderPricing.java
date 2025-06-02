/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author JAYDEN
 */
public class SupplierOrderPricing {

    private int quantity;
    private double productPurchasePrice;
    private double discount;

    public int getQuantity() {
        return quantity;
    }

    public double getProductPurchasePrice() {
        return productPurchasePrice;
    }

    public double getDiscount() {
        return discount;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setProductPurchasePrice(double productPurchasePrice) {
        this.productPurchasePrice = productPurchasePrice;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public SupplierOrderPricing(int quantity, double productPurchasePrice, double discount) {
        this.quantity = quantity;
        this.productPurchasePrice = productPurchasePrice;
        this.discount = discount;
    }

    public SupplierOrderPricing(double discount) {
        this.discount = discount;
    }

    public double discountInPercentage() {
        return discount * 100;
    }

    public int amountOfProduct() {
        return quantity;
    }

    public double priceWithAmount() {
        return quantity * productPurchasePrice;
    }

    public double totalPrice() {
        return productPurchasePrice * amountOfProduct();
    }

    public double discount() {
        return totalPrice() * discount;
    }

    public double totalPriceWithDiscount() {
        return totalPrice() - discount();
    }

}
