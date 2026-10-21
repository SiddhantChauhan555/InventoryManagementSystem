public class Product {
    private int productId;
    private String productName;
    private double price;
    private int quantity;
    private int minimumStock;

    public Product(int productId, String productName, double price, int quantity, int minimumStock) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.minimumStock = minimumStock;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getMinimumStock() {
        return minimumStock;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void displayProduct() {
        System.out.println("Product ID: " + productId);
        System.out.println("Product Name: " + productName);
        System.out.println("Price: " + price);
        System.out.println("Quantity: " + quantity);
        System.out.println("Minimum Stock: " + minimumStock);
    }
}
