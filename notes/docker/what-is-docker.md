# What is Docker

Docker packages an application with everything it needs to run (code, runtime, libraries, config)
into a single **image**, and runs that image as an isolated **container**.

## Why it matters

- Solves "works on my machine" — the container runs the same everywhere
- Faster and lighter than VMs — containers share the host kernel instead of virtualizing hardware
- Standard unit of deployment for microservices and Kubernetes

## Key terms

- **Image** — read-only template built from a Dockerfile
- **Container** — a running instance of an image
- **Dockerfile** — instructions for building an image
- **Registry** — where images are stored (Docker Hub, GHCR, ECR, etc.)
