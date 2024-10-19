# Простая CRM-система

## Задача:
Разработать CRM-систему, которая будет управлять информацией о продавцах и их транзакциях. Система должна включать возможности для создания, чтения, обновления и удаления данных о продавцах и транзакциях. Также должна быть реализована аналитика для обработки и анализа данных.

## Функционал:
Данное приложение содержит следующий ф-л:
1) CRUD операции над продавцом.
2) CR** операции над транзакцией
3) Анализ информации о транзакциях
  * Самый успешный продавец в течении периода.
  * Самое продуктивное время продавца в период.
  * Список продавцов с сумоой меньше указанной  за выбранный период.

Приложение разработано с использованием Spring Boot и включает встроенные стартеры для ускорения разработки.

## Описание API
Приложение предоставляет REST API для работы с данными продавцов и транзакций, а также для выполнения аналитики. 
API не требует аутентификации. 
Для тестирования доступны такие инструменты, как Swagger и Postman.


## SellerController
### Операции над продавцом

| Метод        | Описание                                          | Пример запроса                                                                                 | Пример ответа                                                                                                                         |
|--------------|---------------------------------------------------|------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------|
| POST         | Создаем нового продавца                           | `POST /api/v1/seller`<br>```json { "name": "Vadim", "contactInfo": "+79234567890" }```          | ```json { "id": 2, "name": "Vadim", "contactInfo": "+79234567890", "registrationDate": "2024-10-19T03:51:30.751932" }```              |
| PATCH        | Обновляем информацию о продавце                   | `PATCH /api/v1/seller/1`<br>```json { "name": "Обновленный Продавец", "contactInfo": "newinfo@seller1.com" }``` | ```json { "id": 1, "name": "Обновленный Продавец", "contactInfo": "newinfo@seller1.com" }```                                          |
| GET /all     | Получаем список всех продавцов                    | `GET /api/v1/seller/all`                                                                        | ```json [ { "id": 1, "name": "Voody", "contactInfo": "user@example.com", "registrationDate": "2024-10-19T03:50:11.709405" }, ... ]``` |
| GET /{id}    | Получаем информацию о конкретном продавце         | `GET /api/v1/seller/1`                                                                          | ```json { "id": 1, "name": "Voody", "contactInfo": "info@seller1.com", "registrationDate": "2024-10-12T12:23:23" }```                 |
| DELETE /{id} | Удаляем продавца                                  | `DELETE /api/v1/seller/delete/1`                                                                | `200 Success`                                                                                                                         |

---

## TransactionController
### Операции над транзакциями

| Метод                       | Описание                                        | Пример запроса                                                                                      | Пример ответа                                                                                                          |
|-----------------------------|------------------------------------------------|-----------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------|
| POST                        | Создаем транзакцию                              | `POST /api/v1/transaction`<br>```json { "amount": 100, "date": "2023-10-01", "sellerId": 1, "paymentType": "CASH" }``` | ```json { "id": 2, "sellerId": "1", "paymentType": "CASH", "amount": 100, "transactionDate": "2024-10-19T04:31:25.121245" }``` |
| GET /all                    | Получаем список всех транзакций                 | `GET /api/v1/transaction/all`                                                                       | ```json [ { "id": 1, "sellerId": "1", "paymentType": "CASH", "amount": 100, "transactionDate": "2024-10-19T03:52:24.639844" }, ... ]```  |
| GET /all/{seller_id}         | Получаем список всех транзакций конкретного продавца | `GET /api/v1/transaction/all/1`                                                                    | ```json [ { "id": 1, "sellerId": "1", "paymentType": "CASH", "amount": 100, "transactionDate": "2024-10-19T03:52:24.639844" }, ... ]```  |
| GET /{trans_id}              | Получаем конкретную транзакцию                  | `GET /api/v1/transaction/1`                                                                         | ```json { "id": 1, "sellerId": "1", "paymentType": "CASH", "amount": 100, "transactionDate": "2024-10-19T03:52:24.639844" }```            |

---

## AnalyticController
### Аналитика о системе

| Метод         | Описание                                                        | Пример запроса                                                                  | Пример ответа                                                                                             |
|---------------|-----------------------------------------------------------------|---------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------|
| GET           | Получаем самого успешного продавца                              | `GET /api/v1/analytic?startDate=2024-01-01&endDate=2024-12-31`                   | ```json { "id": 1, "name": "Voodys", "contactInfo": "vsdsdfdsf@gmail.com", "sumAmount": 5000 }```          |
| GET /min      | Получаем список продавцов с суммой меньше указанной              | `GET /api/v1/analytic/min?startDate=2024-01-01&endDate=2024-12-31&min=1000`      | ```json [ { "id": 2, "name": "Vodys2", "contactInfo": "+79823343212", "sumAmount": 500 }, ... ]```         |
| GET /peak     | Получаем самое продуктивное время продавца                       | `GET /api/v1/analytic/peak?startDate=2024-10-01&endDate=2024-10-12&seller_id=1`  | ```json { "periodDate": "2024-10-12", "transactionCount": 150 }```                                        |

---

## Стек технологий:
- **Java** 17
- **Gradle** 8.2.1
- **Spring Boot** 3.2.2
- **JUnit** 5.10
- **Swagger** 3
- **PostgreSQL** 12.20
- **H2** 2.2.220
- **Hibernate** 6.4.1
- **Flyway** 9.22.3
- **Log4j2** 2.23.0

## Инструкция по сборке проекта и запуску:

1. **Настройка конфигураций базы данных**:
    - Откройте файл `application.yml` в разделе настроек базы данных.
    - Для локальной разработки оставьте стандартный порт PostgreSQL 5432:
      ```yaml
      spring:
        datasource:
          url: jdbc:postgresql://localhost:5432/crm
      ```
    - Для запуска в контейнере Docker измените порт на 5430:
      ```yaml
      spring:
        datasource:
          url: jdbc:postgresql://localhost:5430/crm
      ```

2. **Сборка проекта с Gradle**:
    - В терминале, находясь в корне проекта, выполните одну из команд:
      ```bash
      gradle clean build
      ```
      или
      ```bash
      ./gradlew clean build
      ```
    - После успешного выполнения сборки, результат будет находиться в директории:
      ```bash
      build/libs/ShiftCRM-1.0.jar
      ```

3. **Запуск проекта**:
    - Вы можете запустить скомпилированный JAR-файл напрямую, выполнив следующую команду:
      ```bash
      java -jar build/libs/ShiftCRM-1.0.jar
      ```
    - Также возможно запустить проект через IDE (IntelliJ IDEA) с помощью встроенного механизма запуска.

---

#### Запуск проекта в Docker:
Для запуска проекта в Docker, должен быть установлен Docker.

#### Сборка Docker вручную

1. **Подготовка Dockerfile**:
   Убедитесь, что в корне проекта есть файл `Dockerfile`. 

   Это Dockerfile создаст образ для приложения, используя JAR-файл из сборки Gradle.

2. **Сборка Docker-образа вручную**:
   В терминале, находясь в корне проекта, выполните следующую команду для создания образа:

   ```bash
   docker build -t shiftcrm-service .
   ```
   - `-t shiftcrm-service`: присваивает имя вашему образу.
   - `.`: указывает, что контекст для сборки находится в текущей директории.

3. **Создание docker-compose.yml**:
   Убедитесь, что в корне проекта также есть файл `docker-compose.yml`. 

   В этом проекте должны быть определены два сервиса: `postgres` для базы данных и `webapi` для вашего приложения.

4. **Запуск контейнеров**:
   - После создания файлов, выполните команду для запуска контейнеров:

     ```bash
     docker-compose up
     ```

   Эта команда создаст и запустит контейнеры для PostgreSQL и нашего сервиса.

5. **Проверка состояния контейнеров**:
   - Выполните команду для проверки списка запущенных контейнеров:

     ```bash
     docker ps
     ```

   - Приложение будет доступно по адресу:

     ```bash
     http://localhost:8081
     ```

#### Сборка и запуск Docker с использованием IntelliJ IDEA

   - Откройте проект в IntelliJ IDEA.
   - Найдите файл `Dockerfile` в структуре проекта.
   - Кликните правой кнопкой мыши на файл `Dockerfile` и нажмите `Run on Build`.
   - Откройте файл `docker-compose.yml` в IntelliJ IDEA.
   - Кликните правой кнопкой мыши и выберите `docker-compose up`. Это запустит контейнеры PostgreSQL и вашего приложения.

**Доступ к API через браузер**:
   - После запуска контейнеров вы можете получить доступ к API по адресу:

     ```bash
     http://localhost:8081
     ```

   - Swagger доступен по следующему пути:

     ```bash
     http://localhost:8081/swagger-ui/index.html
     ```
### Настройки

Перед запуском убедитесь, что в `application.yml` указаны корректные параметры для базы данных.

- **Локальная среда**: используйте порт 5432 для PostgreSQL.
- **Docker**: измените порт на 5430.

## Тестирование

Для тестирования API рекомендуется использовать инструменты:
- **Swagger** (http://localhost:8082/swagger-ui/index.html)
- **Postman** для отправки и тестирования запросов.

--- 
