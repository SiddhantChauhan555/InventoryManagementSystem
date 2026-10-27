
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // Add a new product
    public boolean addProduct(Product product) {
        String sql = "INSERT INTO products " +
                "(name, price, quantity, minimum_stock) VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, product.getProductName());
            ps.setDouble(2, product.getPrice());
            ps.setInt(3, product.getQuantity());
            ps.setInt(4, product.getMinimumStock());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Add product failed: " + e.getMessage());
            return false;
        }
    }

    // Display all products
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();

        String sql = "SELECT product_id, name, price, quantity, minimum_stock " +
                     "FROM products ORDER BY product_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        rs.getInt("minimum_stock")
                );

                products.add(product);
            }

        } catch (SQLException e) {
            System.out.println("View products failed: " + e.getMessage());
        }

        return products;
    }

    // Search product by ID
    public Product getProductById(int productId) {
        String sql = "SELECT product_id, name, price, quantity, minimum_stock " +
                     "FROM products WHERE product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                            rs.getInt("product_id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getInt("quantity"),
                            rs.getInt("minimum_stock")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Search failed: " + e.getMessage());
        }

        return null;
    }

    // Display products at or below minimum stock
    public List<Product> getLowStockProducts() {
        List<Product> products = new ArrayList<>();

        String sql = "SELECT product_id, name, price, quantity, minimum_stock " +
                     "FROM products WHERE quantity <= minimum_stock";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        rs.getInt("minimum_stock")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Low-stock check failed: " + e.getMessage());
        }

        return products;
    }
    
    // Update product price and quantity
    public boolean updateProduct(int productId, double price, int quantity) {
        String sql = "UPDATE products SET price = ?, quantity = ? " +
                     "WHERE product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, price);
            ps.setInt(2, quantity);
            ps.setInt(3, productId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
            return false;
        }
    }

    // Delete product by ID
    public boolean deleteProduct(int productId) {
        String sql = "DELETE FROM products WHERE product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Delete failed: " + e.getMessage());
            return false;
        }
    }

    // Record a sale and reduce stock safely
    public boolean recordSale(int productId, int quantitySold) {
        if (quantitySold <= 0) {
            System.out.println("Quantity must be greater than zero.");
            return false;
        }

        String stockSql =
            "SELECT price, quantity FROM products WHERE product_id = ? FOR UPDATE";

        String saleSql =
            "INSERT INTO sales (product_id, quantity_sold, unit_price, total_amount) " +
            "VALUES (?, ?, ?, ?)";

        String updateSql =
            "UPDATE products SET quantity = quantity - ? " +
            "WHERE product_id = ? AND quantity >= ?";

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);

            try {
                double price;
                int available;

                // Lock product row while processing this sale
                try (PreparedStatement ps = con.prepareStatement(stockSql)) {
                    ps.setInt(1, productId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            System.out.println("Product not found.");
                            con.rollback();
                            return false;
                        }

                        price = rs.getDouble("price");
                        available = rs.getInt("quantity");
                    }
                }

                if (available < quantitySold) {
                    System.out.println("Insufficient stock. Available: " + available);
                    con.rollback();
                    return false;
                }

                double total = price * quantitySold;

                try (PreparedStatement ps = con.prepareStatement(saleSql)) {
                    ps.setInt(1, productId);
                    ps.setInt(2, quantitySold);
                    ps.setDouble(3, price);
                    ps.setDouble(4, total);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = con.prepareStatement(updateSql)) {
                    ps.setInt(1, quantitySold);
                    ps.setInt(2, productId);
                    ps.setInt(3, quantitySold);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException("Stock update failed.");
                    }
                }

                con.commit();
                System.out.println("Sale recorded! Total: Rs. " + total);
                return true;

            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.out.println("Sale failed: " + e.getMessage());
            return false;
        }
    }

}
