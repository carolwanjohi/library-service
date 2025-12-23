# 🚪 GraphQL Gateway (Backend for Frontend)

## Study Plan

This document describes the **GraphQL Gateway / Backend for Frontend (BFF)** learning track for the Readstack project.
It is intended to be followed **after** the core backend (`library-service`) has reached a reasonable level of production readiness (testing, Dockerization, and basic CI).

Each session is deliberately scoped to **approximately two hours** and is designed to build incrementally on the previous one. The goal is not speed, but clarity, correctness, and architectural understanding.

---

📌 **Recommended prerequisite**
Complete backend Sessions 1–8 in `library-service` before starting this track.

---

## Why a GraphQL Gateway Exists

The GraphQL gateway is a **separate Spring Boot service** whose sole responsibility is to act as an intermediary between the frontend and the backend services.

It:

* Is the **only backend** the frontend communicates with
* Calls `library-service` via **REST**
* Exposes a **frontend-optimized GraphQL schema**
* Shields frontend clients from REST-specific concerns and backend refactors

This separation is intentional and provides several long-term benefits:

* Clean service boundaries and ownership
* No leakage of domain or persistence concerns into the presentation layer
* Freedom to evolve the GraphQL schema independently of REST APIs

---

## Technology Stack

The gateway deliberately uses a small, well-defined set of tools:

* **Spring Boot** (3.5.x)
* **Spring for GraphQL** — GraphQL server runtime
* **Netflix DGS Codegen** — schema-first Java code generation
* **WebClient / RestClient** — downstream REST communication
* **JUnit 5 + GraphQlTester** — testing and contract verification

> Spring for GraphQL is used exclusively for running the GraphQL server.
> Netflix DGS is used **only** for code generation and does not compete with Spring’s programming model.

---

## Session 1 — Gateway Bootstrap

**Why this session matters**

* REST APIs are rarely shaped the way frontend clients need
* GraphQL enables precise data selection and aggregation
* A gateway establishes a clean entry point for frontend traffic

**Work to complete**

* Create a new Spring Boot service (e.g. `readstack-gql-gateway`)
* Add the Spring for GraphQL starter
* Configure and expose the `/graphql` endpoint
* Verify the application starts successfully

**Outcome**

* A running GraphQL server with no resolvers yet

---

## Session 2 — Schema-First API Design

**Why this session matters**

* In GraphQL, the schema *is* the contract
* Frontend teams depend on schema stability

**Work to complete**

* Create `schema.graphqls`
* Define:

    * `Query` (list books, get book by ID, search)
    * `Mutation` (create, update, delete)
    * `Book` and `BookInput` types
* Ensure the schema is loaded and validated at startup

**Outcome**

* A stable, version-controlled GraphQL API contract

---

## Session 3 — Query Resolvers (Read Operations)

**Why this session matters**

* Read operations are simpler and ideal for first backend integration

**Work to complete**

* Implement `@QueryMapping` resolvers
* Call `library-service` REST endpoints using `WebClient` or `RestClient`
* Map REST DTOs to GraphQL response types

**Outcome**

* GraphQL queries backed by real REST calls

---

## Session 4 — Mutation Resolvers (Write Operations)

**Why this session matters**

* Mutations introduce validation, error handling, and state changes

**Work to complete**

* Implement `@MutationMapping` resolvers
* Forward inputs to `library-service`
* Translate HTTP and validation errors into GraphQL-friendly errors

**Outcome**

* Fully functional CRUD operations exposed via GraphQL

---

## Session 5 — Netflix DGS Code Generation

**Why this session matters**

* Schema-first development benefits from strong typing
* Generated code reduces manual mapping and boilerplate

**Work to complete**

* Add the Netflix DGS codegen Gradle plugin
* Generate Java types from the GraphQL schema
* Replace handwritten models with generated types where appropriate
* (Optional) Enable a generated, type-safe GraphQL client

**Outcome**

* A schema-driven, type-safe gateway implementation

---

## Session 6 — Error Contracts and Validation

**Why this session matters**

* GraphQL errors must be predictable and consistent
* Frontend clients should never see raw REST errors

**Work to complete**

* Define a clear REST → GraphQL error mapping strategy
* Normalize validation and not-found errors
* Prevent internal error payloads from leaking through the gateway

**Outcome**

* Frontend-safe, consistent GraphQL error behavior

---

## Session 7 — GraphQL Testing

**Why this session matters**

* GraphQL APIs require contract-level testing, not just unit tests

**Work to complete**

* Use `@GraphQlTest` for resolver slice tests
* Use `GraphQlTester` to execute queries and mutations
* Assert both data shapes and error responses

**Outcome**

* High confidence in schema stability and runtime behavior

---

## Session 8 — Gateway Integration and Runtime

**Why this session matters**

* The gateway must operate correctly alongside other services

**Work to complete**

* Add the gateway to Docker Compose
* Configure service-to-service networking
* Validate local, multi-service startup

**Outcome**

* A fully integrated backend-for-frontend layer

---

## Final Outcome

By completing this track, you will have:

* A cleanly separated GraphQL Backend for Frontend
* Strongly typed, schema-first development practices
* Clear ownership boundaries between domain services and frontend concerns
* A production-ready API that can evolve with frontend needs
