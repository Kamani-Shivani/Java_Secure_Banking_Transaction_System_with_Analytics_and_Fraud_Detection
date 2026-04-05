#  Java-Based Secure Banking Transaction System with Analytics & Fraud Detection

> **Database and table creation was carried out using `pgAdmin4`, ensuring a structured and efficient setup.**

This project is a **Java-based console banking application** developed using **JDBC and PostgreSQL** as part of the **Virtusa Pre-Onboarding Training Assignment**.

The system is designed to simulate real-world banking operations, including account management, transaction processing, reporting, and fraud detection using database-level logic.

---

## 📌 Project Overview

The application enables users to perform core banking operations such as managing accounts, executing transactions, and analyzing financial activity. It also incorporates a **fraud detection mechanism using PostgreSQL triggers**, ensuring enhanced system reliability and data integrity.

---

## ✨ Key Features

### 🔐 Account Management

* View all customer accounts
* Check account balance
* Secure password modification

---

### 💰 Transaction Processing

* Deposit funds into accounts
* Withdraw funds with validation checks
* Transfer funds between accounts
* Automatic database updates for all transactions
* Prevention of invalid operations (e.g., insufficient balance)

---

### 📊 Analytics & Reporting

* Generate customer-wise transaction summaries
* Uses SQL joins and aggregation functions (`SUM`)
* Provides insights into transaction activity

---

### 🚨 Fraud Detection (Database-Level)

* Implemented using **PostgreSQL Trigger and PL/pgSQL**
* Automatically categorizes transactions:

  * Amount > ₹100000 → **HIGH Risk**
  * Amount > ₹50000 → **MEDIUM Risk**
  * Otherwise → **LOW Risk**
* Trigger executes **before transaction insertion**

---

### 💳 Credit Card Simulation

* Apply for a credit card
* Perform transactions using credit
* Repay outstanding credit
* Track used credit and limits

---

## 🛠️ Technology Stack

* **Programming Language:** Java
* **Database:** PostgreSQL
* **Connectivity:** JDBC
* **Concepts Applied:**

  * Object-Oriented Programming (OOP)
  * Exception Handling
  * SQL Joins and Aggregations
  * Database Triggers (PL/pgSQL)

---

## ⚙️ Database Setup

👉 All database setup and management was done using `pgAdmin4`, ensuring a structured and efficient workflow.

1. Create the database:

```sql
create database Banking_system;
```

2. Execute the provided `.sql` script to:

* Create required tables (`customer`, `account`, `transaction_table`)
* Insert sample data
* Configure fraud detection trigger

---

## 🔗 Database Configuration

Update credentials in `DBConnection.java`:

```java
"your_username_here",   // PostgreSQL username (e.g., postgres)
"your_password_here"    // PostgreSQL password
```

---

## 🚀 Execution Steps

1. Open the project in IntelliJ IDEA
2. Add PostgreSQL JDBC Driver (`postgresql-42.x.x.jar`) to classpath
3. Run `Main.java`

---

## 📖 Application Workflow

1. Launch the application

2. Navigate through available modules:

   * Account Operations
   * Transaction Operations
   * Analytics & Reports
   * Card & Security

3. Perform operations such as:

   * Deposits and withdrawals
   * Fund transfers
   * Viewing transaction history
   * Generating reports
   * Managing credit card usage

---

## 🌟 Highlights of the Project

* Integration of **Java with PostgreSQL using JDBC**
* Implementation of **fraud detection at database level**
* Real-time transaction handling with validation
* Modular and structured code design
* Simulation of real-world banking scenarios

---

## 🏆 Learning Outcomes

* Practical experience with **JDBC and database connectivity**
* Understanding of **transaction management systems**
* Implementation of **database triggers for business logic**
* Writing maintainable and modular Java code

---

## 📑 Submission
- Prepared by: Kamani Shivani
- Virtusa Pre-Onboarding Training Assignment
- Developed as a `Java use case project`, demonstrating JDBC integration, PostgreSQL database management, and database-level fraud detection using triggers
---
