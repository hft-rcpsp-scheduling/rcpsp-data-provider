# Development

## 1. System Requirements

### 1.1. Java 17

1. Download: [Java 17+](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
2. Install the executable
3. Set System Environment Variables:
4. New: `JAVA_HOME` = `C:\Program Files\Java\jdk-17`
5. Edit > `PATH` > New: `%JAVA_HOME%\bin`
6. Test command: `java -version`

### 1.2. Maven

1. Download: [Maven 3.8.3+](https://maven.apache.org/download.cgi)
2. Unzip it to: `C:\Program Files\maven`
3. Set System Environment Variables:
4. New: `MAVEN_HOME` = `C:\Program Files\maven`
5. Edit > `PATH` > New: `%MAVEN_HOME%\bin`
6. Test command: `mvn -v`

### 1.3. MySQL

1. Download: [MySQL 8 Server](https://dev.mysql.com/downloads/installer/)
2. Install the executable
3. Select `Server only`
4. Default `root` password: `password` (not for productive DB!)

### 1.4. Docker

1. Download: [Docker Desktop](https://www.docker.com/products/docker-desktop)
2. Install the executable

## 2. Libraries

### 2.1. Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.7.4/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.7.4/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.7.4/reference/htmlsingle/#web)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.7.4/reference/htmlsingle/#data.sql.jpa-and-spring-data)

### 2.2. Guides

The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
