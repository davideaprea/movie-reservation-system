# [Movie reservation system](https://roadmap.sh/projects/movie-reservation-system)

Backend application focused on handling concurrent seat reservations and modular architecture design, built to explore scalability and decoupling in a monolithic system.

## Core features

- **Movie schedules**: create, manage and view available movies showtimes
- **Seat reservation**: book available seats for a specific schedule, with concurrency handling to prevent double bookings
- **Payment integration**: secure payment and refunds via Stripe
- **Hall management**: create halls with a seat map, allowing configuration of seat types (STANDARD, VIP, etc.)

## Architecture

### Modular design

- Each module is self-contained, with its own domain entities, services, and repositories
- Modules communicate via service APIs rather than direct object references, reducing coupling
- Clear module boundaries enable independent evolution and scalability, while preserving the simplicity of a monolithic deployment

### Entity relationships

- To avoid tight coupling between modules, entity relationships are not managed via standard JPA annotations (like
  `@OneToMany` or `@ManyToOne`). Instead, each entity only keeps a reference to the IDs of related entities
- Actual database constraints and relationships are managed using Flyway migrations, ensuring data integrity at the
  database level without introducing dependencies between Java modules

## Built with

- Java 21
- Spring Boot 4
- PostgreSQL
- Docker
- Flyway migrations

# Quick start

## Prerequisites

- [Java 21+](https://www.oracle.com/it/java/technologies/downloads/#java21)
- [Stripe account](https://dashboard.stripe.com/register) for setting up API credentials
- [Maven 3.9.x](https://maven.apache.org/download.cgi) for building, running and testing the application
- [PostgreSQL](https://www.postgresql.org/download/)
- [Docker](https://www.docker.com/get-started/) for running integration tests

## Running the application

1. Run `mvn install` to install project's dependencies
2. Update the [application.yaml](src/main/resources/application.yaml) file by replacing the placeholder values:
    - DATABASE_CONN_URL: database connection URL, e.g.: `jdbc:postgresql://localhost:5432/db_name`
    - STRIPE_API_KEY: API key (client ID) for your Stripe application
    - STRIPE_WEBHOOK_INVOKED_API_SECRET: secret key used to validate incoming Stripe webhooks when they invoke your application APIs
    - JWT_SECRET_KEY: base64-encoded key used to sign JWTs. The decoded key must be at least 32 bytes long (minimum for
      HS256)
    - JWT_EXP_TIME: expiration time for JWTs in milliseconds
    - PAYMENT_TIMEOUT: time allowed for completing a payment before it expires, e.g.: 5m, 30s, 2h
    - EXPIRED_ORDER_CLEANUP_DELAY: delay interval for scheduling the cleanup of uncompleted or expired orders, e.g.: 5m, 30s, 2h
3. Run `mvn spring-boot:run` to start the application
4. Once the application is running, access the API documentation
   via [Swagger UI](http://localhost:8080/swagger-ui/index.html)

## Running integration tests

1. Make sure your local Docker environment is running
2. Run `mvn clean verify`
3. Check the test results in the terminal and monitor container execution via Docker Desktop
