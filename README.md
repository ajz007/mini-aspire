```
NOTE: Hey folks anyone who has come across this profile for looking at samples for mini-aspire test by
Aspire ( https://aspireapp.com) then please do not use this. This project was rejected by them without
giving any reasons. I have requested them for reasons but havent received any response yet.
Probably, they dont understand Java :) 
```
# mini-aspire
mini-aspire is a basic loan application backend where users can largely do the following:

1. Register
2. Open Loan Account
3. Inquire Repayments
4. Repay the individual emi/repayments

The apis are exposed via open api.

# Table of contents
1. [Configs](#configs)
2. [Build and Run](#build-and-run)
   1. [Windows](#windows)
   2. [Linux](#linux)
   3. [URLs](#urls)
3. [Architecture](#architecture)
4. [Basic Functional Flow](#basic-functional-flow)
5. [Design choices](#design-choices)
   1. [Monolith vs Microservices](#monolith-vs-microservices)
   2. [RDBMS vs NoSQL](#rdbms-vs-nosql)
   3. [Microservices Design Patterns implemented](#microservices-design-patterns-implemented)


# Configs
1. Database
    1. The database used here is H2 which is an in-memory database but also supports saving the file to the local disk
    2. To update the config, go to :
         1.  **<module>/src/main/resources/application.properties**
         2. Default database setup is  **in-memory** 
            ``` 
               spring.datasource.url=jdbc:h2:mem:loan 
            ```
       Here, ***loan*** is the database name for Loan microservice
    4. For it to retain the data even after server restart we can set the url to  **local disk** 
            ``` 
               spring.datasource.url=jdbc:h2:D:/data/loan 
            ```
      Above configuration is for ***Loan DB*** and data would be saved at ***D:/data/loan***
       
   **Please give different path for each microservice database**

# Build and Run

###  Windows
   1. To build the project
      1. ```
         cd mini-aspire
         ```
      2. ```
         mvnw clean install    
         ```
        
   2. To run the project:
      1. ``` 
         start_all_service.bat
         ```

###  Linux
1. To build the project
    1. ```
       cd mini-aspire
       ```
    2. ```
       mvnw clean install
       ```

2. To run the project:
    1. ```
       ./start_all_service.sh
       ```

###  URLs
1. To check the status of all services on the **DISCOVERY SERVER** (*Please ignore using the urls on this page)
   http://localhost:8079
2. To check the openapi end for all services
   http://localhost:8080/swagger-ui.html
3. DB for **USER** microservice
   http://localhost:8083/h2-console/  
   3.1 When prompted for **jdbcurl**, please give the same value as given in **user/src/main/resources/application.properties**
   ```
      jdbc:h2:mem:user
   ```
4. DB for **LOAN**
   http://localhost:8081/h2-console/  
   4.1 When prompted for **jdbcUrl**, please give the same value as given in **loan/src/main/resources/application.properties**
   ```
      jdbc:h2:mem:loan
   ```
5. DB for **PAYMENT**
   http://localhost:8085/h2-console/  
   5.1 When prompted for **jdbcUrl**, please give the same value as given in **payment/src/main/resources/application.properties**
   ```
      jdbc:h2:mem:payment
   ```

# Architecture

![Screenshot](microservice_arch.png)

   Overall the application consists of 6 microservices:
   1. ### API-GATEWAY : 
      As the name suggests its the gateway to the application and all the requests would come to this service. 
      It would be responsibile for the following in this application:
      1. Request routing : Forwards the request to the appropriate microservice
      2. Validate JWT Token
      3. Load balancing. Co-ordinates with discovery server for this
      3. Aggregation of the open-apis
   2. ### DISCOVERY SERVER
      1. This acts as a service registry and is a repository of the host and port for all the microservices. Each service would register to this.
      2. Its will store the list of the service replica's ip and port against the service name.
   3. ### AUTH
      1. Auth service is responsible for login and token generation. 
      2. It co-ordinates with user service for this.
   4. ### USER
      1. This service stores the user information.
      2. **It maintains its own database** for this. It has following tables in that :
         1. USER
   5. ### LOAN
      1. This service stores the information about the loans and their related payments
      2. **It maintains its own database** for this. It has following tables in that :
         1. LOAN
         2. REPAYMENTS
         3. LOAN_REPAYMENTS
   6. ### PAYMENTS
      1. This servoce stores the information related to payments.
      2. **It maintains its own database** for this. It has following tables in that :
         1. PAYMENT
      3. This micro-service acts as the orchestrator between LOAN and PAYMENTS to complete a Payment transaction.
   
# Basic Functional Flow

1. **Register user** 
   1. Go to http://localhost:8080/swagger-ui.html
   2. Select User Service from **'Select a definition'** drop down (Look at the top right corner)
   3. Go to **'POST' /user/register** to register user
2. **Create Loan**
   1. Go to http://localhost:8080/swagger-ui.html
   2. Select Loan Service from **'Select a definition'** drop down (Look at the top right corner)
   3. Go to **'POST' /loan** to create loan
3. **Create Payment (repay next scheduled repayment) **
   1. Go to http://localhost:8080/swagger-ui.html
   2. Select Payment Service from **'Select a definition'** drop down (Look at the top right corner)
   3. Go to **'POST' /payment** to pay for next scheduled repayment.

# Design choices

### Monolith vs Microservices

### Advantages of Monoliths
1. Easy build
2. Easy to deploy
3. Easy to maintain user sessions

### Challenges of Monoliths
1. Difficult to maintain as they grow in size
2. One service and choke the entire system
3. Ends up creating dependency hell
4. Difficult to scale beyond a limit

### Advantages of Microservices
1. Flexibility to choose any technology for each microservice 
2. Quick and easy to add new features
3. Flexibility to scale up and down each

### Challenges of Microservices
1. Too many services means a lot of effort goes into facilitating coordination
2. Deployments become complex
3. Basic transactions might need complex design patterns to achieve

### Making the choice
Our problem statement does not have volumetrics associated but, largely we can say the following:
   1. The Volume of users and loans would increase in same proportion. 
   2. The volume of payments would grow multi-fold compared to user/loans.
   3. As more users add to the application there are possibilities that the volume of transactions might be very high 
      on certain days like start of month or maybe end of month where our application might see sudden burst of transactions.


Going by the above hypothesis, I believe that the nature of the application in terms of resource requirements are pretty dynamic
and taking a monolithic approach for this application would either result in too few resources or wasteful allocation of resources.
Going by the microservices approach we would be able to,
1. Allocate different resources for each application module(microservice) ie; low resources for something like user registration and pretty high for repayments.
2. We would also be able to dynamically scale up/down the resource for payments during the end of month or start of month
3. Separate allocation of resources to different resources would ensure that user does not get stuck during login while the load for payments is too high.
   But, with monolith this is very much a possibility and might end up in bad user experience and onboarding which can end up in loosing customers.
4.** So, for above reasons, I would go ahead with microservices**

# RDBMS vs NoSQL

Largely, we can say that the application would have following entities:
1. User
2. Loan
3. Repayments
4. Payments

If we see closely we can conclude fairly that there is a good amount of relationship among the entities and given that there is financial transactions involved we care a lot more
about the atomicity and data integrity hence,** I would go ahead with the RDBMS as the database here.**
Please note that I am using H2 in-memory database for development which is RDBMS based DB but, we can replace this with any other DB like Oracle, MySql etc.
The choice of H2 is purely for ease of development and deployment for anybody who wants to validate this project.

# Microservices Design Patterns implemented

1. **Bounded Context**
   The first problem that we face while developing the microservices is, how to define the boundaries of a certain micro-service.
   For this particular problem we can go ahead with business sub-domain which would result in following microservices:
   1. User -- Handling user services like, registration and validation of users
   2. Loan -- Handling loan creation, checking repayments etc
   3. Payments -- Handling the payments of the due repayments
   4. Auth -- For authentication
2. **Service Discovery**
   1. In order for the microservices to be able to independently scale up and scale down, the microservices must be:
      1. **Stateless** -- To implement this, we are not maintaining any sessions on each micro-service. 
         Without the presence of session, we must have a way to identify a user and if he is logged in. For this, we are going ahead with JWT tokens.
         JWT(Json Web Token) would be generated during login and would be expected in each request. The gateway service would validate this and allow or reject the request.
      2. In order to de-couple the services from hardcoding the addresses we can use a **registry/naming service**. Each microservice would register to this registry and registry
         would keep the record of addresses for each service. If a microservice, needs to communicate with another microservice then it can get the address from registry and call the service.
         This is implemented in our discovery-service
3. **Api-Gateway**
   1. In order to avoid client to have to find out individual microservices and communicate with each of them, we use a gateway layer which becomes a central point of contact for any client.
   2. The gateway would get the address of the specific microservice from registry and forward the request to that service.
   3. The gateway also does load balancing, rate limiting, authentication, authorization etc
4. **Distributed transactions**
   1. For our usecase where we are doing a repayment, the transaction involves two services
      1. Loans
      2. Payment
   Therefore, we need to ensure that the transactions are completed completely/rolledback.
   There are largely two ways to handle the distributed transactions in microservices:
         1. **Service Choreography**
            Choreography is an event driven approach for achieving eventual consistency using a message broker. This is a really nice and clean way to achieve distributed transactions. 
            Also, since the process is async due to message broken, the overall performance is faster.
            The problem with this approach comes when there are too many events for identifying one unit of work. It becomes increasingly difficult to put all the pieces together and
            becomes difficult to debug the issue.
         2. **Service Orchestration**
            In this pattern, we have one controller service which knows about all the involved transactions and microservices and ensures that the overall transaction status is not inconsistent.
           ** For, the current application, I would be taking this approach** where the Payment microservice acts as a controller. To start with I am only taking care of the sunny day transactions, due to time constraint :).
            I will try to introduce rollback in future versions of the application.
