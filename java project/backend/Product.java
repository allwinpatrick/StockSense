package backend;

public class Product {
    private int productId;
    private String name;
    private int quantity;
    private double price;

    public Product(int productId, String name, int quantity, double price) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalValue() {
        return price * quantity;
    }

    @Override
    public String toString() {
        return productId + " | " + name + " | Qty: " + quantity + " | Price: " + price;
    }
}