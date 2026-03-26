# Movie reservation system

## Quick start
### Prerequisites
- [Java 21+](https://www.oracle.com/it/java/technologies/downloads/#java21)
- [PayPal developer account](https://developer.paypal.com/home/) for setting up API credentials
- [Maven 3.9.x](https://maven.apache.org/download.cgi) for building, running and testing the application
- [PostgreSQL](https://www.postgresql.org/download/)
- [Docker](https://www.docker.com/get-started/) for running integration tests

### Running the application
1. Run `mvn install` to install project's dependencies
2. Update the [application.yaml](src/main/resources/application.yaml) file by replacing the placeholder values:
    - DATABASE_CONN_URL: database connection URL, e.g.: `jdbc:postgresql://localhost:5432/db_name`
    - PAYPAL_CLIENT_ID: PayPal application client ID
    - PAYPAL_CLIENT_SECRET: PayPal application client secret
    - JWT_SECRET_KEY: base64 key used to sign generated JWTs. The decoded value must have a minimum size of 32 bytes
    - JWT_EXP_TIME: expiration time for JWTs in milliseconds
3. Run `mvn spring-boot:run` to start the application
4. Once the application is running, access the API documentation via [Swagger UI](http://localhost:8080/swagger-ui/index.html)

### Running integration tests
1. Make sure your local Docker environment is running
2. Run `mvn clean verify`
3. Check the test results in the terminal and monitor container execution via Docker Desktop