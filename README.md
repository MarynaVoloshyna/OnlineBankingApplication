# Online Banking Application

Welcome to the Online Banking Application, a simple yet powerful web application that enables users to manage their bank accounts and transactions securely. This application is built using Java technologies and incorporates Thymeleaf as the templating engine for the user interface. It follows a role-based access control system for different types of users and features a responsive design to ensure a seamless experience on various screen sizes and devices.


## Features

- **Role-Based Access Control**: The application distinguishes between three main roles: Admin, Managers and Clients.
  Admin has super power and can operate managers, create new clients and open bank accounts for them as well as check all info about bank accounts and transactions.
  Managers have administrative privileges, while Clients can manage their bank accounts and check their transactions with ability to get transaction receipt.

- **User Registration**: New users can register their accounts by providing their email and password. A user role is assigned during registration using email address verification. A domain must be defined to be registered as a manager. Passwords are securely hashed before storage.
                                                                    **Manager registration form**
  
  <img width="1369" alt="Снимок экрана 2023-09-05 в 19 17 34" src="https://github.com/MarynaVoloshyna/OnlineBankingApplication/assets/115135949/d0fc72d6-55bc-430f-9ee2-236c1cb3c284">
**Client registration**
https://github.com/MarynaVoloshyna/OnlineBankingApplication/assets/115135949/718a010e-afe7-42a8-b4b9-75035f5a5f34

- **Bank Account Management**: Clients can create, view and update, their bank accounts. Each client is allowed to have one bank account per currency (UAH, USD, EUR).
                                                                      **Client dashboard view**
 
<img width="1435" alt="Снимок экрана 2023-09-05 в 17 25 14" src="https://github.com/MarynaVoloshyna/OnlineBankingApplication/assets/115135949/66119e07-7611-44cd-9777-a3de9962894e">


- **Transaction History**: Users can view their transaction history, filter transactions by date, and download transaction receipts.

- **Manager Actions**: Managers can oversee and manage client accounts. They can assign or reassign clients to different managers.
- **Admin Actions**: Admin has super power and can operate managers, edit their info, change manager's clients list and manager level. Also create new clients and open bank accounts for them. Of course admin can check all info about all bank accounts and transactions.
                                                                      **Admin dashboard view**
  <img width="1439" alt="Снимок экрана 2023-09-05 в 14 56 08" src="https://github.com/MarynaVoloshyna/OnlineBankingApplication/assets/115135949/a76f74f4-c94d-474f-95e9-024de70ffda3">

  ![Sept-11-2023 11-08-17](https://github.com/MarynaVoloshyna/OnlineBankingApplication/assets/115135949/6ae7bad2-ea1e-42dc-8260-7f811c1f8133)
**Change manager's data (except of ID number)**
![changeManagerData](https://github.com/MarynaVoloshyna/OnlineBankingApplication/assets/115135949/b2c29c0e-a618-4bad-b131-89031f3131f1)

## Technologies Used

- **Spring Boot**: A powerful framework for building Java-based web applications.

- **Spring Security**: Provides authentication and authorization features to secure the application.

- **Hibernate**: An ORM framework for seamless database operations.

- **MySQL Database**: Stores user data, account details, and transaction records.
  
- **Thymeleaf**: A server-side Java templating engine for dynamic web content generation.
  
- **Bootstrap**: A responsive front-end framework for designing web pages with HTML, CSS, and JavaScript.


## Getting Started

1. Clone the repository:

   ```sh
   git clone https://github.com/MarynaVoloshyna/OnlineBankingApplication.git
   cd OnlineBankingApplication

