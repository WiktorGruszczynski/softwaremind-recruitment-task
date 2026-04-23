# Product Management System

This project is a comprehensive web application for managing a product catalog, developed as a recruitment task. It features a robust Spring Boot backend, a reactive Angular frontend, and a scalable Cassandra database.

## 1. Tech Stack

The application utilizes a modern tech stack to ensure security, scalability, and maintainability:

* **Backend**: Spring Boot 3.5.13 with Java 21.
* **Database**: Apache Cassandra 5.0.8 for high-availability data storage.
* **Frontend**: Angular framework for a responsive user interface.
* **Security**: Spring Security with OAuth2 Resource Server (JWT) and CSRF protection.
* **Validation**: Jakarta Validation for enforcing business rules, such as preventing negative prices.
* **Infrastructure**: Docker and Docker Compose for containerization.
* **CI/CD**: GitHub Actions for automated building and testing.
* **Testing**: JUnit 5, MockMvc, and Testcontainers for integration and security verification.

## 2. Documentation

### API Endpoints
The following table summarizes the available REST API endpoints for product management:

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| **POST** | `/api/products` | Add a new product |
| **GET** | `/api/products` | Retrieve the full list of products |
| **GET** | `/api/products/{id}` | Retrieve details for a specific product |
| **PUT** | `/api/products/{id}` | Update an existing product |
| **DELETE** | `/api/products/{id}` | Remove a product from the system |
| **GET** | `/api/products/category/{category}` | Filter products by their category |

### Interactive API Documentation
The project includes **Swagger UI**, which provides an interactive interface to explore and test the API endpoints. Once the application is running, it is accessible at:
* **Swagger UI:** `/api/swagger-ui.html`

### Security and Roles
* **Authorized Access**: Every request to the API (excluding registration and login) must be authorized.
* **Role-Based Access Control (RBAC)**:
    * **USER**: Authorized to browse products and view details.
    * **ADMIN**: Authorized for full management operations, including adding, editing, and deleting products.

### Testing Strategy
The system's reliability is verified through several testing layers:
* **Unit Tests**: Verification of API responses and data validation (e.g., price cannot be negative).
* **Security Tests**: Ensuring correct endpoint permissions based on user roles.
* **Integration Tests**: Performed using an embedded database or Testcontainers.

##  3. How to Install

To build and run this application, you must have **Docker** and **Docker Compose** installed.

### Installation Steps
1.  **Clone the repository**:
    ```bash
    git clone <repository-url>
    cd softwaremind-recruitment-task
    ```

2.  **Environment Configuration**:
    The application requires administrative credentials for the initial setup. You can set these as environment variables in your `.env` file:
    * `ADMIN_EMAIL`: The email for the default admin account.
    * `ADMIN_PASSWORD`: The password for the admin account.

3.  **Launch via Docker Compose**:
    Run the following command to build and start the database, backend, and frontend services simultaneously:
    ```bash
    docker compose up --build
    ```

4.  **Accessing the Services**:
    * **Frontend UI**: Open your browser at `http://localhost:4200`.
    * **Backend API**: Accessible at `http://localhost:8080/api`.
    * **Cassandra Database**: The database service is available on port `9042`.

---
*Author: Wiktor Gruszczyński*
*Project completed as part of