
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class InventoryManager implements StockOperations {

    private final ProductDAO dao = new ProductDAO();

    public void addProduct(Product product) {
        if (product.getProductId() > 0) {
            System.out.println(
                "Product ID auto-generates in MySQL. Enter 0 for ID."
            );
            return;
        }

        if (product.getProductName() == null ||
            product.getProductName().trim().isEmpty() ||
            product.getPrice() <= 0 ||
            product.getQuantity() < 0 ||
            product.getMinimumStock() < 0) {
            System.out.println("Invalid product details.");
            return;
        }

        if (dao.addProduct(product)) {
            System.out.println("Product added successfully!");
        }
    }

    public void viewProducts() {
        List<Product> products = dao.getAllProducts();

        if (products.isEmpty()) {
            System.out.println("No products found.");
            return;
        }

        for (Product p : products) {
            p.displayProduct();
            System.out.println("--------------------");
        }
    }

    public void searchProduct(int productId) {
        Product p = dao.getProductById(productId);

        if (p == null) {
            System.out.println("Product not found.");
        } else {
            p.displayProduct();
        }
    }

    public void purchaseStock(int productId, int quantity) {
        if (quantity <= 0) {
            System.out.println("Quantity must be greater than zero.");
            return;
        }

        String sql =
            "UPDATE products SET quantity = quantity + ? WHERE product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, quantity);
            ps.setInt(2, productId);

            if (ps.executeUpdate() > 0) {
                System.out.println("Stock purchased successfully!");
            } else {
                System.out.println("Product not found.");
            }

        } catch (SQLException e) {
            System.out.println("Purchase failed: " + e.getMessage());
        }
    }

    public void sellProduct(int productId, int quantity) {
        if (dao.recordSale(productId, quantity)) {
            System.out.println("Sale completed successfully!");
        }
    }

    public void showLowStock() {
        List<Product> products = dao.getLowStockProducts();

        if (products.isEmpty()) {
            System.out.println("No low-stock products.");
            return;
        }

        for (Product p : products) {
            p.displayProduct();
            System.out.println("WARNING: LOW STOCK");
            System.out.println("--------------------");
        }
    }
public void updateProduct(int productId, double price, int quantity) {
    if (price <= 0 || quantity < 0) {
        System.out.println("Invalid price or quantity.");
        return;
    }

    if (dao.updateProduct(productId, price, quantity)) {
        System.out.println("Product updated successfully!");
    } else {
        System.out.println("Product not found or update failed.");
    }
}

public void deleteProduct(int productId) {
    if (dao.deleteProduct(productId)) {
        System.out.println("Product deleted successfully!");
    } else {
        System.out.println("Product not found or deletion failed.");
    }
}
    public void viewTransactions() {
        String sql =
            "SELECT sale_id, product_id, quantity_sold, unit_price, " +
            "total_amount, sale_date FROM sales ORDER BY sale_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println("Sale ID: " + rs.getInt("sale_id"));
                System.out.println("Product ID: " + rs.getInt("product_id"));
                System.out.println("Quantity sold: " +
                                   rs.getInt("quantity_sold"));
                System.out.println("Unit price: Rs. " +
                                   rs.getBigDecimal("unit_price"));
                System.out.println("Total: Rs. " +
                                   rs.getBigDecimal("total_amount"));
                System.out.println("Date: " + rs.getTimestamp("sale_date"));
                System.out.println("--------------------");
            }

            if (!found) {
                System.out.println("No sales recorded yet.");
            }

        } catch (SQLException e) {
            System.out.println("Could not read sales history: " +
                               e.getMessage());
        }
    }
}
