# Warehouse Demo Application

## Introduction
This is a simple warehouse management application to demonstrate a few usage of REST-calls on stocks in a warehouse. The available feautures exposed are: 
- Move stock from one warehouse to another;
- Get a list of all stocks in a warehouse;
- Subscribe to daily mail of all empty space in a warehouse.

The application is made with Spring Boot with a embed Tomcat server and H2 memory as database of the warehouses. The package structure is defined as followed:
```
└───tvh
    └───WarehouseDemo
        │   WarehouseDemoApplication.java
        ├───adapters
        │   └───rest
        │           WarehouseRestController.java
        ├───application_logic
        │       ScheduledTasks.java
        │       WarehouseService.java
        ├───domain
        │       EmailUtil.java
        │       MoveStockRequest.java
        │       Warehouse.java
        └───persistence
                WarehouseRepository.java
```
- *WarehouseDemoApplication.java* is the Main class and initializes a few dummy data.
- *WarehouseRestController.java* defines the exposed features via REST.
- *ScheduledTasks.java* is responsible for sending daily warehouse volume report to its subscribers.
- *WarehouseService.java* is the DAO who carries out the REST requests.
- *EmailUtil.java* is a help class for creating and sending emails.
- *MoveStockRequest.java* defines the received request to move stock between two warehouses.
- *Warehouse.java* is the entity class of Warehouse.
- *WarehouseRepository.java* defines the queries for CRUD operations on Warehouse. 

## Getting Started
### Requirements
- Java SDK 11+.
- A web browser.

### Build and Test API
Use the Maven wrapper included by Spring Initializr, *mvnw*.
1. To run the application with:
    ```
    mvnw compile
    ```
2. To build the application with:
    ```
    mvnw package
    ```
    Then to execute the JAR file with:
    ```
    java -jar target/WarehouseDemo-0.0.1-SNAPSHOT.jar
    ```
3. In order to test the REST API, it is recommended to use the [Swagger UI](localhost:8081/swagger-ui.html). The used server port is 8081, if it is blocked, then run the JAR with the following line:
    ```
    java -jar -Dserver.port=XXXX target/WarehouseDemo-0.0.1-SNAPSHOT.jar
    ```
    and replace *XXXX* with a currently not used port. 
