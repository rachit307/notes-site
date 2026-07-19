# Docker Glossary — Plain-Language Explanations

Every somewhat-technical term used across these Docker notes, explained simply. If a word in
another note feels unfamiliar, it's probably defined here.

**Kernel** — the core part of an operating system that talks directly to the hardware (CPU,
memory, disk) and lets every other program run on top of it. Every program on your computer,
including Docker containers, ultimately depends on the kernel to actually do anything.

**Process** — a running instance of a program. When you double-click an app or run a command,
the OS creates a process for it. A container, underneath everything, is just a specially
restricted process.

**Daemon** — a program that runs continuously in the background, without you directly watching or
typing into it, waiting to handle requests. Think of it like a receptionist who's always at their
desk, ready to help whenever someone asks — you don't watch them work, you just get results when
you ask.

**Host / host machine** — the actual physical or virtual computer that Docker (or anything else)
is running on. "The host" just means "your computer" or "the server," as opposed to something
running inside a container.

**Hypervisor** — special software that lets one physical computer pretend to be several separate
computers, each with its own fake CPU, memory, and disk. This is how virtual machines work.
Containers do **not** use a hypervisor — that's the key difference.

**Virtual Machine (VM)** — a complete simulated computer, created using a hypervisor, that runs
its own full operating system as if it were a separate physical machine. Heavier and slower to
start than a container, but more isolated.

**REST API** — a common way for two programs to talk to each other over a network, using standard
web requests (the same style your browser uses to load a webpage). When we say "the Docker CLI
sends a REST API request to the daemon," it just means: the command-line tool sends a structured
message asking the background program to do something.

**CLI (Command-Line Interface)** — a way of controlling a program by typing text commands into a
terminal, instead of clicking buttons in a graphical app. `docker run`, `git push`, etc. are all
CLI commands.

**Syscall (system call)** — the specific, low-level way any program asks the kernel to do
something it can't do on its own — like creating a new process, opening a file, or using memory.
You rarely write these yourself, but tools like `runc` use them directly to build containers.

**Namespace (Linux namespace)** — a kernel feature that lets a process have its own private "view"
of something, without actually being separate hardware. E.g., a container's **network namespace**
means it can have its own IP address, distinct from the host's — even though it's still running on
the same machine.

**cgroup (control group)** — a kernel feature that limits how much of a resource (CPU, memory) a
process is allowed to use. This is what stops one container from hogging all the RAM on a shared
machine.

**Isolation** — a general word for "keeping one thing from seeing or affecting another." When we
say a container is "isolated," we mean it can't see other containers' files, processes, or network
traffic, even though they're all running on the same physical machine.

**Image** — a read-only, pre-packaged snapshot of an application and everything it needs to run
(code, tools, settings). Think of it like a recipe or a frozen meal — it's the "template" you use
to actually make and run something.

**Container** — a running (or stopped) instance made from an image — like taking that frozen meal
and actually cooking/serving it. Multiple containers can be started from the same single image.

**Layer** — one step of changes recorded while building an image (e.g. "these files were added,"
"this package was installed"). Docker stacks layers on top of each other to build the final image,
and can reuse unchanged layers instead of redoing that work — similar to how you might reuse
leftover prep work when cooking a similar recipe again instead of starting from scratch.

**Registry** — an online storage service for images, similar in spirit to how GitHub stores code
repositories, but for Docker images instead. Docker Hub is the default, most well-known one.

**OCI (Open Container Initiative)** — an industry standard that defines exactly how a container
image and container runtime should behave, so that different tools (Docker, other container
engines) can all work together predictably, the same way a USB standard lets different brands'
cables and devices work together.

**Runtime (container runtime)** — the actual software responsible for starting and running a
container, once an image has been decided on. `runc` is the low-level runtime Docker uses
underneath.

**Orchestration / orchestrator** — a system (like Kubernetes) that manages many containers running
across many machines automatically — deciding where they run, restarting them if they crash, and
scaling them up or down as needed. Without one, you'd have to do all of that manually yourself.

**YAML** — a plain-text file format used for configuration (like `docker-compose.yml` or GitHub
Actions workflow files), designed to be easier for humans to read and write than something like
JSON. Indentation (spacing) matters a lot in YAML — misaligned spaces are a very common bug source.

**Environment variable** — a named value that a program can read to configure its own behavior,
set from outside the program itself, without changing its code (e.g. `PORT=8080`, or a database
password). Used constantly in Docker to configure containers without rebuilding the image.

**Port / port mapping** — a number a network service listens on to accept connections (e.g. web
servers commonly use port 8080 or 80). "Port mapping" (`-p 8080:8080` in Docker) means: requests
coming to this port on the host machine should be forwarded into the container's matching port.

**Bind mount vs Volume** — both let a container access files outside its own throwaway filesystem.
A **bind mount** points directly at a specific folder on your actual computer. A **volume** is
storage that Docker manages for you in its own dedicated location, better suited for data you want
to keep long-term and move between environments.

**CI/CD (Continuous Integration / Continuous Deployment)** — the practice of automatically
building, testing, and (optionally) deploying code every time it changes, instead of doing those
steps by hand. Covered in more depth in the CI/CD notes.
