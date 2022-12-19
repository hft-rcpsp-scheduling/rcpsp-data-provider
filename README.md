[![Continuous-Integration](https://github.com/hft-rcpsp-scheduling/rcpsp-data-provider/actions/workflows/ci.yml/badge.svg)](https://github.com/hft-rcpsp-scheduling/rcpsp-data-provider/actions/workflows/ci.yml)
[![Docker-Image](https://github.com/hft-rcpsp-scheduling/rcpsp-data-provider/actions/workflows/docker-image.yml/badge.svg)](https://github.com/hft-rcpsp-scheduling/rcpsp-data-provider/actions/workflows/docker-image.yml)
[![Deployment](https://github.com/hft-rcpsp-scheduling/rcpsp-data-provider/actions/workflows/deployment.yml/badge.svg)](https://github.com/hft-rcpsp-scheduling/rcpsp-data-provider/actions/workflows/deployment.yml)

# RCPSP Data Provider

This application provides an API to __serve raw data (projects)__ and __validate solutions__. It should build the
infrastructure for scheduling-algorithm implementations and defines the input- and output-format. In addition, the
application can store solutions for later use.

Please read the [Container Section](.documentation/docker.md) to run the application and
the [Development Documentation](.documentation/dev-tools.md) to enhance the application. If there are still some
questions,
maybe the [FAQ Page](.documentation/faq.md) can help out.

## Documentation

* [API Usage](.documentation/api-usage.md)
* [Development Tools](.documentation/dev-tools.md)
* [Docker Commands](.documentation/docker.md)
* [Deployment](.documentation/deployment.md)
* [Postman Load Testing](.documentation/postman.md)
* [FAQ](.documentation/faq.md)

## REST API

* [Controller & Models](src/main/java/com/hft/provider/controller)
* Local Documentation: [http://localhost:8080/swagger-ui/](http://localhost:8080/swagger-ui/)
* Deployed Documentation: [http://rcpsp-provider.com/swagger-ui/](http://193.196.52.129/swagger-ui/)

Have a look into the last [Releases](https://github.com/hft-rcpsp-scheduling/rcpsp-data-provider/releases) to get more
information.

## Project Structure

```
root
 |__ .docker                            --> non standard docker descriptors
 |__ .documentation                     --> documentation to different topics
 |__ .github                            --> github descriptors for workflows (actions) and templates (issues & PRs)
 |__ .mvn                               --> devault spring mvn wrapper library
 |__ .postman                           --> postman collections for load testing
 |__ src                                
 |    |__ main                          
 |    |     |__ java/com/hft/provider   --> root package
 |    |     |     |__ config            --> global spring configuration beans
 |    |     |     |__ controller        --> rest controllers for the api
 |    |     |     |     |__ model       --> models exposed by the api
 |    |     |     |__ database          --> internal database classes for projects & solutions
 |    |     |     |__ eval              --> solution evaluation
 |    |     |     |__ excel             --> classes to generate different excel files
 |    |     |     |__ file              --> reader for resource files
 |    |     |     |__ Application.java  --> Main-Class
 |    |     |__ resources               --> directory with app-configs, projects** & their makespans**
 |    |__ test                          --> mirrored package structure of the main project with test classes
 |__ docker-compose.yml & Dockerfile    --> standard docker descriptors
 |__ mvnw & mvnw.cmd                    --> default spring mvn wrapper
 |__ pom.xml                            --> maven descriptor with dependencies
 
```

> **Project- and Makespan-Resources are
> from [https://www.om-db.wi.tum.de/psplib/getdata_sm.html](https://www.om-db.wi.tum.de/psplib/getdata_sm.html).

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
