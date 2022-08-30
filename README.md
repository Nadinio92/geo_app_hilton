# Hilton: IP Geolocation

How to start and general design:
---
Prerequisites: JDK 11, Maven3, internet access

1. Run `mvn clean install` to build your application
2. Start application with `java -jar target/geo_app-1.0-SNAPSHOT.jar server config.yml`
3. To check that your application is running enter url `http://localhost:8080/v1/geolocation?ip=123.255.32.23`, any other valid IPv4 address will work. 
4. Database web console:
   1. Check logs to find H2 web console URL. Example: `com.hilton.utils.AppRunnerUtils: H2 web console started at url: http://192.168.56.1:8082`. 
   2. On login form ensure you selected: `Generic H2 (Server)`
   3. JDBC url: `jdbc:h2:tcp://localhost/~/test`
   4. Username: `sa`

Design details
1. Database schema versioned managed by Liquibase and defined in `src/main/resources/migrations.xml`
2. Application stores received geolocation data in json format in `GEOLOCATION.PAYLOAD` column
3. H2 database and Liquibase migration integrated to start automatically on application startup
4. Guice used as a dependency injection framework
5. Added custom Hibernate Validator annotation @Ipv4(`src/main/java/com/hilton/utils/Ipv4.java`) to validate IP address inputs
6. Created scheduler to refresh deprecated geolocations(older than 5mins) every minute
7. As per requirements Guava cache used to store geolocations
8. Application parameters might be configured in `config.yml` under `app` prefix
9. Additional logs will be printed to validate app behavior whenever:
   1. Cache has no IP address data 
   2. Geolocation data received from remote api server and stored in the database
   3. Geolocation data retrieved from the database
   4. Deprecated 5+mins old geolocations refreshed
10. Simple integration test developed: `com.hilton.integration.IntegrationTest`

