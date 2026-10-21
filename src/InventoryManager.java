import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class InventoryManager implements StockOperations {
    private Product[] products;
    private int count;
    private final int MAX_PRODUCTS = 100;

    public InventoryManager() {
        products = new Product[MAX_PRODUCTS];
        count = 0;
        loadProducts();
    }

    public void addProduct(Product product) {
        if (count == MAX_PRODUCTS) {
            System.out.println("Inventory is full.");
            return;
        }

        if (findProduct(product.getProductId()) != -1) {
            System.out.println("Product ID already exists.");
            return;
        }

        products[count] = product;
        count++;
        saveProducts();
        System.out.println("Product added successfully.");
    }

    public void viewProducts() {
        if (count == 0) {
            System.out.println("No products available.");
            return;
        }

        for (int i = 0; i < count; i++) {
            products[i].displayProduct();
            System.out.println("--------------------");
        }
    }

    public void searchProduct(int productId) {
        int index = findProduct(productId);

        if (index == -1) {
            System.out.println("Product not found.");
        } else {
            products[index].displayProduct();
        }
    }

    public void purchaseStock(int productId, int quantity) {
        int index = findProduct(productId);

        if (index == -1) {
            System.out.println("Product not found.");
            return;
        }

        if (quantity <= 0) {
            System.out.println("Quantity must be greater than zero.");
            return;
        }

        Product product = products[index];
        product.setQuantity(product.getQuantity() + quantity);

        saveProducts();
        saveTransaction("PURCHASE", productId, quantity);
        System.out.println("Stock purchased successfully.");
    }

    public void sellProduct(int productId, int quantity) {
        int index = findProduct(productId);

        if (index == -1) {
            System.out.println("Product not found.");
            return;
        }

        if (quantity <= 0) {
            System.out.println("Quantity must be greater than zero.");
            return;
        }

        Product product = products[index];

        if (quantity > product.getQuantity()) {
            System.out.println("Insufficient stock.");
            return;
        }

        product.setQuantity(product.getQuantity() - quantity);

        saveProducts();
        saveTransaction("SALE", productId, quantity);
        System.out.println("Product sold successfully.");

        if (product.getQuantity() <= product.getMinimumStock()) {
            System.out.println("Low-stock alert: " + product.getProductName());
        }
    }

    public void showLowStock() {
        boolean found = false;

        for (int i = 0; i < count; i++) {
            if (products[i].getQuantity() <= products[i].getMinimumStock()) {
                products[i].displayProduct();
                System.out.println("LOW STOCK");
                found = true;
            }
        }

        if (!found) {
            System.out.println("No low-stock products.");
        }
    }

    public void viewTransactions() {
        try {
            BufferedReader br = new BufferedReader(
                new FileReader("transactions.txt"));

            String line;
            boolean found = false;

            while ((line = br.readLine()) != null) {
                System.out.println(line);
                found = true;
            }

            br.close();

            if (!found) {
                System.out.println("No transactions recorded.");
            }
        } catch (IOException e) {
            System.out.println("No transaction history available.");
        }
    }

    private int findProduct(int productId) {
        for (int i = 0; i < count; i++) {
            if (products[i].getProductId() == productId) {
                return i;
            }
        }

        return -1;
    }

    private void saveProducts() {
        try {
            FileWriter fw = new FileWriter("products.txt");

            for (int i = 0; i < count; i++) {
                fw.write(products[i].getProductId() + "\n");
                fw.write(products[i].getProductName() + "\n");
                fw.write(products[i].getPrice() + "\n");
                fw.write(products[i].getQuantity() + "\n");
                fw.write(products[i].getMinimumStock() + "\n");
            }

            fw.close();
        } catch (IOException e) {
            System.out.println("Error saving products.");
        }
    }

    private void loadProducts() {
        try {
            BufferedReader br = new BufferedReader(
                new FileReader("products.txt"));

            String idLine;

            while ((idLine = br.readLine()) != null) {
                String nameLine = br.readLine();
                String priceLine = br.readLine();
                String quantityLine = br.readLine();
                String minimumLine = br.readLine();

                if (nameLine == null || priceLine == null ||
                    quantityLine == null || minimumLine == null) {
                    break;
                }

                int id = Integer.parseInt(idLine);
                String name = nameLine;
                double price = Double.parseDouble(priceLine);
                int quantity = Integer.parseInt(quantityLine);
                int minimum = Integer.parseInt(minimumLine);

                if (count < MAX_PRODUCTS && findProduct(id) == -1) {
                    products[count] = new Product(
                        id, name, price, quantity, minimum);
                    count++;
                }
            }

            br.close();
        } catch (IOException e) {
            System.out.println("Starting with an empty inventory.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid data found in products file.");
        }
    }

    private void saveTransaction(String type, int productId, int quantity) {
        try {
            FileWriter fw = new FileWriter("transactions.txt", true);

            fw.write(type + " | Product ID: " + productId
                + " | Quantity: " + quantity + "\n");

            fw.close();
        } catch (IOException e) {
            System.out.println("Error saving transaction.");
        }
    }
}
