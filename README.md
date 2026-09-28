# jobconnect
Job seeker and employer meeting point
  # JobConnect — Job Management Platform

JobConnect is a full-stack-ready job management platform built with **Java and Spring Boot**. It provides a secure REST API for job seekers, employers, and administrators to manage jobs, applications, users, and related activities.

The project was developed as a professional Java/Spring Boot portfolio project, with emphasis on **REST API development, authentication, authorization, database integration, and application security**.

## Project Status

**Development:** Completed
**GitHub:** Available
**Online Deployment:** Coming Soon

## Key Features

* User registration and authentication
* JWT-based authentication
* Role-based access control
* Job seeker accounts
* Employer accounts
* Administrator accounts
* Job creation and management
* Job application submission
* Application status management
* User and role management
* Protected administrative endpoints
* Ownership-based authorization
* Global exception handling
* Custom 401 and 403 security responses
* MySQL database integration
* RESTful API architecture

## User Roles

### Job Seeker

Job seekers can:

* View available jobs
* Apply for jobs
* View their applications
* Manage their own application information
* Track application status

### Employer

Employers can:

* Create jobs
* Manage their own job postings
* View applications related to their jobs
* Manage application status where authorized

### Administrator

Administrators can:

* Manage users
* Manage user roles
* Manage jobs
* Monitor applications
* Access protected administrative functions

## Technology Stack

* **Java**
* **Spring Boot**
* **Spring Security**
* **JWT**
* **Spring Data JPA**
* **Hibernate**
* **MySQL**
* **REST API**
* **Maven**
* **Git & GitHub**
* **Postman**
* **HTML/CSS/JavaScript** for the frontend interface

## Security

Security was treated as an important part of the project rather than an afterthought.

The application includes:

* JWT authentication
* Password authentication
* Role-based authorization
* Protected API endpoints
* Ownership checks
* Custom authentication entry point
* Custom access-denied handling
* Global exception handling
* HTTP 401 and 403 responses
* Protection against unauthorized application manipulation
* Validation of protected application fields

Security functionality was tested using authenticated and unauthorized requests through Postman/cURL.

## Database

JobConnect uses **MySQL** for persistent data storage.

The database stores information including:

* Users
* Roles
* Jobs
* Applications

Spring Data JPA and Hibernate are used for database interaction.

## REST API

The backend exposes REST endpoints for major application functions, including:

```text
/users
/jobs
/applications
/admin
```

Access to protected endpoints depends on the authenticated user's role and ownership of the requested resource.

## Project Structure

The project follows a layered Spring Boot architecture with components for:

```text
Controller
Service
Repository
Entity
Security
Exception Handling
Configuration
```

This structure separates application responsibilities and makes the project easier to maintain and extend.

## Running the Project Locally

### Requirements

Before running JobConnect, install:

* Java JDK
* Maven
* MySQL
* Git

### Database

Create the JobConnect MySQL database and configure the database connection using your local environment configuration.

Sensitive credentials should be supplied through environment variables or local configuration and should not be committed to GitHub.

### Start the Application

Clone the repository, configure the database, and run the Spring Boot application.

The application runs locally at:

```text
http://localhost:8080
```

## Testing

The project was tested through multiple API security and functionality scenarios, including:

* Authentication testing
* Role authorization testing
* Unauthorized endpoint access
* Job ownership checks
* Application ownership checks
* Application status protection
* Protected-field manipulation testing
* HTTP 401 and 403 responses
* Administrative endpoint protection

## Future Improvements

Planned improvements may include:

* Online production deployment
* Email notifications
* Advanced job search and filtering
* Resume/CV management
* Employer analytics
* Improved frontend interface
* Cloud deployment and production monitoring

## Portfolio

JobConnect is one of my Java/Spring Boot portfolio projects, demonstrating practical experience in:

* Backend development
* REST API development
* Spring Boot
* Spring Security
* JWT authentication
* MySQL
* Database design
* Authorization
* Security testing
* Git/GitHub

## Author

**Egbunu Malik David**

**Fountain Information Service (FIS)**

Fountain Information Service focuses on technology, software development, digital skills training, and practical technology solutions.

