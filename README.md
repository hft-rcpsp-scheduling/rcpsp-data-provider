[![Continuous-Integration](https://github.com/hft-rcpsp-scheduling/rcpsp-data-provider/actions/workflows/ci.yml/badge.svg)](https://github.com/hft-rcpsp-scheduling/rcpsp-data-provider/actions/workflows/ci.yml)
[![Docker-Image](https://github.com/hft-rcpsp-scheduling/rcpsp-data-provider/actions/workflows/docker-image.yml/badge.svg)](https://github.com/hft-rcpsp-scheduling/rcpsp-data-provider/actions/workflows/docker-image.yml)
[![Deployment](https://github.com/hft-rcpsp-scheduling/rcpsp-data-provider/actions/workflows/deployment.yml/badge.svg)](https://github.com/hft-rcpsp-scheduling/rcpsp-data-provider/actions/workflows/deployment.yml)

# RCPSP Data Provider

This application provides an API to __serve raw data (projects)__ and __validate solutions__. It should build the
infrastructure for scheduling-algorithm implementations and defines the input- and output-format. In addition, the
application can store solutions for later use.

Please read the [Container Section](#Container) to run the application and
the [Development Documentation](.doc/development.md) to enhance the application.

## REST API

* [Controller & Models](src/main/java/com/hft/provider/controller)
* Local Documentation: [http://localhost:8080/swagger-ui/](http://localhost:8080/swagger-ui/)
* Deployed Documentation: [http://rcpsp-provider.com/swagger-ui/](http://193.196.52.129/swagger-ui/)

Have a look into the last [Releases](https://github.com/hft-rcpsp-scheduling/rcpsp-data-provider/releases) to get more
information.

### Scheduling Usage

This shows the main use-case of the api:

```
API                                   Scheduling Client
 | <----- request data for one project ----- | 
 | ------------ send project --------------> |   
 |                                           | execute scheudling algorithm
 |                                           | fill the start-days in the project
 | <- request evaluation for the solution -- |
 | ------------ send feedback -------------> |                          
 |                                           | check feasibility from the feedback
```

> Open the Swagger-UI to get specific information about the REST-calls and their related data.

### Visualization Usage

```
API                                    Scheduling Client
 |                                           | execute scheudling algorithm
 | <-------- request visualization --------- |
 | ------- send visualization file --------> |
```

```
                   UI                         API              Scheduling Client
                    |                          |                       | execute scheudling algorithm
                    |                          | <-- save solution --- |  
                    | -- request solutions --> |                       | 
                    | <--- send solutions ---- |                       |
visualize solutions |                          |                       |
```

## Configuration

* Default profile: [application.properties](src/main/resources/application.properties)
* Production profile: [application-prod.properties](src/main/resources/application-prod.properties)
* Test profile: [application-test.properties](src/main/resources/application-test.properties)

| Key                                        |               Example               | Description                                                                      |
|:-------------------------------------------|:-----------------------------------:|:---------------------------------------------------------------------------------|
| server.port                                |                8080                 |                                                                                  |
| spring.datasource.url                      |     jdbc:mysql://localhost:3306     |                                                                                  |
| spring.datasource.username                 |                root                 | Should be injected into the productive container (if possible not root).         |
| spring.datasource.password                 |              password               | Should be injected into the productive container.                                |
| spring.jpa.hibernate.ddl-auto              |               update                | Values: `create` -> `update` -> `none` (mode for the db schema update)           |
| spring.sql.init.mode                       |               always                | Values: `always` or `never` (mode to initialise date in the db)                  |
| spring.jpa.defer-datasource-initialization |                true                 | Values: `true` or `false` (includes [data.sql](src/main/resources) in data init) |
| spring.jpa.properties.hibernate.dialect    | org.hibernate.dialect.MySQL8Dialect |                                                                                  |
| spring.jpa.show-sql                        |                false                | Values: `true` or `false` (mode for sql logging)                                 |
| logging.level.root                         |                INFO                 | Values: `DEBUG` -> `INFO` -> `WARN` -> `ERROR` (mode for general logging)        |

> To get more information about the configuration, please read the official documentations.

## Container

* [Dockerfile](Dockerfile)
* Uses production profile: [application-prod.properties](src/main/resources/application-prod.properties)

### Latest Image

* [docker-compose.yml](docker-compose.yml)
* Used to run a stable version of the application.
* [docker-compose-prod.yml](.docker/docker-compose-prod.yml) maps the api-port to `80`

__Pull & Run the stack:__

```shell
docker compose pull
docker compose up -d
```

__Stop & Delete stack:__

```shell
docker compose rm --stop --force
```

### Local Image

* [docker-compose-build.yml](.docker/docker-compose-build.yml)
* Used for development or run old versions of the application.

__Build & Run the stack:__

```shell
docker compose --file .docker/docker-compose-build.yml up --build
```

__Build & Run the container (requires separate database):__

```shell
docker build -t data-provider .
docker run -d -p 8080:8080 --name provider-app data-provider
```

__Stop & Delete stack:__

```shell
docker compose rm --stop --force
```

## Further information

* [Development Documentation](.doc/development.md)
* [Deployment Documentation](.doc/deployment.md)
* [Postman Load Testing](.doc/postman.md)
