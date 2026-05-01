# Database & Persistence Rules

## Query Policy

We use a high-level abstraction layer to interact with the database.
Native SQL queries bypass our safety checks and are not allowed.

- **Forbidden:** Native SQL strings, `@Query(nativeQuery = true)`, or direct JDBC statements.
- **Mandatory:** Use [Spring Data JPA Repositories].
