[![Continuous-Integration](https://github.com/hft-rcpsp-scheduling/rcpsp-data-provider/actions/workflows/ci.yml/badge.svg)](https://github.com/hft-rcpsp-scheduling/rcpsp-data-provider/actions/workflows/ci.yml)
[![Docker-Image](https://github.com/hft-rcpsp-scheduling/rcpsp-data-provider/actions/workflows/docker-image.yml/badge.svg)](https://github.com/hft-rcpsp-scheduling/rcpsp-data-provider/actions/workflows/docker-image.yml)

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
* Local image:
  * Build command: `docker build -t data-provider .`
  * Run command: `docker run -d -p 8080:8080 --name provider-app data-provider`
* Latest release image:
  * Pull command: `docker pull ghcr.io/hft-rcpsp-scheduling/rcpsp-data-provider:latest`
  * Run command: `docker run -d -p 8080:8080 --name provider-app ghcr.io/hft-rcpsp-scheduling/rcpsp-data-provider:latest`

## Deployment

1. Register at the bwCloud https://login.bwidm.de/ 
2. Login at https://portal.bw-cloud.org/project/
   * Create new volume
   * Create new instance (Ubuntu) 
     * Select the new volume
     * Add your ssh public key
   * Add new port rules under security groups
     * Egress TCP port 80
     * Ingres TCP port 80
3. Login via ssh using the ip like so 
```shell
ssh ubuntu@193.196.52.129
```
4. Update the Server
```shell
sudo apt-get update
sudo apt-get upgrade
```
5. Install Docker
```shell
sudo apt install docker.io
sudo systemctl start docker
sudo systemctl enable docker
sudo groupadd docker
sudo usermod -aG docker ${USER}
```
6. Pull docker image from repository
```shell
sudo docker pull ghcr.io/hft-rcpsp-scheduling/rcpsp-data-provider:latest
sudo docker run -d -p 80:8080 --name provider-app ghcr.io/hft-rcpsp-scheduling/rcpsp-data-provider:latest --restart always
```
7. Check configuration for Program name: docker-proxy with the Local Address 0.0.0.0:80
```shell
sudo apt install net-tools
sudo netstat -plnut
```

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
