# service-account
Account management service.

# Task

Реализовать приложение для работы со счетом клиента. Приложение должно поддерживать следующие ф-ии:
- Зачисление денежных средств на счет
- Списание денежных средств со счета
- Получение актуального остатка на счете

Функциональные ограничения:
Значение на счете клиента не может принимать отрицательные значения.

Требования к реализации:
- не использовать constraint базы данных при реализации функциональных ограничений
- покрытие основной функциональности unit-тестами
- фреймворк spring boot
- сборка gradle
- запуск приложения командной строкой (gradle, docker-compose)
- результат должен быть оформлен в виде git-репозитория с кодом приложения с инструкцией по развертыванию приложения и предоставлен в виде ссылки на этот репозиторий.

# Solution
Spring Boot application with efficient approach to handle requests (transaction isolation and caching).
Liquibase for db migration.
Dockerfile and docker compose for container support.
Unit tests for service layer. Gradle for build.

# API

##### Create an account for the client

Request:

```text
curl -X POST \
  http://localhost:8090/v1/accounts \
  -H 'content-type: application/json' \
  -d '{
  "clientId": "954d4b23-5096-41cd-91a1-c704054984ef",
  "accountBalance": 123.45
}'
```

`accountBalance` is optional, default is `0`. Can not be less than `0`.

Response:
```json
{
    "accountId": "92929076-2643-4b5a-9428-14f61bf6ed29"
}
```

##### Get account balance

Request:

```text
curl -X GET \
  http://localhost:8090/v1/accounts/92929076-2643-4b5a-9428-14f61bf6ed29/balance \
  -H 'content-type: application/json'
```
Response:
```json
{
  "accountBalance": 123.45
}
```


##### Update account balance: add or subtract
To add provide the amount without sign. To subtract provide the amount with '-' sign.

Request (add):

```text
curl -X PATCH \
  http://localhost:8090/v1/accounts/92929076-2643-4b5a-9428-14f61bf6ed29/balance \
  -H 'content-type: application/json' \
  -d '{
  "addToAccountBalance": 1.13
}'
```

Request (subtract):

```text
curl -X PATCH \
  http://localhost:8090/v1/accounts/92929076-2643-4b5a-9428-14f61bf6ed29/balance \
  -H 'content-type: application/json' \
  -d '{
  "addToAccountBalance": -1.13
}'
```

Response `204 No Content` when successful or `400 Bad Request` in case, when balance constraint violation occurs.

# How to run

`docker-compose up --build`

# Nice to have:
- Cover by unit tests controller layer (check validations);
- Set up logging;
- Localize errors;
- Security: authentication and authorization;
- Caching: move to Redis or similar. Current solution will not work correctly in multi instance mode.
- …