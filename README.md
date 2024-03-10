# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.2.3/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.2.3/maven-plugin/reference/html/#build-image)

# Park NL
ParkNL is an application which helps with parking in the Netherlands

# Author
- Name: Padmaprasad Ganesan
- Email: padmaprasad1729@gmail.com
- Created : 10 Mar 2024

# Tech stack
- Java 17
- Spring Boot
- Maven
- Mongo DB

# Build project
- Move to the project path
- ./mvnw clean install

# Run by Maven
- Pre-requisite : Run mongoDB and configure in properties
- Move to the project path
- ./mvnw spring-boot:run

# Run by Docker
- Pre-requisite : Docker Desktop
- Move to the project path
- docker compose up
- 
# Swagger
- http://localhost:8081/swagger-ui/index.html

# Open Api Doc
- http://localhost:8081/v3/api-docs