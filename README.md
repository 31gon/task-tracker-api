# Task Tracker API

Portfolio project - REST API for task and/or issue tracking, built with Spring Boot.

## Status
  In progress - currently just a basic kick starter code, nothing wonderful. Features being add incrementally.

## On TO-DO list
 - Spring Boot (Web, Security, Data JPA)
 - PostgreSQL
 - Docker Compose
 - JWT auth

## Roadmap
 - [1] Project on-default
 - [1] Entities & CRUD
 - [1] JWT Auth
 - [1] Filtering & pagination
 - [0] Tests
 - [0] Docker setup
 - [0] API docs

## Local setup
Require local profile to run. Create `src/main/resources/application-local.yaml` with your own 256-bit secret:
```yaml
jwt:
  secret: <your-generated-secret>
```
Generate one with: `openssl rand -base64 32`
 - IDEA: Select `Run / Debug Configuration` -> `Edit Configuration` -> Add into `Active profiles` line `local`
 - CLI: Add `SPRING_PROFILES_ACTIVE=local` before run command. Present below RUN include this line

## RUN
```bash
SPRING_PROFILES_ACTIVE=local ./gradlew bootRun
```