# Digital Banking Platform (DevOps Training Project)

A Modular Monolith Spring Boot application simulating a bank's core services,
built to practice a full DevOps toolchain (Docker, CI/CD, Kubernetes,
Monitoring, Logging, Security, IaC) end to end.

## Stack
- Java 21, Spring Boot 4.1
- Spring Data JPA, Spring Security (JWT), Spring Validation
- PostgreSQL + Liquibase
- Redis
- Maven

## Modules (package-by-feature)
- `auth` — register/login, JWT issuing, Spring Security config
- `customer` — customer CRUD
- `account` — account opening, freeze/unfreeze, balances
- `transaction` — deposit / withdraw / transfer with pessimistic locking
- `notification` — stub, logs events (would become its own service later)
- `common` — base entity, API envelope, global exception handling

## Run locally with Docker Compose
```bash
docker compose up --build
```
This starts: app (8080), postgres (5432), redis (6379), nginx (80 → proxies to app).

Swagger UI: http://localhost:8080/swagger-ui.html

## API quick tour
1. `POST /api/auth/register` — create a login user
2. `POST /api/auth/login` — get a JWT
3. `POST /api/customers` — create a customer profile
4. `POST /api/accounts` — open an account for that customer
5. `POST /api/transactions/deposit` / `/withdraw` / `/transfer`

All endpoints except `/api/auth/**` need `Authorization: Bearer <token>`.

## Next steps (matches the Sprint plan)
- Sprint 2: tests (unit + integration with Testcontainers)
- Sprint 3: CI/CD pipeline (GitLab CI: validate/test/build/docker-build/security-scan/deploy)
- Sprint 4: Monitoring (Prometheus/Grafana) — actuator already exposes `/actuator/prometheus`
- Sprint 5: Logging (ELK or Loki+Promtail+Grafana)
- Sprint 6: Kubernetes manifests / Helm
- Sprint 7: Terraform for infra provisioning

## Versioning 
Working version: 1.4.0 
