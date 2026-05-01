# Code Style Guidelines

## Null Safety

Direct null comparisons are strictly prohibited.

- **Forbidden:**
    - `if (obj == null)` or `if (obj != null)`
    - `if (a.equals(b))`
- **Mandatory:** Use `java.util.Objects` methods:
    - Use `Objects.isNull(variable)` for null checks.
    - Use `Objects.nonNull(variable)` for non-null checks.

## Imports

- Always ensure `java.util.Objects` is imported.

## Types and Primitives

Use the following logic to choose between primitives and wrappers:

- **Use wrappers (Long, Boolean):**
    - For all database entities (fields that can be NULL in the DB).
    - For DTOs and API Request/Response objects.
    - When a value is optional or represents a "missing" state.
- **Use Primitives (long, boolean, int):**
    - For local variables inside high-performance loops or calculations.
    - For internal logic where a value is strictly required and has a sensible default (e.g., counters starting at 0).
    - For constants that never represent a "null" state.

## Final Modifiers

To ensure immutability and prevent accidental reassignment, use the `final` keyword for method parameters and local variables in
implementation classes.

- **Mandatory `final` for parameters:**
    - In **Controllers** (methods handling requests).
    - In **Service Implementations** (classes marked with `@Service`).
    - In any internal logic/methods within regular classes.
- **Strictly Forbidden `final` in parameters:**
    - In **Interfaces** (Service interfaces, etc.).
    - In **Repository** method signatures.
- **Local Variables:**
    - Use `final` for local variables whenever they are not intended to be reassigned.

