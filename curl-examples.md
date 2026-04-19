# Curl Examples

Base URL:

```bash
http://localhost:8080
```

## Authors

```bash
curl -X POST http://localhost:8080/api/authors \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Naguib","lastName":"Mahfouz","nationality":"Egyptian"}'
```

```bash
curl "http://localhost:8080/api/authors?page=0&size=10&sort=lastName,asc"
```

```bash
curl http://localhost:8080/api/authors/1/books
```

## Books

```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{"title":"Cairo Trilogy","isbn":"ISBN-5001","genre":"Novel","publishedYear":"1956","authorId":1}'
```

```bash
curl "http://localhost:8080/api/books?page=0&size=10&sort=title,asc"
```

```bash
curl "http://localhost:8080/api/books/search?title=Cairo&genre=Novel&publishedYear=1956"
```

## Members

```bash
curl -X POST http://localhost:8080/api/members \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Ali","lastName":"Hassan","email":"ali@example.com","phoneNumber":"01000000000"}'
```

```bash
curl "http://localhost:8080/api/members?page=0&size=10&sort=lastName,asc"
```

```bash
curl "http://localhost:8080/api/members/search?name=Ali"
```

## Borrow Records

```bash
curl -X POST http://localhost:8080/api/borrow-records \
  -H "Content-Type: application/json" \
  -d '{"memberId":1,"bookId":1}'
```

```bash
curl -X PUT http://localhost:8080/api/borrow-records/1/return
```

```bash
curl http://localhost:8080/api/borrow-records/member/1
```

```bash
curl http://localhost:8080/api/borrow-records/active
```
