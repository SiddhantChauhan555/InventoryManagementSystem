import model.Product;

public class Main {
    public static void main(String[] args) {
        Product p1 = new Product(
            101, "Keyboard", 1499.00, 10
        );

        p1.displayDetails();
    }
}