//ECommerceOrderManager.java
import java.sql.*;
import java.util.*;

public class ECommerceOrderManager {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ecommerce";
    private static final String USER = "root";
    private static final String PASS = "subhapahari@2020";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            while (true) {
                System.out.println("\n1. Add Product\n2. Add Customer\n3. Place Order\n4. View Orders\n5. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> addProduct(conn, scanner);
                    case 2 -> addCustomer(conn, scanner);
                    case 3 -> placeOrder(conn, scanner);
                    case 4 -> viewOrders(conn);
                    case 5 -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid option.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void addProduct(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter category: ");
        String category = scanner.nextLine();
        System.out.print("Enter price: ");
        double price = scanner.nextDouble();

        String query = "INSERT INTO products (name, category, price) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, name);
            ps.setString(2, category);
            ps.setDouble(3, price);
            ps.executeUpdate();
            System.out.println("Product added.");
        }
    }

    static void addCustomer(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        String query = "INSERT INTO customers (name, email) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.executeUpdate();
            System.out.println("Customer added.");
        }
    }

    static void placeOrder(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter customer ID: ");
        int customerId = scanner.nextInt();
        scanner.nextLine();

        String insertOrder = "INSERT INTO orders (customer_id, total_price) VALUES (?, 0.0)";
        PreparedStatement psOrder = conn.prepareStatement(insertOrder, Statement.RETURN_GENERATED_KEYS);
        psOrder.setInt(1, customerId);
        psOrder.executeUpdate();

        ResultSet rs = psOrder.getGeneratedKeys();
        rs.next();
        int orderId = rs.getInt(1);

        double total = 0.0;
        while (true) {
            System.out.print("Enter product ID (0 to finish): ");
            int productId = scanner.nextInt();
            if (productId == 0) break;

            System.out.print("Enter quantity: ");
            int qty = scanner.nextInt();

            String priceQuery = "SELECT price FROM products WHERE product_id=?";
            PreparedStatement psPrice = conn.prepareStatement(priceQuery);
            psPrice.setInt(1, productId);
            ResultSet priceRS = psPrice.executeQuery();

            if (priceRS.next()) {
                double price = priceRS.getDouble("price");
                total += price * qty;

                String insertItem = "INSERT INTO order_items (order_id, product_id, quantity) VALUES (?, ?, ?)";
                PreparedStatement psItem = conn.prepareStatement(insertItem);
                psItem.setInt(1, orderId);
                psItem.setInt(2, productId);
                psItem.setInt(3, qty);
                psItem.executeUpdate();
            }
        }

        String updateOrder = "UPDATE orders SET total_price=? WHERE order_id=?";
        PreparedStatement psUpdate = conn.prepareStatement(updateOrder);
        psUpdate.setDouble(1, total);
        psUpdate.setInt(2, orderId);
        psUpdate.executeUpdate();

        System.out.println("Order placed. Total amount: ₹" + total);
    }

    static void viewOrders(Connection conn) throws SQLException {
        String query = "SELECT o.order_id, c.name, o.total_price FROM orders o JOIN customers c ON o.customer_id = c.customer_id";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            System.out.printf("Order ID: %d | Customer: %s | Total: ₹%.2f%n",
                    rs.getInt("order_id"), rs.getString("name"), rs.getDouble("total_price"));
        }
    }
}
