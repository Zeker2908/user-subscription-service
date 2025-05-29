# User Subscription Service

Микросервис для управления пользователями и их подписками на цифровые сервисы.

## Технологии
- Java 17
- Spring Boot 3
- PostgreSQL
- Liquibase (миграции)
- MapStruct (маппинг DTO)
- Lombok
- SLF4J (логирование)

## API Endpoints

### Пользователи
- `POST /users` - Создать пользователя
- `GET /users` - Получить список пользователей (с пагинацией)
- `GET /users/{id}` - Получить пользователя по ID
- `PUT /users/{id}` - Обновить пользователя
- `DELETE /users/{id}` - Удалить пользователя

### Подписки
- `POST /users/{id}/subscriptions` - Добавить подписку пользователю
- `GET /users/{id}/subscriptions` - Получить подписки пользователя
- `DELETE /users/{userId}/subscriptions/{subscriptionId}` - Удалить подписку
- `GET /subscriptions/top` - Получить ТОП-3 популярных подписки

## Запуск

### Требования
- Docker
- Docker Compose

### Команды
```bash
docker-compose up --build
```

Сервис будет доступен на `http://localhost:8080`

## Конфигурация
Настройки подключения к БД и другие параметры можно изменить в файлах:
- `application-dev.yml` - для разработки
- `application-prod.yml` - для продакшена

## Логирование
Логи пишутся в консоль с разным уровнем детализации для dev/prod окружений.