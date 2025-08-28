# Mutual Fund Transaction Management System

A comprehensive Spring Boot application for managing mutual fund transactions, client information, and fund schemes for Asset Management Companies (AMCs).

## 🚀 Features

### Core Functionality
- **AMC Management**: Create, update, and manage Asset Management Companies
- **Client Management**: Complete client lifecycle management with validation
- **Scheme Management**: Manage mutual fund schemes with ISIN codes
- **Transaction Processing**: Handle buy/sell transactions with NAV calculations
- **Excel Import**: Bulk upload transaction data from Excel files
- **Reporting**: Comprehensive reporting and analytics

### Technical Features
- **RESTful APIs**: Complete CRUD operations for all entities
- **Data Validation**: Comprehensive input validation with custom error messages
- **Exception Handling**: Global exception handling with proper error responses
- **Database Integration**: MySQL with JPA/Hibernate
- **File Upload**: Excel file processing with Apache POI
- **API Documentation**: Swagger/OpenAPI integration
- **Production Ready**: Optimized for production deployment

## 🛠️ Technology Stack

- **Backend**: Spring Boot 3.4.4, Java 17
- **Database**: MySQL 8.0
- **ORM**: JPA/Hibernate
- **Build Tool**: Maven
- **Documentation**: Swagger/OpenAPI
- **Validation**: Bean Validation (Jakarta)
- **Excel Processing**: Apache POI

## 📋 Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

## 🚀 Quick Start

### 1. Database Setup

```sql
CREATE DATABASE finance;
USE finance;
```

### 2. Configuration

Update `src/main/resources/application.properties` with your database credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/finance?useSSL=false&serverTimezone=UTC
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Build and Run

```bash
# Clone the repository
git clone <repository-url>
cd finance-backend

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080/api`

## 📚 API Documentation

Once the application is running, you can access:
- **Swagger UI**: `http://localhost:8080/api/swagger-ui.html`
- **API Docs**: `http://localhost:8080/api/api-docs`

## 🔧 API Endpoints

### AMC Management
- `POST /api/amcs` - Create new AMC
- `GET /api/amcs` - Get all AMCs
- `GET /api/amcs/{id}` - Get AMC by ID
- `PUT /api/amcs/{id}` - Update AMC
- `DELETE /api/amcs/{id}` - Delete AMC

### Client Management
- `POST /api/clients` - Create new client
- `GET /api/clients` - Get all clients
- `GET /api/clients/{id}` - Get client by ID
- `PUT /api/clients/{id}` - Update client
- `DELETE /api/clients/{id}` - Deactivate client
- `GET /api/clients/active` - Get active clients
- `GET /api/clients/search?query={query}` - Search clients

### Scheme Management
- `POST /api/schemes` - Create new scheme
- `GET /api/schemes` - Get all schemes
- `GET /api/schemes/{id}` - Get scheme by ID
- `PUT /api/schemes/{id}` - Update scheme
- `DELETE /api/schemes/{id}` - Delete scheme

### Transaction Management
- `POST /api/transactions/upload` - Upload Excel file with transactions
- `GET /api/transactions` - Get all transactions
- `GET /api/transactions/{id}` - Get transaction by ID

### FIFO (First In, First Out) Management
- `POST /api/fifo/create-purchase-lot` - Create a new purchase lot
- `POST /api/fifo/calculate-redemption` - Calculate FIFO for redemption
- `POST /api/fifo/process-redemption` - Process redemption with FIFO
- `GET /api/fifo/lots/{pan}/{schemeId}` - Get current lots for PAN and scheme
- `GET /api/fifo/remaining-units/{pan}/{schemeId}` - Get total remaining units
- `GET /api/fifo/cost-basis/{pan}/{schemeId}` - Get total cost basis
- `GET /api/fifo/unrealized-gain-loss/{pan}/{schemeId}` - Calculate unrealized gains/losses

### Switch Transaction Management
- `POST /api/switch/switch-out` - Process switch-out transaction
- `POST /api/switch/switch-in` - Process switch-in transaction
- `POST /api/switch/complete-switch` - Process complete switch transaction
- `GET /api/switch/history/{pan}` - Get switch transaction history

### Reporting & Analytics
- `GET /api/reports/portfolio/{pan}` - Get portfolio summary for PAN
- `GET /api/reports/portfolios` - Get all portfolios
- `GET /api/reports/portfolio-value/{pan}` - Get portfolio values by scheme
- `GET /api/reports/transactions/{pan}` - Get transaction history for PAN
- `GET /api/reports/transactions/scheme/{schemeId}` - Get transaction history by scheme
- `GET /api/reports/transaction-summary/{pan}` - Get transaction summary by type
- `GET /api/reports/amc-investment-summary` - Get AMC-wise investment summary
- `GET /api/reports/top-schemes` - Get top schemes by investment
- `GET /api/reports/client-count-by-tax-status` - Get client count by tax status
- `GET /api/reports/top-clients` - Get top clients by investment
- `GET /api/reports/current-nav` - Get current NAV by scheme
- `GET /api/reports/nav-history/{schemeId}` - Get NAV history for scheme

## 📊 Data Models

### AMC Master
- AMC ID (Primary Key)
- AMC Code (Unique, 2-5 characters)
- AMC Name (2-100 characters)
- Created/Updated timestamps

### Client Master
- Client ID (Primary Key)
- Name, DOB, PAN (Unique)
- Tax Status (Individual, Proprietor, HUF, Company)
- Mobile (Unique, Indian format)
- Email (Unique)
- Address, Active status
- Created/Updated timestamps

### Scheme Master
- Scheme ID (Primary Key)
- AMC (Foreign Key)
- Scheme Code (3-20 characters)
- Scheme Name (3-150 characters)
- ISIN Code (Unique, 12 characters)
- Created/Updated timestamps

### Transaction Info
- Transaction ID (Primary Key)
- PAN, Tax Status, Transaction Type
- DOB, Transaction Date
- Units, Amount, NAV
- Scheme ID, Folio Number
- Created/Updated timestamps

### Transaction Lots (FIFO)
- Lot ID (Primary Key)
- PAN, Scheme ID, Folio Number
- Purchase Date, Purchase NAV
- Purchase Units, Purchase Amount
- Remaining Units, Active Status
- Created/Updated timestamps

## 📁 Project Structure

```
src/main/java/com/rtd/finance_backend/
├── controller/          # REST Controllers
├── service/            # Service Interfaces
│   └── serviceImpl/    # Service Implementations
├── repository/         # Data Access Layer
├── model/             # Entity Classes
├── dto/               # Data Transfer Objects
├── exception/         # Custom Exceptions
├── helperclasses/     # Utility Classes
└── FinanceBackendApplication.java
```

## 🔒 Security & Validation

### Input Validation
- Bean Validation annotations on all entities
- Custom validation for PAN, mobile, email formats
- Business rule validation (unique constraints)
- File upload validation (size, format)

### Error Handling
- Global exception handler
- Custom exception classes
- Structured error responses
- Proper HTTP status codes

## 📈 Reporting Features

The system supports various reports:
- Portfolio summaries by client
- Transaction history with date ranges
- AMC-wise investment summaries
- Client analytics by tax status
- NAV tracking and history
- **FIFO-based capital gains calculations**
- **Lot-wise portfolio tracking**
- **Unrealized gains/losses analysis**

## 🚀 Production Deployment

### Environment Variables
```bash
export SPRING_PROFILES_ACTIVE=prod
export DB_URL=jdbc:mysql://your-db-host:3306/finance
export DB_USERNAME=your_username
export DB_PASSWORD=your_password
```

### Docker Deployment
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/finance-backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
```

### Health Checks
- Application health: `GET /api/actuator/health`
- Database connectivity check
- File system checks

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## 📝 License

This project is licensed under the MIT License.

## 🆘 Support

For support and questions:
- Create an issue in the repository
- Contact the development team
- Check the API documentation

## 🔄 Version History

- **v1.0.0** - Initial production release
  - Complete CRUD operations
  - Excel import functionality
  - Comprehensive validation
  - Production-ready configuration
  - **FIFO (First In, First Out) implementation**
  - **Capital gains calculation**
  - **Lot-wise transaction tracking**
  - **Switch transaction management**
  - **Comprehensive reporting & analytics**
  - **Portfolio management**
  - **Advanced transaction processing**
