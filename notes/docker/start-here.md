# Start Here — How to Read These Docker Notes

If you're new to Docker, read in this order. Each note builds on the last, and any unfamiliar term
along the way is explained in **glossary.md** — keep it open in another tab.

1. **glossary.md** — skim once first so terms like "daemon," "kernel," and "namespace" aren't a
   surprise later. You don't need to memorize it — just know it's there to check back against.
2. **what-is-docker.md** — the big picture: what problem Docker solves and why it exists.
3. **architecture.md** — the main pieces (client, daemon, registry) and how a `docker run` command
   actually flows through the system, down to the Linux kernel.
4. **containers-and-lifecycle.md** — what a container actually is, how it differs from a virtual
   machine, and the states it moves through (created, running, stopped, removed).
5. **dockerfile-and-images.md** — how images are actually built, instruction by instruction, plus
   why multi-stage builds exist.
6. **compose-and-desktop.md** — running more than one container together, and what Docker Desktop
   is doing behind its friendly UI.
7. **commands-cheatsheet.md** — reference this once you're comfortable with the concepts above and
   want the actual commands to type.
8. **additional-concepts.md** — extra depth (namespace types, orchestration, registries vs
   repositories) — best read after the basics feel solid.
9. **interview-qa.md** — final review pass once everything else makes sense — these are the
   concise, "say this out loud in an interview" versions of everything above.

**A tip for absolute beginners:** don't try to memorize commands first. Understand *why* each
piece exists (what problem it solves) before worrying about the exact syntax — the commands become
much easier to remember once the underlying concept clicks.
