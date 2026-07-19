# Docker Compose and Docker Desktop

## Docker Compose

A tool for defining and running **multi-container** applications with a single YAML file, instead
of manually running several `docker run` commands with matching network/volume flags. This is
exactly what you used for order-service + notification-service + Postgres + Kafka.

```yaml
services:
  app:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - db
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/mydb
  db:
    image: postgres:16
    environment:
      - POSTGRES_PASSWORD=secret
    volumes:
      - dbdata:/var/lib/postgresql/data

volumes:
  dbdata:
```

**Key things Compose handles for you automatically:**
- Creates a shared network so services can reach each other by service name (`db`, not an IP)
- `depends_on` controls **start order** — but not readiness. `db` container starting doesn't mean
  Postgres is accepting connections yet — that's why apps often need retry logic or a healthcheck
  dependency (`depends_on: db: condition: service_healthy`)
- One command (`docker compose up`) replaces a whole sequence of manual `docker run`/`docker network`
  commands

## Docker Desktop

The GUI application (Mac/Windows/Linux) that bundles:
- The Docker daemon (`dockerd`) running inside a lightweight VM (since Docker fundamentally needs a
  Linux kernel, and Mac/Windows don't have one natively)
- A visual dashboard — see running containers, images, volumes, logs, resource usage without
  memorizing CLI commands
- Built-in **Kubernetes** (single-node cluster) you can enable with one checkbox — handy for local
  testing, though you're already using minikube separately, which is more standard for realistic
  multi-node practice

**Interview-relevant point:** Docker Desktop is a *convenience layer* over the same `dockerd` +
containerd + runc stack we covered earlier — it's not a different underlying technology, just a
friendlier way to run it on non-Linux machines.

## Volumes vs bind mounts vs tmpfs (interview-level distinction)

| Type | Where it lives | Use case |
|---|---|---|
| **Named volume** | Docker-managed storage area | Persistent data (databases), portable across environments |
| **Bind mount** | Direct path on host filesystem | Local development — live-edit code/config from host |
| **tmpfs mount** | RAM only, never touches disk | Temporary/sensitive data that shouldn't persist at all |

## Networking modes (quick reference)

- **bridge** (default) — isolated internal network, containers reach each other via container name
  if on a user-defined bridge, or via `--link` (deprecated) on the default bridge
- **host** — container shares the host's network stack directly, no port mapping needed but no
  isolation either
- **none** — no networking at all
- **overlay** — for multi-host networking (Docker Swarm) — not usually relevant unless the company
  uses Swarm instead of Kubernetes
