# Docker

> Prerequisite: [Docker Desktop](https://www.docker.com/products/docker-desktop/)

__(Commands are based on repository root)__

## 1. Container

* [Dockerfile](../Dockerfile)
* Uses production profile: [application-prod.properties](../src/main/resources/application-prod.properties)

## 2. Run Application Stack

### 2.1. Run by building the current code

__Usage:__ Local deployment of development images

[docker-compose-build.yml](../.docker/docker-compose-build.yml)

```shell
docker compose --file .docker/docker-compose-build.yml up --build
```

Accessible at [http://localhost:8080](http://localhost:8080).

### 2.2. Run with GitHub Packages (Port 8080)

__Usage:__ Local deployment of production images

[docker-compose.yml](../docker-compose.yml)

__Pull & Run the stack:__

```shell
docker compose pull
docker compose up
```

Accessible at [http://localhost:8080](http://localhost:8080).

### 2.3. Run with GitHub Packages (Port 80)

__Usage:__ IaaS deployment of production images

[docker-compose-prod.yml](../.docker/docker-compose-prod.yml)

```shell
docker compose --file .docker/docker-compose-prod.yml up
```

Accessible at [http://localhost:80](http://localhost:80).

## 3. Build & Run single Containers

### 3.1. API

[Dockerfile](../Dockerfile)

```shell
docker build -t  data-provider .
docker run -p 8080:8080 --name provider-app data-provider
```

### 3.2. Database

```shell
docker run -p 3306:3306 -e MYSQL_ROOT_PASSWORD="password" mysql-db mysql:8.0
```
