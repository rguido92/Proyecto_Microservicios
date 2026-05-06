# AGENTS.md

## Project Overview

Java 17 / Spring Boot 3.2.5 Maven multi-module microservices project.

## Module Structure

| Module | Port | Database | Type | Notes |
|--------|------|----------|------|-------|
| `eurekaServer` | 8700 | None | Eureka Server | Must start first |
| `apigateaway` | 8080 | None | Spring Cloud Gateway | Not registered in Eureka |
| `usuarios` | 8702 | MySQL `usuariosproyecto` | REST API | JPA, Hibernate `validate` |
| `reservas` | 8701 | MySQL `reservasproyecto` | REST API | JPA, Hibernate `validate` |
| `comentarios` | 8703 | MongoDB `comentariosProyecto` | GraphQL | GraphiQL at `/comentarios` |

## Startup Order

Eureka (8700) → API Gateway (8080) → microservices (any order)

## Key Config Facts

- Root `pom.xml` (packaging=pom) lists modules but does NOT inherit Spring Boot parent — each module declares it independently
- MySQL credentials: `root` / `abc123` (hardcoded in `application.properties`)
- MongoDB: `localhost:27017`, no auth configured
- Hibernate: `ddl-auto=validate` in usuarios/reservas — schema must exist before startup
- Comentarios excludes `DataSourceAutoConfiguration` (line 1 of its application.properties)
- Spring Cloud version: `2023.0.0` (root pom) vs `2023.0.1` (apigateaway, eurekaServer) — intentional mismatch
- Eureka client version pinned to `4.1.0` in usuarios, reservas, comentarios
- Lombok `1.18.22` used in usuarios and reservas; comentarios uses default from parent

## Build Commands

```bash
# Build all modules
cd proyecto_microservicios-main
mvn clean install

# Build single module
mvn -pl <module> -am clean install

# Run single module
mvn -pl <module> spring-boot:run
```

## API Entry Points

- REST APIs: via Gateway at `http://localhost:8080` or direct on respective ports
- GraphQL (comentarios): `http://localhost:8703/comentarios` (GraphiQL enabled)
- Eureka dashboard: `http://localhost:8700`
- OpenAPI docs: `/api-usuarios`, `/api-reservas`, `/api-comentarios`

## Database Setup

MySQL schemas `usuariosproyecto` and `reservasproyecto` are auto-created (`createDatabaseIfNotExist=true`), but tables must exist for Hibernate `validate` mode.

## Notes

- Module `apigateaway` is misspelled (not "apigateway") — use exact name in Maven commands
- No tests found in repo — `mvn test` will pass with no test execution
- No CI/CD config (`.github/`, `.gitlab-ci.yml`, etc.) present
- No `opencode.json` or other OpenCode config exists
