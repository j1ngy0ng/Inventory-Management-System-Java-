/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JAYDEN
 */
public class SupplierOrderReport {

    private List<SupplierOrder> orders;

    public SupplierOrderReport(List<SupplierOrder> orders) {
        this.orders = orders;
    }

    public void generateReport() {
            generateReport("Supplier", SupplierOrder::getSupplierID);
            generateReport("Category", SupplierOrder::getCategory);
    }

    private void generateReport(String reportType, ReportCriteria criteria) {
        List<String> criteriaList;
        double[] salesByCriteria;
        
        criteriaList = collectUniqueCriteria(criteria);
        salesByCriteria = calculateTotalSalesByCriteria(criteriaList, criteria);
        System.out.printf("\nTotal Order by %s:\n", reportType);
        for (int i = 0; i < criteriaList.size(); i++) {
            System.out.printf("%s: %s, Total Sales: %.2f\n", reportType, criteriaList.get(i), salesByCriteria[i]);
        }
    }

    private List<String> collectUniqueCriteria(ReportCriteria criteria) {
        List<String> uniqueCriteriaList = new ArrayList<>();
        for (SupplierOrder order : orders) {
            if ("Approved and coming".equals(order.getStatus()) || "Arrived".equals(order.getStatus())) {
                String key = criteria.getCriteria(order);
                if (!uniqueCriteriaList.contains(key)) {
                    uniqueCriteriaList.add(key);
                }
            }
        }
        return uniqueCriteriaList;
    }

    private double[] calculateTotalSalesByCriteria(List<String> criteriaList, ReportCriteria criteria) {
        double[] salesByCriteria = new double[criteriaList.size()];
        for (SupplierOrder order : orders) {
            if ("Approved and coming".equals(order.getStatus()) || "Arrived".equals(order.getStatus())) {
                String key = criteria.getCriteria(order);
                int index = criteriaList.indexOf(key);
                if (index != -1) {
                    salesByCriteria[index] += order.getTotalPrice();
                }
            }
        }
        return salesByCriteria;
    }

    private interface ReportCriteria {
         String getCriteria(SupplierOrder order);
    }

}
