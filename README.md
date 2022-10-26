[![Continuous-Integration](https://github.com/hft-rcpsp-scheduling/rcpsp-data-provider/actions/workflows/ci.yml/badge.svg)](https://github.com/hft-rcpsp-scheduling/rcpsp-data-provider/actions/workflows/ci.yml)
[![Docker-Image](https://github.com/hft-rcpsp-scheduling/rcpsp-data-provider/actions/workflows/docker-image.yml/badge.svg)](https://github.com/hft-rcpsp-scheduling/rcpsp-data-provider/actions/workflows/docker-image.yml)
[![Deployment](https://github.com/hft-rcpsp-scheduling/rcpsp-data-provider/actions/workflows/deployment.yml/badge.svg)](https://github.com/hft-rcpsp-scheduling/rcpsp-data-provider/actions/workflows/deployment.yml)

# RCPSP Data Provider

This application provides an API to __serve raw data__ and __validate solutions__. It should build the infrastructure
for scheduling-algorithm implementations and defines the input- and output-format.

## REST API

* [Controller & Models](src/main/java/com/hft/provider/controller)
* Local Documentation: [http://localhost:8080/swagger-ui/](http://localhost:8080/swagger-ui/)
* Deployed Documentation: [http://rcpsp-provider.com/swagger-ui/](http://193.196.52.129/swagger-ui/)

### Usage

This shows the main use-case of the api:

```
API                                        Client
 | <----- request data for one project ----- | 
 | ------------ send project --------------> |   
 |                                           | execute scheudling algorithm
 |                                           | fill the start-days in the project
 | <- request evaluation for the solution -- |
 | ------------ send feedback -------------> |                          
 |                                           | check feasibility in the feedback
 | <-------- request visualization --------- |
 | ------- send visualization file --------> |
```

> Open the Swagger-UI to get specific information about the REST-calls and their related data.

## Configuration

* Default profile: [application.properties](src/main/resources/application.properties)
* Production profile: [application-prod.properties](src/main/resources/application-prod.properties)

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

Used to run a stable version of the application.

__Pull command:__

```shell
docker pull ghcr.io/hft-rcpsp-scheduling/rcpsp-data-provider:latest
```

__Run command:__

```shell
docker run -d -p 8080:8080 --name provider-app ghcr.io/hft-rcpsp-scheduling/rcpsp-data-provider:latest
```

### Local Image

Used for development or run old versions of the application.

__Build command:__

```shell
docker build -t data-provider .
```

__Run image:__

```shell
docker run -d -p 8080:8080 --name provider-app data-provider
```

## Further information

* [Development Documentation](.doc/development.md)
* [Deployment Documentation](.doc/deployment.md)
