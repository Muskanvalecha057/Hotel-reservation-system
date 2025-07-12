# 🏨 Hotel Reservation System - Java Swing Project

This is a **Hotel Reservation System** built using Java Swing and connected to a backend MySQL database. It allows users to search, book, cancel, and view hotel room reservations.

---

## 🚀 Features

- **Login System**
  - User login with database validation

- **Main Menu**
  - Navigate to Search, Book, Cancel, and View options

- **Room Search**
  - Filter by room type, check-in/out dates, and number of persons
  - Rooms already booked for selected dates are marked as **"Booked"**

- **Booking Confirmation**
  - Shows selected room and stay details
  - Data passed via static variables
  - Booking saved in database

- **Booking Receipt**
  - Combines user, room, and booking details
  - Shows full receipt after booking

- **Cancel Booking**
  - Allows cancellation based on current date and check-out
  - Calculates refund if applicable
  - Shows appropriate message if already checked out or stay completed

---

## 🛠️ Technologies Used

- Java Swing (GUI)
- NetBeans IDE (Drag-and-Drop)
- MySQL
- JDBC

---

## 📁 Database Structure

- **users**: user_id, name, email, phone, cnic
- **room**: room_id, type, price, status, capacity
- **bookings**: booking_id, user_id, room_id, check_in, check_out, persons

---

## ✅ How to Run

1. Open project in NetBeans
2. Configure your MySQL DB credentials in DB connection file
3. Run `Welcome.java`
4. Login or create an account
5. Start booking, viewing, or canceling rooms!

---

## 🖼️ GUI Screenshots

### 🔹 Welcome Page
![Welcome Page](screenshots/welcome page.png)

### 🔹 Login Page
![Login Page](screenshots/Login page.png)

### 🔹 User Menu
![User Menu](screenshots/user menu.png)

### 🔹 Room Search
![Room Search](screenshots/room search.png)

### 🔹 Booking Receipt
![Receipt](screenshots/Receipt.png)

### 🔹 Cancel Booking
![Cancel Booking](screenshots/cancel booking.png)

---

## 👩‍💻 Developed By

**Muskan Valecha**  
Student of Computer Science  
Interested in UI/UX and Java Projects

