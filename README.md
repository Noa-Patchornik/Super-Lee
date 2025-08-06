# Super-Lee

## 📚 Project Overview

This project was developed as part of a university course in software system design.  
It simulates a Human Resources Management System for a fictional supermarket chain, **Super-Lee**, supporting employee scheduling, shift planning, payroll, contract management, and more.

The system includes **two user roles**:
- HR Manager – with full access to employee and shift data
- Regular Employee – with limited functionality (availability, contract view, payroll info)

The system is built using **Java (Maven)** with support for modular structure, SQLite persistence, and command-line interface (CLI) interaction.

---

## 🧩 Features

### For HR Managers:
- Full CRUD over employees
- Contract and bank info management
- Role assignments and removal
- Weekly shift planning and scheduling
- Payroll calculation and report generation
- Employee data loading from and saving to persistent storage
- Data querying by ID, role, branch, or history
- Export reports to files (e.g. payroll reports)

### For Regular Employees:
- Fill shift availability (constraints) by date
- View salary based on hours worked and contract
- Display personal and contract information
- View submitted availability

---

## 📁 Project Structure
```
.
dev/
├──src/
│    ├── main/
│    │   ├── java/
│    │   │   ├── Data/           # Data Access Layer (DAL) – handles database interaction (SQLite)
│    │   │   ├── Domain/         # Business Logic – entities, models, and core system rules, access to DAL layer
│    │   │   ├── Service/        # Service Layer – coordinates between domain and presentation layers, handle exceptions
│    │   │   └── Presentation/   # Presentation Layer – CLI-based UI and user input handling
│    │   └── resources/
│    │       └── MYDB/             # Connection to the DB of the project using SQLite.
│    │
│    ├── test/
│    │   └──   java/       # Unit tests    
├── pom.xml                # Maven project configuration and dependencies
└── README.md              # Project documentation (this file)
```

## 🛠️ Technologies Used

- Java 17
- SQLite
- CLI-based architecture
- Maven for dependency management
- Exception handling and input validation
- Modular and layered system design

---

