package backend;

import java.util.HashMap;
import java.util.Map;

public class Bill {
    private Map<Product, Integer> items = new HashMap<>();

    public void addItem(Product p, int qty) {
        if (p.getQuantity() >= qty) {
            items.put(p, items.getOrDefault(p, 0) + qty);
            p.setQuantity(p.getQuantity() - qty);
        } else {
            System.out.println("Not enough stock for: " + p.getName());
        }
    }

    public void printBill() {
        double total = 0;
        System.out.println("\n===== STOCKSENSE BILL =====");
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            Product p = entry.getKey();
            int qty = entry.getValue();
            double cost = qty * p.getPrice();
            System.out.println(p.getName() + " | Qty: " + qty + " | Cost: " + cost);
            total += cost;
        }
        System.out.println("---------------------------");
        System.out.println("TOTAL AMOUNT: " + total);
        System.out.println("===========================\n");
    }
    public String getReceipt() {
        StringBuilder sb = new StringBuilder();
        double total = 0;
    
        sb.append("===== STOCKSENSE BILL =====\n");
    
        for (var entry : items.entrySet()) {
            Product p = entry.getKey();
            int qty = entry.getValue();
            double cost = qty * p.getPrice();
    
            sb.append(p.getName())
              .append(" | Qty: ")
              .append(qty)
              .append(" | Cost: ")
              .append(cost)
              .append("\n");
    
            total += cost;
        }
    
        sb.append("---------------------------\n");
        sb.append("TOTAL AMOUNT: ").append(total).append("\n");
        sb.append("===========================\n");
    
        return sb.toString();
    }
}