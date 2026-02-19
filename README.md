# Booking System API

RESTful microservice built with Spring Boot 4 that manages user data for a booking platform. The service stores documents in a MongoDB Atlas cluster and exposes CRUD endpoints that can be exercised from Postman or any HTTP client.

## Stack & Configuration

- **Runtime:** Java 17, Spring Boot 4.0.2
- **Persistence:** Spring Data MongoDB
- **Database:** `booking-system-db` on MongoDB Atlas
- **Required variable:** `MONGODB_URI` (`mongodb+srv://.../booking-system-db?retryWrites=true&w=majority&appName=Cluster0`)

```powershell
# PowerShell example
setx MONGODB_URI "mongodb+srv://<user>:<password>@cluster.mongodb.net/booking-system-db?retryWrites=true&w=majority&appName=Cluster0"
```

## How to Run

1. Clone the repository and move to the root `bookingSystem` folder.
2. Make sure `MONGODB_URI` is available in the shell (`Get-ChildItem Env:MONGODB_URI`).
3. Run `mvn clean spring-boot:run` (or `./mvnw spring-boot:run`).
4. The API will be available at `http://localhost:8081`.

## Endpoints

| Method | Path                 | Description              |
| ------ | -------------------- | ------------------------ |
| GET    | `/api/v1/users`      | Lists all users          |
| GET    | `/api/v1/users/{id}` | Fetches a user by id     |
| POST   | `/api/v1/users`      | Creates a new user       |
| PUT    | `/api/v1/users/{id}` | Updates an existing user |
| DELETE | `/api/v1/users/{id}` | Deletes a user           |

Sample JSON body:

```json
{
  "name": "Jane Doe",
  "email": "jane@example.com"
}
```

## Manual Test Evidence (Postman)

The CRUD flow was validated against the Atlas cluster with Postman 11. Each screenshot was taken after verifying that the `users` collection contained the expected documents.

- **POST** – user creation returning `201 Created`.
  ![POST](https://github.com/user-attachments/assets/4a11bd38-ff63-4e3d-a84e-ae3fb17e30a0)
- **GET (collection & detail)** – listing all users and fetching by id.
  ![GET](https://github.com/user-attachments/assets/ca1aff40-7523-4735-a20c-787d77f4f41a)
  ![GET detail](https://github.com/user-attachments/assets/90e7655f-4e11-4433-a233-5428500c0132)
- **PUT** – update returning `200 OK` with the modified fields.
  ![PUT](https://github.com/user-attachments/assets/ed328cf1-4eb6-4679-aa7f-ea2f4e11f910)
- **DELETE** – deletion returning `204 No Content`.
  ![DELETE](https://github.com/user-attachments/assets/78666272-d8db-4f0c-b0e9-167b71240c7b)

## Security Layer

- Added Spring Security + JWT to protect every business endpoint.
- Passwords are stored hashed (BCrypt) before persisting the `users` collection.
- JWT tokens are signed with HS256 (`jwt.secret`) and expire after `jwt.expiration` milliseconds (default 1h).

### Public authentication endpoints

| Method | Path                    | Description                                     |
| ------ | ----------------------- | ----------------------------------------------- |
| POST   | `/api/v1/auth/register` | Creates a user (no token required)              |
| POST   | `/api/v1/auth/login`    | Returns `{ "token", "expiresIn" }` for the user |

### How to call secured endpoints

1. `POST /api/v1/auth/register` with `{ "name", "email", "password" }` to create credentials.
2. `POST /api/v1/auth/login` with `{ "email", "password" }` to obtain the JWT.
3. Include header `Authorization: Bearer <token>` in all `/api/v1/users/**` requests.
4. On 401 responses, refresh the token (tokens expire after 1 hour by default).

## Evidence - Postman

First, a user is registered.
<img width="2116" height="1102" alt="Captura de pantalla 2026-02-16 204454" src="https://github.com/user-attachments/assets/94e5dc3b-0297-43c2-a1bf-337601126f05" />

Then log in with your credentials:

<img width="2116" height="1109" alt="Captura de pantalla 2026-02-16 205706" src="https://github.com/user-attachments/assets/3c7dffab-5677-4f80-86a0-2741262f72f4" />

And a query is made, indicating in the headers the authorization with the token generated previously during login.

<img width="2117" height="1165" alt="Captura de pantalla 2026-02-16 210038" src="https://github.com/user-attachments/assets/1bd7ea59-16fd-4b5a-9f25-afbb502ecd4e" />



## Unit Tests - Coverage


<img width="2341" height="1230" alt="image" src="https://github.com/user-attachments/assets/5e24d029-0353-4da3-9a28-67a3dd0d471a" />

The following image shows that coverage for each service is greater than 70%:

<img width="2879" height="1292" alt="image" src="https://github.com/user-attachments/assets/3dcce2e1-412c-41f9-922f-732b063a7a6f" />


## Evidence of endpoint test with deployment URL

<img width="2115" height="1315" alt="Captura de pantalla 2026-02-18 192401" src="https://github.com/user-attachments/assets/cb0c49a9-9854-448a-a1d6-360710d3fa54" />

<img width="2878" height="1626" alt="image" src="https://github.com/user-attachments/assets/babe119f-1300-4f72-bfd6-959278d79ca0" />

