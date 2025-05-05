# E-Commerce Mobile Cover Website

A full-stack e-commerce application for selling mobile phone covers, built with Spring Boot and Angular.

## Features

- **User Authentication**
  - JWT-based login/signup
  - Google Sign-In with OAuth 2.0
  - Role-based access control (User, Admin)

- **Product Management**
  - Browse products by category
  - Search and filter products
  - Product details with images and descriptions
  - Admin panel for managing products

- **Shopping Experience**
  - Shopping cart functionality
  - Wishlist for saving favorite items
  - Product ratings and reviews
  - Responsive design for all devices

- **Order Processing**
  - Secure checkout process
  - Multiple payment options (UPI, Credit/Debit Card)
  - Order tracking and history
  - Email notifications for orders

- **Security**
  - CSRF protection
  - CORS handling
  - Secure password storage with BCrypt
  - HTTPS support

## Technology Stack

### Backend
- Java Spring Boot
- Spring Security with JWT
- Spring Data JPA
- PostgreSQL Database
- RESTful API Design
- Swagger/OpenAPI Documentation

### Frontend
- Angular 16
- Angular Material UI Components
- RxJS for state management
- Responsive design with SCSS

### Payment Integration
- Razorpay/Stripe sandbox integration

## Getting Started

For detailed setup instructions, environment configuration, and troubleshooting, please refer to the [Local Development Setup Guide](SETUP.md).

### Prerequisites
- Java 17 or higher
- Node.js 16 or higher
- npm 8 or higher
- PostgreSQL 13 or higher

### Quick Start

#### Backend Setup
1. Clone the repository
2. Configure the database in `src/main/resources/application.yml`
3. Run the Spring Boot application:
   ```
   mvn spring-boot:run
   ```

#### Frontend Setup
1. Navigate to the frontend directory:
   ```
   cd frontend
   ```
2. Install dependencies:
   ```
   npm install
   ```
3. Start the development server:
   ```
   npm start
   ```
4. Open your browser and navigate to `http://localhost:4200`

## Development

### Running in Development Mode
- Backend will run on `http://localhost:8080`
- Frontend will run on `http://localhost:4200`

### Building for Production
1. Build the frontend:
   ```
   cd frontend
   npm run build
   ```
2. Build the backend:
   ```
   mvn clean package
   ```
3. The complete application will be packaged as a JAR file in the `target` directory

## API Documentation
- API documentation is available at `http://localhost:8080/api/swagger-ui.html` when the application is running

## License
This project is licensed under the MIT License - see the LICENSE file for details.
