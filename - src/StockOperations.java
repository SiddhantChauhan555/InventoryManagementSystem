public interface StockOperations {
    void addProduct(Product product);
    void viewProducts();
    void searchProduct(int productId);
    void purchaseStock(int productId, int quantity);
    void sellProduct(int productId, int quantity);
    void showLowStock();
    void viewTransactions();
}
