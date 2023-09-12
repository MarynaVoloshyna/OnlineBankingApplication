# Online Banking Application

Welcome to the "Friendly Bank"  - fullstack online banking application, a simple yet powerful web application that enables users to manage their bank accounts and transactions securely. This application is built using Java technologies and incorporates Thymeleaf as the templating engine for the user interface. It follows a role-based access control system for different types of users and features a responsive design to ensure a seamless experience on various screen sizes and devices.


## Features

- **Role-Based Access Control**: The application distinguishes between three main roles: Admin, Managers and Clients.
  Admin has super power and can operate managers, create new clients and open bank accounts for them as well as check all info about bank accounts and transactions.
  Managers have administrative privileges, while Clients can manage their bank accounts and check their transactions with ability to get transaction receipt.

## Client-role user functionality:
1. **Create an account**: A manager is assigned automatically upon client registration. The admin can manually change the manager for the client.
2. **Open accounts in three currencies**: Limitation - only 3 accounts for each client, one for each currency.
3. **Ones replenishment of accounts**: The limit is no more than 1,000 UAH and 100 USD/EUR for foreign currency accounts. This setting may be changed in the `application.properties` file.
4. **Transactions**: Transfer from any of your own to any bank card. Overdraft is prohibited, prevention of doubling the balance (attempt to withdraw and transfer money from the same card).
5. **Checking your information entered during registration**
6. **Manager contacts**

## Manager-role user functionality:
1. **Account registration with email domain verification**: Unable to register if the domain is not "@friendlybank.com."
2. **Check customers, their accounts, transactions**
3. **Download the transaction receipt**
4. **Create a client**
5. **Create a client account with a check for a limit of 3 accounts for each client**
6. **Each manager can have no more than 10 clients. This number can be changed in the `application.properties` file**
7. **Manager does not have access to admin tools**

## Admin-role user functionality:
1. **Basic statistics**: The left part of the dashboard displays basic statistics - how many users are in the application, how many bank accounts are open, and how much money is in all user accounts at the UAH rate.
2. **Admin tools**: The central part displays admin tools:
   - View all managers and their information, as well as view the list of clients of each manager. Ability to search by manager's name.
   - View all bank clients, their accounts, transactions, tax number, and who is the manager of this client. Ability to edit client data (except ID) and change the manager.
   - All bank accounts with a transition to the transaction page for each account.
   - The entire history of transactions with the ability to filter by account number, by date, or both by number and by date.
   - Search for a manager by email address.
   - Search for a manager by the client's tax number.
   - Search for a client's bank account manager.
   - Search for a manager based on his level of experience.
   - Change of manager data.
3. **Left part**: The left part is the same as the manager's - the ability to create a client account, bank account, and check self-information.


##
Below are screenshots of the application in action on the Heroku website, but I suggest you log in yourself and try using it. You can explore various features of the application, open accounts, conduct transactions, and more. You can also try out the roles of an admin and a manager.

[Link at Heroku](https://online-banking-application-1efd81143102.herokuapp.com)


 For access to the application on the Heroku service, please use the following credentials:
   - Admin:
     - Login: admin@friendlybank.com
     - Password: admin
   - Manager:
     - Login: max@friendlybank.com
     - Password: max
   - Client:
     - We recommend registering yourself to understand how easy and user-friendly the application is.
     - IMPORTANT: It is not necessary to use a real email address, but it should pass the domain check (specify domains). Please also          make up your tax identification number, which should consist of 8 digits.


## Client view

**Client registration process**
- Mobile screen
![1](https://github.com/MarynaVoloshyna/OnlineBankingApplication/assets/115135949/c34f7026-301b-4bdd-a9d8-8b0aca9909fd)
![2](https://github.com/MarynaVoloshyna/OnlineBankingApplication/assets/115135949/93d1ecde-3083-4656-a9a2-8d96060756ea)
![3](https://github.com/MarynaVoloshyna/OnlineBankingApplication/assets/115135949/4b1523b4-4d15-4ee1-913b-003718fdea72)

- Desctop full screen
  
  <img width="1435" alt="Снимок экрана 2023-09-05 в 17 25 14" src="https://github.com/MarynaVoloshyna/OnlineBankingApplication/assets/115135949/66119e07-7611-44cd-9777-a3de9962894e">


* **Replenishment process**


![3031B9FB-9E52-4DA6-BFA4-3207C02861CD](https://github.com/MarynaVoloshyna/OnlineBankingApplication/assets/115135949/701092c5-930a-4976-a314-fac3322c69f3)


* **Transfer money process**

![4](https://github.com/MarynaVoloshyna/OnlineBankingApplication/assets/115135949/ca093f6b-4a98-4b47-9cd0-f3d07a50dfbf)



* **Transaction history by one bank account**

<img width="296" alt="clientCardHistory" src="https://github.com/MarynaVoloshyna/OnlineBankingApplication/assets/115135949/60feaf8f-8750-4d37-a179-8036842c9dce">



* **Check client's info**

![35108D2C-13FC-4BC0-81D9-9AC2FF739346](https://github.com/MarynaVoloshyna/OnlineBankingApplication/assets/115135949/8a0d3679-d519-4e8b-9f8e-2c2f36158efa)



* **Storing passwords in the DB**
<img width="1083" alt="passwordStore" src="https://github.com/MarynaVoloshyna/OnlineBankingApplication/assets/115135949/b1feeb80-0495-4fde-932c-66b649ff723a">

## Manager view
* **Manager's email verification**

<img width="1369" alt="emailcheckingManagerRegistration" src="https://github.com/MarynaVoloshyna/OnlineBankingApplication/assets/115135949/d5db44b2-a93f-4afe-babd-9873355c21f4">

* **Manager dashboard**

<img width="1432" alt="managerDashboard" src="https://github.com/MarynaVoloshyna/OnlineBankingApplication/assets/115135949/1b6cc872-789b-4696-8dcb-5583e71b3c73">

* **Open account restriction**

<img width="1432" alt="managerClientAccountRestriction" src="https://github.com/MarynaVoloshyna/OnlineBankingApplication/assets/115135949/85bbf2c2-467b-48b8-b336-a911a6d071a0">

* **Access denied page**

<img width="1435" alt="accessDenied" src="https://github.com/MarynaVoloshyna/OnlineBankingApplication/assets/115135949/d729d95c-80e7-404c-ba89-937e61ed0272">

## Admin view

* **Admin dashboard**

<img width="1432" alt="adminDashboard" src="https://github.com/MarynaVoloshyna/OnlineBankingApplication/assets/115135949/3e864308-0b25-46c2-b491-561cd2c94651">


* **Checking manager and his clients information**
  ![Sept-11-2023 11-08-17](https://github.com/MarynaVoloshyna/OnlineBankingApplication/assets/115135949/6ae7bad2-ea1e-42dc-8260-7f811c1f8133)

  

* **Change manager's data (except of ID number)**

![changeManagerData](https://github.com/MarynaVoloshyna/OnlineBankingApplication/assets/115135949/b2c29c0e-a618-4bad-b131-89031f3131f1)



* **Filtering transactions**

![adminFilteringTransactions](https://github.com/MarynaVoloshyna/OnlineBankingApplication/assets/115135949/fa9e3a6d-e56f-40a5-8a66-5d6d79e54393)



* **Change manager for client**

<img width="1435" alt="ADMINmanagerChangintForClient" src="https://github.com/MarynaVoloshyna/OnlineBankingApplication/assets/115135949/852fd652-969e-438a-b264-0cd45d0533fa">

  


                                                                      
 




  






## Technologies Used

- **Spring Boot**: A powerful framework for building Java-based web applications.

- **Spring Security**: Provides authentication and authorization features to secure the application.

- **Hibernate**: An ORM framework for seamless database operations.

- **MySQL Database**: Stores user data, account details, and transaction records.

- **Maven**: A build tool for managing project dependencies and building Java-based applications.
  
- **Thymeleaf**: A server-side Java templating engine for dynamic web content generation.
  
- **Bootstrap**: A responsive front-end framework for designing web pages with HTML, CSS, and JavaScript.




