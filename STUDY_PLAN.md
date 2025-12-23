# 📚 Java 21 + Spring Boot Full-Stack Study Plan

This document captures the **step-by-step learning journey** followed to build the `library-service` ecosystem.
It is deliberately grounded in **real-world fixes, refactors, and architectural decisions**, rather than presenting an idealized or purely theoretical tutorial.

Each session is designed to take **approximately two hours** and should be completed **sequentially**, as later sessions build directly on the foundations established earlier.

---

## Project Overview

* **Core service:** `library-service`
* **Group:** `com.readstack`
* **Base package:** `com.readstack.library`
* **Java:** 21 (Amazon Corretto via SDKMAN)
* **Spring Boot:** 3.5.x
* **Build tool:** Gradle (Groovy DSL)
* **Database:** MySQL (Dockerized)
* **Testing:** JUnit 5, Mockito (agent-based), H2, Testcontainers
* **Containers:** Docker, Docker Compose v2
* **CI:** GitHub Actions (planned)
* **Frontend (Phase 3):** React (NX workspace)

---

## Phase 0 — Foundations & Tooling

### Session 0A — Local Environment Setup ✅

**Focus areas**

* Java 21 installation via SDKMAN
* IntelliJ IDEA Ultimate configuration
* Docker Desktop (Apple Silicon)
* Gradle Wrapper usage
* Docker Compose v2 behavior

**Key lessons learned**

* Mockito now requires an explicit Java agent
* Alpine-based JVM images have specific quirks
* `docker-compose.yml` no longer requires a `version` field
* Java toolchains are preferable to relying on system JDKs

---

## Phase 1 — Core Backend (Spring Boot + JPA)

### Session 1 — Spring Boot Bootstrap ✅

**What was built**

* Spring Boot 3.5.5 application
* Gradle Groovy-based build configuration
* Explicit `mainClass` configuration

**Issues encountered and resolved**

* `Main class name has not been configured`
* Groovy SDK misconfiguration
* `bootJar` vs `bootRun` execution failures

**Core concepts**

* Spring Boot auto-configuration
* Gradle build lifecycle
* Java toolchains and reproducible builds

---

### Session 2 — Domain, Repository, Service, Controller ✅

**Implemented components**

* `Book` JPA entity with validation constraints
* `BookRepository`

    * `JpaRepository`
    * `JpaSpecificationExecutor`
* `BookService` with transactional boundaries
* `BookController` exposing REST endpoints

**Key design decisions**

* Controllers delegate business logic to services
* Repositories are not exposed directly, except for specification-based search
* Domain-specific exceptions originate from the service layer

---

### Session 3 — Testing Strategy ✅

#### Session 3A — Unit Tests (Service Layer)

* Pure Mockito-based tests
* No Spring context involved
* Java agent–based Mockito configuration for Java 21 compatibility

#### Session 3B — Slice Tests

* `@WebMvcTest` using `@MockitoBean`
* `@DataJpaTest` backed by H2
* Explicit test profile configuration

**Key takeaways**

* `@MockBean` is deprecated; `@MockitoBean` should be used instead
* Slice tests are not integration tests
* H2 is appropriate for repository slices but insufficient for production parity

---

### Session 4 — DTOs and MapStruct ✅

**Implemented**

* `BookCreateUpdateDto` for input
* `BookDto` for output
* `BookMapper` using MapStruct with the Spring component model

**Key lessons learned**

* Use `@BeanMapping` (not `@Mapping`) for update operations
* Always annotate mutable targets with `@MappingTarget`
* Explicitly ignore `id` when mapping from client input
* Avoid `Mappers.getMapper()` when using Spring-managed mappers
* Entity–DTO mapping belongs at the controller boundary

---

### Session 5 — Validation and Error Handling ✅

**Why this session exists**

* Default Spring validation errors are verbose and inconsistent
* Frontend clients require a predictable error contract
* Validation should fail fast at the API boundary

**What was implemented**

* Global exception handling via `@RestControllerAdvice`
* Centralized handling of `MethodArgumentNotValidException`
* Stable validation error payload containing:

    * `timestamp`
    * `status`
    * `error`
    * `message`
    * `fieldErrors[]`

**Outcome**

* Clients can reliably render validation feedback
* Validation behavior is fully covered by MVC tests

---

## Phase 2 — Infrastructure & Production Readiness

This phase focuses on moving from a "works on my machine" backend to a system that behaves predictably across environments and is safe to evolve.

### Session 6 — Integration Tests with Testcontainers 🔜

**Why this session exists**

* H2 behaves differently from MySQL
* Dialect, schema, and constraint issues often surface late
* Confidence is required before Dockerization and CI

**Planned work**

* Introduce Testcontainers (MySQL 8.x)
* Shared container base class for tests
* Repository integration tests without mocks
* Controller integration tests with a full Spring context

**Expected outcome**

* High confidence in production database behavior
* Clear separation between slice tests and true integration tests

---

### Session 7 — Dockerization (Backend) 🔜

**Why this session exists**

* Local development environments hide production issues
* Java, Docker, and Alpine images have sharp edges
* Environment parity is critical for reliability

**Planned work**

* Multi-stage Dockerfile
* Amazon Corretto 21 on Alpine
* `.env`-driven configuration
* Clean, non-deprecated JVM options

**Expected outcome**

* Fully reproducible backend image
* No reliance on developer machine state

---

### Session 8 — Continuous Integration with GitHub Actions 🔜

**Why this session exists**

* Tests only add value when run automatically
* CI enforces discipline and prevents regressions

**Planned work**

* GitHub Actions workflow
* Gradle dependency caching
* Execution of unit, slice, and Testcontainers tests
* Fail-fast pipeline design

**Expected outcome**

* Every push and pull request is validated
* Increased confidence when refactoring or extending the system

---

## Phase 2.5 — GraphQL Gateway (Backend for Frontend)

This phase introduces a **separate GraphQL gateway service** that the frontend communicates with. The gateway calls `library-service` via REST and exposes a frontend-optimized GraphQL schema.

The gateway is intentionally kept **independent** from `library-service` to:

* Preserve clear service boundaries
* Avoid mixing domain logic with presentation concerns
* Allow the GraphQL schema to evolve independently

### Session 9 — GraphQL Gateway Bootstrap 🔜

**Why this session exists**

* REST APIs are not always frontend-friendly
* GraphQL allows precise data shaping and aggregation
* A gateway simplifies frontend data access

**Planned work**

* Create a new Spring Boot service (e.g. `readstack-gql-gateway`)
* Add Spring for GraphQL
* Define a schema-first GraphQL API for books
* Expose a `/graphql` endpoint

**Expected outcome**

* Running GraphQL server with schema-based queries

---

### Session 10 — Gateway → library-service Integration 🔜

**Why this session exists**

* Gateways should delegate domain logic, not duplicate it

**Planned work**

* Use `WebClient` or `RestClient` to call `library-service`
* Map REST DTOs to GraphQL types
* Handle downstream errors cleanly

**Expected outcome**

* GraphQL queries backed by real REST calls

---

### Session 11 — Netflix DGS Code Generation 🔜

**Why this session exists**

* Schema-first development benefits from strong typing
* Generated code reduces manual boilerplate

**Planned work**

* Add Netflix DGS codegen Gradle plugin
* Generate Java types from GraphQL schema
* Optionally generate a type-safe GraphQL client

**Expected outcome**

* Consistent, schema-driven models across the gateway

---

### Session 12 — GraphQL Testing & Error Contracts 🔜

**Why this session exists**

* GraphQL errors must be predictable for frontend consumers

**Planned work**

* Use `GraphQlTester` for resolver tests
* Test error mapping from REST → GraphQL
* Validate schema evolution safety

**Expected outcome**

* Well-tested, frontend-safe GraphQL API

---

## Phase 3 — Frontend (React + NX)

This phase focuses on consuming the GraphQL gateway from a modern React application.

**Why this phase exists**

* Backend APIs only provide value when consumed by real clients
* Validation and error contracts must be exercised end to end
* Full-stack awareness improves backend design decisions

**Frontend goals**

* Treat the backend strictly as an external API
* Respect DTO and GraphQL schema contracts
* Render validation and domain errors cleanly
* Practice production-grade client–server integration

---

## Current Skill Position

> **Early mid-level backend engineer**, with strong testing discipline and growing production awareness.

---
