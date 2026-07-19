# Dockerfile and Images

## What a Dockerfile is

A text file with step-by-step instructions to build an image. Each instruction creates a new
**layer**, and Docker caches layers — if a layer hasn't changed, Docker reuses the cached version
instead of rebuilding it. This is why instruction **order matters** for build speed.

## Key instructions

| Instruction | Purpose |
|---|---|
| `FROM` | Base image to build on top of (e.g. `FROM eclipse-temurin:21-jre-alpine`) |
| `WORKDIR` | Sets the working directory for subsequent instructions |
| `COPY` | Copies files from build context into the image |
| `ADD` | Like COPY, but can also fetch URLs and auto-extract tar files (prefer COPY unless you need those extras) |
| `RUN` | Executes a command **at build time**, result is baked into the image (e.g. `RUN mvn package`) |
| `CMD` | Default command run **at container start** — only one CMD takes effect, can be overridden at `docker run` |
| `ENTRYPOINT` | Like CMD but harder to override — use when the container should always run as that executable |
| `EXPOSE` | Documents which port the app listens on (doesn't actually publish it — that's `-p` at `docker run`) |
| `ENV` | Sets environment variables available at build and runtime |
| `ARG` | Build-time-only variable, not available in the running container |

**CMD vs ENTRYPOINT interview point:** `ENTRYPOINT` defines the fixed command, `CMD` provides
default *arguments* to it. Combine them: `ENTRYPOINT ["java","-jar","app.jar"]` with `CMD []` for
default args that a user can still override with `docker run image --some-flag`.

## Layer caching — why order matters

Put things that change **least often** first. Classic Java example:

```dockerfile
COPY pom.xml .
RUN mvn dependency:go-offline   # cached unless pom.xml changes
COPY src ./src
RUN mvn package                  # reruns every time source changes, but deps stay cached
```

If you copied `src` before resolving dependencies, every source change would force Maven to
re-download dependencies — much slower rebuilds.

## Multi-stage builds

Use multiple `FROM` statements in one Dockerfile so the final image only contains what's needed
to *run* the app, not build it.

```dockerfile
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
```

**Why this matters (interview answer):** the build stage has the full JDK + Maven (~600MB+), but
the final image only ships the JRE + your jar (~150-200MB). Smaller images mean faster deploys,
less attack surface, and lower registry storage costs.

## Image layers and `docker history`

Every instruction that modifies the filesystem creates a layer. Run `docker history <image>` to
see each layer and its size — a good way to spot bloat (e.g. leftover build tools, unnecessary
`apt-get` cache) that should be cleaned up in the same `RUN` line it was created in:

```dockerfile
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*
```

Doing the install and cleanup in **one** `RUN` keeps it in one layer — splitting into separate
`RUN` lines means the cleanup can't shrink a layer that was already committed.
