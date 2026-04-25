# Book Order System

Spring Boot приложение для заказа книг с намеренными ошибками для обучения тестировщиков.

## Запуск

### Локально
```bash
mvn spring-boot:run
```

### Docker
```bash
docker-compose up --build
```

## API Endpoints

Swagger UI: http://localhost:8080/swagger-ui.html

### Books API
- `GET /api/books` - получить все книги
- `GET /api/books/{id}` - получить книгу по ID
- `POST /api/books` - создать книгу
- `PUT /api/books/{id}` - обновить книгу
- `DELETE /api/books/{id}` - удалить книгу
- `GET /api/books/search/author?author=` - поиск по автору
- `GET /api/books/search/title?title=` - поиск по названию
- `GET /api/books/low-stock` - книги с низким остатком

### Orders API
- `GET /api/orders` - получить все заказы
- `GET /api/orders/{id}` - получить заказ по ID
- `POST /api/orders` - создать заказ
- `PATCH /api/orders/{id}/status?status=` - обновить статус заказа
- `DELETE /api/orders/{id}` - отменить заказ

## Стек технологий
- Spring Boot 3.2
- Spring Data JPA
- H2 in-memory database
- SpringDoc OpenAPI (Swagger)
- Lombok
- Maven
