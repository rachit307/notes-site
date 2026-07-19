# notes-site

Personal interview-prep notes site. Spring Boot app that reads markdown files from
`notes/<category>/<note>.md` and serves them with sidebar navigation.

## Run locally

```bash
mvn spring-boot:run
```

Then open http://localhost:8080

## Add a note

Drop a `.md` file into `notes/<category>/`. The first `# Heading` line becomes the
title. If there's no heading, the filename is used (dashes become spaces). No code
changes needed — the sidebar rebuilds from disk on every request.

```
notes/
  docker/
    what-is-docker.md
    architecture.md
  kubernetes/
    pods-and-services.md
  java/
    design-patterns.md
```

