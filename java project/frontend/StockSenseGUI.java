package frontend;
import backend.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StockSenseGUI extends JFrame {

    private final Inventory inventory = new Inventory();
    private final Bill bill = new Bill();

    // UI Components
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"ID", "Name", "Quantity", "Price"}, 0
    );
    private final JTable inventoryTable = new JTable(tableModel);

    // Add product fields
    private final JTextField addId = new JTextField();
    private final JTextField addName = new JTextField();
    private final JTextField addQty = new JTextField();
    private final JTextField addPrice = new JTextField();

    // Update stock fields
    private final JTextField updId = new JTextField();
    private final JTextField updQty = new JTextField();

    // Bill fields
    private final JTextField billId = new JTextField();
    private final JTextField billQty = new JTextField();
    private final JTextArea billArea = new JTextArea(10, 40);

    public StockSenseGUI() {
        super("StockSense - Inventory & Billing System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Add Product", buildAddProductPanel());
        tabs.addTab("View Inventory", buildInventoryPanel());
        tabs.addTab("Update Stock", buildUpdateStockPanel());
        tabs.addTab("Generate Bill", buildBillPanel());
        tabs.addTab("Save", buildSavePanel());

        add(tabs);
    }

    private JPanel buildAddProductPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        form.add(new JLabel("Product ID:"));
        form.add(addId);

        form.add(new JLabel("Name:"));
        form.add(addName);

        form.add(new JLabel("Quantity:"));
        form.add(addQty);

        form.add(new JLabel("Price:"));
        form.add(addPrice);

        JButton addBtn = new JButton("Add Product");
        addBtn.addActionListener(e -> addProduct());

        form.add(new JLabel(""));
        form.add(addBtn);

        panel.add(form, BorderLayout.NORTH);
        return panel;
    }

    private JPanel buildInventoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refreshInventoryTable());

        top.add(refreshBtn);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(inventoryTable), BorderLayout.CENTER);

        return panel;
    }

    private JPanel buildUpdateStockPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        form.add(new JLabel("Product ID:"));
        form.add(updId);

        form.add(new JLabel("Quantity to Add (+) / Remove (-):"));
        form.add(updQty);

        JButton updBtn = new JButton("Update Stock");
        updBtn.addActionListener(e -> updateStock());

        form.add(new JLabel(""));
        form.add(updBtn);

        panel.add(form, BorderLayout.NORTH);
        return panel;
    }

    private JPanel buildBillPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(2, 4, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        form.add(new JLabel("Product ID:"));
        form.add(billId);

        form.add(new JLabel("Quantity:"));
        form.add(billQty);

        JButton addToBillBtn = new JButton("Add to Bill");
        addToBillBtn.addActionListener(e -> addToBill());

        JButton printBillBtn = new JButton("Show Bill");
        printBillBtn.addActionListener(e -> showBill());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.add(addToBillBtn);
        buttons.add(printBillBtn);

        billArea.setEditable(false);
        billArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        panel.add(form, BorderLayout.NORTH);
        panel.add(buttons, BorderLayout.CENTER);
        panel.add(new JScrollPane(billArea), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel buildSavePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton saveBtn = new JButton("Save Inventory to File");
        saveBtn.addActionListener(e -> {
            try {
                FileManager.saveInventory(inventory.getProducts());
                JOptionPane.showMessageDialog(this, "Inventory saved successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Save failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(saveBtn, BorderLayout.NORTH);
        return panel;
    }

    // ===================== Actions =====================

    private void addProduct() {
        try {
            int id = Integer.parseInt(addId.getText().trim());
            String name = addName.getText().trim();
            int qty = Integer.parseInt(addQty.getText().trim());
            double price = Double.parseDouble(addPrice.getText().trim());

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name cannot be empty.");
                return;
            }
            if (qty < 0 || price < 0) {
                JOptionPane.showMessageDialog(this, "Quantity/Price cannot be negative.");
                return;
            }

            inventory.addProduct(new Product(id, name, qty, price));
            JOptionPane.showMessageDialog(this, "Product added!");
            clearAddForm();
            refreshInventoryTable();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter valid numbers for ID/Qty/Price.");
        }
    }

    private void updateStock() {
        try {
            int id = Integer.parseInt(updId.getText().trim());
            int qtyChange = Integer.parseInt(updQty.getText().trim());

            Product p = inventory.searchProduct(id);
            if (p == null) {
                JOptionPane.showMessageDialog(this, "Product not found.");
                return;
            }

            // prevent stock going negative
            if (p.getQuantity() + qtyChange < 0) {
                JOptionPane.showMessageDialog(this, "Not enough stock to remove that much.");
                return;
            }

            boolean ok = inventory.updateStock(id, qtyChange);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Stock updated.");
                refreshInventoryTable();
                updId.setText("");
                updQty.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Update failed.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter valid numbers for ID and quantity.");
        }
    }

    private void addToBill() {
        try {
            int id = Integer.parseInt(billId.getText().trim());
            int qty = Integer.parseInt(billQty.getText().trim());

            if (qty <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be > 0.");
                return;
            }

            Product p = inventory.searchProduct(id);
            if (p == null) {
                JOptionPane.showMessageDialog(this, "Product not found.");
                return;
            }

            if (p.getQuantity() < qty) {
                JOptionPane.showMessageDialog(this, "Not enough stock available.");
                return;
            }

            bill.addItem(p, qty);
            JOptionPane.showMessageDialog(this, "Added to bill.");
            refreshInventoryTable();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter valid numbers for ID and quantity.");
        }
    }

    private void showBill() {
        // This needs a receipt string from Bill.
        // If you don't have it yet, add the small method below in Bill.java (next section).
        try {
            billArea.setText(bill.getReceipt());
        } catch (Exception ex) {
            billArea.setText("Bill display failed. Add getReceipt() method in Bill.java.\n" + ex.getMessage());
        }
    }

    private void refreshInventoryTable() {
        tableModel.setRowCount(0);
        for (Product p : inventory.getProducts()) {
            tableModel.addRow(new Object[]{
                    p.getProductId(),
                    p.getName(),
                    p.getQuantity(),
                    p.getPrice()
            });
        }
    }

    private void clearAddForm() {
        addId.setText("");
        addName.setText("");
        addQty.setText("");
        addPrice.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StockSenseGUI().setVisible(true));
    }
}