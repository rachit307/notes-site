# Docker Interview Q&A

Quick-reference answers — expand on any of these verbally with an example from your own projects
(Order Notification System, minikube deployment, or this notes site) if asked to elaborate.

**Q: What is Docker and why use it?**
Packages an app with its runtime and dependencies into a portable image, so it runs identically
everywhere. Solves "works on my machine," lighter and faster than VMs since containers share the
host kernel instead of virtualizing hardware.

**Q: Difference between an image and a container?**
An image is a read-only template (built from a Dockerfile). A container is a running instance of
that image, with its own writable layer on top.

**Q: What is a Dockerfile?**
A text file of instructions (`FROM`, `COPY`, `RUN`, `CMD`, etc.) used to build an image, layer by
layer, with caching between layers that haven't changed.

**Q: CMD vs ENTRYPOINT?**
`ENTRYPOINT` sets the fixed executable; `CMD` provides default arguments that can be overridden at
`docker run`. Combine both for a command with overridable default flags.

**Q: What's a multi-stage build and why use one?**
Multiple `FROM` statements in one Dockerfile — an early stage has full build tools (e.g. Maven +
JDK), the final stage copies only the compiled artifact into a minimal runtime image (e.g. JRE
only). Shrinks final image size significantly and avoids shipping build tools to production.

**Q: How does Docker achieve isolation without a hypervisor?**
Via Linux kernel **namespaces** (isolate what a process can see — PIDs, network, mounts, hostname)
and **cgroups** (limit what it can use — CPU, memory, I/O). A container is just a sandboxed host
process, not a separate virtual machine.

**Q: What happens internally when you run `docker run`?**
CLI sends a REST request to `dockerd` → `dockerd` delegates to `containerd` → `containerd` spawns a
`containerd-shim` (keeps the container alive if the daemon restarts) → `runc` reads the OCI bundle,
sets up namespaces/cgroups via kernel syscalls, execs the process, then exits.

**Q: How do you persist data across container restarts?**
Use a **volume** (Docker-managed) or **bind mount** (host path). Anything written to a container's
own writable layer is lost when the container is removed.

**Q: How do containers on the same host talk to each other?**
Put them on the same user-defined bridge network — Docker's embedded DNS lets them resolve each
other by container/service name, no hardcoded IPs needed.

**Q: What's the difference between `docker stop` and `docker kill`?**
`stop` sends `SIGTERM` (graceful shutdown), waits a grace period (default 10s), then sends
`SIGKILL` if the process hasn't exited. `kill` sends `SIGKILL` immediately — no graceful shutdown.

**Q: How do you reduce Docker image size?**
Use a slim/alpine base image, multi-stage builds, combine `RUN` commands to avoid extra layers,
clean up package manager caches in the same layer they were created, and use `.dockerignore` to
keep unnecessary files out of the build context.

**Q: What's the difference between `COPY` and `ADD`?**
Functionally similar for local files, but `ADD` also auto-extracts tar archives and can fetch
remote URLs. Best practice: prefer `COPY` unless you specifically need one of those extras — it's
more predictable.

**Q: How would you debug a container that keeps crashing on startup?**
`docker logs <container>` first for the error. If it exits before logs are useful, try
`docker run -it --entrypoint /bin/sh <image>` to get a shell inside without running the normal
entrypoint, and manually run the startup command to see the full error.

**Q: Docker Swarm vs Kubernetes?**
Both orchestrate containers across multiple hosts. Swarm is simpler, built into Docker, easier to
set up but far less feature-rich. Kubernetes is the industry standard — more complex, but handles
scaling, self-healing, rolling updates, and config management far more robustly. Almost all modern
production setups (including what you're learning on minikube) use Kubernetes.
