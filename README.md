# JobConnect — Job Management REST API

**A Java Spring Boot portfolio project by Egbunu Malik David**

JobConnect is a job management platform that connects job seekers and employers through a REST API. It supports user management, job postings, job applications, and application status tracking, with role-based access control and JWT authentication.

The project demonstrates practical backend development using Java, Spring Boot, Spring Security, MySQL, and REST API design.

## Project Status

- **Development:** Core features implemented
- **API documentation:** Swagger UI / OpenAPI
- **Database:** MySQL
- **Build:** Maven
- **Online deployment:** Planned
- **Source code:** Available on GitHub

## Project Highlights

- RESTful API development with Spring Boot
- JWT-based authentication
- Role-based authorization for job seekers, employers, and administrators
- Job posting and application management
- Resource ownership and access-control checks
- MySQL database integration using Spring Data JPA and Hibernate
- Centralized exception handling
- Custom handling of HTTP 401 Unauthorized and 403 Forbidden responses
- API testing using Swagger UI and Postman

## Key Features

### Job Seekers
- Register and authenticate.
- Browse available job postings.
- Submit job applications.
- View personal applications and track their status.
- Manage their own profile and permitted information.

### Employers
- Register an employer account and authenticate.
- Create and manage job postings.
- View applications associated with their jobs.
- Manage application statuses where authorized.

### Administrators
- Manage users and permitted user roles.
- Access protected administrative endpoints.
- Manage and monitor application activities.
- Perform authorized administrative operations.

## Technology Stack

| Technology | Purpose |
|---|---|
| Java | Backend programming |
| Spring Boot | Application framework |
| Spring Security | Authentication and authorization |
| JWT | Token-based authentication |
| Spring Data JPA | Database access |
| Hibernate | Object-relational mapping |
| MySQL | Persistent data storage |
| Maven | Dependency management and builds |
| Swagger UI / OpenAPI | API documentation and exploration |
| Postman | API testing |
| Git and GitHub | Version control and source hosting |
| HTML, CSS and JavaScript | Frontend interface |

## Architecture

JobConnect follows a layered Spring Boot architecture:

- **Controller:** Handles HTTP requests and responses.
- **Service:** Contains application business logic.
- **Repository:** Provides database access.
- **Entity:** Represents persistent application data.
- **Security:** Handles JWT authentication and access control.
- **Configuration:** Defines application and API documentation settings.
- **Exception handling:** Provides consistent error responses.

This separation of responsibilities supports maintainability and future development.

## Security Implementation

Security is an important part of the project.

Implemented security features include:

- JWT authentication for protected operations.
- Role-based access control.
- Protection of administrative endpoints.
- Ownership checks for protected resources.
- Authentication and access-denied handlers.
- HTTP 401 and 403 error handling.
- Validation and protection of sensitive application fields.
- Restrictions on unauthorized access to jobs, applications, and user information.

Security and functionality have been tested using authenticated and unauthenticated API requests, including tests of protected endpoints and role-specific access.

## REST API

The main API resource groups include:

| Resource | Purpose |
|---|---|
| `/auth/login` | User authentication |
| `/users` | User registration and management |
| `/users/me` | Access to the authenticated user's profile |
| `/jobs` | Job browsing and management |
| `/jobs/my` | Employer job management |
| `/applications` | Job application operations |
| `/applications/my` | Access to the authenticated job seeker's applications |
| `/admin` | Administrative operations |

Access to individual operations depends on authentication, user role, and applicable resource ownership rules.

### Explore the API

When running locally, open Swagger UI:

**http://localhost:8080/swagger-ui/index.html**

Swagger UI allows you to inspect documented endpoints and test API requests. Protected endpoints require the appropriate authentication token and permissions.

Online API access will be added after production deployment.

## Database

JobConnect uses MySQL to persist application data, including users, jobs, and job applications.

Spring Data JPA and Hibernate handle database interaction.

For local development, create the database and configure the connection using your local Spring Boot configuration. Keep database passwords, JWT secrets, and other sensitive credentials out of the public repository.

## Running Locally

### Prerequisites

Install:

- Java JDK
- MySQL Server
- Maven, if not using the Maven Wrapper
- Git

### 1. Clone the repository

```bash
git clone https://github.com/davidmalik2019/jobconnect.git
cd jobconnect
```

### 2. Configure MySQL

Create the database named `jobconnect_db` if that matches your local configuration.

Configure the database URL, username, password, and other required application settings in your local configuration. Do not commit private credentials.

### 3. Start the application

From the project root, run:

```bash
mvn spring-boot:run
```

Alternatively, build the application:

```bash
mvn clean package
```

Then run the generated JAR from the `target` directory, using the actual filename produced by the build:

```bash
java -jar target/jobconnect-0.0.1-SNAPSHOT.jar
```

Ensure the database is running and the application configuration is correct before starting the application.

### 4. Open the application

The default local address is:

- Application: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui/index.html

## Testing and Verification

Development testing has included:

- Login and JWT authentication.
- Access to the authenticated user's profile.
- Employer access to their job postings.
- Role-based endpoint authorization.
- Unauthorized access handling.
- Resource ownership and application access checks.
- Protected-field validation.
- Administrative endpoint protection.
- HTTP 401 and 403 responses.

The packaged Spring Boot JAR was built successfully and started locally. The authenticated `GET /users/me` and `GET /jobs/my` endpoints were also tested successfully through Swagger UI.

These results demonstrate local testing; they do not imply that a public production deployment is already available.

## Future Improvements

Potential future enhancements include:

- Production deployment over HTTPS.
- Advanced job search and filtering.
- Email notifications.
- Resume and CV management.
- Employer analytics.
- Further frontend improvements.
- Production monitoring and operational logging.

## Developer Portfolio

JobConnect is part of my Java backend development portfolio. It demonstrates practical work with:

- Java and Spring Boot
- REST API design and implementation
- Spring Security and JWT
- MySQL and JPA/Hibernate
- Role-based access control
- API testing and debugging
- Maven builds
- Git and GitHub

**Developer:** Egbunu Malik David  
**Organization:** Fountain Information Service (FIS)

**Repository:** https://github.com/davidmalik2019/jobconnect

Fountain Information Service focuses on technology, software development, digital skills training, and practical technology solutions.
