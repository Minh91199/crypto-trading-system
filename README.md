# **Crypto Trading System**

Welcome to the **Crypto Trading System**, an application built for aggregating cryptocurrency prices, facilitating trades, and integrating with major exchanges like Binance and Huobi. Designed with maintainability and modularity in mind, this system provides performance, scalability, and adaptability to new market requirements.

---

## **Table of Contents**
1. [Overview](#overview)
2. [Key Features](#key-features)
3. [Tech Stack](#tech-stack)
4. [Project Structure](#project-structure)
5. [Setup and Installation](#setup-and-installation)
6. [API Endpoints](#api-endpoints)
7. [Contact](#contact)
---

## **overview**
The Crypto Trading System is a Java-based project that fetches and combines crypto prices from multiple sources, stores essential trading data, and allows for user transactions. It leverages Spring Boot for rapid development, H2 as an in-memory database for quick testing, and MapStruct to minimize boilerplate code during DTO conversions.

---

## **key-features**
- **Real-Time Data Aggregation**: Consolidate and store crypto price data from multiple external APIs.
- **Scheduled Fetching**: Automatically sync data at configurable intervals using Spring’s scheduling features.
- **Trade Execution**: Support BUY/SELL orders based on the latest aggregated prices.
- **User Wallet Management**: Track and display user wallet balances for supported currencies.
- **Extensible Architecture**: Easily integrate additional exchanges by implementing the PriceProvider interface.
---

## **tech-stack**
- **Java 17** – Modern LTS version for clean, performant, and maintainable code.
- **Spring Boot** – Provides auto-configuration and REST API development frameworks.
- **Spring Data JPA (Hibernate)** – Simplifies data persistence with relational databases.
- **OpenFeign** – Streamlines HTTP client code for external API calls.
- **MapStruct** – Eliminates most boilerplate in DTO/Entity mappings.
- **Lombok** – Reduces getter/setter/constructor overhead.
- **H2** – In-memory database for convenient local development and testing.
- **Scheduler** – Spring Scheduler for periodic price fetching tasks.

---

## **Project Structure**
```plaintext
com.minhle.cryptotrading.crypto_trading_system
├── connector
│   ├── client           # Feign interfaces for API integration
│   ├── dto              # Data Transfer Objects (DTOs) for outgoing requests
│   └── response         # Models representing external API response structures
├── constant             # Holds constants (e.g., currency strings, configuration keys)
├── controller           # REST controllers (handles HTTP requests)
├── entity               # JPA entities (Wallet, User, Trade, AggregatedPrice, etc.)
├── enums                # Enum definitions (TradeType, etc.)
├── exception            # Custom exceptions and global error handling classes
├── mapper               # MapStruct mapper interfaces
├── model
│   ├── request          # API request DTOs
│   └── response         # API response DTOs
├── provider             # PriceProvider implementations for various exchanges
├── repository           # Repository interfaces for data persistence
├── scheduler            # Scheduled tasks for price fetching
└── service              # Business logic, trade execution, wallet management   
```

---

## **Setup and Installation**

### **Prerequisites**
- **Java 17** installed.
- **Maven** for building the project

---

### **Steps**
1. Clone the repository:
   ```bash
   git clone https://github.com/Minh91199/crypto-trading-system.git
   cd crypto-trading-system
   ```
2. Configure the application:
    - Update the database credentials in `application.properties`
      ```bash
        spring.datasource.username=sa
        spring.datasource.password=
      ```
    - Update H2 database console endpoint:
      ```bash
        spring.h2.console.path=/h2-console
      ```
3. Build and run the project:
      ```bash
      mvn clean install 
      java -jar target/crypto-trading-system-0.0.1-SNAPSHOT.jar
      ```
4. Access the database:
    - http://localhost:8080/h2-console

5. Send API request:
    - Host: http://localhost:8080
    - Endpoint: refer to [API Endpoints](#api-endpoints)

----

### **API Endpoints**
| HTTP Method |                    Endpoint                     |                    Description                    |
|:-----------:|:-----------------------------------------------:|:-------------------------------------------------:|
|     GET     |              `/api/prices/latest`               |           Get latest aggregated prices            |
|    POST     |                  `/api/trades`                  |            Submit a new trade request             |
|     GET     |              `/api/trades/history`              |             Get historical trade data             |
|     GET     |             `/api/wallets/balance`              |              Get all wallet balances              |

---

### *Contact*
- Email: lehuynhnhatminh999@gmail.com