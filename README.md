# Spring Boot Calculator

A simple calculator REST API using Spring Boot.

## Requirements

* Java 17 or above
* Maven 3.9 or above, or use the Maven bundled with IntelliJ IDEA

## Run the project

```bash
mvn spring-boot:run
```

The application starts at:

```text
http://localhost:8080
```

## API Examples

### Addition

```text
GET http://localhost:8080/api/calculator/add?a=10\&b=5
```

### Subtraction

```text
GET http://localhost:8080/api/calculator/subtract?a=10\&b=5
```

### Multiplication

```text
GET http://localhost:8080/api/calculator/multiply?a=10\&b=5
```

### Division

```text
GET http://localhost:8080/api/calculator/divide?a=10\&b=5
```

Example response:

```json
{
  "firstNumber": 10.0,
  "secondNumber": 5.0,
  "operation": "ADD",
  "result": 15.0
}
```

```text
Developer writes code
↓
Git and GitHub
↓
Jenkins CI/CD
↓
Maven build and testing
↓
Docker image
↓
Docker container
↓
Kubernetes
↓
AWS deployment

Final goal:

Push code to GitHub
↓
Jenkins automatically builds and tests
↓
Creates Docker image
↓
Deploys application
↓
Application runs on AWS

