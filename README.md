# RCPSP Data Provider

This application provides an API to __serve data__ and __validate solutions__. It should build the infrastructure for
algorithm implementations and defines the input- and output-format.

## API

* [Controller & Models](src/main/java/com/hft/provider/controller)
* Documentation: [http://localhost:8080/swagger-ui/](http://localhost:8080/swagger-ui/)

## Configuration

* Default profile: [application.properties](src/main/resources/application.properties)
* Production profile: [application-prod.properties](src/main/resources/application-prod.properties)

| Key         | Example | Description |
|:------------|:-------:|:------------|
| server.port |  8080   |             |

## Container

* [Dockerfile](Dockerfile)
* Uses production profile: [application-prod.properties](src/main/resources/application-prod.properties)
* Build command: `docker build -t data-provider .`
* Run command: `docker run -d -p 8080:8080 --name provider-app data-provider`

## System Requirements

<details>
  <summary>Java 17</summary>

1. Download: [Java 17+](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
2. Install the executable
3. Set System Environment Variables:
4. New: `JAVA_HOME` = `C:\Program Files\Java\jdk-17`
5. Edit > `PATH` > New: `%JAVA_HOME%\bin`
6. Test command: `java -version`

</details>

<details>
  <summary>Maven</summary>

1. Download: [Maven 3.8.3+](https://maven.apache.org/download.cgi)
2. Unzip it to: `C:\Program Files\maven`
3. Set System Environment Variables:
4. New: `MAVEN_HOME` = `C:\Program Files\maven`
5. Edit > `PATH` > New: `%MAVEN_HOME%\bin`
6. Test command: `mvn -v`

</details>

<details>
  <summary>MySQL</summary>

1. Download: [MySQL 8 Server](https://dev.mysql.com/downloads/installer/)
2. Install the executable
3. Select `Server only`
4. Default `root` password: `password` (not for productive DB!)

</details>

<details>
  <summary>Docker</summary>

1. Download: [Docker Desktop](https://www.docker.com/products/docker-desktop)
2. Install the executable

</details>
