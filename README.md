# ThingsBoard Development Environment

A Dockerized development and production setup for the ThingsBoard IoT platform, restructured into independent `api` (Spring Boot) and `web` (Angular) services with an added modular API-versioning layer. It demonstrates containerized full-stack orchestration for a large Java/Angular monorepo and a pattern for running multiple API versions side by side.

## What's Inside

- **ThingsBoard 4.3.0** platform source, split into `api/` (Spring Boot backend) and `web/` (Angular 18 frontend)
- **`api/api-modules/`** — a versioned API layer (`core`, `v1`, `v2`) showing how multiple API versions can be maintained and upgraded independently as separate Maven modules
- **Docker Compose stacks** for development (hot reload, remote JVM debugging, pgAdmin, Kafka UI) and production
- **`docker/`** — additional compose variants and configs for Kafka, Valkey (cluster/sentinel), Cassandra, EDQS, Prometheus/Grafana, Nginx, HAProxy, and MQTT

## Tech Stack

- **Backend**: Java 17, Spring Boot, Maven (multi-module)
- **Frontend**: Angular 18, Angular Material, NgRx
- **Data**: PostgreSQL, Valkey (Redis-compatible cache)
- **Messaging**: Kafka, MQTT (Eclipse Mosquitto)
- **Infra**: Docker Compose, Nginx

## Quickstart

```bash
git clone <this-repo>
cd thingsboard-dev

# Development (hot reload + debug port)
docker-compose -f docker-compose.dev.yml up

# Production
docker-compose -f docker-compose.prod.yml up -d
```

| Service | URL |
|---------|-----|
| Web UI | http://localhost:4200 |
| API | http://localhost:8080 |
| Swagger | http://localhost:8080/swagger-ui.html |
| pgAdmin (dev) | http://localhost:5050 |
| Kafka UI (dev) | http://localhost:8090 |
| JVM debug (dev) | localhost:5005 |

Default login: `sysadmin@thingsboard.org` / `sysadmin`. JWT secrets are auto-generated and database migrations run on startup; change passwords before any production use.

## Structure

```
├── api/                    # Spring Boot backend
│   ├── application/        # Core ThingsBoard server
│   ├── api-modules/        # Versioned API layer (core, v1, v2)
│   ├── common/ dao/ rule-engine/ transport/ msa/  # ThingsBoard platform modules
│   └── Dockerfile          # Multi-stage (dev/prod)
├── web/                     # Angular frontend
│   └── Dockerfile           # Multi-stage (dev/prod)
├── docker/                  # Extra compose variants, nginx/mqtt/monitoring configs
├── docker-compose.dev.yml
└── docker-compose.prod.yml
```

## License

Apache License 2.0
