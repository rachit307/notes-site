# Containers and Lifecycle

## Containers vs VMs (classic interview question)

| | Container | Virtual Machine |
|---|---|---|
| Isolation | Process-level, via Linux namespaces + cgroups | Full hardware virtualization via a hypervisor |
| Kernel | Shares the host's kernel | Runs its own full guest OS + kernel |
| Startup time | Milliseconds to seconds | Seconds to minutes |
| Size | MBs (just app + deps) | GBs (full OS) |
| Density | Many containers per host | Fewer VMs per host (more overhead each) |

**One-line answer:** a container is just a regular host process, sandboxed with namespaces
(isolation) and constrained with cgroups (resource limits) — no hypervisor, no separate kernel.

## Container lifecycle states

```
created -> running -> paused (optional) -> stopped -> removed
```

- **created** — `docker create` — container exists but hasn't started
- **running** — `docker start` / `docker run` — process is executing
- **paused** — `docker pause` — process frozen (SIGSTOP), memory still allocated
- **stopped/exited** — `docker stop` (graceful, sends SIGTERM then SIGKILL after timeout) or the
  process exited on its own
- **removed** — `docker rm` — container's filesystem and metadata deleted

## Container filesystem

A container's writable layer sits on top of its image's read-only layers (union filesystem).
**Anything written inside a running container is lost when the container is removed**, unless
it's in a mounted **volume**. This is why containers are meant to be treated as disposable/stateless
— persistent data belongs in a volume or external database, not the container's own filesystem.

## Restart policies

Set with `--restart` on `docker run`, controls what happens if a container exits:

- `no` — default, never auto-restart
- `on-failure[:max-retries]` — restart only if it exits with a non-zero code
- `always` — always restart, even after a manual stop (until explicitly removed)
- `unless-stopped` — like `always`, but won't restart if you manually stopped it

## Health checks

A `HEALTHCHECK` instruction in the Dockerfile (or `--health-cmd` at runtime) lets Docker
periodically verify the app inside is actually working, not just that the process is alive:

```dockerfile
HEALTHCHECK --interval=30s --timeout=3s CMD curl -f http://localhost:8080/actuator/health || exit 1
```

Orchestrators (Kubernetes, Docker Swarm) use this to decide whether to route traffic to a
container or restart it.
