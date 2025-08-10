
---

````markdown
# E-Commerce Order Manager (Java + MySQL)

## 📌 Overview
The **E-Commerce Order Manager** is a Java-based console application that connects to a **MySQL database** and allows you to:
- Add new products
- Add new customers
- Place orders (with multiple products and quantities)
- View all orders along with customer details

This project demonstrates:
- **JDBC** database connectivity
- **PreparedStatement** & **ResultSet** usage
- Basic **CRUD operations** in MySQL
- A **menu-driven** Java console program

---

## 🛠 Requirements

- **Java**: JDK 17 or above (any version with `java.sql` will work)
- **MySQL**: 8.x or compatible
- **MySQL Connector/J**: Latest version (e.g., `mysql-connector-j-8.4.0.jar`)

---

## 📂 Database Setup

### 1. Create Database
```sql
CREATE DATABASE ecommerce;
USE ecommerce;
````

### 2. Create Tables

```sql
CREATE TABLE products (
    product_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    price DOUBLE NOT NULL
);

CREATE TABLE customers (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT NOT NULL,
    total_price DOUBLE NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

CREATE TABLE order_items (
    item_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);
```

---

## ⚙️ How to Run

1. **Clone/Download** this project into a folder.
2. **Place MySQL Connector JAR** in your project directory.
3. **Compile** the Java program:

   ```bash
   javac -cp .;mysql-connector-j-8.4.0.jar ECommerceOrderManager.java
   ```

   *(On macOS/Linux use `:` instead of `;` in classpath)*
4. **Run** the program:

   ```bash
   java -cp .;mysql-connector-j-8.4.0.jar ECommerceOrderManager
   ```

---

## 📜 Menu Options

When you run the program, you will see:

```
1. Add Product
2. Add Customer
3. Place Order
4. View Orders
5. Exit
```

### 1. Add Product

* Enter **name**, **category**, and **price** to store product details.

### 2. Add Customer

* Enter **customer name** and **email** to store in customers table.

### 3. Place Order

* Enter **customer ID** and add one or more products with quantities.
* The total price is calculated automatically.

### 4. View Orders

* Lists all orders with customer names and total prices.

---

## 🖼 Example Output

```
1. Add Product
2. Add Customer
3. Place Order
4. View Orders
5. Exit
Choose an option: 1
Enter product name: Laptop
Enter category: Electronics
Enter price: 55000
Product added.

Choose an option: 2
Enter customer name: John Doe
Enter email: john@example.com
Customer added.

Choose an option: 3
Enter customer ID: 1
Enter product ID (0 to finish): 1
Enter quantity: 2
Enter product ID (0 to finish): 0
Order placed. Total amount: ₹110000.0

Choose an option: 4
Order ID: 1 | Customer: John Doe | Total: ₹110000.00
```

---



