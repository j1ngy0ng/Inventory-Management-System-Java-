import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class OrderProcessor {

    public void filterOrders(String inputFile, String outputFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split("\\|");
                
                // Check if the status is "Arrived" (index 11 in the array)
                if (fields.length >= 13 && fields[11].equalsIgnoreCase("Arrived")) {
                    // Create a SupplierOrder object using the fields
                    SupplierOrder order = new SupplierOrder(  
                        fields[0], // orderId
                        fields[1], // orderDate
                        fields[2], // expiryDate
                        fields[3], // productId
                        fields[4], // productName
                        fields[5], // unit
                        Integer.parseInt(fields[6]), // quantity
                        Double.parseDouble(fields[7]), // pricePerUnit
                        Double.parseDouble(fields[8]), // totalPrice
                        fields[9], // category
                        Double.parseDouble(fields[10]), // discount
                        fields[11], // status
                        fields[12]  // supplierID
                    );
                    // Write the order to the output file
                    bw.write(order.toString());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

