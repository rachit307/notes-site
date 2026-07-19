# Docker — Additional Concepts

Gaps not covered in the other notes — hypervisors, namespace types, orchestration, registries,
and known limitations.

## Hypervisor types (context for "why containers don't need one")

- **Bare-metal / native hypervisor** — runs directly on host hardware, no underlying OS needed
  (e.g. VMware ESXi, Hyper-V). Used in real virtualization.
- **Hosted hypervisor** — runs as an application on top of an existing OS (e.g. VirtualBox, VMware
  Workstation).

Containers skip this layer entirely — no hypervisor at all, which is the core reason they start
faster and use fewer resources than VMs.

## The 5 Linux namespace types Docker uses

Each isolates one specific kind of resource so a container can't see or affect things outside it:

- **PID** — process IDs; a container's process 1 is invisible to other containers
- **Mount (mnt)** — filesystem mount points; container sees its own root filesystem
- **Network (net)** — network interfaces, IP addresses, routing tables
- **IPC** — inter-process communication (shared memory, semaphores) between processes
- **User** — maps container UIDs/GIDs to different host UIDs/GIDs, so "root" inside a container
  doesn't have to mean root on the host

(There's also a UTS namespace for hostname isolation — commonly grouped with these five.)

## Docker Swarm — brief comparison to Kubernetes

Docker's own built-in clustering tool — combines multiple Docker hosts into one virtual host,
exposing the standard Docker API so existing tooling still works. **Simpler to set up than
Kubernetes but far less capable** — fewer scheduling options, weaker self-healing, smaller
ecosystem. Almost all serious production setups use Kubernetes instead; Swarm mostly comes up in
interviews as a "here's the simpler alternative" comparison point, not something you'd choose for
a real system today.

## Container orchestration — what it actually automates

Beyond just "running containers," an orchestrator (Kubernetes, Swarm) handles:
- Scheduling containers onto available hosts based on resource needs
- Scaling replicas up/down based on load
- Restarting/rescheduling containers if a host fails or a container crashes
- Load balancing traffic across replicas
- Rolling updates and rollbacks with no downtime

This is the "why Kubernetes exists" answer in one paragraph — a single Docker host doing
`docker run` has none of this; you'd be building it all yourself with scripts otherwise.

## Registry vs Repository (commonly confused)

- **Registry** — the server/service that stores and distributes images (Docker Hub, GHCR, a
  private registry)
- **Repository** — a collection of related images within a registry, sharing a name but
  differentiated by tags (e.g. `myapp:1.0`, `myapp:2.0` are two tags in the `myapp` repository)

## Where volumes physically live on disk

Docker-managed named volumes are stored under `/var/lib/docker/volumes` on Linux hosts (a
different path on Windows). You generally shouldn't touch this directly — always go through
`docker volume` commands or mount syntax, since Docker tracks volume metadata separately.

## Known limitations of Docker (good for "any downsides?" questions)

- Containers don't hit true bare-metal performance — networking/isolation layers add some overhead
  compared to running directly on hardware
- Persistent data needs deliberate handling (volumes) — it's not automatic like a VM's disk
- GUI applications are awkward to containerize — Docker is designed around headless server
  processes
- Not every app benefits — the biggest wins are for apps designed as independent, stateless
  services; monolithic legacy apps see less benefit

## Monitoring Docker in production

Common tools that come up: **Prometheus + Grafana** (metrics + dashboards, probably the most
common open-source combo), **cAdvisor** (container-level resource metrics), the **ELK/Elastic
stack** (log aggregation — directly relevant given your Elasticsearch experience at Apple), and
commercial options like Datadog or Dynatrace for full-stack observability.
