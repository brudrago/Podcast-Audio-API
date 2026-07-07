# Podcast Audio API

Backend API for managing podcast episodes and audio files, built with Clojure and PostgreSQL.

## Tech Stack

- Clojure
- Leiningen
- Compojure
- Ring
- Jetty
- PostgreSQL
- next.jdbc
- HikariCP
- Migratus
- Docker Compose

## Requirements

Make sure you have installed:

- Java 17+
- Leiningen
- Docker
- Docker Compose

## Environment Variables

Create your local `.env` file from the example file:

```bash
cp .env.example .env
```

Edit `.env` with your local database configuration:

```text
DATABASE_URL=jdbc:postgresql://localhost:5432/podcast_db
DATABASE_HOST=localhost
DATABASE_PORT=5432
DATABASE_NAME=podcast_db
DATABASE_USER=podcast_user
DATABASE_PASSWORD=podcast_password
```

The `.env` file contains local environment configuration and must not be committed.

The `.env.example` file documents the environment variables required to run the application and should be committed to the repository.

## Running the Database

Start PostgreSQL with Docker Compose:

```bash
docker compose up -d
```

Check if the container is running:

```bash
docker compose ps
```

You can also check the running containers with:

```bash
docker ps
```

## Installing Dependencies

Install the project dependencies:

```bash
lein deps
```

## Loading Environment Variables

The application reads the database configuration from environment variables.

Before running migrations, starting the REPL, or starting the application, load the variables from `.env`:

```bash
set -a
source .env
set +a
```

## Running Database Migrations

Make sure PostgreSQL is running and the environment variables are loaded.

Start the REPL:

```bash
lein repl
```

Load the migrations namespace:

```clojure
(require '[audio-api.database.migrations :as migrations])
```

Run the migrations:

```clojure
(migrations/migrate)
```

Exit the REPL:

```clojure
(exit)
```

## Running the Application

Make sure the environment variables are loaded:

```bash
set -a
source .env
set +a
```

Start the application:

```bash
lein run
```

The API will be available at:

```text
http://localhost:3000
```

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/bd-audio` | Health check |
| GET | `/bd-audio/episodes` | List all episodes |
| GET | `/bd-audio/episode/:id` | Get an episode by ID |
| POST | `/bd-audio/episodes` | Create an episode |

## Creating an Episode

Example request:

```http
POST /bd-audio/episodes
Content-Type: application/json
```

Request body:

```json
{
  "id": "44444444-4444-4444-4444-444444444449",
  "audioKey": "episodes/4/audio.mp3",
  "title": "API Architecture",
  "description": "An episode about backend architecture.",
  "durationSeconds": 300,
  "publishedAt": "2026-07-07T20:00:00"
}
```

Example response:

```json
{
  "id": "44444444-4444-4444-4444-444444444449",
  "audioKey": "episodes/4/audio.mp3",
  "title": "API Architecture",
  "description": "An episode about backend architecture.",
  "durationSeconds": 300,
  "publishedAt": "2026-07-07 20:00:00.0",
  "status": "draft"
}
```

## Error Responses

The API returns JSON responses for validation and application errors.

### Invalid Request

Example:

```json
{
  "error": "Title is required",
  "field": "title"
}
```

HTTP Status:

```text
400 Bad Request
```

### Invalid UUID

```json
{
  "error": "Invalid UUID"
}
```

HTTP Status:

```text
400 Bad Request
```

### Episode Already Exists

```json
{
  "error": "Episode already exists"
}
```

HTTP Status:

```text
409 Conflict
```

### Episode Not Found

```json
{
  "error": "Episode not found"
}
```

HTTP Status:

```text
404 Not Found
```

## Project Structure

```text
src/audio_api/
├── core.clj
├── routes.clj
├── handler.clj
│
├── database/
│   ├── connection.clj
│   └── migrations.clj
│
├── episodes/
│   ├── mapper.clj
│   ├── repository.clj
│   └── service.clj
│
└── middleware/
    └── error.clj
```

## Architecture

The application follows a layered architecture:

```text
HTTP Request
      |
      v
Routes
      |
      v
Handler
      |
      v
Mapper
      |
      v
Service
      |
      v
Repository
      |
      v
PostgreSQL
```

### Routes

Defines HTTP methods and endpoints.

### Handler

Handles HTTP requests and creates HTTP responses.

### Mapper

Converts between the external API representation and the internal application representation.

The API uses `camelCase`:

```text
durationSeconds
publishedAt
audioKey
```

The application uses Clojure `kebab-case` keywords:

```text
:duration-seconds
:published-at
:audio-key
```

PostgreSQL uses `snake_case`:

```text
duration_seconds
published_at
audio_key
```

### Service

Contains application rules, validations, and type conversions.

Examples:

```text
String -> UUID

String -> LocalDateTime
```

### Repository

Handles database access and SQL queries.

### Database

PostgreSQL stores episode metadata.

Audio files will be stored separately in object storage.

## Stopping the Database

Stop the containers:

```bash
docker compose down
```

The PostgreSQL data is preserved in the Docker volume.

To stop the containers and delete the database volume:

```bash
docker compose down -v
```

**Warning:** this command permanently removes the local database data.

## Development Status

Currently implemented:

- PostgreSQL running with Docker Compose
- Database connection with next.jdbc and HikariCP
- Database migrations with Migratus
- Environment-based database configuration
- Episode repository
- Episode service
- Episode validation
- Episode mapper
- Centralized error handling
- Health check endpoint
- Create episode endpoint
- List episodes endpoint
- Get episode by ID endpoint

Planned:

- Automated tests
- Update and delete episode endpoints
- Object storage integration
- Audio upload
- Audio download
- Integration with the Flutter mobile application
