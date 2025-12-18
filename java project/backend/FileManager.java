package backend;
import java.io.*;
import java.util.List;

public class FileManager {

    public static void saveInventory(List<Product> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("inventory.txt"))) {
            for (Product p : list) {
                bw.write(p.getProductId() + "," + p.getName() + "," +
                        p.getQuantity() + "," + p.getPrice());
                bw.newLine();
            }
            System.out.println("Inventory saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }
}