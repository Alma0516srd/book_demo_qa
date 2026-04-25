# Тестирование BookStore API

## Endpoints для тестирования

### 1. Проверка доступности (Ping)
```
GET /api/ping
Ожидаемый ответ: {"status": "UP", "timestamp": "2026-04-25T..."}
```

### 2. Книги
```
GET  /api/books              - Получить все книги
GET  /api/books/{id}         - Получить книгу по ID
GET  /api/books/search/author?author=...  - Поиск по автору
GET  /api/books/search/title?title=...     - Поиск по названию
GET  /api/books/low-stock    - Книги с остатком < 5
POST /api/books              - Создать книгу (body: JSON)
PUT  /api/books/{id}         - Обновить книгу
DELETE /api/books/{id}       - Удалить книгу
```

### 3. Заказы
```
GET  /api/orders             - Получить все заказы
GET  /api/orders/{id}        - Получить заказ по ID
POST /api/orders             - Создать заказ
```

## Тестовые сценарии

### Основные проверки
1. **Ping** - сервер отвечает, status = UP
2. **GET /api/books** - возвращает список (может быть пустой)
3. **POST /api/books** - создаёт книгу, возвращает 201
4. **GET /api/books/{id}** - получить конкретную книгу
5. **PUT /api/books/{id}** - обновить данные книги
6. **DELETE /api/books/{id}** - удалить книгу

### Проверки на ошибки
1. GET несуществующей книги -> 404
2. DELETE несуществующей книги -> 204 (even if not found - это баг)
3. POST с невалидными данными -> 400

### Тестовые данные для POST /api/books
```json
{
  "title": "Test Book",
  "author": "Test Author",
  "isbn": "1234567890123",
  "price": 19.99,
  "stock": 10
}
```

## Запуск приложения
```bash
./mvnw spring-boot:run
# или
docker-compose up
```

Приложение доступно на: http://localhost:8080
Swagger UI: http://localhost:8080/swagger-ui.html