# Library Management System

Spring Boot project for managing:
- Authors
- Books
- Members
- Borrow / Return operations

## Prerequisites

- Java 17
- Maven Wrapper (included as `mvnw` / `mvnw.cmd`)

## Team

| Full Name | ID Number |
|---|---:|
| Nayera Sherif | 45 |
| Samar Hatem | 22 |
| Shahd Ayman | 25 |
| Rahma Fathy | 18 |
| Haneen Mohamed | 16 |

## Project Idea

The system models a library lifecycle:
1. Create Author
2. Create Book and link it to Author
3. Create Member
4. Member borrows an available Book
5. Member returns the Book

When a book is borrowed, it becomes unavailable. When returned, it becomes available again.

## Main Domain Flow

### Entities
- `Author` -> has many `Book`
- `Book` -> belongs to one `Author`
- `Member` -> has many `BorrowRecord`
- `BorrowRecord` -> links one `Member` with one `Book` and stores borrow/return dates

### Borrow Flow
1. Validate member exists
2. Validate book exists
3. Validate book is available
4. Validate no active duplicate borrow for the same member/book
5. Create borrow record
6. Mark book as unavailable

### Return Flow
1. Validate borrow record exists
2. Validate it is not already returned
3. Set return date and returned flag
4. Mark book as available

## Business Rules Implemented

- Cannot borrow unavailable book
- Cannot create duplicate active borrow for same member and book
- Cannot create/update book with non-existing author
- Duplicate member email is rejected
- Duplicate ISBN is rejected

## Exception Handling Strategy

Centralized in `GlobalExceptionHandler` with clear HTTP mapping:
- `404 Not Found` for missing resources
- `409 Conflict` for duplicate resources and operation conflicts
- `400 Bad Request` for validation and malformed requests
- `500 Internal Server Error` fallback

Custom exceptions include:
- `ResourceNotFoundException`
- `DuplicateResourceException`
- `OperationConflictException`

## N+1 Analysis

The endpoint most affected by N+1 risk is fetching books with their author details (`GET /api/books`) and fetching borrow records with member/book details. A naive lazy loading flow can trigger one query for the main list and additional queries per row for related entities. We resolved this by using `@EntityGraph` in repositories and `LEFT JOIN FETCH` for author-with-books retrieval, reducing query count and stabilizing integration behavior.

## N+1 Problem: What It Is

The N+1 query problem happens when:
1. One query fetches a list of parent entities
2. Then extra query runs for each row to fetch related data

Example:
- Load 10 books (1 query)
- Load each book's author lazily (10 more queries)
- Total = 11 queries

This hurts performance as data grows.

## How We Solved N+1

We solved it using fetch optimization at repository level:

1. `@EntityGraph` for eager loading needed relationships in one optimized query path
2. `JOIN FETCH` query where needed for author-with-books retrieval

Applied in:
- `BookRepository` using `@EntityGraph(attributePaths = {"author"})`
- `BorrowRecordRepository` using `@EntityGraph(attributePaths = {"member", "book"})`
- `AuthorRepository.findByIdWithBooks(...)` using `LEFT JOIN FETCH`

Result:
- Fewer database round-trips
- Reduced risk of lazy-loading issues in mapping/serialization
- More stable integration behavior

## Integration Testing

We added integration tests to validate end-to-end logic:

- `LibraryApplicationTests` (service-oriented integration checks)
- `ProjectLogicL1Test` (API-level workflow tests)

Covered scenarios:
- Context loads
- Create Author + Book
- Borrow Book flow
- Return Book flow
- Conflict/validation/not-found scenarios

## Run and Test

### Run application (Windows)

```bat
.\mvnw.cmd spring-boot:run
```

### H2 Console (step-by-step)

1. Start the application using `./mvnw.cmd spring-boot:run`
2. Open `http://localhost:8080/h2-console`
3. Use JDBC URL: `jdbc:h2:mem:librarydb`
4. Username: `sa`
5. Password: (leave empty)
6. Click Connect

### Run all tests

```bat
.\mvnw.cmd test
```

### Run API integration test only

```bat
.\mvnw.cmd -Dtest=ProjectLogicL1Test test
```

## API Modules

- `/api/authors`
- `/api/books`
- `/api/members`
- `/api/borrow-records`

This structure keeps modules decoupled while still integrated through business workflows.

## API Testing Artifacts

- Postman collection: `postman/library-management.postman_collection.json`
- Curl examples: `curl-examples.md`
- Group info: `groups.txt`
