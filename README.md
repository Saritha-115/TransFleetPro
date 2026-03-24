# 🚛 TransFleet Pro – Fleet Management System

## 📌 Overview
**TransFleet Pro** is a desktop-based Transportation & Fleet Management System developed using **Java Swing** and **MySQL**.

The system automates delivery operations, optimizes fleet allocation, and provides analytical insights to support efficient decision-making in logistics management.

This project was developed as part of the **Enterprise Application Development (EAD)** coursework for the *Diploma in Software Engineering*.

---

## 🎯 Key Features

### 🚚 Order Management
- Create and manage delivery orders  
- Assign vehicles and drivers  
- Track order lifecycle:
  - Pending  
  - Assigned  
  - In-Transit  
  - Delivered  
  - Cancelled  

---

### 💰 Dynamic Pricing Engine
Implements the **Strategy Pattern** to calculate pricing based on:
- Customer type  
- Distance  
- Priority level  

---

### 🚛 Fleet & Driver Management
- Track vehicle availability  
- Manage driver assignments  
- Automatic resource allocation and release  

---

### 📊 Reporting & Analytics
- Fleet performance reports  
- Profitability analysis  
- Utilization tracking  
- PDF report generation using **JasperReports**  

---

### ✅ Validation & Error Handling
- Centralized validation utilities  
- Custom business exceptions  
- Capacity and availability validation  

---

## 🏗️ System Architecture

The system follows the **MVC (Model–View–Controller)** architecture:

- **Model** → Represents database entities  
- **View** → Java Swing user interface  
- **Controller (DAO Layer)** → Business logic and database operations  

### Benefits:
- Better maintainability  
- Scalable design  
- Clean code structure  

---

## 🛠️ Technology Stack

- **Language:** Java (JDK 8+)  
- **UI Framework:** Java Swing  
- **Database:** MySQL 8.0  
- **Reporting:** JasperReports  
- **Architecture:** MVC  

---

## 🧠 Design Patterns Used

- **Singleton Pattern** → Database connection management  
- **DAO Pattern** → Data access abstraction  
- **Strategy Pattern** → Dynamic pricing logic  
- **Factory Pattern** → Object creation handling  

---

## 🗂️ Project Structure


com.transfleet

│

├── config # Database connection (Singleton)

├── model # Entity classes

├── dao # CRUD operations

├── view # UI components (Swing)

├── strategy # Pricing strategies

├── util # Validation utilities

├── exceptions # Custom exceptions

├── reports # JasperReports integration

└── resources # Report templates (JRXML)


---

## 🗄️ Database Design

- Normalized to **Third Normal Form (3NF)**  
- Uses foreign key constraints for data integrity  
- ENUM fields for controlled values  
- Indexed columns for optimized performance  

### Core Relationship:
The **Orders** table acts as the central entity linking:
- Customers  
- Vehicles  
- Drivers  
- Routes  
- Payments  
- Expenses  

---

## 🎥 Demo Video
👉 Add your demo video link here  

---

## ▶️ How to Run the Project

### Prerequisites
- Java JDK 8 or higher  
- MySQL Server  
- NetBeans IDE  

### Steps
1. Start MySQL server  
2. Import `transfleet_db.sql` into MySQL  
3. Open the project in NetBeans  
4. Add required JAR libraries  
5. Run `MainDashboard.java`  

---

## 🚀 Future Enhancements
- Web-based version (Spring Boot + React)  
- Real-time GPS tracking integration  
- Role-based authentication system  
- REST API support  

---

## 👩‍💻 Developed By

**Saritha Themiyadasa**  
Diploma in Software Engineering  
Enterprise Application Development (EAD)
