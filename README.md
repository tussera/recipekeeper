# Recipe Keeper API

API that allows users to manager their favourite recipes.

### Features:
- Add
- Update
- Remove
- Search

### Available filter criteria:

- Whether or not the dish is vegetarian
- The number of servings
- Specific ingredients (either include or exclude)
- Text search within the instructions.

## Tech Stack

- Java 21
- Spring Boot (Web, JPA, Test)
- Maven
- Docker
- PostgreSQL
- Swagger

## Build

The project can be build with the following command:

`mvn clean install`

## Run

You can run the application using docker compose:

`docker compose up --build`

Swagger is available at:
http://localhost:8080/swagger-ui/index.html

## Have fun!