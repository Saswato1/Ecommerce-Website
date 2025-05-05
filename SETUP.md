# Local Development Setup Guide

This document provides detailed instructions for setting up and running the E-Commerce Mobile Cover application on your local machine.

## Table of Contents
- [Prerequisites](#prerequisites)
- [Backend Setup](#backend-setup)
  - [Database Setup](#database-setup)
  - [Environment Variables](#environment-variables)
  - [Building and Running](#building-and-running-backend)
- [Frontend Setup](#frontend-setup)
  - [Installing Dependencies](#installing-dependencies)
  - [Configuration](#frontend-configuration)
  - [Running the Application](#running-the-frontend)
- [Accessing the Application](#accessing-the-application)
- [API Documentation](#api-documentation)
- [Troubleshooting](#troubleshooting)

## Prerequisites

Ensure you have the following installed on your system:

- **Java Development Kit (JDK)** - Version 17 or higher
  - [Download JDK](https://www.oracle.com/java/technologies/downloads/)
  - Verify installation: `java -version`

- **Maven** - Version 3.6 or higher
  - [Download Maven](https://maven.apache.org/download.cgi)
  - Verify installation: `mvn -version`

- **PostgreSQL** - Version 13 or higher
  - [Download PostgreSQL](https://www.postgresql.org/download/)
  - Verify installation: `psql --version`

- **Node.js** - Version 16 or higher
  - [Download Node.js](https://nodejs.org/)
  - Verify installation: `node -v`

- **npm** - Version 8 or higher (comes with Node.js)
  - Verify installation: `npm -v`

- **Angular CLI** - Version 16 or higher
  - Install: `npm install -g @angular/cli`
  - Verify installation: `ng version`

## Backend Setup

### Database Setup

1. Start PostgreSQL service if not already running
2. Create a new database:
   ```sql
   CREATE DATABASE ecommerce_db;
   ```
3. The default credentials in the application are:
   - Username: postgres
   - Password: postgres

   If you need to use different credentials, update them in the application.yml file.

### Environment Variables

The application uses several environment variables for configuration. You can set these in your environment or create a `.env` file in the project root.

Required environment variables:

```
# OAuth2 Configuration (Required for Google Sign-In)
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret

# JWT Configuration (Optional - default value is provided)
JWT_SECRET=your_jwt_secret_key

# Payment Gateway Configuration (Required for payment processing)
RAZORPAY_KEY_ID=your_razorpay_key_id
RAZORPAY_KEY_SECRET=your_razorpay_key_secret
STRIPE_API_KEY=your_stripe_api_key
STRIPE_WEBHOOK_SECRET=your_stripe_webhook_secret
```

For email functionality, update the following in `application.yml`:
```yaml
spring:
  mail:
    username: your-email@gmail.com
    password: your-app-password
```

### Building and Running Backend

1. Navigate to the project root directory
2. Build the project:
   ```
   mvn clean install
   ```
3. Run the application:
   ```
   mvn spring-boot:run
   ```

The backend server will start on http://localhost:8080/api

## Frontend Setup

### Installing Dependencies

1. Navigate to the frontend directory:
   ```
   cd frontend
   ```
2. Install dependencies:
   ```
   npm install
   ```

### Frontend Configuration

The frontend is configured to connect to the backend API at http://localhost:8080/api by default. If your backend is running on a different URL, update the `apiUrl` in `src/environments/environment.ts`:

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api' // Change this if needed
};
```

### Running the Frontend

1. From the frontend directory, start the development server:
   ```
   npm start
   ```
   or
   ```
   ng serve
   ```

2. The application will be available at http://localhost:4200

## Accessing the Application

- Frontend: http://localhost:4200
- Backend API: http://localhost:8080/api
- Swagger UI (API Documentation): http://localhost:8080/api/swagger-ui.html

## API Documentation

The API documentation is available through Swagger UI when the application is running:
http://localhost:8080/api/swagger-ui.html

## Troubleshooting

### Common Issues

1. **Database Connection Issues**
   - Ensure PostgreSQL is running
   - Verify database name, username, and password in application.yml
   - Check that the database has been created

2. **Port Conflicts**
   - If port 8080 is already in use, you can change the backend port in application.yml:
     ```yaml
     server:
       port: 8081  # Change to an available port
     ```
   - If port 4200 is already in use, you can start Angular with a different port:
     ```
     ng serve --port 4201
     ```

3. **Environment Variables Not Recognized**
   - Ensure environment variables are properly set
   - For Windows, use `set VARIABLE_NAME=value`
   - For Linux/Mac, use `export VARIABLE_NAME=value`
   - Restart your terminal after setting environment variables

4. **JWT Token Issues**
   - If you're experiencing authentication problems, check that the JWT_SECRET is properly set
   - The default JWT secret is provided in application.yml, but it's recommended to set your own for security

5. **CORS Issues**
   - The backend is configured to allow requests from http://localhost:4200
   - If you're running the frontend on a different URL, update the CORS configuration in SecurityConfig.java

6. **Node.js or npm Issues**
   - Try clearing npm cache: `npm cache clean --force`
   - Ensure you're using compatible versions of Node.js and npm

7. **Angular CLI Issues**
   - Try reinstalling Angular CLI: `npm uninstall -g @angular/cli && npm install -g @angular/cli`

For additional help, refer to the project's GitHub issues or create a new issue if you encounter a problem not listed here.