# 📚 Study Plan — Java 21 + Spring Boot Full-Stack Path

This document tracks the **step-by-step learning journey** used to build the `library-service` project.
It reflects **real-world fixes, refactors, and decisions** made while developing the codebase — not a theoretical tutorial.

Each session is designed to take **~2 hours** and should be completed **sequentially**.

---

## Project Context

* **Project name:** `library-service`
* **Group:** `com.readstack`
* **Base package:** `com.readstack.library`
* **Java:** 21 (Amazon Corretto via SDKMAN)
* **Spring Boot:** 3.5.x
* **Build tool:** Gradle (Groovy DSL)
* **Database:** MySQL (Dockerized)
* **Testing:** JUnit 5, Mockito (agent-based), H2, Testcontainers
* **Containers:** Docker, Docker Compose v2
* **CI:** GitHub Actions (planned)
* **Frontend (Phase 2):** React (NX workspace)

---

## Phase 0 — Foundations & Tooling

### Session 0A — Local Environment Setup ✅

**Topics**

* Java 21 via SDKMAN
* IntelliJ IDEA Ultimate
* Docker Desktop (Apple Silicon)
* Gradle Wrapper
* Docker Compose v2 behavior

**Key lessons**

* Mockito now requires a Java agent
* Alpine + Corretto JVM quirks
* `docker-compose.yml` `version` is obsolete
* Java toolchains > system JDKs

---

## Phase 1 — Core Backend (Spring Boot + JPA)

### Session 1 — Spring Boot Bootstrap ✅

**Built**

* Spring Boot 3.5.5 application
* Gradle Groovy build
* Explicit `mainClass` configuration

**Fixes encountered**

* `Main class name has not been configured`
* Groovy SDK confusion
* `bootJar` vs `bootRun` failures

**Concepts**

* Spring Boot auto-configuration
* Gradle lifecycle
* Java toolchains

---

### Session 2 — Domain, Repository, Service, Controller ✅

**Implemented**

* `Book` JPA entity (constraints + schema)
* `BookRepository`

    * `JpaRepository`
    * `JpaSpecificationExecutor`
* `BookService` (transactional boundaries)
* `BookController` (REST)

**Key decisions**

* Controllers delegate to services
* Repositories only exposed directly for search
* Service throws domain exceptions

---

### Session 3 — Testing Strategy ✅

#### Session 3A — Unit Tests (Service)

* Pure Mockito
* No Spring context
* Java agent–based Mockito configuration

#### Session 3B — Slice Tests

* `@WebMvcTest` with `@MockitoBean`
* `@DataJpaTest` with H2
* Explicit test profile configuration

---

### Session 4 — DTOs + MapStruct ✅

**Implemented**

* `BookCreateUpdateDto`
* `BookDto`
* `BookMapper` (Spring component model)

**Key lessons**

* Use `@BeanMapping`, not `@Mapping`, for updates
* Always mark update targets with `@MappingTarget`
* Explicitly ignore `id`
* Do not use `Mappers.getMapper()` with Spring mappers
* Mapping belongs at the controller boundary

---

### Session 5 — Validation & Error Handling ✅

**Why this session exists**

* Raw Spring validation errors are noisy and inconsistent
* Frontends need a predictable error contract
* Validation must fail fast at the API boundary

**Implemented**

* `@RestControllerAdvice`–based global exception handling
* `MethodArgumentNotValidException` mapping
* Stable error payload with:

    * timestamp
    * status
    * error
    * message
    * fieldErrors[]

**Outcome**

* Clients can reliably render validation errors
* Validation behavior is fully covered by MVC tests

---

### Session 6 — Integration Tests with Testcontainers 🔜

**Why this session exists**

* H2 behaves differently from MySQL
* Schema, dialect, and constraint issues surface late without real DB tests
* Confidence is required before Docker + CI

**Planned work**

* Introduce Testcontainers (MySQL 8.x)
* Shared container base class
* Repository integration tests (no mocks)
* Controller integration tests (full Spring context)

**Outcome**

* High confidence that production DB behavior is correct
* Clear separation between slice tests and true integration tests

---

### Session 7 — Dockerization (Backend) 🔜

**Why this session exists**

* Local setups hide production issues
* Java + Docker + Alpine has sharp edges
* Environment parity is critical

**Planned work**

* Multi-stage Dockerfile
* Amazon Corretto 21 (Alpine)
* `.env`-driven configuration
* Clean JVM options (no deprecated flags)

**Outcome**

* Reproducible backend image
* Zero reliance on developer machine state

---

### Session 8 — GitHub Actions CI 🔜

**Why this session exists**

* Tests only matter if they run automatically
* CI enforces discipline and prevents regressions

**Planned work**

* GitHub Actions workflow
* Gradle build caching
* Unit, slice, and Testcontainers tests
* Fail-fast pipeline

**Outcome**

* Every push and PR is validated
* Confidence to refactor and extend safely

---

## Phase 3 — Frontend (React + NX)

**Why this phase exists**

* Backend APIs only matter when consumed
* Validation and error contracts must be exercised by a real client
* Full-stack understanding improves backend design

**Frontend goals**

* Treat the backend as an external API
* Respect DTO contracts and validation errors
* Practice production-grade integration

---

## Current Skill Position

> **Early mid-level backend engineer** with strong testing discipline and production awareness.

---
