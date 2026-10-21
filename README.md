# Inventory Stock Management System

A simple console based inventory management project written in Java.

## Features

- Add and view products
- Search for a product by ID
- Record stock purchases and sales
- Prevent sales beyond available stock
- Show low-stock alerts
- Save inventory and transaction history in text files
- Load saved inventory when the program starts

## Java concepts used

- Classes and objects
- Constructors and methods
- Encapsulation using private variables and getter/setter methods
- Interface implementation
- Arrays and loops
- Scanner for console input
- FileReader, FileWriter and BufferedReader
- Basic exception handling

## Files

- `Product.java` — product details
- `StockOperations.java` — inventory interface
- `InventoryManager.java` — inventory operations and file handling
- `Main.java` — menu and user input
- `products.txt` — created automatically when inventory is saved
- `transactions.txt` — created automatically when a transaction is recorded

## How to run

Make sure the Java JDK is installed. Open a terminal in this folder and run:

```bash
javac Product.java StockOperations.java InventoryManager.java Main.java
java Main
```

## Notes

The program stores up to 100 products in an array. The text files are created in the current working directory when the program writes data.
