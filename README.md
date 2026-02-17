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
