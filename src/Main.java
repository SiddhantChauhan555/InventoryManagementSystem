
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        InventoryManager manager = new InventoryManager();

        int choice = 0;

        do {
            System.out.println("\n===== INVENTORY MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Product");
            System.out.println("2. View All Products");
            System.out.println("3. Search Product");
            System.out.println("4. Purchase Stock");
            System.out.println("5. Sell Product");
            System.out.println("6. View Low-Stock Products");
            System.out.println("7. View Transaction History");
            System.out.println("8. Update Product");
            System.out.println("9. Delete Product");
            System.out.println("10. Exit");
            System.out.print("Enter your choice: ");

            if (!sc.hasNextInt()) {
                System.out.println("Please enter a valid number.");
                sc.nextLine();
                continue;
            }

            choice = sc.nextInt();

            int id, quantity;
            double price;

            switch (choice) {
                case 1:
                    sc.nextLine();

                    System.out.print("Enter product name: ");
                    String name = sc.nextLine().trim();

                    System.out.print("Enter price: ");
                    price = sc.nextDouble();

                    System.out.print("Enter initial quantity: ");
                    quantity = sc.nextInt();

                    System.out.print("Enter minimum stock limit: ");
                    int minimum = sc.nextInt();

                    if (name.isEmpty() || price <= 0 ||
                        quantity < 0 || minimum < 0) {
                        System.out.println("Invalid product details.");
                    } else {
                        Product product = new Product(
                            0, name, price, quantity, minimum
                        );
                        manager.addProduct(product);
                    }
                    break;

                case 2:
                    manager.viewProducts();
                    break;

                case 3:
                    System.out.print("Enter product ID: ");
                    id = sc.nextInt();
                    manager.searchProduct(id);
                    break;

                case 4:
                    System.out.print("Enter product ID: ");
                    id = sc.nextInt();

                    System.out.print("Enter purchase quantity: ");
                    quantity = sc.nextInt();

                    manager.purchaseStock(id, quantity);
                    break;

                case 5:
                    System.out.print("Enter product ID: ");
                    id = sc.nextInt();

                    System.out.print("Enter sale quantity: ");
                    quantity = sc.nextInt();

                    manager.sellProduct(id, quantity);
                    break;

                case 6:
                    manager.showLowStock();
                    break;

                case 7:
                    manager.viewTransactions();
                    break;

                case 8:
                    System.out.print("Enter product ID to update: ");
                    id = sc.nextInt();

                    System.out.print("Enter new price: ");
                    price = sc.nextDouble();

                    System.out.print("Enter new quantity: ");
                    quantity = sc.nextInt();

                    manager.updateProduct(id, price, quantity);
                    break;

                case 9:
                    System.out.print("Enter product ID to delete: ");
                    id = sc.nextInt();

                    System.out.print("Enter 1 to confirm deletion: ");
                    int confirm = sc.nextInt();

                    if (confirm == 1) {
                        manager.deleteProduct(id);
                    } else {
                        System.out.println("Deletion cancelled.");
                    }
                    break;

                case 10:
                    System.out.println("Exiting Inventory System. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 10);

        sc.close();
    }
}
