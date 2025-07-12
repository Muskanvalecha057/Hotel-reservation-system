# 🏨 Hotel Reservation System - Java Swing Project

This is a **Hotel Reservation System** built using Java Swing (GUI) and connected with a backend database. It allows users to search, book, cancel, and view hotel room reservations with a simple interface.

---

## 🚀 Features

- **Welcome and Login Pages**
  - User authentication using database
  - Stores and uses user ID after login

- **Main Menu**
  - Navigate to Search, Book, Cancel, or View pages

- **Room Search**
  - Filter by room type, number of persons, check-in and check-out dates
  - Shows room availability and prices
  - If room is already booked during selected dates, shows status as **"Booked"**

- **Booking System**
  - Room selection through a table with a **"Book" button**
  - Displays booking confirmation page with:
    - Room ID, type, price
    - Check-in, check-out, number of persons
    - Nights stayed and total amount
  - Booking details saved to `bookings` table

- **Receipt Page**
  - Fetches and displays combined data from:
    - `users` table (name, email, etc.)
    - `bookings` table (check-in, check-out, etc.)
    - `room` table (room details)

- **Booking Cancellation**
  - User provides Booking ID and User ID
  - System fetches details and:
    - If current date < check-out: updates check-out to today & calculates **refund**
    - If today == check-out: shows **already checked out**
    - If stay is completed: shows **stay completed**

---

## 🛠️ Technologies Used

- **Java Swing** (GUI)
- **NetBeans** (Drag & Drop Interface)
- **MySQL** (Database)
- **JDBC** (Java Database Connectivity)

---

## 📁 Database Structure

### `users` Table
- `user_id` (PK)
- `name`, `email`, `phone`, `cnic`

### `room` Table
- `room_id` (PK)
- `type`, `price`, `status`, `capacity`

### `bookings` Table
- `booking_id` (PK)
- `user_id` (FK)
- `room_id` (FK)
- `check_in`, `check_out`, `persons`

---

## ✅ How to Run

1. Clone the project in NetBeans
2. Set up MySQL database and update DB connection in Java
3. Run the project starting from the **Welcome.java** file
4. Login or register as a user
5. Search, Book, or Cancel rooms easily!

---

## 📌 Notes

- All data is passed between forms using static variables
- Room availability is checked before booking
- GUI fully developed using NetBeans drag-and-drop interface
- Supports basic validation and edge case handling

---

## 👤 Developed By

**Muskan Valecha**  
Bachelor's in Computer Science  
Special interest in UI/UX and Java-based systems  
