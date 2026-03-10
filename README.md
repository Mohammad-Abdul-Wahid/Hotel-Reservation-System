
# 🏨 Hotel Reservation System

A simple **Java + MySQL** based Hotel Reservation System that allows users to manage hotel room bookings through a console interface. This project demonstrates how to integrate Java applications with relational databases using the **Type‑4 JDBC driver (MySQL Connector/J)**.

---

## 📌 Features
- Reserve a room for a guest
- View all reservations with formatted output
- Retrieve room number by reservation ID
- Update reservation details (guest name, room number, contact number)
- Delete reservations by ID
- Exit with a friendly message

---

## 🛠️ Tech Stack
| Component        | Technology |
|------------------|------------|
| Language         | Java (JDK 8 or above) |
| Database         | MySQL |
| JDBC Driver      | MySQL Connector/J (Type‑4 driver) |
| IDE (optional)   | IntelliJ IDEA / Eclipse / NetBeans |

---

## ⚙️ Installation & Setup

### 1. Clone the repository
```bash
git clone https://github.com/your-username/hotel-reservation-system.git
cd hotel-reservation-system
```

### 2. Configure MySQL Database
Create a database:
```sql
CREATE DATABASE hotel_db;
```

Create the `reservations` table:
```sql
CREATE TABLE reservations (
    reservation_id INT AUTO_INCREMENT PRIMARY KEY,
    guest_name VARCHAR(255) NOT NULL,
    room_number INT NOT NULL,
    contact_number VARCHAR(20),
    reservation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 3. Add JDBC Driver
- Download **MySQL Connector/J** from the official MySQL site.
- Place the `.jar` file in your project’s `lib/` folder.
- Add it to the classpath.

### 4. Configure Database Connection
Update credentials in `HotelReservationSystem.java`:
```java
private static final String url = "jdbc:mysql://localhost:3306/hotel_db";
private static final String username = "root";
private static final String password = "your_password";
```

### 5. Compile and Run
```bash
javac -d bin -cp lib/mysql-connector-j-8.0.xx.jar src/HotelReservationSystem.java
java -cp bin:lib/mysql-connector-j-8.0.xx.jar HotelReservationSystem
```

---

## 📂 Project Structure
```
HotelReservationSystem/
│── src/
│   └── HotelReservationSystem.java
│── lib/ (MySQL Connector JAR)
│── README.md
```

---

## 🚀 Future Enhancements
- Add validation to prevent double booking of rooms
- Implement search by guest name or contact number
- Build a GUI using JavaFX or Swing
- Add role‑based access (Admin vs Guest)
- Generate reports (occupancy, revenue, etc.)

---

## 👨‍💻 Developer Notes
- Always close database connections, statements, and result sets to avoid memory leaks.
- Use **PreparedStatement** instead of Statement to prevent SQL injection.
- Handle exceptions gracefully and provide user‑friendly error messages.

---
