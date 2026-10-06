# Task Tracker API

REST API for task tracking, built with Spring Boot as a portfolio project.

[![CI](https://github.com/31gon/task-tracker-api/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/31gon/task-tracker-api/actions/workflows/ci.yml)

## Features

- JWT authentication (register, login) with Spring Security
- Task CRUD with validation and error handling
- Pagination, sorting, and filtering
- PostgreSQL with Spring Data JPA
- Unit and integration tests
- Docker Compose setup
- Swagger UI

## Stack

Java 21 / Spring Boot 4.1.1 / Gradle (Kotlin DSL) / PostgreSQL / JUnit 5 / Docker

## Run

JWT needs a secret. Create `src/main/resources/application-local.yaml`:

```yaml
jwt:
  secret: <output of: openssl rand -base64 32>
```

Start the database and the app:

```
docker compose up -d
SPRING_PROFILES_ACTIVE=local ./gradlew bootRun
```

In IDEA: Run Configuration -> Active profiles -> `local`.

Swagger UI: http://localhost:8080/swagger-ui.html

## Example requests

```
curl -X POST localhost:8080/auth/register -H "Content-Type: application/json" \
  -d '{"username":"demo","password":"demo1234"}'

curl -X POST localhost:8080/auth/login -H "Content-Type: application/json" \
  -d '{"username":"demo","password":"demo1234"}'

curl -X POST localhost:8080/tasks -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" -d '{"title":"First task"}'

curl "localhost:8080/tasks?status=<status>&page=0&size=10&sort=<field>,desc" \
  -H "Authorization: Bearer <token>"
```

## Tests

```
./gradlew test
```

## Learning path

Smaller repos I built while learning each topic:

- [entities-and-crud](https://github.com/31gon/entities-and-crud)
- [jwt-auth](https://github.com/31gon/jwt-auth)
- [paging-and-filtering](https://github.com/31gon/paging-and-filtering)
- [tests-study](https://github.com/31gon/tests-study)
