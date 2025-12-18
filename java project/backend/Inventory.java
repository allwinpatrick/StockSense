package backend;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<Product> products = new ArrayList<>();

    public void addProduct(Product p) {
        products.add(p);
    }

    public Product searchProduct(int id) {
        for (Product p : products) {
            if (p.getProductId() == id) {
                return p;
            }
        }
        return null;
    }

    public boolean updateStock(int id, int qty) {
        Product p = searchProduct(id);
        if (p != null) {
            p.setQuantity(p.getQuantity() + qty);
            return true;
        }
        return false;
    }

    public void displayInventory() {
        System.out.println("\n--- INVENTORY LIST ---");
        for (Product p : products) {
            System.out.println(p);
        }
    }

    public List<Product> getProducts() {
        return products;
    }
}