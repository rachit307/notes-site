# Docker architecture and internals

## The three main pieces

- **Docker client** — the CLI. Sends REST API requests to the daemon; does no actual work itself.
- **Docker daemon (`dockerd`)** — runs on the host, manages images, containers, networks, volumes.
- **Registry** — stores images (Docker Hub, GHCR, private registries). `docker pull` / `docker push`.

## What happens internally on `docker run`

1. **CLI** sends the request to `dockerd` over its REST API.
2. **dockerd** resolves the image (pulling if needed) and delegates to **containerd**.
3. **containerd** manages the container lifecycle and spawns a **containerd-shim** per container.
4. The **shim** becomes the container's parent process — so it survives even if `dockerd`/`containerd` restart.
5. **runc** does the actual work: reads an OCI-spec bundle, calls Linux syscalls to set up
   **namespaces** (isolation) and **cgroups** (resource limits), execs the process, then exits.

## Key insight

A container is **not a VM**. There's no hypervisor and no separate kernel — it's a normal Linux
process sandboxed with namespaces and constrained with cgroups. That's why containers start in
milliseconds while VMs take much longer.
