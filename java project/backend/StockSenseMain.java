package backend;
import java.util.Scanner;

public class StockSenseMain {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Inventory inventory = new Inventory();
        Bill bill = new Bill();

        while (true) {
            System.out.println("\n===== STOCKSENSE MENU =====");
            System.out.println("1. Add Product");
            System.out.println("2. View Inventory");
            System.out.println("3. Update Stock");
            System.out.println("4. Generate Bill");
            System.out.println("5. Save Inventory");
            System.out.println("6. Exit");
            System.out.print("Choose option: ");

            int ch = sc.nextInt();

            switch (ch) {

                case 1:
                    System.out.print("Product ID: ");
                    int id = sc.nextInt();
                    System.out.print("Name: ");
                    String name = sc.next();
                    System.out.print("Quantity: ");
                    int qty = sc.nextInt();
                    System.out.print("Price: ");
                    double price = sc.nextDouble();
                    inventory.addProduct(new Product(id, name, qty, price));
                    break;

                case 2:
                    inventory.displayInventory();
                    break;

                case 3:
                    System.out.print("Enter Product ID: ");
                    int pid = sc.nextInt();
                    System.out.print("Enter qty to add: ");
                    int q = sc.nextInt();
                    if (inventory.updateStock(pid, q)) {
                        System.out.println("Stock Updated.");
                    } else {
                        System.out.println("Product not found.");
                    }
                    break;

                case 4:
                    System.out.print("Enter Product ID: ");
                    int bid = sc.nextInt();
                    System.out.print("Quantity: ");
                    int bqty = sc.nextInt();

                    Product p = inventory.searchProduct(bid);
                    if (p != null) {
                        bill.addItem(p, bqty);
                        bill.printBill();
                    } else {
                        System.out.println("Product not found.");
                    }
                    break;

                case 5:
                    FileManager.saveInventory(inventory.getProducts());
                    break;

                case 6:
                    System.out.println("Exiting StockSense...");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid Option!");
            }
        }
    }
}