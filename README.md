# ThingsBoard Development Environment

[![CI](https://github.com/mortogo321/thingsboard-dev/actions/workflows/ci.yml/badge.svg)](https://github.com/mortogo321/thingsboard-dev/actions/workflows/ci.yml)
[![License](https://img.shields.io/badge/license-Apache--2.0-blue.svg)](LICENSE)
![Java 17](https://img.shields.io/badge/java-17-blue.svg)
![Angular 18](https://img.shields.io/badge/angular-18-red.svg)
![Docker Compose](https://img.shields.io/badge/docker-compose-pinned-blue.svg)

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

## Reproducibility

All infrastructure images are pinned (no `latest` tags) and CI validates both
compose stacks, builds + tests the versioned API layer, and builds the web
`deps`/`development` image stages on every push to `main`:

| Component | Pin | Why |
|-----------|-----|-----|
| PostgreSQL | `17.8-alpine` | Stable PG 17, matches platform support |
| Valkey | `9.0.6-alpine` | Redis-compatible cache |
| Kafka / Zookeeper | Confluent `7.9.10` | Bundles Kafka 3.9.x, matching `kafka-clients 3.9.1` in `api/pom.xml` |
| Mosquitto | `2.1.2-alpine` | MQTT broker |
| Kafka UI (dev) | `v0.7.2` | Topic inspection |
| pgAdmin (dev) | `9.18` | DB admin |
| API build/runtime | Temurin `17-jdk/jre-alpine` | ThingsBoard 4.3 targets Java 17 — floating JDK tags break the Maven build |
| Web build | `node:22-alpine` | Highest Node major officially supported by Angular 18 |
| Web runtime | `nginx:1.30.5-alpine-slim` | Static hosting + `/health` endpoint |

The full API production image (complete ThingsBoard Maven package, 8GB+ RAM /
30+ min) is intentionally outside CI — build it manually for releases with
`docker build -f api/Dockerfile --target production ./api`. Dependabot watches
Docker, Maven (`/api`), npm (`/web`), and GitHub Actions weekly.

## Known Limitations

- **Web production build**: `yarn build:prod` currently fails on 600+
  pre-existing AoT template errors in the vendored `web/` source (missing
  `@NgModule` annotations, unimported `formGroup`/`translate` usages across
  dashboard modules). Verified locally; the application sources are untouched
  by the infrastructure work above. Repairing the UI (upstream re-sync or
  incremental module fixes) is a separate workstream — CI pins the boundary by
  building the `deps` (proves `yarn install` resolves) and `development`
  stages only.

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
