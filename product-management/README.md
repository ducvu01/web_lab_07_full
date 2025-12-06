# Product Management System

## Student Information

- **Name:** Vu Dinh Duc
- **Student ID:** ITCSIU23004
- **Class:**

## Technologies Used

- Spring Boot 3.3.5
- Spring Data JPA
- MySQL 8.0
- Thymeleaf
- Maven

## Setup Instructions

1. Import project into VS Code
2. Create database: `product_management`
3. Update `application.properties` with your MySQL credentials
4. Run: `mvn spring-boot:run`
5. Open browser: http://localhost:8080/products

## Completed Features

- [x] CRUD operations
- [x] Search functionality
- [x] Advanced search with filters
- [x] Validation
- [x] Sorting
- [ ] Pagination
- [ ] REST API (Bonus)

## Project Structure

product-management/
│
├── src/
│ ├── main/
│ │ ├── java/
│ │ │ └── com/example/productmanagement/
│ │ │ ├── ProductManagementApplication.java (Main)
│ │ │ ├── entity/
│ │ │ │ └── Product.java
│ │ │ ├── repository/
│ │ │ │ └── ProductRepository.java
│ │ │ ├── service/
│ │ │ │ ├── ProductService.java
│ │ │ │ └── ProductServiceImpl.java
│ │ │ └── controller/
│ │ │ └── ProductController.java
│ │ │
│ │ └── resources/
│ │ ├── application.properties
│ │ ├── static/
│ │ │ └── css/
│ │ └── templates/
│ │ ├── product-list.html
│ │ └── product-form.html
│ │
│ └── test/
│ └── java/
│
├── pom.xml (Maven dependencies)
└── README.md


## Database Schema
See `schema.sql` for database structure.


## Time Spent
Approximately 4 hours



## Screenshots
See in the report.