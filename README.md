# Ticketing System

This repository contains a comprehensive Ticketing System with a multi-module architecture. It includes:

1. **Backend** - A Spring Boot application for handling APIs and business logic.
2. **Frontend** - An Angular application for the user interface.
3. **CLI Application** - A Java CLI application for administrative tasks.

---

## Table of Contents

1. [Features](#features)
2. [Technologies Used](#technologies-used)
3. [Setup Instructions](#setup-instructions)
   - [Backend (Spring Boot)](#backend-spring-boot)
   - [Frontend (Angular)](#frontend-angular)
   - [CLI Application (Java)](#cli-application-java)
4. [Usage](#usage)
5. [Contributing](#contributing)
6. [License](#license)

---

## Features

- Ticket creation and management.
- Multi-user access (Admin, Vendors, Customers).
- Real-time updates for ticket availability.
- Integration between CLI, backend, and frontend.

---

## Technologies Used

- **Backend**: Java, Spring Boot
- **Frontend**: Angular, TypeScript
- **CLI Application**: Java (Standard Java Application)
- **Build Tools**: Maven for Java, Angular CLI for Angular

---

## Setup Instructions

### Prerequisites

Ensure the following tools are installed on your system:

- Java JDK 17+
- Node.js 18+ and npm
- Angular CLI 15+
- Maven

### Backend (Spring Boot)

1. Navigate to the `backend` directory:
   ```bash
   cd backend
   ```
2. Update the `application.properties` file with your MySQL database credentials.

3. Build and run the application using Maven:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
4. The backend will start at `http://localhost:8080`.

### Frontend (Angular)

1. Navigate to the `frontend` directory:
   ```bash
   cd frontend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Run the Angular application:
   ```bash
   ng serve
   ```
4. The application will be available at `http://localhost:4200`.

### CLI Application (Java)

1. Navigate to the `cli` directory:
   ```bash
   cd cli
   ```
2. Build the application using Maven:
   ```bash
   mvn clean package
   ```
3. Run the CLI application:
   ```bash
   java -jar target/cli-application.jar
   ```

---

## Usage

### Backend API Endpoints

The backend provides a REST API for interacting with the system. Refer to the `API Documentation` for detailed endpoint information.

### Angular Frontend

The frontend provides a user-friendly interface for interacting with the system. Features include:
- User registration/login.
- Ticket booking.
- Ticket availability display.

### CLI Application

The CLI application is used for administrative tasks such as managing users, tickets, and system configurations.

---

## Contributing

Contributions are welcome! To contribute:
1. Fork the repository.
2. Create a feature branch.
3. Commit your changes.
4. Submit a pull request.

---


