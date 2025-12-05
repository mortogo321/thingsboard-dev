# ThingsBoard Development Environment

Custom ThingsBoard IoT platform with separate API (Spring Boot) and Web (Angular) for full customization.

## Prerequisites

- Docker & Docker Compose
- Node.js & Yarn (optional, for local web development)
- Java 17+ & Maven (optional, for local API development)

## Quick Start

### Development

```bash
docker-compose -f docker-compose.dev.yml up
```

| Service | URL |
|---------|-----|
| Web UI | http://localhost:4200 |
| API | http://localhost:8080 |
| Swagger | http://localhost:8080/swagger-ui.html |
| Debug | localhost:5005 (JVM remote debug) |
| pgAdmin | http://localhost:5050 |
| Kafka UI | http://localhost:8090 |

### Production

```bash
docker-compose -f docker-compose.prod.yml up -d
```

| Service | URL |
|---------|-----|
| Web UI | http://localhost:4200 |
| API | http://localhost:8080 |

## Project Structure

```
├── api/                        # Spring Boot backend
│   ├── Dockerfile              # Multi-stage (dev/prod)
│   ├── entrypoint.sh           # JWT gen, migrations, seeders
│   ├── .env.development
│   ├── .env.production
│   └── api-modules/            # Modular versioned APIs
│       ├── core/               # Shared components
│       ├── v1/                 # API v1 module
│       └── v2/                 # API v2 module
├── web/                        # Angular frontend
│   ├── Dockerfile              # Multi-stage (dev/prod)
│   ├── entrypoint.sh           # Auto dependency management
│   ├── .env.development
│   └── .env.production
├── docker/
│   ├── nginx/nginx.conf
│   └── mqtt/mosquitto.conf
├── docker-compose.dev.yml
└── docker-compose.prod.yml
```

## Services

| Service | Dev Port | Description |
|---------|----------|-------------|
| API | 8080 | ThingsBoard backend |
| Web | 4200 | ThingsBoard UI |
| PostgreSQL | 5432 | Database |
| Redis (Valkey) | 6379 | Cache |
| Kafka | 29092 | Message queue |
| Zookeeper | 2181 | Kafka coordination |
| MQTT | 1883, 9001 | IoT messaging |
| Kafka UI | 8090 | Kafka management |
| pgAdmin | 5050 | Database management |

## Development

### Hot Reload

Code changes auto-reload without rebuild:
- **API**: Spring Boot DevTools
- **Web**: Angular watch mode

```bash
docker-compose -f docker-compose.dev.yml up      # Start
docker-compose -f docker-compose.dev.yml down    # Stop
# Edit code, save - changes reflect automatically
```

### Web Dependencies

Auto-installs when:
- `node_modules` missing
- OS changed (host to container)
- `yarn.lock` modified

```bash
# Option 1: Let container handle it
docker-compose up

# Option 2: Install on host first
cd web && yarn install
docker-compose up   # Detects OS mismatch, reinstalls
```

### Remote Debugging

Connect IDE to `localhost:5005` for JVM debugging.

## API Versioning

Modular API versioning with independent module upgrades.

### Structure

```
api/api-modules/
├── pom.xml                 # Parent POM
├── core/                   # Shared: config, dto, exceptions
│   └── pom.xml            # v1.0.0
├── v1/                     # API v1: stable
│   └── pom.xml            # v1.0.0
└── v2/                     # API v2: latest features
    └── pom.xml            # v2.0.0
```

### Endpoints

| Version | Endpoint | Status |
|---------|----------|--------|
| v1 | `/api/v1/*` | Stable |
| v2 | `/api/v2/*` | Latest |

### Module Upgrade

Each version is an independent Maven module:

```xml
<!-- Upgrade v2 without affecting v1 -->
<dependency>
    <groupId>org.thingsboard.api</groupId>
    <artifactId>api-v1</artifactId>
    <version>1.0.0</version>
</dependency>
<dependency>
    <groupId>org.thingsboard.api</groupId>
    <artifactId>api-v2</artifactId>
    <version>2.1.0</version>  <!-- Upgraded independently -->
</dependency>
```

### Configuration

```properties
# .env
API_ENABLED_VERSIONS=v1,v2    # Active versions
API_DEFAULT_VERSION=v1        # Default for /api/*
API_DEPRECATED_VERSIONS=      # Show deprecation warnings
```

### Adding New Version

1. Create module: `api/api-modules/v3/`
2. Add pom.xml with parent reference
3. Implement controllers under `/api/v3/*`
4. Add to `API_ENABLED_VERSIONS=v1,v2,v3`

## Environment Variables

### API

| Variable | Description |
|----------|-------------|
| APP_ENV | development / production |
| DB_HOST | PostgreSQL host |
| REDIS_HOST | Redis host |
| KAFKA_BOOTSTRAP_SERVERS | Kafka servers |
| JWT_SECRET | Auto-generated if empty |
| API_ENABLED_VERSIONS | Enabled API versions (v1,v2) |
| API_DEFAULT_VERSION | Default API version |
| API_DEPRECATED_VERSIONS | Deprecated versions (warnings)

### Web

| Variable | Description |
|----------|-------------|
| VITE_API_URL | API endpoint |
| VITE_WS_URL | WebSocket endpoint |
| VITE_MQTT_HOST | MQTT broker host |

## Commands

```bash
# Start
docker-compose -f docker-compose.dev.yml up
docker-compose -f docker-compose.prod.yml up -d

# Stop
docker-compose -f docker-compose.dev.yml down

# Logs
docker-compose -f docker-compose.dev.yml logs -f api
docker-compose -f docker-compose.dev.yml logs -f web

# Rebuild (after Dockerfile changes)
docker-compose -f docker-compose.dev.yml up --build

# Reset data
docker-compose -f docker-compose.dev.yml down -v
```

## Default Credentials

| Role | Email | Password |
|------|-------|----------|
| System Admin | sysadmin@thingsboard.org | sysadmin |
| Tenant Admin | tenant@thingsboard.org | tenant |

## Production Notes

- Update passwords in `api/.env.production`
- JWT_SECRET auto-generates on first run
- Migrations run on every startup
- Seeders run only in non-production

## License

Apache License 2.0
