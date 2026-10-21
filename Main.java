import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        InventoryManager manager = new InventoryManager();

        int choice;

        do {
            System.out.println("\nINVENTORY MANAGEMENT SYSTEM");
            System.out.println("1. Add Product");
            System.out.println("2. View All Products");
            System.out.println("3. Search Product");
            System.out.println("4. Purchase Stock");
            System.out.println("5. Sell Product");
            System.out.println("6. View Low-Stock Products");
            System.out.println("7. View Transaction History");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter product ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter product name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter price: ");
                    double price = sc.nextDouble();

                    System.out.print("Enter initial quantity: ");
                    int quantity = sc.nextInt();

                    System.out.print("Enter minimum stock limit: ");
                    int minimum = sc.nextInt();

                    if (id <= 0 || price <= 0 ||
                        quantity < 0 || minimum < 0) {
                        System.out.println("Invalid product details.");
                    } else {
                        Product product = new Product(
                            id, name, price, quantity, minimum);
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
                    System.out.println("Exiting Inventory System.");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 8);

        sc.close();
    }
}
