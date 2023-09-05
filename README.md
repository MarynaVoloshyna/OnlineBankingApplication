# Online Banking Application

Welcome to the Online Banking Application, a simple yet powerful web application that enables users to manage their bank accounts and transactions securely. This application is built using Java technologies and follows a role-based access control system for different types of users.

## Features

- **Role-Based Access Control**: The application distinguishes between two main roles: Managers and Clients. Managers have administrative privileges, while Clients can manage their bank accounts.

- **User Registration**: New users can register their accounts by providing their email and password. Passwords are securely hashed before storage.

- **Bank Account Management**: Clients can create, view, update, and delete their bank accounts. Each client is allowed to have one bank account per currency (UAH, USD, EUR).

- **Transaction History**: Users can view their transaction history, filter transactions by date, and download transaction receipts.

- **Manager Actions**: Managers can oversee and manage client accounts. They can assign or reassign clients to different managers.

## Technologies Used

- **Spring Boot**: A powerful framework for building Java-based web applications.

- **Spring Security**: Provides authentication and authorization features to secure the application.

- **Hibernate**: An ORM framework for seamless database operations.

- **MySQL Database**: Stores user data, account details, and transaction records.

## Getting Started

1. Clone the repository:

   ```sh
   git clone https://github.com/MarynaVoloshyna/OnlineBankingApplication.git
   cd OnlineBankingApplication


Admin dashboard view
<img width="1439" alt="Снимок экрана 2023-09-05 в 14 56 08" src="https://github.com/MarynaVoloshyna/OnlineBankingApplication/assets/115135949/a76f74f4-c94d-474f-95e9-024de70ffda3">

Client dashboard view
<img width="1435" alt="Снимок экрана 2023-09-05 в 17 25 14" src="https://github.com/MarynaVoloshyna/OnlineBankingApplication/assets/115135949/66119e07-7611-44cd-9777-a3de9962894e">
