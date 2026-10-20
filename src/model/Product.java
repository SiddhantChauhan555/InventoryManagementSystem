package model;

public class Product {
    private int id;
    private String name;
    private double price;
    private int quantity;

    public Product(int id, String name,
                   double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public void displayDetails() {
        System.out.println("Product ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Price: Rs. " + price);
        System.out.println("Quantity: " + quantity);
    }
}