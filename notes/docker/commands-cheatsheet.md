# Docker Commands Cheat Sheet

## Images

```bash
docker build -t myapp:1.0 .          # build image from Dockerfile in current dir
docker images                        # list local images
docker rmi myapp:1.0                 # remove an image
docker pull nginx:latest             # download image from registry
docker push myuser/myapp:1.0         # upload image to registry
docker history myapp:1.0             # show layers and their sizes
docker tag myapp:1.0 myuser/myapp:1.0 # add a new tag to an existing image
```

## Containers

```bash
docker run -d -p 8080:8080 --name notes myapp:1.0   # run detached, map host:container port
docker ps                            # list running containers
docker ps -a                         # list all containers, including stopped
docker stop notes                    # graceful stop (SIGTERM, then SIGKILL after timeout)
docker start notes                   # start a stopped container
docker restart notes
docker rm notes                      # remove a stopped container
docker logs -f notes                 # follow logs live
docker exec -it notes /bin/sh        # open a shell inside a running container
docker inspect notes                 # full JSON metadata (IP, mounts, env, etc.)
docker stats                         # live CPU/memory usage per container
```

## Volumes

```bash
docker volume create mydata
docker volume ls
docker run -v mydata:/app/data myapp:1.0   # named volume, managed by Docker
docker run -v $(pwd)/data:/app/data myapp  # bind mount, maps a host path directly
docker volume rm mydata
```

**Named volume vs bind mount:** a named volume is managed by Docker (lives under Docker's storage
area, portable, easier to back up); a bind mount points directly at a path on your host filesystem
(useful for local dev — edit a file on the host, see it reflected instantly in the container).

## Networks

```bash
docker network create mynet
docker network ls
docker run --network mynet --name db postgres
docker run --network mynet --name app myapp   # 'app' can reach 'db' by name: db:5432
docker network inspect mynet
```

Containers on the same **user-defined bridge network** can resolve each other by container name —
this is how Compose services talk to each other without hardcoded IPs.

## Cleanup

```bash
docker system prune             # remove stopped containers, unused networks, dangling images
docker system prune -a          # also remove all unused images, not just dangling ones
docker container prune          # remove all stopped containers
docker image prune -a           # remove all unused images
docker volume prune             # remove unused volumes (careful — this can delete data)
```

## Docker Compose

```bash
docker compose up -d            # start all services in docker-compose.yml, detached
docker compose down             # stop and remove containers, networks (add -v to also remove volumes)
docker compose logs -f service-name
docker compose build            # rebuild images defined in compose file
docker compose ps
```
