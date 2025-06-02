/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author acer
 */
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.*;
import java.util.stream.Collectors;

public class Product {

    private String id;
    private String name;
    private double salesPrice;
    private double purchasePrice;
    private String unit;
    private String category;
    private String supplierID;

    public Product(String id, String name, double salesPrice, double purchasePrice, String unit, String category, String supplierID) {
        this.id = id;
        this.name = name;
        this.salesPrice = salesPrice;
        this.purchasePrice = purchasePrice;
        this.unit = unit;
        this.category = category;
        this.supplierID = supplierID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(double salesPrice) {
        this.salesPrice = salesPrice;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    public void display() {
        System.out.printf("| %-10s | %-20s | %-11.2f | %-14.2f | %-15s | %-20s | %-19s |\n",
                id, name, salesPrice, purchasePrice, unit, category, supplierID);
    }

    @Override
    public String toString() {
        return String.format("| %-10s | %-20s | %-11.2f | %-14.2f | %-15s | %-20s | %-19s |",
                id, name, salesPrice, purchasePrice, unit, category, supplierID);
    }

    public static void displayHeader() {
        System.out.println("\n[Product List]");
        System.out.println("|ID          |Product Name          | Sales Price | Purchase Price | Unit            | Category             | Supplier ID         |");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------");
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj){
            return true; //If the objects are the same instance
        }
        if(obj == null||getClass()!= obj.getClass()){
            return false;
        }
        Product other = (Product) obj;
        return this.id.equals(other.getId());
    }
}
