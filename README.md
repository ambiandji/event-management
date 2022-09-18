# Event Management API

This is a sample Java / Maven / Spring Boot (version 2.7.3) to demonstrate the ability de write robust API in Java and Spring Boot.

## How to test

## How to Run

This application is packaged as a jar, which has Tomcat 8 embedded. No Tomcat or JBoss installation is necessary. You run it using the ```java -jar``` command.

* Clone this repository ```git clone https://github.com/ambiandji/event-management```
* Make sure you are using JDK 17 and Maven 3.8.5
* You can build the project and run the tests by running ```mvn clean package```
* Once successfully built, you can run the service by one of these two methods:
```
        java -jar target/event-management-1.0.0.jar
or
        mvn spring-boot:run 
```

Once the application runs you should see something like this

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.7.3)

2022-09-15 19:18:47.103  INFO 60926 --- [           main] o.crok4it.em.EventManagementApplication  : Starting EventManagementApplication v1.0.0 using Java 17.0.3.1 on MacBook-Pro-de-Brice.local with PID 60926 (/Users/bamk/Projets/Java/event-management/target/event-management-1.0.0.jar started by bamk in /Users/bamk/Projets/Java/event-management)
2022-09-15 19:18:47.105  INFO 60926 --- [           main] o.crok4it.em.EventManagementApplication  : No active profile set, falling back to 1 default profile: "default"
2022-09-15 19:18:48.282  INFO 60926 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Bootstrapping Spring Data JPA repositories in DEFAULT mode.
2022-09-15 19:18:48.351  INFO 60926 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository scanning in 57 ms. Found 2 JPA repository interfaces.
2022-09-15 19:18:49.052  INFO 60926 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 5000 (http)
2022-09-15 19:18:49.063  INFO 60926 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2022-09-15 19:18:49.063  INFO 60926 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.65]
2022-09-15 19:18:49.156  INFO 60926 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2022-09-15 19:18:49.156  INFO 60926 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 1968 ms
2022-09-15 19:18:49.612  INFO 60926 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2022-09-15 19:18:49.737  INFO 60926 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2022-09-15 19:18:49.922  INFO 60926 --- [           main] liquibase.database                       : Set default schema name to public
2022-09-15 19:18:50.050  INFO 60926 --- [           main] liquibase.lockservice                    : Successfully acquired change log lock
2022-09-15 19:18:50.531  INFO 60926 --- [           main] liquibase.changelog                      : Reading from public.databasechangelog
2022-09-15 19:18:50.617  INFO 60926 --- [           main] liquibase.lockservice                    : Successfully released change log lock
2022-09-15 19:18:50.690  INFO 60926 --- [           main] o.hibernate.jpa.internal.util.LogHelper  : HHH000204: Processing PersistenceUnitInfo [name: default]
2022-09-15 19:18:50.740  INFO 60926 --- [           main] org.hibernate.Version                    : HHH000412: Hibernate ORM core version 5.6.10.Final
2022-09-15 19:18:50.896  INFO 60926 --- [           main] o.hibernate.annotations.common.Version   : HCANN000001: Hibernate Commons Annotations {5.1.2.Final}
2022-09-15 19:18:50.993  INFO 60926 --- [           main] org.hibernate.dialect.Dialect            : HHH000400: Using dialect: org.hibernate.dialect.PostgreSQLDialect
2022-09-15 19:18:51.558  INFO 60926 --- [           main] o.h.e.t.j.p.i.JtaPlatformInitiator       : HHH000490: Using JtaPlatform implementation: [org.hibernate.engine.transaction.jta.platform.internal.NoJtaPlatform]
2022-09-15 19:18:51.564  INFO 60926 --- [           main] j.LocalContainerEntityManagerFactoryBean : Initialized JPA EntityManagerFactory for persistence unit 'default'
2022-09-15 19:18:52.162  WARN 60926 --- [           main] JpaBaseConfiguration$JpaWebConfiguration : spring.jpa.open-in-view is enabled by default. Therefore, database queries may be performed during view rendering. Explicitly configure spring.jpa.open-in-view to disable this warning
2022-09-15 19:18:53.280  INFO 60926 --- [           main] o.s.b.a.e.web.EndpointLinksResolver      : Exposing 1 endpoint(s) beneath base path '/actuator'
2022-09-15 19:18:53.346  INFO 60926 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 5000 (http) with context path ''
2022-09-15 19:18:53.363  INFO 60926 --- [           main] o.crok4it.em.EventManagementApplication  : Started EventManagementApplication in 6.817 seconds (JVM running for 7.315)
```

## About the Service

This API allows you to manage the artists and venues where they can organize show. It is possible for a user to see the list of shows, artists and venues. For a given artist, we can see his past and upcomming shows
Here is what this little application demonstrates:

* Full integration with the latest **Spring** Framework: inversion of control, dependency injection, etc.
* Packaging as a single jar with embedded container (tomcat): No need to install a container separately on the host just run using the ``java -jar`` command
* Demonstrates how to set up healthcheck, metrics, info, environment, etc. endpoints automatically on a configured port. Inject your own health / metrics info with a few lines of code.
* Writing a RESTful service using annotation: supports both JSON request / response; simply use desired ``Accept`` header in your request
* Exception mapping from application exceptions to the right HTTP response with exception details in the body
* *Spring Data* Integration with JPA/Hibernate with just a few lines of configuration and familiar annotations.
* Automatic CRUD functionality against the data source using Spring *Repository* pattern
* The use of liquibase for migration
* Demonstrates MockMVC test framework with associated libraries
* BDD with cucumber an integration test
* All APIs are "self-documented" by OpenAPi 3 using annotations

Here are some endpoints you can call:

### Get information about system health, configurations, etc.

```
http://localhost:5091/env
http://localhost:5091/health
http://localhost:5091/info
http://localhost:5091/metrics
```

### Create an artist resource

```
POST /api/v1/artist
Accept: application/json
Content-Type: application/json

{
"name" : "Beds R Us",
"description" : "Very basic, small rooms but clean",
"city" : "Santa Ana",
"rating" : 2
}

RESPONSE: HTTP 201 (Created)
Location header: http://localhost:8090/example/v1/hotels/1
```

### Retrieve a paginated list of hotels

```
http://localhost:8090/example/v1/hotels?page=0&size=10

Response: HTTP 200
Content: paginated list 
```

### Update a hotel resource

```
PUT /example/v1/hotels/1
Accept: application/json
Content-Type: application/json

{
"name" : "Beds R Us",
"description" : "Very basic, small rooms but clean",
"city" : "Santa Ana",
"rating" : 3
}

RESPONSE: HTTP 204 (No Content)
```
### To view Swagger 2 API docs

Run the server and browse to ```http://localhost:5000/swagger-ui/index.html#```

### Questions and Comments: ambiandji@gmail.com

